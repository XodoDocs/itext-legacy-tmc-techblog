# what is amazon lambda

AWS Lambda lets you run code without provisioning or managing servers. 
You pay only for the compute time you consume - there is no charge when your code is not running. 
With Lambda, you can run code for virtually any type of application or backend service - all with zero administration. 
Just upload your code and Lambda takes care of everything required to run and scale your code with high availability. 
You can set up your code to automatically trigger from other AWS services or call it directly from any web or mobile app.

# project idea

The idea behind this project is to create an audiobook reader. It uses 3 kinds of services.
 - iText library, in order to extract the content and structure of a pdf document
 - AWS Lambda, to host our java functionality
 - Alexa API, to have an amazon echo dot device read the extracted content aloud

# what you'll need

You will need:
 - developer account for amazon (https://developer.amazon.com/)
 - amazon echo dot (optional, but cool)
 - iText7 library (java)
 - a tagged pdf document
 
# reading a tagged pdf document

We want our audiobook reader to be able to navigate the document, by chapter, page and paragraph.
This concept (chapter, page, paragraph) will be called PdfUnit from now on.

{code:java}
public enum PdfUnit{
    
    DOCUMENT,       // entire document
    CHAPTER,        // chapter
    CHAPTER_PART,   // part of a chapter (Alexa is limited to 8000 characters)
    PAGE,           // single page
    PARAGRAPH       // a single paragraph
    
}
{code}

Every piece of text in the document can then be represented with 3 characteristics:
 - paragraph
 - page
 - chapter
 We'll model this as a PdfLocation.
 
 {code:java}
 public class PdfLocation {
    
    private final int chapter;
    private final int chapterPart;
    private final int page;
    private final int paragraph;
    
    public PdfLocation()
    {
        this.chapter = 0;
        this.chapterPart = 0;
        this.page = 0;
        this.paragraph = 0;
    }
    
    public PdfLocation(int chapter, int chapterPart, int page, int paragraph)
    {
        this.chapter = chapter;
        this.chapterPart = chapterPart;
        this.page = page;
        this.paragraph = paragraph;
    }
           
    public int getChapter()
    {
        return chapter;
    }
    
    public int getChapterPart()
    {
        return chapterPart;
    }
    
    public int getPage()
    {
        return page;
    }
    
    public int getParagraph()
    {
        return paragraph;
    }
    
    @Override
    public String toString()
    {
        // numeric magic because humans tend to dislike 0-based indices
        return "chapter " + (chapter + 1) + (chapterPart == 0 ? "" : (", part " + (chapterPart + 1))) + ", page " + (page + 1) +  ", paragraph " + (paragraph + 1);
    }
}
{code}
 
You'll notice that we added a fourth level, chapter parts. This is a workaround for one of the limitations offered by the Alexa API.
This API allows you to voice only 8000 characters at once. Although this is not a limitation for paragraphs or pages, complete chapters are often
more than 8000 characters long. To overcome this, we split a chapter into parts when needed.
 
In order to achieve this level of navigation, we need a tagged pdf document, and we need a way of representing that internal structure that can be easily navigated.
We'll represent the entire document as a tree-like datastructure. The root node represents the entire document. The document-node then splits into its children (chapters).
Each chapter splits into chapter-parts, each chapter part splits into pages and each page into paragraphs.

This way, we can use tree-walking algorithms to go to a previous or next PdfUnit.

{code:java}
public class PdfNavigationNode
    {
        private PdfUnit unit = PdfUnit.PARAGRAPH;
        private String text = "";
        
        private PdfNavigationNode parent = this;
        private List<PdfNavigationNode> children = new ArrayList<>();
                
        public PdfNavigationNode(PdfUnit unit, String text)
        {
            this.text = text;
            this.unit = unit;
        }
        
        public boolean add(PdfNavigationNode node)
        {
            children.add(node);
            node.parent = this;
            return true;
        }
        
        public PdfUnit getUnit()
        {
            return unit;
        }
         
        public String getText()
        {
            if(!children.isEmpty())
            {
                String retval = text;
                for(int i=0;i<children.size();i++)
                {
                    retval += children.get(i).getText();
                }
                return retval;
            }
            else
            {
                return text;
            }
        }
        
        public List<PdfNavigationNode> getChildren()
        {
            return children;
        }
        
        public PdfNavigationNode getParent()
        {
            return parent;
        }
    }
{code}

Now of course, we need a way of building this tree structure.
Since there are multiple options for this, we first define an interface. 
ITreeBuildingAlgorithm enables us to pass around any function that turns a list of marked content into a (root) PdfNavigationNode.

{code:java}
public interface ITreeBuildingAlgorithm {
    
    /**
     * Turns a List<IMarkedContent> in a tree structure of PdfNavigationNode, returning the root node
     * @param content
     * @return 
     */
    public PdfNavigationNode build(List<IMarkedContent> content);
   
}
{code}


We then build a specific implementation of this interface, that will build trees the Alexa API can voice.

{code:java}
public class AlexaTreeBuildingAlgorithm implements ITreeBuildingAlgorithm{

    private static final int MAX_CHAPTER_PART_LENGTH = 7500;
    
    @Override
    public PdfNavigationNode build(List<MarkedContentExtractor.IMarkedContent> content) 
    {
        // root
        PdfNavigationNode root = new PdfNavigationNode(PdfUnit.DOCUMENT, "");
        
        // recursively build tree
        delegate(root, content);
        
        // return
        return root;
    }
    
    private void delegate(PdfNavigationNode root, List<IMarkedContent> content)
    {
        // build document by adding chapters
        if(root.getUnit() == PdfUnit.DOCUMENT)
        {
            // loop over content to find headers
            for(int i=0;i<=content.size();i++)
            {
                // find first header
                while(i < content.size() && content.get(i).getRole() != PdfName.H){ i++;}
                if(i >= content.size() || content.get(i).getRole() != PdfName.H) return;
            
                // find next header
                int j = i + 1;
                while(j < content.size() && content.get(j).getRole() != PdfName.H){ j++; }
                
                // make new node
                PdfNavigationNode child = new PdfNavigationNode(PdfUnit.CHAPTER, "");
                root.add(child);
                delegate(child, content.subList(i, j));
                
                i = j-1;
            }
        }
        // build chapters by adding chapter parts
        else if(root.getUnit() == PdfUnit.CHAPTER)
        {
            int len = 0;
            int prevBreak = 0;
            for(int i=0;i<=content.size();i++)
            {
                if(i == content.size() || len + content.get(i).getText().length() > MAX_CHAPTER_PART_LENGTH)
                {
                    // make new node                  
                    PdfNavigationNode child = new PdfNavigationNode(PdfUnit.CHAPTER_PART, "");
                    root.add(child);                    
                    delegate(child, content.subList(prevBreak, i));
                    
                    len = 0;
                    prevBreak = i;
                }
                else
                {
                    len += content.get(i).getText().length();
                }
            }
        }
        // build chapter parts by adding pages
        else if(root.getUnit() == PdfUnit.CHAPTER_PART)
        {
            int prevPage = content.get(0).getPageNr();
            int prevBreak = 0;
            for(int i=0;i<=content.size();i++)
            {               
                int pageNr = i == content.size() ? 0 : content.get(i).getPageNr();
                if(i == content.size() || pageNr != prevPage)
                {
                    // make new node                  
                    PdfNavigationNode child = new PdfNavigationNode(PdfUnit.PAGE, "");
                    root.add(child);
                    delegate(child, content.subList(prevBreak, i));
                    
                    // next page
                    prevPage = pageNr;
                    prevBreak = i;
                }                
            }
        }
        // build pages by adding paragraphs
        else if(root.getUnit() == PdfUnit.PAGE)
        {
            for(int i=0;i<content.size();i++)
            {
                if(content.get(i).getRole() == PdfName.Span)
                {
                    String text = content.get(i).getText();
                    if(text.replaceAll(" +", "").isEmpty())
                        continue;
                    PdfNavigationNode child = new PdfNavigationNode(PdfUnit.PARAGRAPH, content.get(i).getText());
                    root.add(child);
                }
            }
        }
    }   
}
{code}

# navigating the document

The job of a PdfNavigator object is to walk this tree and return the appropriate text for a given PdfLocation.
It should also offer an implementation for next and previous commands.

# building the alexa skill

# testing our application

# deploying

# recap