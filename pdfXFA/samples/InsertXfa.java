package com.itextpdf.utility.xfa;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.XfaForm;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Michael Demey
 */
public class InsertXfa {

    public void insertXfa(PdfReader reader, PdfWriter writer, String file)
            throws DocumentException, IOException,
            ParserConfigurationException, SAXException {

        PdfDictionary root = reader.getCatalog();
        PdfDictionary acro = root.getAsDict(PdfName.ACROFORM);
        // Add acroform dictionary
        if (acro == null) {
            acro = new PdfDictionary();
            root.put(PdfName.ACROFORM, reader.addPdfObject(acro));
        }

        // Construct XFA array
        PdfObject tmp = acro.get(PdfName.XFA);
        if (tmp == null) {
            root.put(new PdfName("NeedsRendering"), PdfBoolean.PDFTRUE);

            PdfArray ar = new PdfArray();

            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setNamespaceAware(true);
            DocumentBuilder db = fact.newDocumentBuilder();
            Document doc = db.parse(new FileInputStream(file));

            Map<String, Node> xfaNodes = XfaForm.extractXFANodes(doc);

            // Placeholder for preamble
            ar.add(new PdfString("preamble"));
            ar.add((PdfObject) null);

            // Add all nodes in the XFA array
            for (Map.Entry<String, Node> entry : xfaNodes.entrySet()) {
                String nodename = entry.getKey();
                Node node = entry.getValue();
                PdfStream stream = new PdfStream(
                        XfaForm.serializeDoc(node));
                stream.flateCompress(writer.getCompressionLevel());
                ar.add(new PdfString(nodename));
                ar.add(writer.addToBody(stream).getIndirectReference());

                // By removing all children we can easily extract the preamble
                // and postamble afterwards
                doc.getChildNodes().item(0).removeChild(node);
            }

            // Extract preamble and postamble
            String prepost = new String(XfaForm.serializeDoc(doc));
            int pos = prepost.indexOf("</");
            String preamble = prepost.substring(0, pos).trim();
            String postamble = prepost.substring(pos).trim();

            // Insert preamble in the placeholder space
            PdfStream preStream = new PdfStream(preamble.getBytes());
            preStream.flateCompress(writer.getCompressionLevel());
            ar.set(1, writer.addToBody(preStream).getIndirectReference());

            // Add postamble
            PdfStream postStream = new PdfStream(postamble.getBytes());
            postStream.flateCompress(writer.getCompressionLevel());
            ar.add(new PdfString("postamble"));
            ar.add(writer.addToBody(postStream).getIndirectReference());

            // Add XFA array to acroform dictionary
            acro.put(PdfName.XFA, new PdfArray(ar));
        }
    }

}