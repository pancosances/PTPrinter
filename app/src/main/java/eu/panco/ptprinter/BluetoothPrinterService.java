package eu.panco.ptprinter;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Logger;

import eu.panco.ptprinter.bluetoothservice.BluetoothDeviceState;
import eu.panco.ptprinter.printerservice.PrinterQueue;

public class BluetoothPrinterService {

    // Debugging
    private static final String TAG = "BtService";
    // Name for the SDP record when creating server socket
    private static final String NAME = "BtService";
    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");
    // Member fields
    private final BluetoothAdapter mAdapter;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private BluetoothDeviceState mState;
    private Logger logger = Logger.getLogger(BluetoothPrinterService.class.getName());


    /**
     * Constructor. Prepares a new BluetoothChat session.
     */
    public BluetoothPrinterService() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = BluetoothDeviceState.NONE;
    }

    /**
     * Return the current connection state.
     */
    public synchronized BluetoothDeviceState getState() {
        return mState;
    }

    private synchronized void setState(BluetoothDeviceState state) {
        mState = state;
    }

    public synchronized void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (mState == BluetoothDeviceState.CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(BluetoothDeviceState.CONNECTING);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, final String socketType) {

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one
        // device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        setState(BluetoothDeviceState.CONNECTED);

        // call print queue to print
       //PrinterQueue.getQueue().print();
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        setState(BluetoothDeviceState.NONE);
    }

    /**
     * Write to the ConnectedDevice in an unsynchronized manner
     *
     * @param out The bytes to write
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedDevice
        synchronized (this) {
            if (mState != BluetoothDeviceState.CONNECTED)
                return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Write to the ConnectedDevice in an unsynchronized manner after sleepTime
     *
     * @param out       The bytes to write
     * @param sleepTime sleep time
     */
    public void write(byte[] out, long sleepTime) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedDevice
        synchronized (this) {
            if (mState != BluetoothDeviceState.CONNECTED)
                return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out, sleepTime);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(BluetoothDeviceState.NONE);
        // Start the service over to restart listening mode
        BluetoothPrinterService.this.start();
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(BluetoothDeviceState.LISTEN);

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
    }

    private void connectionLost() {
        setState(BluetoothDeviceState.NONE);
        // Start the service over to restart listening mode
        BluetoothPrinterService.this.start();
    }

    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            try {
                setName("AcceptThread" + mSocketType);

                BluetoothSocket socket = null;

                // Listen to the server socket if we're not connected
                while (mState != BluetoothDeviceState.CONNECTED) {
                    try {
                        // This is a blocking call and will only return on a
                        // successful connection or an exception
                        socket = mmServerSocket.accept();
                    } catch (Exception e) {
                        break;
                    }

                    // If a connection was accepted
                    if (socket != null) {
                        synchronized (BluetoothPrinterService.this) {
                            switch (mState) {
                                case LISTEN:
                                case CONNECTING:
                                    // Situation normal. Start the connected thread.
                                    connected(socket, socket.getRemoteDevice(),
                                            mSocketType);
                                    break;
                                case NONE:
                                case CONNECTED:
                                    // Either not ready or already connected. Terminate
                                    // new socket.
                                    try {
                                        socket.close();
                                    } catch (IOException e) {
                                        Log.e(TAG, "Exception", e);
                                    }
                                    break;
                            }
                        }
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }

        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            try {
                setName("ConnectThread" + mSocketType);

                // Always cancel discovery because it will slow down a connection
                mAdapter.cancelDiscovery();

                // Make a connection to the BluetoothSocket
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    mmSocket.connect();
                } catch (Exception e) {
                    Log.e(TAG, "Exception", e);
                    // Close the socket
                    try {
                        mmSocket.close();
                    } catch (IOException e2) {
                        Log.e(TAG, "Exception", e2);
                    }
                    connectionFailed();
                    return;
                }

                // Reset the ConnectThread because we're done
                synchronized (BluetoothPrinterService.this) {
                    mConnectThread = null;
                }

                // Start the connected thread
                connected(mmSocket, mmDevice, mSocketType);
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device. It handles all
     * incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (Exception e) {

            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {

            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // send data
                    //EventBus.getDefault().post(new BtMsgReadEvent(bytes, buffer));
                } catch (IOException e) {

                    connectionLost();
                    // Start the service over to restart listening mode
                    BluetoothPrinterService.this.start();
                    break;
                } catch (Exception e) {
                    connectionLost();
                    BluetoothPrinterService.this.start();
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }

        public void write(byte[] buffer, long sleepTime) {
            try {
                Thread.sleep(sleepTime);
                mmOutStream.write(buffer);
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception", e);
            }
        }
    }

}
