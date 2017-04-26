# pdfHTML: Whitepaper
## Introduction 
### What is it?
pdfHTML is an add-on module on the iText7 framework that transforms HTML and accompanying CSS into a PDF Documents.
Built for creation of pdf files from html-templates, pdfHTML allows you to automate pdf generation for documents like internal reports, tickets, invoices and more.

pdfHTML provides you with the means for creating beatiful and fucntional pdfs
without forcing your designers to learn the complex pdf syntax or the intracies of the iText7 framework.
They simply need to utilize their common, well-honed HTML and CSS skills to create the template,
and pdfHTML takes care of the rest, staying as close as possible to the way the HTML is rendered inside a browser.
### How to use it (Basic Example)

## Input formats

### HTML & CSS
HTML is a markup language that allows content creators to make simple human-readable files with explicit semantic metadata in the form of tags.
This metadata is then used by a HTML viewing application to determine where on the screen the content will be shown.
The HTML file in itself is a plain-text file utilizing a number of specific constructs, and it is a HTML viewer's responsibility to portray the content faithfully.
It is most prominently used for markup of web pages, and as such the primary HTML viewer applications are browsers.

The main feature of HTML is the concept of *tags*, which denote the limits of a textual element. For example, a piece of content that constitutes a paragraph will be surrounded by the opening and closing tags of type p:

```html
<p>This content constitutes a paragraph</p>
```

A list is a wrapper around a number of list items:

```html
<ul>
  <li>This is the first list item</li>
  <li>This is the second list item</li>
</ul>
```

Similar constructs exist for smaller units of text, for tables, headers, images, videos, hyperlinks, sections,
and other common semantic divisions that can be made in a web page's content.

Browsers will interpret these tags to layout and display the document,
using a number of reasonable defaults that will result in a readable document.
However, that would result in all web pages basically looking the same structurally.
Changing the visual properties of an element is relatively easy:
for any tag, you can add attributes to set a property to a non-default value.

```html
<p style="background-color:yellow;">Text</p>
```

This looks easy, but becomes problematic for longer documents and especially when editing files,
and when you want a certain layout to be consistent over a number of separate HTML documents.

CSS can solve this problem by allowing pooling of the stylistic properties of HTML tags,
so that they're defined in one place and will apply to all attributes of a certain type.
As such, long documents don't need a lot of manual styling if the CSS is well-written.

An easy example can show the basic use case for CSS.
If no style sheets are used, then all styling must happen in the HTML `style` attribute.

```html
<p style="color:red;font-size:16;background-color:yellow;">Text 1</p>
<p style="color:red;font-size:16;background-color:yellow;">Text 2</p>
<p style="color:red;font-size:16;background-color:yellow;">Text 3</p>
<p style="color:red;font-size:16;background-color:yellow;">Text 4</p>
<p style="color:red;font-size:16;background-color:yellow;">Text 5</p>
```

When using a style sheet, you can define all style attributes once and have them defined on every part of the HTML content that fulfills the condition.
That implies that there is only one place where all style attributes are defined, which makes it easier to analyze problems and to change a property for all instances.

```html
<style>
p {
    color:red;
    font-size:16;
    background-color:yellow;
}
</style>
<p>Text 1</p>
<p>Text 2</p>
<p>Text 3</p>
<p>Text 4</p>
<p>Text 5</p>
```

Above, the letter `p` inside the `style` tag signifies the condition that an HTML element must fulfill in order to get the defined CSS styles.

The CSS file can be a separate file which is then referenced from the HTML file, making it easier to use a single CSS file for a number of related documents, or to use a combination of multiple CSS files for one HTML file.

The CSS format also has other uses, mostly tied in with other XML-based formats such as SVG.

### DOM
TODO: Hierarchical tree model, easy for depth-first searches.

## pdfHTML: an in-depth look
### Basics: Tag & Css mapping
### Simple Example
### Configuration Options
### Customizations
### Integration with existing add-ons
pdfCalligraph example
### Advanced Example: Accesible pdf creation
## Roadmap
## Comparison to XMLWorker
