# html2pdf: config options

## Introduction

iText 7's new add-on html2pdf is a tool that aims to greatly simplify HTML to PDF conversion.
This is a straightforward and uniform use case, so many users will get satisfactory results with the one-line code sample below.
For more complex usage, you may need to provide some configuration to html2pdf.
In this post, I will attempt to explain why you may need to use the config options, and how to use them.

## Basics

The default way to use html2pdf is either one of two basic one-line code samples:

```java
// "input.html" is the source (input) file
// "output.pdf" is the target (output) file
HtmlConverter.convertToPdf(new File("input.html"), new File("output.pdf"));
```

or

```java
HtmlConverter.convertToPdf("<p>contents</p>", new FileOutputStream("output.pdf"));
```

There are a number of overloads for these methods, all of which (including the ones above)
end up calling the method with the below signature:

```java
static void convertToPdf(InputStream htmlStream, PdfDocument pdfDocument, ConverterProperties converterProperties)
```

## ConverterProperties

Through the various method overloads, you can specify a certain set of input parameter types,
but there is always the optional third parameter `ConverterProperties`.
This parameter contains the basic configuration options that allow users to customize handling of the input data in various ways.
We will now elaborate on these options so that you can configure your html2pdf code for optimal results.

### baseUri

If the HTML file requires any external resources, such as a standalone CSS file or image files,
then html2pdf file needs to know where these are located.
That location may either be a URI on the local file system or online.

Html2pdf will try to use a reasonable default value in case you don't specify a baseUri.
If you use a String parameter to pass your HTML, then the default value will be the folder where the code is executed.
If you use the overload of `convertToPdf` with a File parameter, it will use the same location as the input file.

If neither of these defaults is right for you, then you will need to define the default resource location root.
Then, any references in the HTML file will start from that path to find the resource.
It is possible to use the `../` syntax to go up in the directory tree,
but for other purposes, the specified URI acts as the root folder for determining paths.
So e.g. `<img src="static/img/logo.png"/>` and `<img src="/static/img/logo.png"/>` will both refer to the same file,
relative to the baseUri folder you specified or to the default value.

### mediaDeviceDescription

The viewport is the user's visible area of a web page.
The viewport varies with the device, and will be smaller on a mobile phone than on a computer screen.
Before tablets and mobile phones, web pages were designed only for computer screens,
and it was common for web pages to have a static design and a fixed size.
Then, when we started surfing the internet using tablets and mobile phones,
fixed size web pages were too large to fit the viewport.
Initially, browsers on those devices scaled down the entire web page to fit the screen.
This was a quick fix that had lots of drawbacks. As a response to this problem,
HTML5 introduced a method to let web designers take control over the viewport, through the `<meta>` tag.

With html2pdf, you can specify the kind of viewport your original file was designed for,
when it isn't specified by a `<meta>` element.

```java
// TODO: example
ConverterProperties props = new ConverterProperties();
props.setMediaDeviceDescription(new MediaDeviceDescription(MediaType.PRINT));
```
### tagWorkerFactory

If you want to define custom rules for existing HTML tags,
then you can create a bespoke ITagWorker implementation that will execute logic defined by you.
The most common use cases are to handle a tag in a nonstandard way or as a no-op,
but you can also implement a custom tag for specific purposes.
After you implement this interface or extend an existing implementation,
you still need to register it with html2pdf so that it knows what to call.

This can easily be achieved by extending `DefaultTagWorkerFactory` and overriding the following method:

```java
public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
    if (tag.name().equalsIgnoreCase("custom")) {
        return new CustomTagWorker(); // implements ITagWorker
    }
	if (tag.name().equalsIgnoreCase("p")) {
        return new CustomParagraphTagWorker(); // extends ParagraphTagWorker
    }
	// default return value should be null
	// so the default implementations can be called
    return null;
}
```

One particular use case might be to add dynamic content to your pdf, such as barcodes: in that case,
you can define `<qrcode>http://www.example.com</qrcode>` in the source HTML,
rather than having to generate an image separately.
Your custom TagWorker then leverages the iText APIs to create the QR code, and adds it to the document.

### cssApplierFactory

The exact same customization strategy can be implemented for CSS properties.
If you want to turn a certain CSS attribute into a no-op, or provide custom logic,
or create a handler for a custom attribute, then you can extend `DefaultTagWorkerFactory`,
which allows you to return a custom `ICssApplier` implementation.

### fontProvider

If you want to customize the fonts that can be used by html2pdf,
then you can define a `FontProvider` that will act as a repository for those fonts.

There is probably little reason to extend html2pdf's `DefaultFontProvider` class,
because you can configure exactly which fonts will be supplied to the PDF rendering logic.
On top of the basic behavior that you can define in the constructor,
you can also add a font that you specify as a location on the system,
a `byte[]`, or an iText `FontProgram` object.

`DefaultFontProvider` has a constructor with three boolean arguments, which allow you to specify:

* whether or not to select the 14 standard PDF fonts
* whether or not to select a number of Free fonts included with html2pdf
* whether or not to try and select all fonts installed in the system's default font folder

The boolean arguments for the default constructor are (true, true, false).
You can of course also add fonts one by one on custom locations.

```java
ConverterProperties props = new ConverterProperties();
FontProvider dfp = new DefaultFontProvider(true, false, false);
dfp.addFont("/path/to/MyFont.ttf");
props.setFontProvider(dfp);
HtmlConverter.convertToPdf("<p>contents</p>", new FileOutputStream("output.pdf"), props);
```
