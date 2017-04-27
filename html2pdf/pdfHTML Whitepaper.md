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

The main feature of HTML is the concept of *tags*, which denote the limits of a textual element.
For example, a piece of content that constitutes a paragraph will be surrounded by the opening and closing tags of type p:

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

Above, the letter `p` inside the `style` tag is called the selector, which signifies the condition that an HTML element must fulfill in order to get the defined CSS styles.
There are multiple kinds of conditions, and it is possible in HTML to assign a `class` and/or a unique `id` attribute to an element, in order to further differentiate.

```html
<p class="withClass">Text 1</p>
<p class="withClass" id="withId">Text 2</p>
<p class="withClass">Text 3</p>
<p class="withClass">Text 4</p>
<p class="withClass">Text 5</p>
```

You can get fine-grained control of which styles are applied by combining different element properties into a more complex selector,
e.g. selecting all but the second item.

```css
p.withClass:not(#withId) {
    background-color:yellow;
}
#withId {
    font-size: 30px;
}
```

The CSS file can be a separate file which is then referenced from the HTML file,
making it easier to use a single CSS file for a number of related documents,
or to use a combination of multiple CSS files for one HTML file.

The CSS format also has other uses, mostly tied in with other XML-based formats such as SVG.

### DOM
An essential feature of HTML, and related standards such as XML, is that they require that all elements are strictly nested in each other.
```html
<div><p>legal</p></div>
<div><p>illegal</div></p>
```
The consequence of this requirement is that you can visualize the hierarchy of an HTML document in a tree model called the Domain Object Model or DOM.

TODO: image

A browser will use the DOM to determine how the defined CSS interacts with the HTML elements,
so that it can calculate the required space for each element and apply the correct visual styling properties.
The DOM is also the basis for interactions with other concepts such as JavaScript, which will provide interactivity to a web page.

### Media queries in CSS

In HTML, it is possible to specify CSS rules depending on the media that the document will be rendered on.
The most common use case is to add some rules for printing, so that the document fits nicely on a normal-width page.

You can do this either by specifying a separate stylesheet in the HTML `<head>` tag ...

```html
<head>
  <link rel="stylesheet" type="text/css" href="theme.css">
  <link rel="stylesheet" type="text/css" href="print.css" media="print">
</head> 
```

... or by adding a `@media` annotation in your CSS rules:

```css
@media only print {
    p {
        margin-left:36px;
    }
}
@media not print {
    p {
        margin-left:216px;
    }
}
```

## pdfHTML: an in-depth look
### Basics: Tag & Css mapping
### Simple Example

### Configuration Options

Configuration settings can be enabled with the optional last parameter `ConverterProperties` of all `HtmlConverter` public methods.
This parameter contains the basic configuration options that allow users to customize handling of the input data in various ways.
We will now elaborate on these options so that you can configure your pdfHTML code for optimal results.

#### baseUri

If the HTML file requires any external resources, such as a standalone CSS file or image files,
then pdfHTML file needs to know where these are located.
That location may either be a URI on the local file system or online.

pdfHTML will try to use a reasonable default value in case you don't specify a baseUri.
If you use a String parameter to pass your HTML, then the default value will be the folder where the code is executed.
If you use the overload of `convertToPdf` with a File parameter, it will use the same location as the input file.

If neither of these defaults is right for you, then you will need to define the default resource location root.
Then, any references in the HTML file will start from that path to find the resource.
It is possible to use the `../` syntax to go up in the directory tree of your file system,
but for other purposes, the specified URI acts as the root folder for determining paths.
So e.g. `<img src="static/img/logo.png"/>` and `<img src="/static/img/logo.png"/>` will both refer to the same file,
relative to the baseUri folder you specified or to the default value.

#### fontProvider

If you want to customize the fonts that can be used by pdfHTML,
then you can define a `FontProvider` that will act as a repository for those fonts.

There is probably little reason to extend pdfHTML's `DefaultFontProvider` class,
because you can configure exactly which fonts will be supplied to the PDF rendering logic.
On top of the basic behavior that you can define in the constructor,
you can also add a font that you specify as a location on the system,
a `byte[]`, or an iText `FontProgram` object.

`DefaultFontProvider` has a constructor with three boolean arguments, which allow you to specify:

* whether or not to select the 14 standard PDF fonts
* whether or not to select a number of Free fonts included with pdfHTML
* whether or not to try and select all fonts installed in the system's default font folder

The boolean arguments for the default constructor are `(true, true, false)`.
You can of course also add fonts one by one on custom locations.

```java
ConverterProperties props = new ConverterProperties();
FontProvider dfp = new DefaultFontProvider(true, false, false);
dfp.addFont("/path/to/MyFont.ttf");
props.setFontProvider(dfp);
```

#### mediaDeviceDescription

If your input file uses media queries, then you can simply tell pdfHTML to interpret the relevant set of rules:

```java
ConverterProperties props = new ConverterProperties();
props.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));
```

The registered `MediaType` constants are:

* MediaType.ALL
* MediaType.AURAL
* MediaType.BRAILLE
* MediaType.EMBOSSED
* MediaType.HANDHELD
* MediaType.PRINT
* MediaType.PROJECTION
* MediaType.SCREEN
* MediaType.SPEECH
* MediaType.TTY
* MediaType.TV

### Customizations

There are two plugin mechanisms in pdfHtml that allow you to execute custom behavior on your HTML and CSS conversion process.
They work very similarly to one another, and are also .

Much like the configuration options specified above, registering your plugin code with pdfHtml happens through the `ConverterProperties`:

```java
ConverterProperties props = new ConverterProperties();
props.setTagWorkerFactory(new MyTagWorkerFactory()); // Custom HTML parsing
props.setCssApplierFactory(new MyCssApplierFactory()); // Custom CSS parsing
```

#### tagWorkerFactory

If you want to define custom rules for existing HTML tags,
then you can create a bespoke ITagWorker implementation that will execute logic defined by you.
The most common use cases are to handle a tag in a nonstandard way or as a no-op,
but you can also implement a custom tag for your specific purposes.
After you implement this interface or extend an existing implementation,
you still need to register it with pdfHTML so that it knows what to call.

This can be achieved by extending `DefaultTagWorkerFactory` and overriding the following method:

```java
public class MyTagWorkerFactory extends DefaultTagWorkerFactory {
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if (tag.name().equalsIgnoreCase("custom")) {
            return new CustomTagWorker(); // implements ITagWorker
        }
        if (tag.name().equalsIgnoreCase("p")) {
            return new CustomParagraphTagWorker(); // extends ParagraphTagWorker
        }
        // default return value should be either null
        // so the default implementations can be called ...
        return null;
        // ... or you can directly call the superclass method
        // for the exact same effect
        return super.getTagWorker(tag, context);
    }
}
```

One particular use case might be to add dynamic content to your pdf, such as barcodes: in that case,
you can define `<qrcode>http://www.example.com</qrcode>` in the source HTML,
rather than having to generate an image separately.
Your custom TagWorker then leverages the iText APIs to create the QR code, and adds it to the document.

TODO: add QRCode example

#### cssApplierFactory

By default, pdfHTML will only execute CSS logic on the standard HTML tags.
If you have defined a custom HTML tag and you want to apply CSS to it,
then you will also have to write an `ICssApplier` and register it by extending `DefaultTagWorkerFactory`.

Similarly, you may want to change the way a standard HTML tag reacts to a CSS property.
In that case, you can extend the `ICssApplier` for that tag and write custom logic.

TODO: add color-blindness example

### Integration with existing add-ons
pdfCalligraph example
### Advanced Example: Accessible pdf creation
## Roadmap
## Comparison to XMLWorker
