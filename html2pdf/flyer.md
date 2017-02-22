flyer pdfHtml
-------------

pdfHtml allows you to easily convert HTML to PDF. pdfHtml supports HTML 5 and CSS 3 when it makes sense in a PDF context. 

pdfHtml also provides you with an API that is easy to use, extensible, and fully customizable. pdfHtml is built on top of iText 7. Even when you're using pdfHtml, you can still use the convenient document model of iText or the  powerful low level API which lets you go to the hardcore syntax level of your PDF files.


## Easy to use

pdfHtml provides a convenience API which allows you to convert an HTML file to a PDF file or iText elements, allowing you to fine tune the parse elements.

input.html
```
<html>
 <head>
  <title>Hello World!</title>
 </head>
 <body>
  <p>Hello World!</p>
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

[todo: insert img of output pdf]

Convert to iText Elements
```
List<IElement> iTextElements = HtmlConverter.convertToElements(
        new FileInputStream("path/to/input.html")
);
```

## Customizability

You can customize pdfHtml in many ways. And we've made pdfHtml very easy to ext

You can add support for new tags you use in your work flow. 

qr.html
```

```

[todo: insert img of qr.pdf]

You can also change the behavior of existing tags, such as paragraphs and tables. Or you can choose to handle CSS differently. 