package eu.panco.ptprinter.bluetoothservice;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by mpancisin on 5/18/2016.
 */
public class ConnectedDevice extends Thread {

    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final BluetoothService service;

    public ConnectedDevice(BluetoothService service, BluetoothSocket socket) {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (Exception e) {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        this.socket = socket;
        this.service = service;
        this.inputStream = tmpIn;
        this.outputStream = tmpOut;
    }

    public void run() {
        this.service.setState(BluetoothDeviceState.CONNECTED);
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                // Read from the InputStream
                bytes = this.inputStream.read(buffer);
                // send data
                //EventBus.getDefault().post(new BtMsgReadEvent(bytes, buffer));
            } catch (Exception e) {
                this.service.connectionLost();
                break;
            }
        }
    }

    public void write(byte[] buffer) {
        try {
            this.outputStream.write(buffer);
        } catch (Exception e) {
            Log.e(this.getName(), "Exception", e);
        }
    }

    public void write(byte[] buffer, long sleepTime) {
        try {
            Thread.sleep(sleepTime);
            this.outputStream.write(buffer);
        } catch (Exception e) {
            Log.e(this.getName(), "Exception", e);
        }
    }

    public void cancel() {
        try {
            this.inputStream.close();
            this.outputStream.close();
            this.socket.close();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "Exception", e);
        }
    }
}
