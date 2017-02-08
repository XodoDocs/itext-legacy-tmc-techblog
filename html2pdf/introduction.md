# Html2Pdf: A brief introduction

## Introduction
When we released iText 7 in <insert release month>, it did not support all features provided in iText5 yet. Among the features that did not make initial release was the ability to transform html and css content into a pdf document. In iText5, we have XmlWorker for this use-case. But the number of tags and css declarations that XmlWorker supports is limited, XmlWorker supports the subset of most frequently used tags and properties that make sense in a pdf-context(so no videos, control and animations etc.). This focus was as much a choice as it was a necessity: the iText5 rendering framework limited what tags and properties could be easily implemented. The rendering framework in iText5 grew organically over the years, it was built with the static page and drawing concept of the pdf specification in mind and not the free-flowing ways of the browsers intended to paint html on your screen.

Now, it is time to unveil <Html2Pdf official product name, my money's on PdfCoil>, the newer, sleeker XmlWorker, build using iText7. iText7 is a from the ground-up rewrite of iText5, intended to provide the same functionality but with a more robust, more easily extendable foundation. The renderer-framework in particular received some attention, and now functions by building a tree of renderers and their child-renderers, and traversing it bottom-up to perform the final lay-out operations. This approach is much better suited to dealing with html to pdf conversion than the old model in iText5, and allows us to implement future support for tags and css properties.

Like XmlWorker, <Html2pdf> is designed to work straight out of the box, no customization or configuration necessary. Just feed the html and any resource into the convertor and the pdf rolls out, as seen in figure 1 and the code below:

```java
ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLocation);
HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDestination), converterProperties);
```

![Figure 1: Html2pdf high-level flow][high_flow]

To illustrate, this input html <link example html> and css <link example css> will result in this output <link output pdf>.
Like XmlWorker, it is still possible for the user to define their own, custom way for parsing tags and css, but that is a more advanced topic for a different blogpost.

## <Html2Pdf> Workflow

<Html2Pdf> works in 2 phases. In the first phase, the html is parsed into an internal document format. In addition to creating an hierarchical tree-structure, this step also takes care of commonly found malformed html, adding missing closing tags and other common mistakes. The css-sheets are parsed into an internal style-sheet as well.

In the second phase, HtmlWorker walks through the tree in a depth-first manner. Each tag is processed in 2 steps: A first processing step when the tag is encountered for the first time in the walk, and a final step after all of its children have been processed. How each tag is processed depends on which TagWorker class and CssApplier class it is mapped to. During the processing the tagWorker creates an iText layout object, then processes every child, applies css using the CssApplier instance and finally returns the lay-out object to its parent. The process is visualized in figure <flow> and figure <node process flow>. 

![Figure 2: Html2pdf internal flow][internal_flow]

![Figure 3: Html2pdf tag processing flow][tag_processing]

## Short Comparison to XmlWorker
When comparing Html2Pdf to our old solution XmlWorker in terms of output and support, Html2Pdf can be seen as XmlWorker++. It has a much better support for a wider array of html tags and css properties. The foundation of iText7 is also better suited to any updates, changes and patches down the road. While there will not be a lot of difference in output when processing simple html and css, XmlWorker quickly stumbles when confronted with more complex structure or certain Css properties. Examples of areas where Html2Pdf beats XmlWorker: Borders on non-table elements (and in quality on table elements as well), handling of Arabic and other right-to-left scripts, support of responsive design through media queries, ...

[high_flow]: Images/Html2Pdf_Flow.png "High Level flow"

[internal_flow]: Images/Blogpost_overview_internal_flow.png "Internal flow"

[tag_processing]: Images/Blogpost_overview_flow_tagProcessing.png "Tag processing flow"