package eu.panco.ptprinter.bluetoothservice;

import android.bluetooth.BluetoothAdapter;

import eu.panco.ptprinter.printerservice.BluetoothPrinter;

/**
 * Created by mpancisi on 17/10/2017.
 */
public class DiscoveringDevice extends Thread {

    private final BluetoothPrinter printer;
    private final BluetoothAdapter adapter;

    public DiscoveringDevice(BluetoothPrinter printer) {
        this.printer = printer;
        this.adapter = BluetoothAdapter.getDefaultAdapter();

        if (!adapter.isEnabled()) adapter.enable();
        if (adapter.isDiscovering()) adapter.cancelDiscovery();
        adapter.startDiscovery();


    }

    public void run() {

    }

}
