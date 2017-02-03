package com.itextpdf.utility.xfa;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;
import com.itextpdf.tool.xml.xtra.xfa.font.XFAFontSettings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Demey
 */
public class FlattenWithFontMap {

    public void flattenXfa(PdfReader pdfReader, String destination) throws IOException, InterruptedException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destination));
        document.open();

        Map<String, String> fontsMap = new HashMap<String, String>();

        fontsMap.put("MyriadPro-Regular", "ARIALUNI.TTF");
        fontsMap.put("ArialMT", "ARIALUNI.TTF");
        fontsMap.put("Arial-BoldMT", "ARIALUNI.TTF");
        fontsMap.put("Arial-BoldItalicMT", "ARIALUNI.TTF");

        XFAFontSettings xfafs = new XFAFontSettings("C:\\Windows\\Fonts", true, fontsMap);

        XFAFlattener xfaf = new XFAFlattener(document, writer, xfafs);
        xfaf.flatten(pdfReader);

        document.close();
    }
}