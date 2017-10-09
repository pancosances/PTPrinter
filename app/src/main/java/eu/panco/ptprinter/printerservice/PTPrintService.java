package eu.panco.ptprinter.printerservice;

import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;

import eu.panco.ptprinter.PrintDocumentParser;
import eu.panco.ptprinter.receipt.ReceiptDocument;

public class PTPrintService extends PrintService {

    private static final String LocalPrinterId = "BlueToothPrinter";
    private PrinterQueue queue;

    @Override
    protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
        return new PTPrinterDiscoverySession(this);



    }

    @Override
    protected void onRequestCancelPrintJob(PrintJob printJob) {

    }

    @Override
    protected void onPrintJobQueued(PrintJob job) {
        if (this.queue == null) this.queue = new PrinterQueue();

        this.queue.add(job);

        /*printJob.start();
        ReceiptDocument receipt = PrintDocumentParser.parse(printJob);
        PrinterQueue.getQueue().add(receipt.getListBytes());
        printJob.complete();*/
    }

}
