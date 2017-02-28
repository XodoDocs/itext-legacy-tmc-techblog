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

HTML is a flexible standard, and while it is quite extensive, it does not cover every use case or intricacy. Fortun
You can customize pdfHtml in many ways. And we've made pdfHtml very easy to ext

You can add support for new tags you use in your work flow. 

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

[todo: insert img of qr.pdf]

You can also change the behavior of existing tags, such as paragraphs and tables. Or you can choose to handle CSS differently. 


[hello_world]: Images/HelloWorld_Output.png "Hello World output"