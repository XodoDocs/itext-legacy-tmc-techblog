# html2pdf: down the rabbit hole

## Introduction

Depending on your use case, you may need to provide some configuration to html2pdf. In this post,
I will attempt to explain why you may need to use the config options, and how to use them.

## Basics

The default way to use html2pdf is either one of two basic one-line code samples:

```java
// "input.html" is the source (input) file
// "output.pfd" is the target (output) file
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

Even though the first two parameters may differ, there is always the optional third parameter `ConverterProperties`.
This parameter contains the basic configuration options that most users will probably be satisfied with.
We will now elaborate on these options so that you can configure your html2pdf code.

### baseUri

If the HTML file requires any external resources, such as a standalone CSS file or image files,
then html2pdf file needs to know where these are located.
That location may either be a URI on the local file system or online.

By default, it will use the same location as the input file,
if you use the overload of `convertToPdf` with a File parameter.
If you use the direct HTML String parameter, then the default will be the folder where the code is executed.

If neither of these applies for you, then you will need to define the default resource location root.
Then, any references in the HTML file will start from that path to find the resource.
It is possible to use the `../` syntax to go up in the directory tree,
but for other purposes, the specified folder acts as the root location.
So e.g. `<img src="static/img/logo.png"/>` and `<img src="/static/img/logo.png"/>` will both refer to the same file.

### mediaDeviceDescription

You can specify the kind of viewport your original file was designed for.

### tagWorkerFactory

If you want to define custom rules for existing HTML tags,
like handling them in a nonstandard way or as a no-op, or even implement your own tags,
then you can create a bespoke ITagWorker implementation that will execute logic defined by you.
After you implement this interface or extend an existing implementation,
you still need to register it with html2pdf so that it knows what to call.

This can easily be achieved by extending `DefaultTagWorkerFactory` and overriding the following method:

```
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

```
FontProvider dfp = new DefaultFontProvider(true, false, false);
dfp.addFont("/path/to/MyFont.ttf");
new ConverterProperties().setFontProvider(dfp);
```