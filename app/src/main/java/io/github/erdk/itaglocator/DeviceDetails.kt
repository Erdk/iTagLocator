package io.github.erdk.itaglocator

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_device_details.*

class DeviceDetails : AppCompatActivity() {
    companion object{
        val TAG = DeviceDetails::class.qualifiedName
        val EXTRA_DEVICE_NAME = "ExtraDeviceName"
        val EXTRA_DEVICE_ADDRESS = "ExtraDeviceAddress"
    }

    var mDeviceName = ""
    var mDeviceAddress = ""

    var mBLEMgmtService: BLEMgmtService? = null
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
            mBLEMgmtService = (service as BLEMgmtService.LocalBinder).getService()
            if (mBLEMgmtService?.initialize() == false) {
                Log.e(TAG, "Unable to initialize Bluetooth")
                finish()
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBLEMgmtService?.connect(mDeviceAddress)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mBLEMgmtService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_details)
        setSupportActionBar(toolbar)

        mDeviceName = intent.getStringExtra(DeviceDetails.EXTRA_DEVICE_NAME) ?: ""
        mDeviceAddress = intent.getStringExtra(DeviceDetails.EXTRA_DEVICE_ADDRESS) ?: ""

        actionBar!!.title = mDeviceName + "(" + mDeviceAddress + ")"
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
