package eu.panco.ptprinter.bluetoothservice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.Set;
import java.util.UUID;

import eu.panco.ptprinter.BluetoothPrinter;

public class BluetoothDeviceService {

    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private BluetoothDeviceState state;

    private final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");

    private ConnectingDevice connectingThread;
    private ConnectedDevice connectedThread;

    public BluetoothDeviceService() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        // Check if device does not support Bluetooth
    }

    public synchronized BluetoothDeviceState getState() { return this.state; }
    public synchronized void setState(BluetoothDeviceState state) { this.state = state; }
    public BluetoothDevice getDevice() { return this.device; }
    public UUID getUuid() { return this.MY_UUID; }

    public BluetoothPrinter getBluetoothPrinter() {
        if (!adapter.isEnabled()) adapter.enable();

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        for (BluetoothDevice d : pairedDevices) {
            adapter.cancelDiscovery();
            connectingThread = new ConnectingDevice(this);
            connectingThread.start();
        }

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

}
