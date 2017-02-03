package com.itextpdf.utility.xfa;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Demey
 */
public class FlattenWithJavascriptEvents {

    public void flattenXfa(PdfReader pdfReader, String destination) throws IOException, InterruptedException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
        document.open();

        XFAFlattener xfaf = new XFAFlattener(document, writer);

        List<String> events = new ArrayList<String>();
        events.add("click");
        xfaf.setExtraEventList(events);

        xfaf.flatten(pdfReader);

        document.close();
    }
}