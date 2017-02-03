package com.itextpdf.utility.xfa;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.XfaForm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Michael Demey
 */
public class Filling {

    public void fillXfa(String src, String outputPath, String xfaData) throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream(outputPath);
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, fos);
        AcroFields form = stamper.getAcroFields();
        XfaForm xfa = form.getXfa();
        xfa.fillXfaForm(new FileInputStream(xfaData));
        stamper.close();
    }
}