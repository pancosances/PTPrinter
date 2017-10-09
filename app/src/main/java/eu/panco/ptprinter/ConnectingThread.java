package eu.panco.ptprinter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;

public class ConnectingThread extends Thread {

    private final BluetoothDevice device;
    private final BluetoothSocket socket;

    public ConnectingThread(BluetoothDevice device) {
        this.device = device;
        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0001101-0000-1000-8000-00805F9B34FB"));
        } catch (Exception e) {

        }
        this.socket = socket;
    }

    @Override
    public void run() {

    }


    public void cancel() {

    }
}
