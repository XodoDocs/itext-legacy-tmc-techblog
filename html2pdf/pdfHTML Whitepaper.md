# pdfHTML: Whitepaper

## Introduction

### What is it?

pdfHTML is an add-on module for the iText7 framework that transforms HTML and accompanying CSS into a PDF Document.
Built for creation of PDF files from HTML templates, pdfHTML allows you to automate PDF generation for documents like internal reports, tickets, invoices, and more.

pdfHTML provides you with the means for creating beautiful and functional PDFs
without forcing your designers to learn the complex PDF syntax or the intricacies of the iText7 framework.
They simply need to utilize their common, well-honed HTML and CSS skills to create the template,
and pdfHTML takes care of the rest, staying as close as possible to the way the HTML is rendered inside a browser.

### How to use it

pdfHTML is usable out of the box, converting HTML and CSS into PDFs with minimal configuration and code required. 
```java 
ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLoc);
HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
```
The code above converts the HTML found at the `htmlSource` to a PDF stored at `pdfDest`. Both `htmlSource` and `pdfDest` are strings representing absolute paths. Any external resources not referenced using a http hyperlink will be resolved using the path stored in `resourceLoc`. 

## Input formats

### HTML & CSS

HTML is a markup language that allows content creators to make simple human-readable files with explicit semantic metadata in the form of tags.
This metadata is then used by a HTML viewing application to determine where on the screen the content will be shown.
The HTML file in itself is a plain-text file utilizing a number of specific constructs, and it is a HTML viewer's responsibility to portray the content faithfully.
It is most prominently used for markup of web pages, and as such the primary HTML viewer applications are browsers.

The main feature of HTML is the concept of *tags*, which denote the limits of a textual element.
For example, a piece of content that constitutes a paragraph will be surrounded by the opening and closing tags of type `p`:

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

Browsers will interpret these tags to a visual layout and display the document,
using a number of reasonable defaults that will result in a readable rendering.
However, that would result in all web pages basically looking the same structurally.
Changing the visual properties of an element is relatively easy:
for any tag, you can add attributes to set an HTML property to a non-default value.

```html
<p style="background-color:yellow;">Text</p>
```

This looks easy, but becomes problematic for longer documents and especially when editing files,
or when you want a certain layout to be consistent over a number of separate HTML documents.

CSS can solve this problem by allowing pooling of the stylistic properties of HTML tags,
so that they're defined in one place and will apply to all elements of a certain type.
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

When using a style sheet, you can define all style attributes once and have them defined on every part of the HTML content that fulfills a certain condition.
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

![Figure 1: DOM Example][DOM]
**Figure X:** DOM Example

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
On a conceptual level, pdfHTML works by mapping HTML tags to iText7 layout objects, and CSS property declarations to iText layout properties.
On a practical level, this process happens through the use of Tagworkers and Cssappliers. Each HTML tag is mapped to a TagWorker and CSSApplier, and those classes contain the necessary logic to process the tag, selecting the iText layout object it corresponds to and applying any necessary CSS.
When processing the HTML DOM, pdfHTML walks through the tree in a depth-first manner, starting the translation on a tag, and then recursively processing all its children, ending the processing when all it's children have been processed.
The visualization of this process can be seen in figures A and B

![Figure A: pdfHTML internal flow][internal_flow]  
**Figure A**: pdfHTML high level flow

![Figure B: pdfHTML tag processing flow][tag_processing]  
**Figure B**: pdfHTML tag processing

### Simple Example

For a very basic example on how to use pdfHTML, we will use the html for the DOM example, and enrich it with some simple CSS.
```html
<html>
	<head>
	<link rel="stylesheet" type="text/css" href="simple.css"/>
</head>
<body>
	<p>456</p>
	<div>123</div>
</body>
</html>
```
```css
div{
    color: red;
}

```

We will write the output directly to a PDF file, using the following code:
```java
ConverterProperties converterProperties = new ConverterProperties();
HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
```

The resulting pdf:

![Figure X: pdfHTML output][SimplePDF]
**Figure X:** pdfHTML output


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
then you can define a `FontProvider` object that will act as a repository for those fonts.

There is a standard implementation available in the pdfHTML project which is called `DefaultFontProvider`.
There is probably little reason to extend this class, because you can configure exactly which fonts should be supplied to the PDF rendering logic.
On top of the basic behavior that you can define in the constructor, you can also add a font that you specify as a location on the system,
a `byte[]`, or an iText `FontProgram` object.

`DefaultFontProvider` has a constructor with three boolean arguments, which allow you to specify (in this order):

* whether or not to select the 14 standard PDF fonts
* whether or not to select a number of Free fonts included with pdfHTML
* whether or not to try and select all fonts installed in the system's default font folder(s)

There is also a default no-argument constructor for `DefaultFontProvider`;
the values of the boolean arguments for the default constructor are `(true, true, false)`.
In addition to this, you can of course also add fonts one by one on custom locations.

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

* `MediaType.ALL`
* `MediaType.AURAL`
* `MediaType.BRAILLE`
* `MediaType.EMBOSSED`
* `MediaType.HANDHELD`
* `MediaType.PRINT`
* `MediaType.PROJECTION`
* `MediaType.SCREEN`
* `MediaType.SPEECH`
* `MediaType.TTY`
* `MediaType.TV`

### Customizations

There are two plugin mechanisms in pdfHTML that allow you to execute custom behavior on your HTML and CSS conversion process.
They work very similarly to one another.

Much like the configuration options specified above, registering your plugin code with pdfHTML happens through the `ConverterProperties`:

```java
ConverterProperties props = new ConverterProperties();
props.setTagWorkerFactory(new MyTagWorkerFactory()); // Custom HTML parsing
props.setCssApplierFactory(new MyCssApplierFactory()); // Custom CSS parsing
```

#### tagWorkerFactory

If you want to define custom rules for existing HTML tags,
then you can create a bespoke `ITagWorker` implementation that will execute logic defined by you.
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

One particular use case might be to add dynamic content to your PDF, such as barcodes: in that case,
you can define `<qrcode>http://www.example.com</qrcode>` in the source HTML,
rather than having to generate an image separately.
Your custom TagWorker then leverages the iText APIs to create the QR code, and adds it to the document.


The input
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>QRCode Example</title>
    <link rel="stylesheet" type="text/css" href="qrcode.css"/>
</head>
<body>
<p>QR Code below,Q </p>
<qr charset="Cp437" errorcorrection="Q">
With great power comes great current squared times resistance
</qr>

<p>QR Code below, L</p>
<qr charset="Cp437" errorcorrection="L">
    With great power comes great current squared times resistance
</qr>
</body>
</html>
```
```css
qr{
    border:solid 1px red;
    height:200px;
    width:200px;
}
```
The output without a custom tagworker

![Figure X: Output with no custom tagworker][QRCode_noTagworker]
Figure X: Output with no custom tagworker

The output with a custom tagworker

![Figure X: Output with custom tagworker][QRCode]
Figure X: Output with custom tagworker

The custom tagworker itself

```java
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.html.node.IElementNode;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Image;

import java.util.HashMap;
import java.util.Map;

public class QRCodeTagWorker implements ITagWorker {
    private static String[] allowedErrorCorrection = {"L","M","Q","H"};
    private static String[] allowedCharset = {"Cp437","Shift_JIS","ISO-8859-1","ISO-8859-16"};
    private BarcodeQRCode qrCode;
    private Image qrCodeAsImage;

    public QRCodeTagWorker(IElementNode element, ProcessorContext context){
        //Retrieve all necessary properties to create the barcode
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //Character set
        String charset = element.getAttribute("charset");
        if(checkCharacterSet(charset )){
            hints.put(EncodeHintType.CHARACTER_SET, charset);
        }
        //Error-correction level
        String errorCorrection = element.getAttribute("errorcorrection");
        if(checkErrorCorrectionAllowed(errorCorrection)){
            ErrorCorrectionLevel errorCorrectionLevel = getErrorCorrectionLevel(errorCorrection);
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        }
        //Create the QR-code
        qrCode = new BarcodeQRCode("placeholder",hints);

    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        //Transform barcode into image
        qrCodeAsImage = new Image(qrCode.createFormXObject(context.getPdfDocument()));
    }
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        //Add content to the barcode
        qrCode.setCode(content);
        return true;
    }
    @Override
    public IPropertyContainer getElementResult() {
        return qrCodeAsImage;
    }
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    private static boolean checkErrorCorrectionAllowed(String toCheck){
        for(int i = 0; i<allowedErrorCorrection.length;i++){
            if(toCheck.toUpperCase().equals(allowedErrorCorrection[i])){
                return true;
            }
        }
        return false;
    }
    private static boolean checkCharacterSet(String toCheck){
        for(int i = 0; i<allowedCharset.length;i++){
            if(toCheck.equals(allowedCharset[i])){
                return true;
            }
        }
        return false;
    }
    private static ErrorCorrectionLevel getErrorCorrectionLevel(String level){
        switch(level) {
            case "L":
                return ErrorCorrectionLevel.L;
            case "M":
                return ErrorCorrectionLevel.M;
            case "Q":
                return ErrorCorrectionLevel.Q;
            case "H":
                return ErrorCorrectionLevel.H;
        }
        return null;

    }
}

```

The custom tagWorkerFactory

```java
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.html.node.IElementNode;

public class QRCodeTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if(tag.name().equals("qr")){
            return new QRCodeTagWorker(tag, context);
        }
        return super.getTagWorker(tag, context);
    }
}

```

#### cssApplierFactory

By default, pdfHTML will only execute CSS logic on the standard HTML tags.
If you have defined a custom HTML tag and you want to apply CSS to it,
then you will also have to write an `ICssApplier` and register it by extending `DefaultTagWorkerFactory`.

Similarly, you may want to change the way a standard HTML tag reacts to a CSS property.
In that case, you can extend the `ICssApplier` for that tag and write custom logic.

As an example, we have written a Cssapplier that simulates various forms of colourblindness. This is done by applying mathematical transforms to the CSS colour values while processing the input (all done in memory, so the input files are unchanged).

Input html and CSS
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Rainbow</title>
    <link rel="stylesheet" type="text/css" href="rainbow.css"/>
</head>
<body>
<div class="purple">purple</div>
<div class="mediumpurple">medium purple</div>
<div class="darkviolet">dark purple</div>
<div class="violet">violet</div>
<div class ="darkblue"> dark blue</div>
<div class="blue">blue</div>
<div class="lightblue">light blue</div>
<div class="darkgreen">dark green</div>
<div class = "green">green</div>
<div class="lightgreen"> light green</div>
<div class="yellow">yellow</div>
<div class="orange">orange</div>
<div class="red">red</div>
<div class = "darkred"> dark red</div>
</body>
</html>
```
```css
.darkred{
    background-color:darkred;
}
.red {
    background-color:red;
}
.orange{
    background-color:orange;
}
.yellow{
    background-color:yellow;
}
.green{
    background-color:green;
     color:white;
}
.darkgreen{
    background-color:darkgreen;
     color:white;
}
.lightgreen{
    background-color:lightgreen;
}
.darkblue{
    background-color:darkblue;
    color:white;
}
.lightblue{
    background-color:lightblue;
}
.blue{
    background-color:blue;
     color:white;
}
.violet{
    background-color:violet;
     color:white;
}
.purple{
    background-color:purple;
     color:white;
}
.mediumpurple{
    background-color:mediumpurple;
    color:white;
}
.darkviolet{
    background-color:darkviolet;
    color:white;
}
```
Standard output

![Figure X: standard output][Rainbow]

Figure X: standard output

Using custom CSS appliers

![Figure X: output simulating achromatomaly][Rainbow_achromatomaly]

Figure X: output simulating achromatomaly

![Figure X: output simulating deuteranopia][Rainbow_deuteranopia]

Figure X: output simulating deuteranopia
 
 
Cssapplier code
```java
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier;
import com.itextpdf.html2pdf.html.node.IStylesContainer;
import com.itextpdf.kernel.color.WebColors;

import java.util.Map;

/**
 * Css applier extending from a blockcssapplier that transforms standard colours into the ones colourblind people see
 */
public class ColourBlindBlockCssApplier extends BlockCssApplier {
    private static final double RGB_MAX_VAL = 255.0;
    private String colourBlindness = ColourBlindnessTransforms.PROTANOPIA;

    /**
     * Set the from of colour blindness to simulate.
     * Accepted values are Protanopia, Protanomaly, Deuteranopia, Deuteranomaly, Tritanopia, Tritanomaly, Achromatopsia, Achromatomaly.
     * Default value is Protanopia
     * @param colourBlindness
     */
    public void setColourBlindness(String colourBlindness){
        this.colourBlindness = colourBlindness;
    }

    @Override
    public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker){
        Map<String, String> cssStyles = stylesContainer.getStyles();
        if(cssStyles.containsKey(CssConstants.COLOR)){
            String newColor = TransformColor(cssStyles.get(CssConstants.COLOR));
            cssStyles.put(CssConstants.COLOR,newColor);
            stylesContainer.setStyles(cssStyles);
        }
        if(cssStyles.containsKey(CssConstants.BACKGROUND_COLOR)){
            String newColor = TransformColor(cssStyles.get(CssConstants.BACKGROUND_COLOR));
            cssStyles.put(CssConstants.BACKGROUND_COLOR,newColor);
            stylesContainer.setStyles(cssStyles);
        }
        super.apply(context, stylesContainer,tagWorker);

    }

    private String TransformColor(String originalColor){
        //Array creation is a bit clumsy, could do with some cleaning
        float[] rgbaColor = WebColors.getRGBAColor(originalColor);
        float[] rgbColor = {rgbaColor[0],rgbaColor[1],rgbaColor[2]};
        float[] newColourRgb = ColourBlindnessTransforms.simulateColourBlindness(colourBlindness,rgbColor);
        float[] newColourRgba = {newColourRgb[0],newColourRgb[1],newColourRgb[2],rgbaColor[3]};
        double[] newColorArray = scaleColorFloatArray(newColourRgba);
        String newColorString = "rgba("+(int) newColorArray[0]+","+(int)newColorArray[1]+","+(int)newColorArray[2]+","+newColorArray[3]+")";
        return newColorString;
    }

    private double[] scaleColorFloatArray(float[] colors){
        double red = (colors[0]*RGB_MAX_VAL);
        double green =  (colors[1]*RGB_MAX_VAL);
        double blue =  (colors[2]*RGB_MAX_VAL);
        double[] res = {red,green, blue, (double) colors[3]};
        return res;
    }
```

CssApplierFactory Code
```java
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.html2pdf.html.node.IElementNode;

public class ColourBlindnessCssApplierFactory extends DefaultCssApplierFactory {

    private String toSimulate;

    public ColourBlindnessCssApplierFactory(String toSimulate){
        this.toSimulate = toSimulate;
    }
    @Override
    public ICssApplier getCustomCssApplier(IElementNode tag) {
        if(tag.name().equals(TagConstants.DIV)){
            ColourBlindBlockCssApplier applier =  new ColourBlindBlockCssApplier();
            applier.setColourBlindness(toSimulate);
            return applier;
        }

        if(tag.name().equals(TagConstants.SPAN)){
            ColourBlindSpanTagCssApplier applier =  new ColourBlindSpanTagCssApplier();
            applier.setColourBlindness(toSimulate);
            return applier;
        }

        return null;
    }
}
``` 
 
#### createAcroForm

Since version 1.0.1, pdfHTML supports the `<form>` tag and its constituents `<input>`, `<button>`, `<textarea>`, etc.
They can be shown in the PDF file as either their visual rendering or as actual form fields in the AcroForm format.
AcroForms are a way to use a PDF file as an input form with a fixed layout.

The createAcroForm option will allow you to leverage the AcroForm capability of the PDF format.
By default this boolean value is set to `false`, so your document will not be editable as a PDF form.

### Integration with existing iText add-on

pdfHTML integrates seamlessly with pdfCalligraph, iText's advanced typography module,
which is needed in order to correctly show text in complex alphabets such as Arabic and Hindi.
Users of that module know that leveraging it in an application which uses iText requires absolutely no extra code:
if the pdfCalligraph module is available on the classpath at the time that the PDF instructions are written,
then its code will be used to create correct writing in the relevant writing system.

This also holds true for pdfHTML: all you need is the JAR and a license file for pdfCalligraph,
and your HTML parsing code will work out of the box.

### Advanced Example: Accessible PDF creation

With minimal effort, pdfHTML can be configured to generate PDFs that comply to the accesibility standards Pdf/A an Pdf/UA, by leveraging the both the autmate

## Roadmap

## Comparison to XMLWorker
###pdfHTML
Supports more HTML tags & CSS features such as:
<abbr>
floating & fixed positioning
@media

More easily extensible for custom tags

Integrates seamlessly with other iText functionality such as:
Barcodes
PDF/A
pdfCalligraph

Does not require XHTML to function
XHTML is a very strict subset of HTML

###XMLWorker
XMLWorker development
Initially designed to be top-to-bottom and text line based
Does not mesh well with block level elements

XMLWorker limitations
No generic handling of styles
No support for CSS3, media queries (e.g. screen vs print)
Nesting of divs, tables gives problems
Brittle handling of imperfect input (e.g. tags that are not closed properly)
No convenient extension mechanism (custom HTML elements, custom CSS handling)
--> Even small extensions requires the pipeline to be build from scratch



[DOM]: Images/DOM_Example.png "DOM Example"

[internal_flow]: Images/Blogpost_overview_internal_flow.png "Internal flow"

[tag_processing]: Images/Blogpost_overview_flow_tagProcessing.png "Tag processing flow"

[SimplePDF]: Images/simple_trimmed.png "Simple output"

[QRCode_noTagworker]: Images/QRCode_text_trimmed.png "QRCode output without custom tag"

[QRCode]: Images/QRCode_image_trimmed.png "QRCode output"

[Rainbow]: Images/rainbow_base_trimmed.png "Rainbow"

[Rainbow_achromatomaly]: Images/rainbow_achromatomaly_trimmed.png "Rainbow achromatomaly"

[Rainbow_deuteranopia]: Images/rainbow_deuteranopia_trimmed.png "Rainbow deuteranopia"