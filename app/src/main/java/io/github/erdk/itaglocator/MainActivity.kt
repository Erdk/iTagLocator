package io.github.erdk.itaglocator

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter


class MainActivity : AppCompatActivity() {
    private val TAG = "MAINACT"
    private val REQUEST_ENABLE_BT = 1

    private var mBTAdapter: BluetoothAdapter? = null
    private var mBLAvailable = false

    private val mBroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address

                Log.d(TAG, "onReceive: devName = " + deviceName + " MAC: " + deviceHardwareAddress)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "==== TEST LOG ====")

        mBTAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBTAdapter == null) {
            Log.e(TAG, "Cannot find bluetooth adapter")
        } else {
            Log.d(TAG, "Adapter found")
            if (!mBTAdapter!!.isEnabled) {
                Log.d(TAG, "Adapter not enabled")
                var enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
            } else {
                Log.d(TAG, "Adapter enabled, installing broadcast receiver")
                registerReceiver(mBroadcastReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
                mBLAvailable = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mBLAvailable) {
            Log.d(TAG, "Bluetooth available start discovery")
            if (!mBTAdapter!!.startDiscovery()) {
                Log.d(TAG, "cannot start discovery")
            } else {
                Log.d(TAG, "Discovery started")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(mBroadcastReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult")
        when(requestCode) {
            REQUEST_ENABLE_BT -> handleRequestEnableBT(resultCode)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun handleRequestEnableBT(resultCode: Int) {
        Log.d(TAG, "BT enable result code: " + resultCode)
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Bluetooth working")
            mBLAvailable = true
        } else {
            Log.e(TAG, "Bluetooth not working")
        }
    }
}
