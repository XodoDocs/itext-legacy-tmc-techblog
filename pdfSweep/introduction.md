# pdfSweep: A brief introduction

## Introduction

pdfSweep is an iText plugin that removes (redacts) sensitive information from a PDF (Portable Document Format) document. 
Confidentiality is assured, because the redacted information cannot be recovered. 
In a secure two-step process, pdfSweep deletes text and images at user-defined coordinates, or as defined by a regular expression. 
After having parsed the rendering information in the original PDF document, a new PDF document is created without the redacted content.

## Why do we need redaction?

* Freedom of Information Act
* Government declassification procedures
* Data that would endanger the privacy of others
    * Social security numbers
    * Phone numbers
    * Bank account details
    * Name/Firstname of people in a clinical trial
* Proprietary information
* Trade secrets
* ..

## A short history of redaction

In the past redaction meant printing a document, blacking out the necessary information and making a photocopy of the document. That way, any information that is covered by dark ink simply does not get copied. This worked because paper is a WYSIWYG format. There is no hidden data, no metadata that needs to be erased.

![Figure 0: Example redacted document](Images/example_redacted_document.png)
**Figure 0**: Example redacted document

A .pdf document contains instructions for rendering the document in a viewer. Adding an instruction to draw a black rectangle does not erase text-rendering instructions underneath the rectangle.
This essentially means that covering up information no longer works, and actual removal/replacement is needed. Otherwise text-extraction (or simply copy/pasting the text from your viewer to a text editor) would yield the redacted data.

## An example

The pdfSweep workflow has just two easy steps:

* Select those parts of the document that you prefer to have redacted. 
Either be specifying the coordinates, or by inputting a regular expression that fits your needs.
We have already provided a substantial list of common regular expressions to do some of the heavy lifting for you, such as;
social security numbers, phone numbers, dates, etc

* Once a strategy has been defined, any document that matches this template can be redacted. Simply pass the locations to pdfSweep, or invoke pdfAutoSweep with the pattern(s) of your choice.

```java

// load the license needed to be able to run pdfSweep
LicenseKey.loadLicenseFile(licenceFile);

// set input and output file
String input = "AliceInWonderland.pdf";
String output = "AliceInWonderland_redacted.pdf";

// define a strategy
CompositeLocationExtractionStrategy strategy = new CompositeLocationExtractionStrategy();			// a Composite strategy acts as a collection of other strategies
strategy.add(new PatternLocationExtractionStrategy("Alice").setRedactionColor(Color.PINK));                                       // redact all occurences of the word 'Alice' with a pink marker
strategy.add(new PatternLocationExtractionStrategy("((w|W)hite (r|R)abbit)|( rabbit)|(Rabbit)").setRedactionColor(Color.GRAY));   // redact all occurences of 'White Rabbit' (with some variations on case) with a gray marker

// load the document
PdfDocument pdf = new PdfDocument(new PdfReader(input), new PdfWriter(output));

// sweep
PdfAutoSweep autoSweep = new PdfAutoSweep(composite);
autoSweep.cleanUp(pdf);

// close the document
pdf.close();
```

This is the original document:

![Figure 1: pdfSweep example input document](Images/pdfsweep_input_document.png)
**Figure 1**: pdfsweep original input document

And this is after redaction:

![Figure 2: pdfSweep example output document](Images/pdfsweep_output_document.png)
**Figure 2**: pdfsweep redacted output document

As is made clear by this example document (and the code to go with it), it is perfectly possbile to define a custom color for each snippet of text to be redacted.

## How does it work?

1. The end user can specify a regular expression, and optionally a color
2. The document is processed a first time, all instructions in the .pdf document that relate to text rendering are processed. All characters along with their bounding rectangles in the document are stored.
3. These intermediate datastructures are sorted so that all characters are now in logical (reading) order.
4. The regular expression(s) provided by the user are matched.
5. The information about where the match took place, and the bounding rectangles of the characters involved provide pdfSweep with the rectangles that need to be redacted.

![Figure 3: pdfAutoSweep workflow](Images/pdfautosweep_diagram.png)
**Figure 3**: pdfAutoSweep workflow

