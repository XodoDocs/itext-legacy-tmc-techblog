# HTML 2 PDF :: How HTML tags are mapped onto iText layout objects


## iText Layout objects

iText has had a document model for a long time. This model allows developers to quickly grasp how to use iText without having to know much about the PDF specification. The model we use closely resembles HTML. This is because the model both HTML and iText use is based on how documents are formed. These models are designed using concepts that most people can easily understand. Most people have a basic idea of what a paragraph is and what a table does. iText uses objects such as these to simplify the creation of PDF documents and HTML uses similar objects to let you construct HTML files. If you look at both models then you'll see that there are a lot of similarities. There are so many similarities that during the design process of the iText 7 layout module we took a few ideas and implemented these into the iText model, e.g. based on the p-tag's nesting behavior it is not possible to nest Paragraph objects in iText. 

The similarities between HTML and iText objects means that the object models can easily be mapped onto one another. There are a few tags in HTML that don't make sense in the context of PDF, but most correspond to an iText equivalent. This table gives you a brief overview of the default mapping we provided:

<TODO: insert tag mapping table>

As you can see not everything maps perfectly. <TODO: pick one of the things that don't map and elaborate why not and how we fixed it>

This blog post will give you an overview of how HTML2PDF maps the HTML model onto the iText model and how you can influence this process.



## A technical overview of the mapping process

### ITagWorker

tagworkers css appliers

insert graphic on how TagWorkers work

brief explanation on how they work

### mapping flow

show where the mapping happens in the entire process

<TODO: include Sam's flow diagram>

4 steps to parse all nodes

1. Create the Layout object

add text

2. Process the children if any

add text (push current to stack)

3. Apply the CSS

add text

4. Return to the parent layout object

add text


use example to demonstrate the 4 steps. use flow created by Samuel, the stack diagram.



## customizing the mapping

intro on why to customize the mapping

show example on how to map a random tagworker onto a random tags

talk about better use cases: lead up to custom tagworkers



### custom tagworkers

intro sample

show code to implement a custom tagworker

explain different methods to implement


## outro

summarize