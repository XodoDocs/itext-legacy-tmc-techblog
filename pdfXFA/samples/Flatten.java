package com.itextpdf.utility.xfa;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Michael Demey
 */
public class Flatten {

    public void flattenXfa(PdfReader pdfReader, String destination) throws IOException, InterruptedException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
        document.open();

        XFAFlattener xfaf = new XFAFlattener(document, writer);
        xfaf.flatten(pdfReader);

        document.close();
    }
}