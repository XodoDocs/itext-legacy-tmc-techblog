# Html2Pdf: A brief introduction

## Introduction
pdfHTML is the iText 7 version of our previous XML Worker, from iText 5. This add-on gives you ability to transform HTML and CSS content into a PDF document. In the iText5 XML Worker  the number of tags and CSS declarations that were supported were limited, only supporting a subset of most the frequently used tags and properties in the context of PDF(no support for video, sound, control and animations etc.). This focus was as much a choice as it was a necessity: the iText5 rendering framework limited what tags and properties could be easily implemented. The rendering framework in iText5 grew organically over the years, it was built with the static page and drawing concept of the PDF specification in mind and not the free-flowing ways of the browsers intended to paint HTML on your screen.

Now, it is time to unveil pdfHTML, the newer, sleeker XML Worker, built on the iText7 platform. iText7 is a rewrite from the ground-up, intended to provide the same functionality as iText5 but with a more robust and easily extendable foundation. The renderer-framework in particular received some attention, and now functions by building a tree of renderers and their child-renderers, and traversing it bottom-up to perform the final lay-out operations. This approach is much better suited to dealing with HTML to PDF conversion than the old model in iText5, and allows us to implement future support for tags and CSS properties.

Like XML Worker, pdfHTML is designed to work straight out of the box, no customization or configuration necessary. Just feed the HTML and any resource into the convertor and the PDF rolls out, as seen in figure 1 and the code below:

```java
ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLocation);
HtmlConverter.convertToPdf(new FileInputStream(HTMLSource), new FileOutputStream(pdfDestination), converterProperties);
```

![Figure 1: Html2pdf high-level flow][high_flow]

To illustrate, this input HTML (link example HTML) and css (link example css) will result in this output (link output PDF).
Like XML Worker, it is still possible for the user to define their own, custom way for parsing tags and css, but that is a more advanced topic for a different blogpost.

## pdfHTML Workflow
pdfHTML works in 2 phases. In the first phase, the HTML is parsed into an internal document format. In addition to creating an hierarchical tree-structure, this step also takes care of commonly found malformed HTML, adding missing closing tags and other common mistakes. The css-sheets are parsed into an internal style-sheet as well.

In the second phase, HtmlWorker walks through the tree in a depth-first manner. Each tag is processed in 2 steps: A first processing step when the tag is encountered for the first time in the walk, and a final step after all of its children have been processed. How each tag is processed depends on which TagWorker class and CssApplier class it is mapped to. During the processing the tagWorker creates an iText layout object, then processes every child, applies css using the CssApplier instance and finally returns the lay-out object to its parent. The process is visualized in figure 2 and figure 3.

![Figure 2: Html2pdf internal flow][internal_flow]

![Figure 3: Html2pdf tag processing flow][tag_processing]

## Short Comparison to XML Worker
When comparing Html2Pdf to our old solution XML Worker in terms of output and support, Html2Pdf can be seen as XML Worker++. It has a much better support for a wider array of HTML tags and css properties. The foundation of iText7 is also better suited to any updates, changes and patches down the road. While there will not be a lot of difference in output when processing simple HTML and css, XML Worker quickly stumbles when confronted with more complex structure or certain Css properties. Examples of areas where Html2Pdf beats XML Worker: Borders on non-table elements (and in quality on table elements as well), handling of Arabic and other right-to-left scripts, support of responsive design through media queries and more.

## In conclusion
This introductory blogpost marks the release of pdfHTML, the successor to the old iText5 XML Worker. Like it's predecssor, it provides you with a powerful tool for transforming HTML and accompanying CSS into beautiful PDFs using a broad spectrum of CSS properties, even more than XML Worker, while maintaining the ease of use and extensibility of the former.

Interested in trying it yourself? Download our Free Trial (LINK) and get started today.

[high_flow]: Images/Html2Pdf_Flow.png "High Level flow"

[internal_flow]: Images/Blogpost_overview_internal_flow.png "Internal flow"

[tag_processing]: Images/Blogpost_overview_flow_tagProcessing.png "Tag processing flow"

