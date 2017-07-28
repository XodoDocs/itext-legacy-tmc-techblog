# iText DITO

## The need

In electronic document workflows, employees who design a template for data capture are usually not involved in designing the post-processing formats and workflows. iText DITO can change that: the same business user who knows which information they need from the customer, creates a web form and can see what it will look like on a smartphone on tablet, and how it will be shown in a PDF document.

## The solution

A reliable way to capture the data and render it in a consistent paginated form is the only thing lacking from HTML.
iText DITO will bridge that gap, providing an interactive designer tool to define content templates in HTML
which can also reliably be converted to a predefined layout in PDF.
The actual PDF output - as well as the layout differences in HTML on different screen sizes - can easily be previewed in the designer tool.

## Components

iText DITO consists of two components:

designer: A cloud-based front-end designer tool which allows business users to create templates and define data bindings and an underlying template theme (company stationary). This tool allows the user to view the template in a number of common display layouts: desktop, tablet, and smartphone.

DITO SDK: the back-end tool that will execute the data binding definitions on a given data source (e.g. database) and convert the template to the requested output format. This SDK accepts an input file and a data source, and will output either an HTML file or a PDF document.

## Technical workflow

A power user with a background in web design creates a theme that will be used as the default background layout for a number of templates;
in this way, a theme is comparable to company stationary.
This user can make full use of responsive HTML design features in order to make the templates look great
on all output formats from a full HD desktop to a smartphone screen.
In the theme, they can insert a specific template instruction to define where the dynamic content will be placed in the HTML file,
and hence in its visual representation.

A business user then visually designs a number of templates, positioning the dynamic data fields in a structure of their choice.
They can use a number of demo data sets during this design process that conforms to the structure of the data
that will be used by the actual document generation process.

The output of the design process in the front-end is a template file, which can then be deployed on the DITO Server.
The back-end engine will transform the template into fully compliant HTML, including the data that is retrieved from your data sources.
This HTML output can either be a static web page, such as a web representation of an invoice, or an interactive form for data capture.
Alternatively, the template can be converted further to PDF, and can be served to end users and/or sent to file storage/archiving. 

## Add-ons

DITO leverages the proven strengths of iText 7 to provide high-quality output documents at the end of the document workflow.
The PDF document creation process can be easily configured to comply with such derived standards as PDF/A,
and it’s trivial to use iText 7’s add-ons pdfCalligraph for advanced typography and pdfInvoice for accessible e-invoicing.
