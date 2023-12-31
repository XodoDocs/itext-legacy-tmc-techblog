# pdfSweep: A brief introduction

## Introduction

pdfSweep is an iText plugin that removes (redacts) sensitive information from a PDF (Portable Document Format) document. 
Confidentiality is assured, because the redacted information cannot be recovered. 
In a secure two-step process, pdfSweep deletes text and images at user-defined coordinates, or as defined by a regular expression. 
After having parsed the rendering information in the original PDF document, a new PDF document is created without the redacted content.

## Why do we need redaction?

Redaction can be useful whenever the publisher or author of a document wishes to take out certain information. Common usecases include;

* Freedom of Information Act
* General Data Protection Regulation
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

## Challenges in redacting pdf documents

This is a (well-behaved) example of raw PDF document. It tells the viewer to render the text "Appearance" and "Pilot"

```java
[a, -28.7356, p, 27.2652, p, 27.2652, e, -27.2652, a, -28.7356, r, 64.6889, a, -28.7356, n, 27.2652, c, -38.7594, e, 444] TJ
/R10 10.44 Tf
68.16 0.24 Td
[", 17.1965, P, -18.7118, i, -9.35592, l, -9.35592, o, -17.2414, t, -9.35636, ", 17.1965,  , 250] TJ
```

In general though, redacting a pdf document is significantly harder that simply covering up the data that is to be redacted because:
* Text rendering instructions do not need to appear in logical (reading) order
* Text rendering instructions do not always constitute complete words/chunks of text
* Transformations can be applied (shift, scale, font transformation, etc)
* Pdf documents can contain metadata, which should also be checked.
* Pdf documents can contain scripts. Thus adding the possibility of dynamic content.


Similar problems occur with images:
* Images can be defined as a series of drawing operations
* pdfSweep has to process all instructions and find where each shape intersects with the redaction rectangle. And has to find a neat way to avoid drawing in that rectangle.
* Images can be added to a pdf document under many formats. Only formats that iText can support can sensibly be redacted.

## An example

The basic pdfSweep workflow has just two easy steps:

* Select those parts of the document that you prefer to have redacted. 
Either by specifying the coordinates, or by inputting a regular expression that fits your needs.
We have already provided a substantial list of common regular expressions to do some of the heavy lifting for you, such as;
social security numbers, phone numbers, dates, etc

* Pass the locations to pdfSweep, or invoke pdfAutoSweep with the pattern(s) of your choice.

This is an autosweep example that redacts the words 'Alice' and 'White Rabbit' and 'Rabbit' (regardless of casing).
It marks all occurences of Alice with a pink rectangle, and all occurences of 'Rabbit' with a gray rectangle.

## Alternative example

In an alternative workflow, you can add human verification in the workflow

* Select those parts of the document that you'd prefer to have redacted

* Call AutoSweep with the method tentativeCleanUp. This will produce a document where the content was not redacted. But where all content that should be redacted is marked with an annotation. Adobe reader can manage these annotations for you. Allowing you to remove redaction annotations, add extra annotations, and add comments.

* Once you have made your final selection about which redaction annotations to keep, you can hit "apply redaction" in Adobe reader.

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

As is made clear by this example document (and the code to go with it), it is perfectly possible to define a custom color for each snippet of text to be redacted.

Similarly, you could also have specified the exact locations yourself. Following code-snippet demonstrates that usecase:

```java
String input = "iphone_user_guide_untagged.pdf";
String output = "iphone_user_guide_redacted.pdf";       

// load licensekey
LicenseKey.loadLicenseFile("itext_dev_master_license.xml");

// define rectangles
List<Rectangle> rects = Arrays.asList(  new Rectangle(60f, 80f, 460f, 65f), 
                                        new Rectangle(300f, 370f, 215f, 260f)
                                      );

// turn rectangles into cleanup locations
int pagesNum = 130;
List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<>();
for (int i = 0; i < pagesNum; ++i) {
   for (int j = 0; j < rects.size(); ++j) {
       cleanUpLocations.add(new PdfCleanUpLocation(i + 1, rects.get(j)));
   }
}

// open document
PdfDocument pdfDocument = new PdfDocument(new PdfReader(input), new PdfWriter(output));

// perform cleanup
PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDocument, cleanUpLocations);
cleaner.cleanUp();

// close document
pdfDocument.close();

```

### Images

The above examples have mostly showcased how to use pdfSweep on text. Redaction can also be performed on images. Images are automatically redacted when they intersect with the rectangles the user provided. The redacted areas are covered with a colored rectangle, and the underlying image is changed (to prevent image extraction from yielding the original image.)

![Figure 3: Tony Soprano before redaction](Images/tony_soprano_1.png)
**Figure 3**: Tony Soprano as displayed in the original pdf document

![Figure 4: Tony Soprano with redaction](Images/tony_soprano_2.png)
**Figure 4**: Tony Soprano as displayed after the eyes have been redacted to provide anonimity

![Figure 5: Extracted image of Tony Soprano](Images/tony_soprano_3.png)
**Figure 5**: Extracted image of Tony Soprano, notice that the original content is gone

## How does pdfAutoSweep work?

1. The end user can specify a regular expression, and optionally a color
2. The document is processed a first time, all instructions in the .pdf document that relate to text rendering are processed. All characters along with their bounding rectangles in the document are stored.
3. These intermediate datastructures are sorted so that all characters are now in logical (reading) order.
4. The regular expression(s) provided by the user are matched.
5. The information about where the match took place, and the bounding rectangles of the characters involved provide pdfSweep with the rectangles that need to be redacted.

![Figure 6: pdfAutoSweep workflow](Images/pdfautosweep_diagram.png)
**Figure 6**: pdfAutoSweep workflow

## Pre-configured regular expressions

Most of the regular expressions that are already configered to work with autoSweep have been copied from the O' Reilly reference work "Regular Expressions Cookbook". What follows is a short listing of some of these regular expressions and their intended matches.

| name                       | matches                                                                             |
|----------------------------|-------------------------------------------------------------------------------------|
| ROMAN_NUMERALS_STRICT      | a Roman number, matching only those numbers that are valid.                         |
| ROMAN_NUMERALS_FLEXIBLE    | a Roman number, also matching IIII (4), whereas STRICT only matches IV              |
| US_SOCIAL_SECURITY_NUMBER  | a social security number (ddd-dd-dddd)                                              |
| US_ZIP_CODE                | a US zip code (either 5 digit format, or 5+4 format)                                |
| US_CURRENCY                | an amount of US dollars                                                             |
| CANADA_ZIP_CODE            | a Canadian zip code                                                                 |
| UK_ZIP_CODE                | a UK zip code                                                                       |
|                            |                                                                                     |
| DATE_MM_DD_YYYY            | a date, specified in MM/DD/YYYY, separator can be 'space' '/' or '-'                |
| DATE_MM_DD_YYYY_HH_MM_SS   | a date, specified in MM/DD/YYYY HH:MM:SS                                            |
| DATE_DD_MM_YYYY            | a date, specified in DD/MM/YYYY, separator can be 'space' '/' or '-'                |
| DATE_DD_MM_YYYY_HH_MM_SS   | a date, specified in MM/DD/YYYY HH:MM:SS                                            |
|                            |                                                                                     |
| IPV4_ADDRESS               | an Internet Protocol (v4) address                                                   |
| IPV6_ADDRESS               | an Internet Protocol (v6) address                                                   |
| MAC_ADDRESS                | a Media Access Control address                                                      |
| EMAIL_ADDRESS              | an email address                                                                    |
| HTTP_URL                   | a url                                                                               |

## Performance

To give a brief idea of the performance of pdfSweep, we've used the iPhone user manual as a reference. We've redacted the regular expression "(i|I)Phone". You can imagine this occurs quite a lot in that particular document.

Where "copies" signifies how many times the original input document was concatenated with itself. E.g. 4 copies means the document (of 130 pages) was concatenated 4 times (resulting in 520 pages).

Here are the results

| copies |	#pages | #Input file size (Mb)  | #Milliseconds   |
|--------|---------|------------------------|-----------------|
| 1	     | 130     | 3.23                   | 6407            |
| 2	     | 260     | 5.55                   | 10253           |
| 4	     | 520	   | 11.1                   | 17946           |
| 8      | 1040    | 22.5                   | 23445           |

From this table we can clearly see pdfSweep performs linearly in relation to the size of the input document.

## Conclusion

In this whitepaper, we've briefly presented our addon pdfSweep. PdfSweep allows you to seamlessly integrate data redaction in your existing workflow. Redaction rectangles have to be defined, either by using pdfAutoSweep which uses regular expressions to search for matching text and coordinates, or by programmatically enterting the coordinates. PdfSweep will redact both text, images and metadata ensuring complete confidentiality.

Learn more at www.itextpdf.com