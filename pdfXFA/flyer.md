Flyer Template  
Name of product: pdfXFA  
Sub header: an iText 7 add-on   
Title: Dynamic PDF XFA forms for an optimal user experience  
Paragraph:  approx. 120 words  
PDF files are invaluable to automating document processes. XFA is one of the form technologies available in PDF and it allows you to provide your users with a better user experience. Based on XML, XFA Forms allow your PDF forms to grow, shrink, and restructure themselves at runtime, depending on the data entered and user interaction. This allows for a more dynamic user interaction than you find in AcroForms, the classic PDF forms.  pdfXFA allows you to finalize these forms by flattening them and offers an easy to use an API that gives you customization options to make the right fit for your needs.  
In practice:   
In an XFA form, your PDF file can generate new fields based on user responses and automatically restructure them. This user interactivity allows you to serve your users with a custom version of each form depending on their specific answers and needs.  

Side Bar:  
Title: Flattening  
Paragraph: 80 words  
When you receive a filled out XFA Form and you don't want to edit it any further, you can opt to use pdfXFA to flatten your dynamic forms and turn them into static PDF files, ready to be archived. Flattening is the process that removes the functionality and user interactivity of a form but keeps the visual layout and structure. It is a way of finalizing your forms and securing their content.  

Bottom Bar: Learn more at www.itextpdf.com/itext7/pdfXFA  

Back Side:  

How does it work: 107 words  

XFA is based on a set of XML streams. pdfXFA will read these streams and weave them together to get a representation of how the form would look like when rendered. It will then leverage the flexibility and power of the iText APIs to generate a PDF file that looks the same as the XFA form.

Of these examples- what would be most impactful on the back of a flyer? See pdfCalligraph flyer for sample.  
```
List<String> javaScriptEvents = new ArrayList<String>();
javaScriptEvents.add("click");
        
XFAFlattener xfaFlattener = new XFAFlattener();

xfaFlattener
        .setFlattenerProperties(
                new XFAFlattenerProperties()
                        .createXmpMetaData()
                        .setPdfVersion(XFAFlattenerProperties.PDF_1_7)
                        .setTagged())
        .setExtraEventList(javaScriptEvents)
        .setFontSettings(
                new XFAFontSettings()
                        .setEmbedExternalFonts(true)
                        .setFontsPath(fontsDirectory))
        .flatten(xfa, pdf);
```

Key advantages: 3-5 bullets  
1. Take advantage of XFA dynamic forms  
2. Secure your dynamic forms  
3. Simple API for customization  
Bottom Bar: Learn more at: www.itextpdf.com/itext7/pdfXFA  









Original draft below:
# Dynamic PDF XFA forms for an optimal user experience

Dynamic PDF files are invaluable to automating document processes. XFA Forms allow your PDF forms to grow and reformat themselves depending on the data entered, allowing for a more dynamic user interaction than AcroForms allow. For example: In a form, your PDF file can generate new fields based on user responses and automatically fill them if you set the proper parameters. This user interactivity allows you to serve your users with a custom version of each form depending on their specific answers and needs.

When you receive a filled out XFA Form and you don't want to edit it any further, you can opt to use pdfXFA. pdfXFA allows you to flatten your dynamic forms and turn them into static PDF files, ready to be archived. Flattening is the process that removes the functionality and user interactivity of a form but keeps the visual layout and structure. It is a way of finalizing your forms.

pdfXFA offers a very easy to use API that has a lot of customization options.

## General options

pdfXFA allows you to set all kinds of general properties. It even allows you to flatten to PDF/A.

```
XFAFlattener xfaFlattener = new XFAFlattener();

xfaFlattener
        .setFlattenerProperties(
                new XFAFlattenerProperties()
                        .createXmpMetaData()
                        .setPdfVersion(XFAFlattenerProperties.PDF_1_7)
                        .setTagged())
        .flatten(xfa, pdf);
```

## Customizable font settings

pdfXFA allows you to set the following options concerning fonts. 

- font embedding
- font substitution
- give priority to the fonts used in the iText environment

```
XFAFlattener xfaFlattener = new XFAFlattener();

xfaFlattener
        .setFontSettings(
                new XFAFontSettings()
                        .setEmbedExternalFonts(true)
                        .setFontsPath(fontsDirectory))
        .flatten(xfa, pdf);
```

## Simulate user interaction

XFA Forms can contain a lot of Javascript to support the interactivity and to modify the form structure at time of rendering. pdfXFA allows you to simulate these Javascript events while flattening:

```
List<String> javaScriptEvents = new ArrayList<String>();
javaScriptEvents.add("click");
        
XFAFlattener xfaFlattener = new XFAFlattener();

xfaFlattener
        .setExtraEventList(javaScriptEvents)
        .flatten(xfa, pdf);
```

