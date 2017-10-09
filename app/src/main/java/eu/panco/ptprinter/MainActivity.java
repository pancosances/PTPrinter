package eu.panco.ptprinter;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

//import org.apache.pdfbox.util.PDFBoxResourceLoader;

import org.apache.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;

import eu.panco.ptprinter.printerservice.PrinterQueue;
import eu.panco.ptprinter.receipt.ReceiptDocument;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PDFBoxResourceLoader.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void parsePdfClicked(View v) {
        System.out.println("CLICKED");
        PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter pa = new PrintDocumentAdapter() {
            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

            }

            @Override
            public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {

            }
        };
        PrintJob pj = pm.print("TestDocument", pa, null);

        /*File file = new File(Environment.getExternalStorageDirectory() + "/Download/VRP_receipt_detail.pdf");
        ReceiptDocument receipt = PrintDocumentParser.parse(file);
        PrinterQueue.getQueue().add(receipt.getListBytes());*/
    }

}
