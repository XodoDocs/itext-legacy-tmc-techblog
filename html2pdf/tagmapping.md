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
The first step is the most obvious one: If an HTML tag corresponds to an iText Layout object then at one point you have to make an iText Layout object. This is done through the ITagWorker interface. We'll dive into the TagWorkers later on in this post.
2. Process children  
HTML2PDF processes the tags depth-first, meaning that if this tag has any child tags it will loop over all the child tags and go through this flow again with the child tags before finishing the flow for the current tag. Meaning that if a child tag has more children, it will go over them as well, and so on.
3. Apply the CSS  
Aftr processing the child tags, HTML2PDF will apply the CSS to the iText Layout object. This is done through CSS Appliers.
4. Return to the parent layout object  
The Layout object is now complete. It contains its child elements and its CSS is also applied. This object will now be returned to its parent tag for further handling.


TODO use example to demonstrate the 4 steps. Possibly use flow created by Samuel, the stack diagram.


### ITagWorkers and ITagWorkerFactory

The actual processing of the HTML tags into iText objects happens in the ITagWorker implementations. We've provided standard implementations for every HTML tag we support. The mapping of HTML tags onto ITagWorkers happens in the ITagWorkerFactory. Our default implementation is the DefaultTagWorkerFactory and it can easily be extended to support your own tags.

It is important to know that for each tag the HtmlProcessor encounters, by default a new TagWorker instance will be created. So, when you're creating your own custom tags, do keep in mind that it was our intent to have no state in a TagWorker.

<insert graphic on how TagWorkers work>

### customizing the mapping

While our set of tags is quite large and covers most needs, HTML2PDF doesn't support every tag. Some tags don't make sense in a PDF context or maybe you added your own custom tags to your HTML files for processing purposes. You might want to process an unsupported tag and at least add some of its content to the output PDF. Or maybe you want to map every table tag onto a span object, just because you can. Well, you can! HTML2PDF allows for some very extensive customisation concerning the mapping and there's a few options:

1. You can implement the ITagWorkerFactory interface
2. You can extend the DefaultTagWorkerFactory and override the getCustomTagWorker method

We always advise you to use the second option. This option allows you to take advantage of our own internal mapping we made. It also allows you to remap existing tags onto other ITagWorkers as the getCustomTagWorker method has priority over our internal mapping. The following code sample maps everything onto a SpanTagWorker.

```
public class CustomTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        return new SpanTagWorker(tag, context);
    }
}
```

Don't try this at home! There are more useful things to do with a custom mapping.



custom tagworkers

Let's say that in our converted PDF files we want to insert a QR Code based on the data in our database. You could insert this data into the HTML in a custom tag:

```
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

In order for this tag to be picked we'll need to write a TagWorker. This class will parse and process the information inside the tag and its attributes.

QRCodeTagWorker
```
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
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    @Override
    public IPropertyContainer getElementResult() {

        return qrCodeAsImage;
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

Because HTML2PDF doesn't know about the QRCodeTagWorker, we'll need to plug it in into our custom DefaultTagWorkerFactory implementation:
```
public class CustomTagWorkerFactory extends DefaultTagWorkerFactory {

    @Override
    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
        if ( "qr".equalsIgnoreCase(tag.name()) ) {
            return new QRCodeTagWorker(tag, context);
        }

        return null;
    }
}
```

Now, all we need to do it register this into the HTML2PDF workflow:

```
ConverterProperties converterProperties = new ConverterProperties();
converterProperties.setTagWorkerFactory(new CustomTagWorkerFactory());

HtmlConverter.convertToPdf(
        new FileInputStream("C:\\Temp\\qr\\qrcode.html"),
        new FileOutputStream("C:\\Temp\\qr\\out.pdf"),
        converterProperties);
```

Run this code sample and you'll get the following output:

TODO INSERT IMAGE OF PDF FILE


## outro

summarize, yupla
