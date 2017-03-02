# Everything everyone should know about XFA if they want to know everything there is to know about XFA



## What is XFA

XFA, or XML Forms Architecture, is one of two forms architectures in the PDF standard. XFA was originally developed by JetForm in 1999, which was acquired by Adobe in 2002. XFA has been part of the PDF specification since PDF 1.5 (2003). XFA is one of the two form technologies present in the PDF specification. XFA differs from the other technology, AcroForm, in the technology that it uses. AcroForms are built using PDF syntax and XFA Forms are built using XML, as its name implies. XFA has been included to overcome the technical limitations imposed by the PDF specification and to provide a more dynamic user interaction, for better or for worse.

### AcroForms

As mentioned earlier, AcroForms are constructed using PDF syntax. This means that its structure is very rigid and predetermined, just like the rest of PDF. When familiar with PDF syntax and PDF structures, this should feel very familiar. For the uniniated: very briefly put, a PDF file is a hug tree consisting of dictionaries. These dictionaries contain pairs of keys and values. These values can be any PDF object, even dictionaries. We give the values meaning by the context and by the keys they are associated with. In the case of AcroForms the key "/AcroForm" indicates that its associated value contains the AcroForm structure.

![Screenshot highlighting the AcroForm entry](img/001_ACROFORM.png)



### XFA

### Structure of XFA

### PDF 2.0

## How to use pdfXFA

### Filling

### Flattening

#### Customisation (fonts, javascript)

### Insertion of XFA streams into a PDF

## Pitfalls

### Recognising whether XFA is present

When processing forms in a batch, you'd like to know if a PDF file contains an XFA Form, an AcroForm, both, or no form. The following code sample detects which form technology is present. 

```
public class DetectForm {

    /**
     * Checks which (if any) form technologies are present.
     *
     * @param pdfDocument the document of which we want to know if it contains form technologies
     * @return the form technologies present in the provided document
     */
    public FormTechnology detectFormTechnologies(PdfDocument pdfDocument) {
        final PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, false);

        if ( acroForm == null ) {
            return FormTechnology.NONE;
        }

        boolean containsAcroForm = acroForm.getFormFields().size() > 0;

        final XfaForm xfaForm = acroForm.getXfaForm();

        if ( xfaForm.isXfaPresent() ) {
            if ( containsAcroForm ) {
                return FormTechnology.COMBINATION;
            } else {
                return FormTechnology.XFA;
            }
        } else {
            if ( containsAcroForm ) {
                return FormTechnology.ACROFORM;
            }
        }

        return FormTechnology.NONE;
    }

    /**
     * Utility enum to classify PDFs based on the presence or absence of Form Technologies.
     */
    public enum FormTechnology {
        ACROFORM("This PDF only contains an AcroForm"),
        XFA("This PDF only contains an XFA Form"),
        COMBINATION("This PDF contains both an XFA Form and an AcroForm"),
        NONE("This PDF doesn't contain forms.");

        FormTechnology(String message) {
            this.message = message;
        }

        private String message;

        public String getMessage() {
            return message;
        }
    }

    public static void main(String[] args) throws IOException {
        final String[] inputFiles = {
            "src/main/results/playground/forms/flat_acroform.pdf",
            "src/main/resources/pdf/forms/simple_form.pdf",
            "C:\\Temp\\pdfXFA\\xfa.pdf"};

        final DetectForm detectForm = new DetectForm();

        for (int i = 0; i < inputFiles.length; i++) {
            String inputFile = inputFiles[i];

            final FormTechnology formTechnology = detectForm.detectFormTechnologies(new PdfDocument(new PdfReader(inputFile)));

            System.out.println(inputFile);
            System.out.println(formTechnology.getMessage());
            System.out.println();
        }
    }
}
```

### Forms packet

### Digital Signatures

## Summary