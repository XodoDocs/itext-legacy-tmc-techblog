# pdf2Data : Whitepaper

## Brief

### What is it?

pdf2Data is a framework recognizing data inside PDF documents that are based on the same template such as an invoice coming from the same supplier.
This makes it easier to automate your template document workflow and drastically cut your processing time. 
pdf2Data also allows you to cut down on both human error and response time, by automating your processes in a rule based way.

The data recognition is based on a number of rules, which need to be defined in advance per each template field. Typical rules are:
* the same (horizontal / vertical) position on the page
* the same font size and style
* certain text pattern (numeric, currency sign, etc)
* certain keywords on the same as the required field
* certain cell(s) in the table
* ..

From a technical side:

* This means that you can create a fully automated solution for data recognition in PDF document with basic set-up on the original sample template. 
The template relies on dynamic field selectors such as font, styl,e position and text patterns to find the required fields in your data. 
It also leverages iText text extraction, which offers a high fidelity recognition process.
 
* pdf2Data also comes with a convenient web application to enable you to define the selectors in a more intuitive wa than the previous method of relying on a PDF commenting tool such as Adobe Reader to define the selectors in special syntax.

* It is also easilty integrated with pure Java API with CLI (command line interface) and REST interfaces.
 
* It makes use of iText text extraction, which renders high fidelity recognition of your data.

### How does it work?

The whole recognition is based on the following steps:

1. Select parts of the template that correspond to your data fields using the pdf2Data web application or any PDF Viewer with commenting functionality. 
2. Define relevant rules for the correct data extraction in the comment attached to each selection. 
3. Upload the template to our web site and see if we recognized your fields and data inside them. 
4. Upload any other PDF document that is based on the same template and check if we were able to recognize your data.

Steps 1 to 3 need to be done only once per template. Step 4 can be repeated for as many documents as needed. But they all need to be based on the same template. 

## Simple example

![Figure 1: pdfSweep example input document](Images/pdf2data_workflow_simple_001.png)
**Figure 1**: landing page of pdf2Data web application

![Figure 2: pdfSweep example input document](Images/pdf2data_workflow_simple_002.png)
**Figure 2**: an example selector to extract the customer address

![Figure 3: pdfSweep example input document](Images/pdf2data_workflow_simple_003.png)
**Figure 3**: the template, opened in Adobe Acrobat

![Figure 4: pdfSweep example input document](Images/pdf2data_workflow_simple_004.png)
**Figure 4**: sample extraction

## A more in-depth look

### About the selectors

<table style='table-layout:fixed;width:366pt'>
    <tr>
    <td>fontFamily</td>	
    <td>This selector extracts the font name of the annotated text region and then uses this font name to filter glyphs with that font name. It is assumed that all the annotated text of the region has the same font family.</td>
    </tr>
    
    <tr>
    <td>fontSize</td>
    <td>This selector extracts the font size of the annotated text region and then uses this font size to filter glyphs with that font size. It is assumed that all the annotated text of the region has the same font size.</td>
    </tr>
    
    <tr>
    <td>fontStyle</td>
    <td>This selector extracts the font style of the annotated text region, e.g. bold, italic, and then uses this font style to filter glyphs with this font style. It is assumed that all the annotated text of the region has the same font style.</td>
    </tr>
    
    <tr>
    <td>font</td>
    <td>The font selector identifies the font used for the text in the selector region and extracts all symbols with the same font from the PDF document. 
        If the text in the selector region uses several fonts, only the first one is used. 
        The font is considered as a combination of the font family, font size and font style. 
        If only some of these properties should be honoured when extracting text, please use selectors fontFamily, fontSize, fontStyle
        </td>
        </tr>            
</table>    

### Typical usecase : processing an invoice

### Boilerplate code

## Practical guidelines

## Deploying your own pdf2Data web application

## Using the Command Line Interface (CLI)

### Preprocessor

### Parser