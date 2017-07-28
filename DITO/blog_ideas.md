## Talking smack about XFA

For a long time, iText Software has been one of the few third-party companies that supported Adobe's XFA format, 
which allows PDF files to be used as dynamic forms. It is most often used in document workflows,
when users need to fill in a dynamic PDF form that can be flattened into a static PDF document after post-processing.
XFA technology has experienced a relatively low adoption rate over the years by PDF vendors,
requires one of only very few tools to create an XFA file, and has a number of drawbacks in usability.

But XFA may not be the best technology to support dynamic forms in connection with PDF after all:
HTML can fulfill most checkboxes that we need in this process, and it of course has its huge ecosystem of supporting tools.
Since the PDF document is usually the last step - the final document that is to be emailed, stored, or printed -
a document workflow tool where all interactive processing is done in HTML will be much easier to handle than an XFA workflow.

## Roadmap

iText DITO is currently undergoing heavy development,
and its supported features will increment until a full product suite arises.
In its first release that went live in July 2017,
DITO supports the basic use case of output generation based on a visual template,
including repeating data in a basic set of pluggable components.

Starting with its October 2017 release, DITO will also support customizable pluggable components.
For the January 2018 release, the groundwork will be done for supporting input forms,
by implementing customizable data validation and presentation formats for structured data.
Then, starting from its scheduled April 2018 release, DITO will include full support
for interactivity with input forms whose data can be saved into a database.
