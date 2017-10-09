package eu.panco.ptprinter.printerservice;

import android.os.Parcelable;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;

import java.util.ArrayList;
import java.util.List;

public class PTPrinterDiscoverySession extends PrinterDiscoverySession {

    private final PrintService service;


    public PTPrinterDiscoverySession(PrintService service) {
        this.service = service;
    }

    @Override
    public void onStartPrinterDiscovery(List<PrinterId> printerList) {
        //BluetoothDeviceService bds = new BluetoothDeviceService();
        //bds.getBluetoothPrinter();


        PrinterId pi = this.service.generatePrinterId("BluetoothPrinter");
        PrinterInfo printerInfo = new PrinterInfo.Builder(pi, "BluetoothPrinterName", PrinterInfo.STATUS_IDLE).build();
        List<PrinterInfo> infos = new ArrayList<>();
        infos.add(printerInfo);
        addPrinters(infos);
    }

    @Override
    public void onStopPrinterDiscovery() {

    }

    @Override
    public void onValidatePrinters(List<PrinterId> printerIds) {

    }

    @Override
    public void onStartPrinterStateTracking(PrinterId printerId) {
       /*
        PrinterInfo.Builder builder = new PrinterInfo.Builder(printerId, PRINTER, PrinterInfo.STATUS_IDLE);
        PrinterCapabilitiesInfo.Builder capBuilder = new PrinterCapabilitiesInfo.Builder(printerId);

        capBuilder.addMediaSize(PrintAttributes.MediaSize.ISO_A4, true);
        capBuilder.addResolution(new PrintAttributes.Resolution("Default", "Default", 360, 360), true);
        capBuilder.setColorModes(PrintAttributes.COLOR_MODE_COLOR + PrintAttributes.COLOR_MODE_MONOCHROME, PrintAttributes.COLOR_MODE_COLOR);
        capBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);

        PrinterCapabilitiesInfo caps = capBuilder.build();
        builder.setCapabilities(caps);
        PrinterInfo info = builder.build();
        List<PrinterInfo> infos = new ArrayList<PrinterInfo>();
        infos.add(info);
        addPrinters(infos);
        */
    }

    @Override
    public void onStopPrinterStateTracking(PrinterId printerId) {

    }

    @Override
    public void onDestroy() {

    }
}
