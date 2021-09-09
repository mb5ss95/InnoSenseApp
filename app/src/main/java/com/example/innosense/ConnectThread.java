package com.example.innosense;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private ProgressBar progressBar;
    private ListView listView;
    private Context context;

    public ConnectThread(BluetoothDevice device, Context context, ProgressBar progressBar, ListView listView) {
        try {
            mmSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"));
            mmInStream = mmSocket.getInputStream();
            mmOutStream = mmSocket.getOutputStream();
            this.context = context;
            this.progressBar = progressBar;
            this.listView = listView;
        } catch (IOException e) {
            System.out.println("ConnectThread IOE: " + e);
        }
    }

    public void run() {

        try {
            mmSocket.connect();
            if(!mmSocket.isConnected()){
                return;
            }
            mmOutStream.write("please".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init_listView(final ArrayList<String> audioList) {

        // adapterView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                audioList);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.notify();

        if (!audioList.isEmpty()) {
            listView.setOnItemClickListener((parent, view, position, id) -> {
                //String device_name = device_list.get(position).getName();
                //BluetoothDevice device = device_list.get(position).getBluetoothDevice();
                //try_connect(device);
            });
        }
    }
}