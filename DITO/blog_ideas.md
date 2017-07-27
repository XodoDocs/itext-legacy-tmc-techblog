# Talking smack about XFA

For a long time, iText Software has been one of the few third-party companies that supported Adobe's XFA format, 
which allows PDF files to be used as dynamic forms. It is most often used in document workflows,
when users need to fill in a dynamic PDF form that can be flattened into a static PDF document after post-processing.
XFA technology has experienced a relatively low adoption rate over the years by PDF vendors,
requires one of only very few tools to create an XFA file, and has a number of drawbacks in usability.

But XFA may not be the best technology to support dynamic forms in connection with PDF after all:
HTML can fulfill most checkboxes that we need in this process, and it of course has its huge ecosystem of supporting tools.
Since the PDF document is usually the last step - the final document that is to be emailed, stored, or printed -
a document workflow tool where all interactive processing is done in HTML will be much easier to handle than an XFA workflow.
