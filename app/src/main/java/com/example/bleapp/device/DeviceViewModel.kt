package com.example.bleapp.device

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bleapp.services.BluetoothAdapterProvider

class DeviceViewModel (adapterProvider: BluetoothAdapterProvider): ViewModel() {

    //(adapterProvider: BluetoothAdapterProvider, private val context: Context)

    private val mut_devices: MutableLiveData<List<BluetoothDevice>> = MutableLiveData()
    val devices: LiveData<List<BluetoothDevice>> get() = mut_devices

    private val adapter = adapterProvider.getAdapter()
    private var scanner: BluetoothLeScanner? = null
    private var callback: BleScanCallback? = null

    private val settings: ScanSettings
    private val filters: List<ScanFilter>

    private val foundDevices = HashMap<String, BluetoothDevice>()

    init {
        settings = buildSettings()
        filters = buildFilter()
    }

    private fun buildSettings() =
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            //.setReportDelay(2000)
            .build()

    private fun buildFilter() =
        listOf(
            ScanFilter.Builder()
                //.setServiceUuid(FILTER_UUID)
                .build()
        )

    @SuppressLint("MissingPermission")
    fun startScan() {
        if (callback == null) {
            callback = BleScanCallback()
            scanner = adapter.bluetoothLeScanner
            scanner?.startScan(filters, settings, callback)
        }
    }


    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (callback != null) {
            scanner?.stopScan(callback)
            scanner = null
            callback = null
        }
    }

    inner class BleScanCallback : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            foundDevices[result.device.address] = result.device
            mut_devices.postValue(foundDevices.values.toList())

            Log.i(
                "TAGGG",
                "onScanResult: name = ${result.device.name}, address = ${result.device.address}"
            )
        }

        @SuppressLint("MissingPermission")
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach { result ->
                foundDevices[result.device.address] = result.device
                Log.i(
                    "TAGGG",
                    "onScanResult: name = ${result.device.name}, address = ${result.device.address}"
                )
            }

            mut_devices.postValue(foundDevices.values.toList())
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("TAGGG2", "onScanFailed:  scan error $errorCode")
        }
    }
}

class DeviceViewModelFactory(
    private val adapterProvider: BluetoothAdapterProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(DeviceViewModel::class.java)) {
            return DeviceViewModel(adapterProvider) as T
        }
        throw IllegalArgumentException("View Model not found")

        //return super.create(modelClass)
    }
}
