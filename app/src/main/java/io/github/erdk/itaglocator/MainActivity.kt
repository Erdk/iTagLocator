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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var dataset: HashMap<String, String>


    // log text view
    private lateinit var mLogText: TextView

    // start discovery button
    private lateinit var mStartDiscovery: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataset = hashMapOf()

        viewManager = LinearLayoutManager(this)
        viewAdapter = DeviceListAdapter(dataset)
        recyclerView = findViewById<RecyclerView>(R.id.device_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
            }
        }

        // init refs to widgets
        mStartDiscovery = findViewById(R.id.start_discovery)
        mStartDiscovery.setOnClickListener {
                startActivity(intent)
                if (mBAvailable) {
                    if (mBLEScanning) {
                        Log.d(TAG, "Stop LE scan")
                        mStartDiscovery.text = "Start Discovery"
                        mBAdapter?.stopLeScan(mBLECallback)
                        mBLEScanning = false
                    } else {
                        Log.d(TAG, "Start LE scan")
                        mStartDiscovery.text = "Stop Discovery"
                        val startLe = mBAdapter?.startLeScan(mBLECallback)
                        Log.d(TAG, "startLeScan: " + startLe)
                        mBLEScanning = true
                    }
                }
        }

        // setup bluetooth
        mBManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        mBAdapter = mBManager?.adapter

        if (mBAdapter == null) {
            Log.d(TAG, "Cannot find bluetooth adapter")
        } else {
            Log.d(TAG, "Adapter found")
            if (mBAdapter?.isEnabled != true) {
                Log.d(TAG, "Adapter not enabled")
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
        Log.d(TAG, "Device found!")
        device?.let {
            Log.d(TAG, "device: " + it.name + " type: " + it.type + " MAC: " + it.address)
            if (it.address != null && !dataset.containsKey(it.address)) {
                var datasetInput: String
                if (it.name == null) {
                    datasetInput = "<none>"
                } else {
                    datasetInput = it.name
                }

                datasetInput += " " + it.address
                dataset[it.address] = datasetInput

                if (it.uuids != null && it.uuids.isNotEmpty()) {
                    for (uuid in it.uuids) {
                        Log.d(TAG, "uuid: $uuid")
                    }
                }

                viewAdapter.notifyDataSetChanged()
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
        Log.d(TAG, "onActivityResult")
        when(requestCode) {
            REQUEST_ENABLE_BT -> handleRequestEnableBluetooth(resultCode)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Scan should now succeed")
                }
            }
            else -> Log.d(TAG, "Scan will be not effective")
        }
    }

    private fun handleRequestEnableBluetooth(resultCode: Int) {
        Log.d(TAG, "Bluetooth enable request: result code $resultCode")
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Bluetooth working")
            mBAvailable = true
        } else {
            Log.d(TAG, "Bluetooth not working")
        }
    }
}
