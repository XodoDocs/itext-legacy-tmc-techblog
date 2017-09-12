# iText DITO

## The need and solution

In electronic document workflows, users who design a template for data capture are usually not involved in designing the post-processing formats and work flows. This disconnect is unfortunate as in most situations, the business user who knows which information they need from the customer, also knows how the processed output should look like. iText DITO will change this and merge this into a single work flow: The business user creates a web form and can see what it will look like on a smart phone or tablet, and how it will be shown in a PDF document. This allows for a faster design process and for a more consistent look and feel across different forms and documents.

iText DITO provides a full layer of design and connectivity elements on top of the iText 7 platform (core, add-ons, third party products). It includes all necessary elements for interactive design of e-forms and templates, as well as subsequent processing of the inserted data and output documents. Business users discover an easy and user-friendly design environment, for an interactive and non-technical design approach.

The actual PDF output - as well as the layout differences in HTML on different screen sizes - can easily be previewed in the designer tool. One of the few things missing from HTML is a reliable way to capture the data and render it in a consistent paginated form. This consistent paginated form has always been part of the PDF specification and contributed to the success of PDF. However, there is no easy or ready made solution that allows you interactively and dynamically capture user data while keeping the consistency in the processed file. iText DITO will bridge that gap, providing an interactive designer tool to define content templates in HTML which can also reliably be converted to a predefined layout in PDF. 

## Components

iText DITO consists of two components:

[FIXME: needs to be revisited once we have a better view on the designer]
Designer: A front-end designer tool which allows business users to create templates and define data bindings and an underlying template theme, you could compare this to the company stationary. This tool allows the user to view the template in a number of common display layouts: computer, tablet, and smartphone. The template will resize and reflow for all screen sizes.

DITO SDK: the back-end tool that will execute the data binding definitions on a given data source (e.g. database) and convert the template to the requested output format. This SDK accepts an input file and a data source, and will output either an HTML file or a PDF document.

## Technical workflow

[FIXME: needs to be revisited once we have a better view on the designer]
A power user with a background in web design creates a theme that will be used as the default background layout for a number of templates; in this way, a theme is comparable to company stationary. This user can make full use of responsive HTML design features in order to make the templates look great on all output formats from a full HD desktop to a smart phone screen. In the theme, they can insert a specific template instruction to define where the dynamic content will be placed in the HTML file, and hence in its visual representation.

A business user then visually designs a number of templates, positioning the dynamic data fields in a structure of their choice. They can use a number of demo data sets during this design process that conforms to the structure of the data that will be used by the actual document generation process. These sets can be used to test the templates on structural aspects, like tables with repeating data, or by creating a set of multilingual data files, the demo data can be used to test the internationalization capabilities of the templates.  

The output of the design process in the front-end is a template file, which can then be deployed on the DITO Server. The back-end engine will transform the template into fully compliant HTML, including the data that is retrieved from your data sources. This HTML output can either be a static web page, such as a web representation of an invoice, or an interactive form for data capture. Alternatively, the template can be converted further to PDF, and can be served to end users and/or sent to file storage/archiving. 

## Add-ons

iText DITO leverages the proven strengths of iText 7 to provide high-quality output documents at the end of the document workflow. In addition to the iText 7 core library, you can also make use of the iText 7 add ons that are available. You can seamlessly integrate Indic languages using the pdfCalligraph add-on or convert your templates to ZUGFeRD compliant documents using the pdfInvoice add-on. The possibilities are endless.

## Conclusion

iText DITO merges two related flows into a single flow that's easy to maintain and control. Its easy-to-use designer will allow you to output high quality and consistent documents, while being highly extensible and customizable because of the SDK it offers and the underlying potential of iText 7 and its add-ons.
