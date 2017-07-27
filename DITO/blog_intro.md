# iText DITO

## The need

For a long time, iText Software has been one of the few third-party companies that supported Adobe's XFA format, 
which allows PDF files to be used as dynamic forms. It is most often used in document workflows,
when users need to fill in a dynamic PDF form that can be flattened into a static PDF document after post-processing.
XFA technology has experienced a relatively low adoption rate over the years by PDF vendors,
requires one of only very few tools to create an XFA file, and has a number of drawbacks in usability.

But XFA may not be the best technology to support dynamic forms in connection with PDF after all:
HTML can fulfill most checkboxes that we need in this process, and it of course has its huge ecosystem of supporting tools.
Since the PDF document is usually the last step - the final document that is to be emailed, stored, or printed -
a document workflow tool where all interactive processing is done in HTML will be much easier to handle than an XFA workflow.

## The solution

A reliable way to capture the data and render it in a consistent paginated form is the only thing lacking from HTML.
iText DITO will bridge that gap, providing an interactive designer tool to define content templates in HTML
which can also reliably be converted to a predefined layout in PDF.
The actual PDF output - as well as the layout differences in HTML on different screen sizes - can easily be previewed in the designer tool.

## Technical workflow

A power user with a background in web design creates a theme that will be used as the default background layout for a number of templates;
in this way, a theme is comparable to company stationary.
This user needs to have knowledge of how to create a responsive HTML design
which will look great on all output formats from a full HD desktop to a smartphone screen.
In the theme, they must insert a specific template instruction to define where the dynamic content will be placed in the HTML file,
and hence in its visual representation.

A business user then visually designs a number of templates, positioning the dynamic data fields in a structure of their choice.
They can use demo data during this design process that conforms to the structure of the data
that will be used by the actual document generation process.

The output of the design process in the front-end is a template file, which can then be deployed on the DITO Server.
The back-end engine will transform the template into fully compliant HTML, including the data that is retrieved from your existing database.
This HTML output can either be a static web page, such as a web representation of an invoice, or an interactive form for data capture.
This HTML file can be converted further to PDF with iText 7’s add-on tool pdfHtml,
and either or both can be served to end users and/or sent to file storage/archiving. 

## Add-ons

DITO leverages the proven strengths of iText 7 to provide high-quality output documents at the end of the document workflow.
The PDF document creation process can be easily configured to comply with such derived standards as PDF/A,
and it’s trivial to use iText 7’s add-ons pdfCalligraph for advanced typography and pdfInvoice for accessible e-invoicing.

## Roadmap

iText DITO is currently undergoing heavy development.
