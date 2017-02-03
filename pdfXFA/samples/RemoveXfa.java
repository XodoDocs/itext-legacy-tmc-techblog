package com.itextpdf.utility.xfa;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Michael Demey
 */
public class RemoveXfa {

    public void removeXFA(PdfReader reader, String output) throws IOException, DocumentException {
        reader.getAcroFields().removeXfa();
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(output));
        stamper.close();
    }
}