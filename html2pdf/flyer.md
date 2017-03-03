Hi Gents, thanks for uploading this. I have put your draft into the flyer template (which I decided to create tonight, so I will share with everyone for future flyers) and edited a bit below.  There are some places missing information and most notably, I need your help on what example to use on the back.  I have added notes of how many words the sections should approximately be, to help give you an idea of how long the sections should be.  

Things I would like you to review and edit in particular:

1. I think the front page is in OK place, but I would like to add a bit more to the first paragraph for information.
2. Example decisions for the back page
3. Review and add feebdack or ideas for the key advantage bullets on the back.

To help you visualize how this will look, see the pdfCalligraph flyer here:http://itextpdf.com/sites/default/files/attachments/iText_flyer_pdfCalligraph_A4_0.pdf


Flyer Template  
*Name of product:* pdfHTML  
*Sub header:* an iText 7 add-on (this is always here for iText 7 add ons)  
*Title:* Leverage HTML flexibility in PDF  
*Paragraph:*  approx. 120 words 
pdfHtml is an iText7 module that allows you to easily convert HTML and accompanying CSS into beautiful PDF files. The API is easy to use, extensible, and fully customizable.  As an iText 7 add-on you can use pdfHTML for out of the box solutions on it’s own, or in conjunction with other iText 7 add ons if you want to get down to the nitty gritty with iText’s convenient document model and powerful low level functionality.
Down to details (if we make the second paragraph a separate section)  
pdfHtml provides a convenient API which allows you to convert an HTML file to a PDF file or, to a list iText elements, giving you fine control over how to parse and insert the html elements. It also supports parts of the HTML 5 and CSS 3 specification that apply in a PDF context.  

*Side Bar:*
*Title:* Customizable
Paragraph: 80 words
Using pdfHTML you can define your own HTML tags and write logic to ensure your tags are processed the way you want. We've made pdfHTML easy to expand by allowing you to extend or replace our default implementations. In addition to HTML tags, we also provide the possibility for similar operations on CSS styles and properties.  
SH-->If we include the QR code example, add the following text.  
See the back of this flyer for an example where we define our own QR code tag.

*Bottom Bar:* Learn more at www.itextpdf.com/itext7/pdfhtml

*Back Side:*

*How does it work:* 107 words
pdfHtml provides a convenience API which allows you to convert an HTML file straight to a PDF file or to a list iText elements, giving you fine control over how to parse and insert the html elements.

input.html
```
<html>
 <head>
  <title>Hello World!</title>
 </head>
 <body>
  <p>Hello World!</p>
  <p>Have a short list of things you can do with pdfHtml</p>
<ul>
    <li>Convert html to a pdf file out of the box</li>
    <li>Convert html to iText objects</li>
    <li>Define your own custom tags and how to process them</li>
    <li>Define your own ways of handling css, replacing or enhancing our default implementation</li>
</ul>
 </body>
</html>
```

Convert to file
```
HtmlConverter.convertToPdf(
        new FileInputStream("path/to/input.html"),
        new FileOutputStream("path/to/output.pdf")
);
```

![Hello World pdf Output][hello_world]

Convert to iText Elements
```
List<IElement> iTextElements = HtmlConverter.convertToElements(
        new FileInputStream("path/to/input.html")
);
```

Of these three examples- what would be most impactful on the back of a flyer? We could potentially do two, but three might be a tight fit. See pdfCalligraph flyer for sample.  
SH--> The output image for the simple sample and the QR code can be made smaller  
SH--> I'd say the simple sample gets priority, the QR code is a bit more impressive but not really standard use  
Example 1: Input  
Example 2: Convert  
Example 3: Customizable  
As an example, HTML has no tag dedicated to QR codes, but by leveraging the extensibility of pdfHTML and iText7's barcode module we can transform this piece of HTML (and accompanying Css) into pdf where the text in QR code tags is interpreted as the contents for the QR code.

*Key advantages:* 3-5 bullets
1. Easily convert HTML and CSS to PDF
2. Usable out of the box  
3. Option for fine control
4. Possible to extend default behaviour (see QR code)


*Bottom Bar:* Learn more at: www.itextpdf.com/itext7/pdfHTML


Start Original Draft Flyer from Samuel and Michael Below.
flyer pdfHtml
-------------
#pdfHTML: (Insert catchy line here) 
pdfHtml is an iText7 module that allows you to easily convert HTML and accompanying CSS into beatiful pdf files.

pdfHtml provides you with an API that is easy to use, extensible, and fully customizable. pdfHtml is built on top of iText 7 and can be used on its own or in conjuction with the rest of the iText7 modules. This flexibility allows you to choose between an easy to use out-of-the-box solutions or get down to the nitty and gritty levels using iText's convenient document model and powerful low level functionality.

pdfHtml supports the parts of the HTML 5 and CSS 3 specification that make sense in a PDF context.

## Easy to use

pdfHtml provides a convenience API which allows you to convert an HTML file straight to a PDF file or, to a list iText elements, giving you fine control over how to parse and insert the html elements.

input.html
```
<html>
 <head>
  <title>Hello World!</title>
 </head>
 <body>
  <p>Hello World!</p>
  <p>Have a short list of things you can do with pdfHtml</p>
<ul>
    <li>Convert html to a pdf file out of the box</li>
    <li>Convert html to iText objects</li>
    <li>Define your own custom tags and how to process them</li>
    <li>Define your own ways of handling css, replacing or enhancing our default implementation</li>
</ul>
 </body>
</html>
```

Convert to file
```
HtmlConverter.convertToPdf(
        new FileInputStream("path/to/input.html"),
        new FileOutputStream("path/to/output.pdf")
);
```

![Hello World pdf Output][hello_world]

Convert to iText Elements
```
List<IElement> iTextElements = HtmlConverter.convertToElements(
        new FileInputStream("path/to/input.html")
);
```

## Customizability

HTML is a flexible standard, and while it is quite extensive, it does not cover every use case or intricacy. Fortunately, using pdfHTML you can define your own tags and write the necessary logic so that they are processed the way you want. We've made pdfHTML easy to extend for this specific scenario, allowing you to extend or replace our default implementations. In addition to HTML tags, we also provide the possibility for similar operations on Css styles and properties.

As an example, HTML has no tag dedicated to QR codes, but by leveraging the extensibility of pdfHTML and iText7's barcode module we van transform this piece of HTML (and accompanying Css) into pdf where the text in qr code tags is interpreted as the contents for the QR code.

qr.html
```
<html>
<head>
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

qr.css
```
qr{
    border:solid 1px red;
    height:200px;
    width:200px;
}
```

Into this pdf
![QR code output pdf][qr_code]


//Some kind of conclusion here


[hello_world]: Images/HelloWorld_Output.png "Hello World output"

[qr_code]: Images/QRCode_Output.png "QR code example"