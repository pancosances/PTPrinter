package eu.panco.ptprinter.printerservice;

import android.printservice.PrintJob;

import java.util.ArrayList;

import eu.panco.ptprinter.BluetoothPrinterService;
import eu.panco.ptprinter.bluetoothservice.BluetoothDeviceState;

public class PrinterQueue {

    private BluetoothPrinterService service;
    private ArrayList<byte[]> queue;

    public PrinterQueue() {
        this.queue = new ArrayList<>();
    }

    public synchronized void add(byte[] bytes) {
        queue.add(bytes);
        print();
    }

    public synchronized void add(ArrayList<byte[]> bytes) {
        queue.addAll(bytes);
        print();
    }

    public synchronized void print() {
        if (service == null) {
            service = new BluetoothPrinterService();
        }
        if (service.getState() != BluetoothDeviceState.CONNECTED) {
            /*BluetoothPrinter printer = BluetoothDeviceService.getBluetoothPrinter();
            service.connect(printer.getDevice());*/
            return;
        }
        while (queue.size() > 0) {
            service.write(queue.get(0));
            queue.remove(0);
        }
    }

    public void add(PrintJob job) {

    }
}
