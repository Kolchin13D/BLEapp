package com.example.bleapp.control;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import androidx.annotation.NonNull;

import no.nordicsemi.android.ble.BleManager;

public class BleControlManager extends BleManager {



    private BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device =
            bluetoothAdapter.getRemoteDevice("CC:CF:85:77:D1:DF");

    public BleControlManager(@NonNull Context context) {
        super(context);
    }

}
