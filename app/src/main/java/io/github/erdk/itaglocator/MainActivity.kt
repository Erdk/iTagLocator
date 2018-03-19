package io.github.erdk.itaglocator

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val TAG = "MAINACT"
    private val REQUEST_ENABLE_BT = 1
    private val REQUEST_COARSE_LOCATION = 2

    private var mBManager: BluetoothManager? = null
    private var mBAdapter: BluetoothAdapter? = null
    private var mBAvailable = false
    private var mBLEScanning = false

    // log text view
    private lateinit var mLogText: TextView

    // start discovery button
    private lateinit var mStartDiscovery: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
            }
        }

        // init refs to widgets
        mLogText = findViewById(R.id.log)
        mStartDiscovery = findViewById(R.id.start_discovery)
        mStartDiscovery.setOnClickListener {
            if (mBAvailable) {
                if (mBLEScanning) {
                    myLog("Stop LE scan")
                    mStartDiscovery.text = "Start Discovery"
                    mBAdapter?.stopLeScan(mBLECallback)
                    mBLEScanning = false
                } else {
                    myLog("Start LE scan")
                    mStartDiscovery.text = "Stop Discovery"
                    val startLe = mBAdapter?.startLeScan(mBLECallback)
                    myLog("startLeScan: " + startLe)
                    mBLEScanning = true
                }
            }
        }

        // setup bluetooth

        mBManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        mBAdapter = mBManager?.adapter

        if (mBAdapter == null) {
            myLog("Cannot find bluetooth adapter")
        } else {
            myLog("Adapter found")
            if (mBAdapter?.isEnabled != true) {
                myLog("Adapter not enabled")
                val enableBTIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT)
            } else {
                // enable "discovery button
                mStartDiscovery.isEnabled = true
                mBAvailable = true
            }
        }
    }

    private val mBLECallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        myLog("Device found!")
        device?.let {
            myLog("device: " + it.name + " type: " + it.type + " MAC: " + it.address)
            if (it.uuids != null && it.uuids.isNotEmpty()) {
                for (uuid in it.uuids) {
                    myLog("uuid: $uuid")
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()

        if (mBLEScanning) {
            mBAdapter?.stopLeScan(mBLECallback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        myLog("onActivityResult")
        when(requestCode) {
            REQUEST_ENABLE_BT -> handleRequestEnableBluetooth(resultCode)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myLog("Scan should now succeed")
                }
            }
            else -> myLog("Scan will be not effective")
        }
    }

    fun handleRequestEnableBluetooth(resultCode: Int) {
        myLog("Bluetooth enable request: result code $resultCode")
        if (resultCode == Activity.RESULT_OK) {
            myLog("Bluetooth working")
            mBAvailable = true
        } else {
            myLog("Bluetooth not working")
        }
    }

    private fun myLog(text: String) {
        Log.d(TAG, text)
        mLogText.text = mLogText.text as String + "\n$text"
    }
}
