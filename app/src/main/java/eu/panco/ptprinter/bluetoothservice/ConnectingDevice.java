package eu.panco.ptprinter.bluetoothservice;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * Created by mpancisi on 05/10/2017.
 */
public class ConnectingDevice extends Thread {

    private final BluetoothSocket socket;
    private final BluetoothService service;

    public ConnectingDevice(BluetoothService service) {
        this.service = service;
        BluetoothSocket tmp = null;

        try {
            tmp = this.service.getDevice().createRfcommSocketToServiceRecord(service.getUuid());
        } catch (Exception e) {
            Log.e(getName(), "Exception", e);
        }

        this.socket = tmp;
    }

    public void run() {
        setName("ConnectingThread");
        this.service.setState(BluetoothDeviceState.CONNECTING);

        try {
            this.socket.connect();
        } catch (Exception e) {
            Log.e(getName(), "Exception", e);

            try {
                this.socket.close();
            } catch (IOException e2) {
                Log.e(getName(), "Exception", e2);
            }

            this.service.connectionFailed();
            return;
        }

        this.service.successfullyConnected(socket);
    }

    public void cancel() {
        try {
            this.socket.close();
        } catch (Exception e) {
            Log.e(getName(), "Exception", e);
        }
    }
}
