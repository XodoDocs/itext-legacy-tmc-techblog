# pdfHTML: config options

## Introduction

iText 7's new add-on pdfHTML is a tool that aims to greatly simplify HTML to PDF conversion in Java or .NET.
This is a straightforward and uniform use case, so many users will get satisfactory results with the one-line code sample below.
For more complex usage, you may need to provide some configuration to pdfHTML.
In this post, I will attempt to explain why you may need to use the config options, and how to use them.

## Basics

The default way to use pdfHTML is either one of two basic one-line code samples:

```java
HtmlConverter.convertToPdf(new File("input.html"), new File("output.pdf"));
```

or

```java
HtmlConverter.convertToPdf("<p>input</p>", new FileOutputStream("output.pdf"));
```

There are several overloads for these methods, all of which (including the ones above)
end up calling the method with the below signature:

```java
static void convertToPdf(InputStream htmlStream, PdfDocument pdfDocument, ConverterProperties converterProperties)
```

## ConverterProperties

Through the various method overloads, you can specify certain input parameter types in the first two arguments,
but there is always the optional third parameter `ConverterProperties`.
This parameter contains the basic configuration options that allow users to customize handling of the input data in various ways.
We will now elaborate on these options so that you can configure your pdfHTML code for optimal results.

### baseUri

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

### tagWorkerFactory

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

And of course, you'll have to register the implementation in the `ConverterProperties`:

```java
ConverterProperties props = new ConverterProperties();
props.setTagWorkerFactory(new MyTagWorkerFactory());
```

One particular use case might be to add dynamic content to your pdf, such as barcodes: in that case,
you can define `<qrcode>http://www.example.com</qrcode>` in the source HTML,
rather than having to generate an image separately.
Your custom TagWorker then leverages the iText APIs to create the QR code, and adds it to the document.
This use case has been expanded upon in the blog post about tag mappings *TODO: insert blog post link*.

### cssApplierFactory

By default, pdfHTML will only execute CSS logic on the standard HTML tags.
If you have defined a custom HTML tag and you want to apply CSS to it,
then you will also have to write an `ICssApplier` and register it by extending `DefaultTagWorkerFactory`.

Similarly, you may want to change the way a standard HTML tag reacts to a CSS property.
In that case, you can extend the `ICssApplier` for that tag and write custom logic.

### fontProvider

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

### mediaDeviceDescription

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

If your input file uses this feature, then you can simply tell pdfHTML to interpret the relevant set of rules:

```java
ConverterProperties props = new ConverterProperties();
props.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));
```
