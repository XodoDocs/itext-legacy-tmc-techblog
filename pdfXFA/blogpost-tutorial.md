# pdfXfa
pdfXfa is the successor to XFA Worker. It allows you to process and finalize XFA forms. [todo: add padding]


## loadlicensekey  
Before we begin we'll need to load the license file. This is done using our license key library. Make sure to use version 2 and higher. These version are meant to be used in tandem with iText 7 products, while versions 1.x.y are meant to be used with iText 5.  

```  
LicenseKey.loadLicenseFile(pathToFile)  
```  

If this throws an exception, then your key is either invalid or corrupt. Either way, contact us to resolve this issue. If it didn't throw an exception, your key has been loaded and it should work perfectly.  

You can acquire a trial license key on our trials page: http://pages.itextpdf.com/iText-7-Free-Trial-Landing-Page-1.html

## fill  
Filling a forms means that you wish to enter values into a form in an automated way. You can use pdfXFA to fill XFA Forms. To fill a form we'll need a few things:  

- XFA Form
- XML stream containing the data
- OutputStream to which we want to write the filled form

Filling an XFA form can be done using the following code:  

```  
InputStream pdf = new FileInputStream("path/to/file.pdf");  
InputStream xfa = new FileInputStream("path/to/data.xml");  
OutputStream out = new FileOutputStream("path/to/output.pdf");  
  
XFAFiller xfaFiller = new XFAFiller();  
  
xfaFiller.fillXfa(pdf, xfa, out);  
```  

There's a few overloaded methods for this functionality. Use the one that fits best for your use case.

## flatten  
Flattening a form means that you remove the functionality of the form but keep the visual layout intact. This can be seen as a way to finalize a form. You can use pdfXFA to flatten your files. All you need to provide is the input XFA form and an OutputStream. We provide a convenience class to make it easier for you to flatten your files. 

```  
final FileInputStream xfaInputStream = new FileInputStream("path/to/data.xml");  
final FileOutputStream pdfOutputStream = new FileOutputStream("path/to/output.pdf");  

final XFAFlattener xfaFlattener = new XFAFlattener();  
xfaFlattener.flatten(xfaInputStream, pdfOutputStream, properties);  
```  

There are ways to customize this process. This will be done through the use of XFAFlattenerProperties.

### flattenproperties  

pdfXfa allows you to customize the mapping process in many ways. Customization is done through the XFAFlattenerProperties class. This post will try to give you a brief overview of the more important options. 

#### PDF/A

pdfXfa can flatten to PDF/A compliant files. To do so, you'll need to set a few things on the XFAFlattenerProperties object.

- PdfAConformanceLevel
- OutputIntent

Normally you need to set the tagging and the XMP Metadata, but if you chose a PdfAConformanceLevel that needs tagging, pdfXfa will automatically enable it for you. Metadata is always turned on for PDF/A. If you don't require PDF/A, but you do want Metadata or tagged PDF, then you can still turn them on.

```
XFAFlattenerProperties properties = new XFAFlattenerProperties()
                .setConformanceLevel(PdfAConformanceLevel.PDF_A_1B)
                .setOutputIntent(new FileInputStream(icc), ocId, oc, registryName, ocInfo);
                
XFAFlattener xfaFlattener = new XFAFlattener();
xfaFlattener.flatten(xfaInputStream, pdfOutputStream, properties);
```

#### javascript

XFA Forms can contain JavaScript. pdfXFA can execute JavaScript inside XFA forms when flattening. There is one catch and that is that you can't declare which specific event you want to trigger. You can only provide pdfXFA with the trigger you wish to activate.

```
XFAFlattener xfaFlattener = new XFAFlattener();

List<String> javaScriptTriggers = new ArrayList<String>();
javaScriptTriggers.add("click");

xfaFlattener.setExtraEventList(javaScriptTriggers);

xfaFlattener.flatten(new FileInputStream("path/to/input.pdf"), new FileOutputStream("path/to/output.pdf"));
```

#### font customization

Sometimes you'd like to map the fonts used in an XFA form onto other fonts that are available on the system.  pdfXFA provides an easy to map these fonts onto each other. You provide the XFAFlattener a Map containing entries mapping one font onto an other:

```
XFAFlattener xfaFlattener = new XFAFlattener();

Map<String, String> fontMapping = new HashMap<String, String>();
fontMapping.put("font1", "font2");

XFAFlattenerProperties properties = new XFAFlattenerProperties();
properties.setXfaFontSettings(new XFAFontSettings(fontMapping));

xfaFlattener.flatten(new FileInputStream("path/to/input.pdf"), new FileOutputStream("path/to/output.pdf"), properties);
```

Other settings like font embedding or the path to the fonts folder can be set on the XFAFontSettings object.

## Outro