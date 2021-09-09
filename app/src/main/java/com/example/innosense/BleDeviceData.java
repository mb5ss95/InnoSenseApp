package com.example.innosense;

import android.bluetooth.BluetoothDevice;


class BleDeviceData {
        private BluetoothDevice bluetoothDevice;
        private String name;

        BleDeviceData(BluetoothDevice bluetoothDevice, String name) {
            this.bluetoothDevice = bluetoothDevice;
            this.name = name;
        }

        public BluetoothDevice getBluetoothDevice() {
            return bluetoothDevice;
        }

        public String getName() {
            return name;
        }
    }