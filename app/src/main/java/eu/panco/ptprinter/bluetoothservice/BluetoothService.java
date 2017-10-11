package eu.panco.ptprinter.bluetoothservice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.printservice.PrintService;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.util.Set;
import java.util.UUID;

import eu.panco.ptprinter.printerservice.BluetoothPrinter;

public class BluetoothService {

    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothDeviceState state;

    private final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");

    private ConnectingDevice connectingThread;
    private ConnectedDevice connectedThread;

    public BluetoothService() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public synchronized BluetoothDeviceState getState() { return this.state; }
    public synchronized void setState(BluetoothDeviceState state) { this.state = state; }
    public BluetoothDevice getDevice() { return this.device; }
    public UUID getUuid() { return this.MY_UUID; }

    public BluetoothPrinter discoverDevice() {
        if (!adapter.isEnabled()) adapter.enable();
        if (adapter.isDiscovering()) adapter.cancelDiscovery();

        this.device = adapter.getRemoteDevice("0F:03:E0:51:54:84");
        this.connectingThread = new ConnectingDevice(this);
        this.connectingThread.start();

        return null;
    }

    public void connectionFailed() {

    }

    public void connectionLost() {
        this.state = BluetoothDeviceState.NONE;
    }

    public void successfullyConnected(BluetoothSocket socket) {
        connectingThread = null;
        connectedThread = new ConnectedDevice(this, socket);
        connectedThread.start();
    }

    public void stop() {
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
        if (connectingThread != null) {
            connectingThread.cancel();
            connectingThread = null;
        }
    }
}
