package eu.panco.ptprinter;

import android.os.Environment;
import android.printservice.PrintDocument;
import android.printservice.PrintJob;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.panco.ptprinter.receipt.ReceiptDocument;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class PrintDocumentParser {

    public static ReceiptDocument parse(PrintDocument document) {
        ReceiptDocument receipt = new ReceiptDocument("");
        return receipt;
    }

    public static ReceiptDocument parse(PrintJob printJob) {
        String parsedText = null;
        PDFParser parser;
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        FileDescriptor fd = printJob.getDocument().getData().getFileDescriptor();
        FileInputStream fis = new FileInputStream(fd);

        try {
            parser = new PDFParser(fis);
        } catch (IOException e) {
            System.err.println("Unable to open PDF Parser. " + e.getMessage());
            return null;
        }
        try {
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;
            File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + dateFormat.format(date) + ".pdf");
            pdDoc.save(file);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (Exception e) {
            System.err
                    .println("An exception occured in parsing the PDF Document."
                            + e.getMessage());
        } finally {
            try {
                if (cosDoc != null)
                    cosDoc.close();
                if (pdDoc != null)
                    pdDoc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ReceiptDocument(parsedText);
    }

    private static String parsePdfToString(File file) {
        String parsedText = null;
        PDFParser parser;
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        try {
            parser = new PDFParser(new FileInputStream(file));
        } catch (IOException e) {
            System.err.println("Unable to open PDF Parser. " + e.getMessage());
            return null;
        }
        try {
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdfStripper.setSuppressDuplicateOverlappingText(false);
            pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (Exception e) {
            System.err.println("An exception occured in parsing the PDF Document." + e.getMessage());
        } finally {
            try {
                if (cosDoc != null) cosDoc.close();
                if (pdDoc != null) pdDoc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return parsedText;
    }

    public static ReceiptDocument parse(File file) {
        String receiptString = parsePdfToString(file);
        ReceiptDocument receipt = new ReceiptDocument(receiptString);
        return receipt;
    }
}
