package eu.panco.ptprinter.printerservice;

import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;

import java.util.ArrayList;
import java.util.List;

import eu.panco.ptprinter.bluetoothservice.BluetoothService;

public class PTPrinterDiscoverySession extends PrinterDiscoverySession {

    private final PrintService printService;
    private final BluetoothService bluetoothService;
    private BluetoothPrinter printer;


    public PTPrinterDiscoverySession(PrintService service) {
        this.printService = service;
        this.bluetoothService = new BluetoothService();
    }

    @Override
    public void onStartPrinterDiscovery(List<PrinterId> printerList) {
        //this.printer = new BluetoothPrinter(printService);
        //updatePrinterStatus();
    }

    @Override
    public void onStopPrinterDiscovery() {

    }

    @Override
    public void onValidatePrinters(List<PrinterId> printerIds) {

    }

    @Override
    public void onStartPrinterStateTracking(PrinterId printerId) {

    }

    @Override
    public void onStopPrinterStateTracking(PrinterId printerId) {

    }

    @Override
    public void onDestroy() {

    }

    private void updatePrinterStatus() {
        List<PrinterInfo> infos = new ArrayList<>();
        infos.add(this.printer.getInfo());
        addPrinters(infos);
    }
}
