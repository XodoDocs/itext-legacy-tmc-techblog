## loadlicensekey  
Before we begin we'll need to load the license file. This is done using our license key library. Make sure to use version 2 and higher. These version are meant to be used in tandem with iText 7 products, while versions 1.x.y are meant to be used with iText 5.  

{code}  
LicenseKey.loadLicenseFile(pathToFile)  
{code}  

If this throws an exception, then your key is either invalid or corrupt. Either way, contact us to resolve this issue. If it didn't throw an exception, your key has been loaded and it should work perfectly.  

## fill  
You can use pdfXFA to fill XFA Forms. To fill a form we'll need a few things:  

- XFA Form
- XML stream containing the data

Filling an XFA form can be done using the following code:  

{code}  
InputStream pdf = new FileInputStream("path/to/file.pdf");  
InputStream xfa = new FileInputStream("path/to/data.xml");  
OutputStream out = new FileOutputStream("path/to/output.pdf");  
  
XFAFiller xfaFiller = new XFAFiller();  
  
xfaFiller.fillXfa(pdf, xfa, out);  
{code}  

There's a few overloaded methods for this functionality. Use the one that fits best for your use case.

## flatten  
You can use pdfXFA to flatten your files. You can use our XFAFlattener convenience class to do so. 

{code}  
final FileInputStream xfaInputStream = new FileInputStream("path/to/data.xml");  
final FileOutputStream pdfOutputStream = new FileOutputStream("path/to/output.pdf");  

final XFAFlattener xfaFlattener = new XFAFlattener();  
xfaFlattener.flatten(xfaInputStream, pdfOutputStream, properties);  
{code}  

### flattenproperties  

Several properties can be set.