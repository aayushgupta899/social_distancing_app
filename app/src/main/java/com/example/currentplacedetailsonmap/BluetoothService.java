package com.example.currentplacedetailsonmap;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class BluetoothService extends Service {

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                doDiscovery();
            } catch (Exception e) {
                // Restore interrupt status.
                Log.e(TAG, "Exception:"+e.getMessage());
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
//        Message msg = serviceHandler.obtainMessage();
//        msg.arg1 = startId;
//        serviceHandler.sendMessage(msg);
        doDiscovery();
        return Service.START_NOT_STICKY;
    }
    /**
     * Tag for Log
     */
    private static final String TAG = "BluetoothService";

    /**
     * Return Intent extra
     */
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_COUNT = "device_count";

    /**
     * Member fields
     */
    private BluetoothAdapter mBtAdapter;
    /**
     * Newly discovered devices
     */
    public static ArrayAdapter<String> mNewDevicesArrayAdapter;

    @Override
    public void onCreate() {

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
//        HandlerThread thread = new HandlerThread("ServiceStartArguments",
//                Process.THREAD_PRIORITY_BACKGROUND);
//        thread.start();
//
//        // Get the HandlerThread's Looper and use it for our Handler
//        serviceLooper = thread.getLooper();
//        serviceHandler = new ServiceHandler(serviceLooper);
        ArrayAdapter<String> pairedDevicesArrayAdapter =
                new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            //findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        // setProgressBarIndeterminateVisibility(true);
        //setTitle(R.string.scanning);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }
    @Override
    public void onDestroy()
    {
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

    private void returnNewDeviceCount() {
        // Cancel discovery because it's costly and we're about to leave
        Log.d(TAG, "returnNewDeviceCount()");
        mBtAdapter.cancelDiscovery();

        ////////////////////////////////////////////////////////////////////////////
        ///////////////   CRUCIAL PART ! RETURN VALUE SET HERE ! ///////////////////
        ////////// YOU CAN ALSO MOVE THE FOLLOWING CODE TO OTHER LISTENER //////////
        ////////////////////////////////////////////////////////////////////////////
        // For example, you can use mNewDevicesArrayAdapter.getCount() to get the new
        // bluetooth devices number, or use .getItem(index) to get the device item.

        // Create the result Intent and include the MAC address
        Intent intent = new Intent();
        // Put anything you want to return here
        intent.setAction("ServiceBroadcast");
        intent.putExtra(EXTRA_DEVICE_COUNT, mNewDevicesArrayAdapter.getCount());
        // Set result and finish this Activity
        sendBroadcast(intent);
        stopSelf();
    }
    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive()");
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED
                        && (device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.PHONE_SMART
                            || device.getBluetoothClass().getDeviceClass() == BluetoothClass.Device.WEARABLE_WRIST_WATCH)
                        && device.getName() != null
                        )
                {
                        mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //setProgressBarIndeterminateVisibility(false);
                //setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    //String noDevices = getResources().getText(R.string.none_found).toString();
                    //mNewDevicesArrayAdapter.add(noDevices);
                }
                returnNewDeviceCount();
            }
        }
    };
}