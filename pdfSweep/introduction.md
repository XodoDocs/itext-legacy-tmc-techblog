# pdfSweep: A brief introduction

## Introduction

pdfSweep is an iText plugin that removes (redacts) sensitive information from a PDF (Portable Document Format) document. 
Confidentiality is assured, because the redacted information cannot be recovered. 
In a secure two-step process, pdfSweep deletes text and images at user-defined coordinates, or as defined by a regular expression. 
After having parsed the rendering information in the original PDF document, a new PDF document is created without the redacted content.

## pdfSweep Workflow

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
CompositeLocationExtractionStrategy composite = new CompositeLocationExtractionStrategy();			// a Composite strategy acts as a collection of other strategies
composite.add(new RegexMarkingStrategy("Alice", Color.PINK));										// redact all occurences of the word 'Alice' with a pink marker
composite.add(new RegexMarkingStrategy("((w|W)hite (r|R)abbit)|( rabbit)|(Rabbit)", Color.GRAY));	// redact all occurences of 'White Rabbit' (with some variations on case) with a gray marker

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