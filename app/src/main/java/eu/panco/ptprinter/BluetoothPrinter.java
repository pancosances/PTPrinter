package eu.panco.ptprinter;

import android.bluetooth.BluetoothDevice;

public class BluetoothPrinter {

    private final BluetoothDevice device;

    public BluetoothPrinter(BluetoothDevice device) {
        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
