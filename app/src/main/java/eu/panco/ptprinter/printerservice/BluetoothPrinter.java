package eu.panco.ptprinter.printerservice;

import android.bluetooth.BluetoothDevice;
import android.print.PrintAttributes;
import android.print.PrinterCapabilitiesInfo;
import android.print.PrinterId;
import android.print.PrinterInfo;
import android.printservice.PrintService;
import android.support.annotation.NonNull;

public class BluetoothPrinter {

    private final String name = "BluetoothPrinter";
    private final String address = "0F:03:E0:51:54:84";
    private PrinterId id;
    private PrinterInfo.Builder infoBuilder;

    public BluetoothPrinter() {

    }

    public BluetoothPrinter(PrintService service) {
        this.id = service.generatePrinterId(name);
        this.infoBuilder = new PrinterInfo.Builder(id, name, PrinterInfo.STATUS_IDLE);
        infoBuilder.setCapabilities(getCapabilities());
    }

    private PrinterCapabilitiesInfo getCapabilities() {
        PrinterCapabilitiesInfo.Builder capBuilder = new PrinterCapabilitiesInfo.Builder(id);
        capBuilder.addMediaSize(PrintAttributes.MediaSize.ISO_A4, true);
        capBuilder.addResolution(new PrintAttributes.Resolution("Default", "Default", 360, 360), true);
        capBuilder.setColorModes(PrintAttributes.COLOR_MODE_MONOCHROME, PrintAttributes.COLOR_MODE_MONOCHROME);
        capBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);
        return capBuilder.build();
    }

    public PrinterInfo getInfo() {
        return infoBuilder.build();
    }

    public PrinterId getId() {
        return id;
    }



}
