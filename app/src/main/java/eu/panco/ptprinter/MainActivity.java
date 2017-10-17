package eu.panco.ptprinter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;

//import org.apache.pdfbox.util.PDFBoxResourceLoader;

import org.apache.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import eu.panco.ptprinter.bluetoothservice.BluetoothService;
import eu.panco.ptprinter.printerservice.PrintDocument;
import eu.panco.ptprinter.printerservice.PrinterQueue;
import eu.panco.ptprinter.receipt.ReceiptDocument;

public class MainActivity extends Activity {

    private BluetoothService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PDFBoxResourceLoader.init(this);
    }

    @Override
    protected void onDestroy() {
        service.stop();
        super.onDestroy();
    }

    public void parsePdfClicked(View v) {
        System.out.println("CLICKED");
        PrintManager pm = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter pa = new PrintDocument();
        PrintJob pj = pm.print("TestDocument", pa, null);


        /*File file = new File(Environment.getExternalStorageDirectory() + "/Download/VRP_receipt_detail.pdf");
        ReceiptDocument receipt = PrintDocumentParser.parse(file);
        PrinterQueue.getQueue().add(receipt.getListBytes());*/
    }

    public void button2Clicked(View v) {
        if (service == null) service = new BluetoothService();

        service.discoverDevice();
    }

}
