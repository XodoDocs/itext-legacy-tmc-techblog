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

One of the advantages of tagged pdf documents is that we get a lot of information on the structure of the document aside from the actual content.
In order the correctly handle this, we build a custom class that extracts this information.

```java
public class MarkedContentExtractor 
{
    public static interface IMarkedContent
    {
        public String getText();
        public int getMCId();
        public PdfName getRole();
        public int getPageNr();
    }
        
    private final ITextSelectionStrategy strategy;
    
    public MarkedContentExtractor()
    {
        this.strategy = new MTextSelectionStrategy();
    }
    
    public MarkedContentExtractor(ITextSelectionStrategy strategy)
    {
        this.strategy = strategy;
    }
    
    public List<IMarkedContent> extract(PdfDocument doc)
    {                             
        // extract all
        Map<Integer, Map<Integer, MarkedContent>> mcs = new HashMap<>();
        for(int i=1;i<=doc.getNumberOfPages();i++)
        {
            mcs.put(i, new HashMap<Integer, MarkedContent>());
            MarkedContentStrategy markedContentStrategy = new MarkedContentStrategy(strategy);
            PdfTextExtractor.getTextFromPage(doc.getPage(i), markedContentStrategy); 
            for(MarkedContent mc : markedContentStrategy.getMarkedContent())
            {
                mc.setPage(i);           
                mcs.get(i).put(mc.getMCId(), mc);
            }
        }     
                  
        // query struct tree
        IPdfStructElem root = doc.getStructTreeRoot();       
        List<IPdfStructElem> els = new ArrayList<>();
        Stack<IPdfStructElem> stk = new Stack<>();
        stk.push(root);
        while(!stk.isEmpty())
        {
            IPdfStructElem e = stk.pop();
            if(e instanceof PdfMcrNumber)
                els.add(e);
            if(e.getKids() == null)
                continue;
            else
                for(IPdfStructElem  c : e.getKids())
                    stk.push(c);
        }        

        for(IPdfStructElem e : els)
        {
            if(e instanceof PdfMcrNumber)
            {
                PdfMcrNumber pse = (PdfMcrNumber) e;
                int mcid = pse.getMcid();
                int page = doc.getPageNumber(pse.getPageObject());
                if(!mcs.containsKey(page) || !mcs.get(page).containsKey(mcid))
                    continue;
                PdfName role = hasParentWithRole(pse, PdfName.H) ? PdfName.H : pse.getRole();
                mcs.get(page).get(mcid).setRole(role);
            }
        }
        
        // return
        List<IMarkedContent> retval = new ArrayList<>();
        for(Map<Integer, MarkedContent> submap : mcs.values())
        {
            retval.addAll(submap.values());
        }
        return retval;
    }

    private boolean hasParentWithRole(IPdfStructElem e, PdfName role)
    {
        IPdfStructElem parent = e;
        while(parent.getParent() != null && !parent.equals(parent.getParent()))
        {
            if(parent.getRole() != null && parent.getRole().equals(role))
                return true;
            parent = parent.getParent();
        }
        return false;
    }

}
```

```java

class MarkedContentStrategy extends SimpleTextExtractionStrategy
{
    private List<MarkedContent> markedContent = new ArrayList<>();
 
    private ITextSelectionStrategy strategy;
    
    public MarkedContentStrategy(ITextSelectionStrategy strategy){ this.strategy = strategy; }
    
    @Override
    public String getResultantText() { return super.getResultantText(); }

    public List<MarkedContent> getMarkedContent(){ return markedContent; }
    
    @Override
    public void eventOccurred(IEventData data, EventType type) {
        super.eventOccurred(data, type);

        if(data instanceof TextRenderInfo)
        {
            TextRenderInfo info = (TextRenderInfo) data;           
            
            String text = strategy.select(info.getText(), info.getActualText(), info.getExpansionText());

            int mcid = info.getMcid();
            int lastMcid = markedContent.isEmpty() ? -1 : markedContent.get(markedContent.size()-1).getMCId();
            if(lastMcid == -1 || mcid != lastMcid)
            {
                PdfName role = info.getCanvasTagHierarchy().isEmpty() ? PdfName.P : info.getCanvasTagHierarchy().get(0).getRole();
                MarkedContent mc = new MarkedContent(role, text, mcid);
                markedContent.add(mc);
            }
            else if(mcid == lastMcid)
            {
                int p = markedContent.size() - 1;
                markedContent.get(p).setText(markedContent.get(p).getText() + text);
            }
        }
    }
    
    @Override
    public Set<EventType> getSupportedEvents() { return super.getSupportedEvents(); }   
}
```

You'll see that we have also defined ITextSelectionStrategy, which is a class that picks what text to use from a piece of content.
Content can have more than one piece of text associated with it. It can be marked with \ActualText or \ExpansionText (so that abbreviations get read as their full text.)

The interface ITextSelectionStrategy wraps a function that expects you make a choice for each piece of content that could have multiple vocalizations.

```java
public interface ITextSelectionStrategy {
    
    /**
     * This function selects what to return in case the text, actualText and expansion text are defined
     * @param text the "normal" text (this is the text being displayed)
     * @param actualText the text marked as \actualText
     * @param expansionText the text marked as \E (typically used for abbreviations)
     * @return 
     */
    public String select(String text, String actualText, String expansionText);
    
}
```

One concrete implementation of this interface is MTextSelectionStrategy,
which handles abbreviations, unless they are given entirely in uppercase.
In any other case, it prefers \ActualText above \ExpansionText, and \ExpansionText above normal text.

```java
public class MTextSelectionStrategy implements ITextSelectionStrategy{

    @Override
    public String select(String text, String actualText, String expansionText) {
        if(text.equals(text.toUpperCase()))
            return text;
        return actualText != null ? actualText : (expansionText != null ? expansionText : text);
    }
    
}
```

This class reads a document and then lists IMarkedContent objects, which contain text, an ID, a page number, and a role (eg: 'heading')
We'll provide a default implementation of this interface as a pojo (plain old java object)

```java
class MarkedContent implements MarkedContentExtractor.IMarkedContent
{
    private PdfName role;
    private String text;
    private final int mcid;
    private int pageNr;
    
    public MarkedContent(PdfName name, String text, int id)
    {
        this.role = name;
        this.text = text;
        this.mcid = id;
        this.pageNr = 0;
    }
    
    public void setPage(int pageNr){ this.pageNr = pageNr; }
    
    @Override
    public int getPageNr(){ return pageNr; }
    
    public void setRole(PdfName role){ this.role = role; }
    
    @Override
    public PdfName getRole(){ return role; }
    
    public void setText(String s){ this.text = s; }
    
    @Override
    public String getText(){ return text; }
    
    @Override
    public int getMCId(){ return mcid; }
}
```

# structuring the document

We want our audiobook reader to be able to navigate the document, by chapter, page and paragraph.
This concept (chapter, page, paragraph) will be called PdfUnit from now on.

```java
public enum PdfUnit{
   
    DOCUMENT,       // entire document
    CHAPTER,        // chapter
    CHAPTER_PART,   // part of a chapter (Alexa is limited to 8000 characters)
    PAGE,           // single page
    PARAGRAPH       // a single paragraph
    
}
```

Every piece of text in the document can then be represented with 3 characteristics:
 - paragraph
 - page
 - chapter
 We'll model this as a PdfLocation.
 
```java
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
```
 
You'll notice that we added a fourth level, chapter parts. This is a workaround for one of the limitations offered by the Alexa API.
This API allows you to voice only 8000 characters at once. Although this is not a limitation for paragraphs or pages, complete chapters are often
more than 8000 characters long. To overcome this, we split a chapter into parts when needed.
 
In order to achieve this level of navigation, we need a tagged pdf document, and we need a way of representing that internal structure that can be easily navigated.
We'll represent the entire document as a tree-like datastructure. The root node represents the entire document. The document-node then splits into its children (chapters).
Each chapter splits into chapter-parts, each chapter part splits into pages and each page into paragraphs.

This way, we can use tree-walking algorithms to go to a previous or next PdfUnit.

```java
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
```

Now of course, we need a way of building this tree structure.
Since there are multiple options for this, we first define an interface. 
ITreeBuildingAlgorithm enables us to pass around any function that turns a list of marked content into a (root) PdfNavigationNode.

```java
public interface ITreeBuildingAlgorithm {
    
    /**
     * Turns a List<IMarkedContent> in a tree structure of PdfNavigationNode, returning the root node
     * @param content
     * @return 
     */
    public PdfNavigationNode build(List<IMarkedContent> content);
   
}
```


We then build a specific implementation of this interface, that will build trees the Alexa API can voice.

```java
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
```

# navigating the document

The job of a PdfNavigator object is to walk this tree and return the appropriate text for a given PdfLocation.
It should also offer an implementation for next and previous commands.

```java
public class PdfNavigator {
     
    PdfNavigationNode root = null;
    
    public PdfNavigator(PdfDocument doc, ITreeBuildingAlgorithm algo)
    {            
        // build tree
        root = algo.build(new MarkedContentExtractor().extract(doc));                
    }

    public int count(int[] location)
    {
        PdfNavigationNode node = root;
        for(int i=0;i<location.length;i++)
        {
            int index = location[i];
            if(index < 0 || index >= node.getChildren().size())
            {
                return 0;
            }
            else
            {
                node = node.getChildren().get(index);
            }
        }
        return node.getChildren().size();
    }

    private PdfNavigationNode locate(PdfLocation l)
    {        
        int[] coords = {l.getChapter(), l.getChapterPart(), l.getPage(), l.getParagraph()};
        
        // navigate to chapter
        PdfNavigationNode node = root;
        for(int i=0;i<4;i++)
        {
            int index = coords[i];
            if(index >= 0 && index < node.getChildren().size())
            {
                node = node.getChildren().get(index);
            }
            else
            {
                return node;
            }
        }        
        return node;
    }
    
    private PdfLocation mark(PdfNavigationNode n)
    {
        int paragraphIndex = 0;
        if(n.getUnit() == PdfUnit.PARAGRAPH)
        {
            paragraphIndex = n.getParent().getChildren().indexOf(n);
            n = n.getParent();
        }
        int pageIndex = 0;
        if(n.getUnit() == PdfUnit.PAGE)
        {
            pageIndex = n.getParent().getChildren().indexOf(n);
            n = n.getParent();
        }       
        int chapterPartIndex = 0;
        if(n.getUnit() == PdfUnit.CHAPTER_PART)
        {
            chapterPartIndex = n.getParent().getChildren().indexOf(n);
            n = n.getParent();
        }        
        int chapterIndex = 0;
        if(n.getUnit() == PdfUnit.CHAPTER)
        {
            chapterIndex = n.getParent().getChildren().indexOf(n);
            n = n.getParent();
        }        
        return new PdfLocation(chapterIndex, chapterPartIndex, pageIndex, paragraphIndex);
    }
    
    public PdfLocation next(PdfLocation l, PdfUnit u)
    {
        // locate correct node in tree
       PdfNavigationNode node = locate(l);
       
       // handle root
       if(node.getParent() == node)
           return l;
       
       // go to the right level
       while(node.getUnit() != u && node.getParent() != node)
           node = node.getParent();
       
       // get parent
       PdfNavigationNode parent = node.getParent();
       
       // sibling information
       int myIndex = parent.getChildren().indexOf(node);
       int nextIndex = myIndex + 1;
       
       if(nextIndex >= parent.getChildren().size())
       {
           return next(l, parent.getUnit());
       }
       else
       {
           return mark(parent.getChildren().get(nextIndex));
       }
    }
    
    public PdfLocation previous(PdfLocation l, PdfUnit u)
    {
        // locate correct node in tree
       PdfNavigationNode node = locate(l);
       
       // handle root
       if(node.getParent() == node)
           return l;
       
       // go to the right level
       while(node.getUnit() != u && node.getParent() != node)
           node = node.getParent();
       
       // get parent
       PdfNavigationNode parent = node.getParent();
       
       // sibling information
       int myIndex = parent.getChildren().indexOf(node);
       int prevIndex = myIndex - 1;
       
       if(prevIndex < 0)
       {
            PdfLocation retval = previous(l, parent.getUnit());
            return retval;
       }
       else
       {
           return mark(parent.getChildren().get(prevIndex));
       }
    }
    
    public String get(PdfLocation l, PdfUnit u)
    {
      PdfNavigationNode node = locate(l);      
      while(node.getParent() != node && node.getUnit() != u)
      {
          node = node.getParent();
      }      
      return node.getText();
    }

    public boolean isValid(PdfLocation l)
    {
        int[] coords = {l.getChapter(), l.getChapterPart(), l.getPage(), l.getParagraph()};
        
        // navigate to chapter
        PdfNavigationNode node = root;
        for(int i=0;i<4;i++)
        {
            int index = coords[i];
            if(index >= 0 && index < node.getChildren().size())
            {
                node = node.getChildren().get(index);
            }
            else
            {
                return false;
            }
        }        
        return true;        
    }   
}
```

# building the alexa skill

Now that we have everything set up to handle tagged pdfs, it's time to integrate this logic with Alexa.
To do this, we need to implement two classes,  a Speechlet (which handles the particular commands for a skill), and a RequestStreamHandler (which acts as the entrypoint for a skill, managing access rights etc).

We'll not go into the full details of implementing a Speechlet (many good examples can be found on GitHub).
Instead we'll have a look at one particular case in the onIntent method.
This method gets called every time the user communicates with the echo dot device and vocalizes a command that is recognized by the skill.
The command is labelled with an intent (e.g. "ReadIntent" or "GotoNextIntent") depending on how you configured things.

```java
 // read unit
        if("ReadIntent".equals(intentName))
        {
            String txt = docByUser.get(userId).get(locationByUser.get(userId), unitByUser.get(userId));
            // go to next location
            PdfLocation nxt = docByUser.get(userId).next(locationByUser.get(userId), unitByUser.get(userId));
            locationByUser.put(userId, nxt);
            // return response
            return buildResponse(txt);
        }
```

In this snippet:
 - docByUser stores the PdfNavigator for each userId (a token generated by Amazon)
 - locationByUser stores the PdfLocation that was last read completely
 - unitByUser stores the current nagivation units per userId
 - buildResponse is a convenience method that wraps a piece of text in a SpeechletResponse object

 The SpeechletRequestStreamHandler acts as the entrypoint in our skill. Every call must first be passed through this handler, and only if the call originated forom an approved application(Id) will the call get handled.
 
 ```java
 public class ITextReaderSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler{
    
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        // supportedApplicationIds.add("amzn1.echo-sdk-ams.app.[unique-value-here]");
        supportedApplicationIds.add("amzn1.ask.skill.<more content should be here>");
    }

    public ITextReaderSpeechletRequestStreamHandler() {
        super(new ITextReaderSpeechlet(), supportedApplicationIds);
    }
}
```
 
# testing our application

# deploying

# recap