package com.example.innosense;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothDevice bleDeviceData;
    ProgressBar progressBar;
    ListView listView;
    TextView textView;
    Button upload, record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_Title();
        init_Wiget();
        check_Permission();
        check_Bluetooth();
    }

    private void start_Connection() {
        //ConnectThread connectThread = new ConnectThread(bleDeviceData, getApplicationContext(), progressBar, listView);
        //connectThread.run();
        init_listView();
    }

    public void init_listView() {

        ArrayList<String> temp_list = new ArrayList<>();

        // adapterView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                temp_list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String device = temp_list.get(position);
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                final Toast toast = new Toast(getApplicationContext());
                toast.setText(device + "로 설정하였습니다!");
                progressDialog.setTitle("스토리 설정 중...");
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            progressDialog.dismiss();
                            toast.show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.progress).setVisibility(View.INVISIBLE);
        findViewById(R.id.audiolist).setVisibility(View.VISIBLE);
        temp_list.add("의좋은 형제");
        temp_list.add("토끼와 거북이");
        temp_list.add("신데렐라");
        temp_list.add("흥부와 놀부");
        temp_list.add("콩쥐팥쥐");
        adapter.notifyDataSetChanged();
    }

    private void init_Wiget() {
        textView = findViewById(R.id.bluename);
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BleListActivity.class);
            startActivityForResult(intent, 0);
        });

        listView = findViewById(R.id.audiolist);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        progressBar = findViewById(R.id.progress);

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Upload.class);
            startActivity(intent);
        });
        record = findViewById(R.id.record);
        record.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Record.class);
            startActivity(intent);
        });
    }

    private void check_Permission() {
        String[] Permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        for (String permission : Permission) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
            }
        }
    }

    public void check_Bluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }

    private void init_Title() {
        setTitle("  InnoSense");
        ActionBar ab = getSupportActionBar();

        ab.setIcon(R.drawable.ic_baseline_bluetooth_24);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String bleDeviceName = data.getStringExtra("BleDevice");
            Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            if (devices.size() > 0) {
                Iterator<BluetoothDevice> iter = devices.iterator();
                while (iter.hasNext()) {
                    BluetoothDevice d = iter.next();
                    if (d.getName().equals("raspberrypi")) {
                        bleDeviceData = d;
                        textView.setText(bleDeviceData.getName());
                        start_Connection();
                        break;
                    }
                }
            }
        } else {
            Toast.makeText(MainActivity.this, "result cancle!", Toast.LENGTH_SHORT).show();
        }
    }
}