package com.example.innosense;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class BleListActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blelist);


        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않습니다!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            init_listView(get_device_list(bluetoothAdapter));
        }
    }

    public ArrayList<BleDeviceData> get_device_list(BluetoothAdapter bluetoothAdapter) {
        ArrayList<BleDeviceData> device_list = new ArrayList<>();
        if (bluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            if (devices.size() > 0) {
                Iterator<BluetoothDevice> iter = devices.iterator();
                while (iter.hasNext()) {
                    BluetoothDevice d = iter.next();
                    //device_list.add(d.getName());
                    device_list.add(new BleDeviceData(d, d.getName()));
                }
            }
        } else {
            Toast.makeText(this, "블루투스를 켜세요!!", Toast.LENGTH_SHORT).show();
        }
        return device_list;
    }

    public void init_listView(final ArrayList<BleDeviceData> device_list) {

        ArrayList<String> temp_list = new ArrayList<>();

        for (BleDeviceData i : device_list) {
            temp_list.add(i.getName());
        }

        // adapterView
        ListView listView = findViewById(R.id.bluelist);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                temp_list);
        listView.setAdapter(adapter);
        if (!temp_list.isEmpty()) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String device = device_list.get(position).getName();
                    System.out.println("55555555555"+ device);
                    Intent intent = new Intent();
                    intent.putExtra("BleDevice", device);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}