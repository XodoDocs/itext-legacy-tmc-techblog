# HTML 2 PDF :: How HTML tags are mapped onto iText layout objects


## iText Layout objects

iText has had a document model for a long time. This model allows developers to quickly grasp how to use iText without having to know much about the PDF specification. The model we use closely resembles HTML. This is because the model both HTML and iText use is based on how documents are formed. These models are designed using concepts that most people can easily understand. Most people have a basic idea of what a paragraph is and what a table does. iText uses objects such as these to simplify the creation of PDF documents and HTML uses similar objects to let you construct HTML files. If you look at both models then you'll see that there are a lot of similarities. There are so many similarities that during the design process of the iText 7 layout module we took a few ideas and implemented these into the iText model, e.g. based on the p-tag's nesting behavior it is not possible to nest Paragraph objects in iText. 

Having so much so similarities between HTML and iText objects means that the object models can easily be mapped onto one another. There are a few tags in HTML that don't make sense in the context of PDF, but most correspond to an iText equivalent. This table gives you a brief overview of the default mapping we provided:

<TODO: insert tag mapping table>
A - ATagWorker
Article - DivTagWorker
BDI - SpanTagWorker
BDO - SpanTagWorker
<TODO: insert tag mapping table>

We tried to map every HTML tag that makes in a PDF file to an iText layout object. This is easy for some objects, a span is still a span, but for other objects we had to decide that they were a modified version of what we already implemented. For instance, an "article" tag is mapped onto a "div" element. The iText 7 layout model doesn't know the concept of an article, so we mapped the article onto a div because when you squint your eyes an article is a div under a different name. Also, some tags were not included because they don't make sense in a PDF, e.g. Audio.

This blog post will give you an overview of how HTML2PDF maps the HTML model onto the iText model and how you can influence this process.


## A technical overview of the mapping process

Let's take a look at how the mapping process is handled. In the diagram "Processing nodes" you can see how HTML2PDF works.

<INSERT DIAGRAM>

There are 4 steps that HTML2PDF goes through to make an iText layout object: 

1. Create the layout object
2. Process children 
3. Apply the CSS
4. Return to the parent layout object

1. Create the layout object
The first step is the most obvious one: If an HTML tag corresponds to an iText Layout object then at one point you have to make an iText Layout object. This is done through the ITagWorker interface. We'll dive into the TagWorkers later on in this post.

2. Process children
HTML2PDF processes the tags depth-firt, meaning that if this tag has any child tags it will loop over all the child tags and go through this flow again with the child tags before finishing the flow for the current tag. Meaning that if a child tag has more children, it will go over them as well, and so on.

3. Apply the CSS
Aftr processing the child tags, HTML2PDF will apply the CSS to the iText Layout object. This is done through CSS Appliers.

4. Return to parent
The Layout object is now complete. It contains its child elements and its CSS is also applied. This object will now be returned to its parent tag for further handling.

TODO use example to demonstrate the 4 steps. use flow created by Samuel, the stack diagram.


### ITagWorker, ITagWorkerAssemblers, and ITagWorkerFactory

The actual processing of the HTML tags into iText objects happens in the ITagWorker implementations. We've provided a standard implementation for every HTML tag we support. 

It is important to know that for each tag the HtmlProcessor encounters, by default a new TagWorker instance will be created. 

insert graphic on how TagWorkers work

brief explanation on how they work


### customizing the mapping

intro on why to customize the mapping

show example on how to map a random tagworker onto a random tags

talk about better use cases: lead up to custom tagworkers



custom tagworkers

intro sample

show code to implement a custom tagworker

explain different methods to implement


## outro

summarize, yupla
