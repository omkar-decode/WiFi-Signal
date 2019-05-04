package com.example.wifisignal;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView ssid;
    TextView timestamp;
    TextView level;
    TextView peak_value;

    EditText et_ssid;
    Button scan;
    Button plot;

//    Spinner ssid_spinner;
//    List<String> ssid_list;
//    ArrayAdapter<String> adapter;

    String wifi_name = "";
    int max_val = -127;
    boolean flag = true;

    int scan_limit = 5;
    int scans_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scans_done = 0;

        ssid = findViewById(R.id.ssid_wifi_signal);
        timestamp= findViewById(R.id.timestamp_wifi_signal);
        level = findViewById(R.id.level_wifi_signal);
        peak_value = findViewById(R.id.peak_wifi_signal);

        et_ssid = findViewById(R.id.SSID);

//        ssid_list = new ArrayList<>();
//        ssid_spinner = findViewById(R.id.wifi_names_spinner);
//        adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_selectable_list_item);
//
//        ssid_spinner.setAdapter(adapter);
//
//        ssid_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                wifi_name = parent.getItemAtPosition(position).toString();
//                wifi_name = ssid_list.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        scan = findViewById(R.id.scan_button);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifi_name = et_ssid.getText().toString();
                if (wifi_name.equals("")) {
                    return;
                }

//                WifiManager wifiManager = (WifiManager) getApplicationContext()
//                        .getSystemService(Context.WIFI_SERVICE);

//                wifiManager.startScan();
//                List<ScanResult> scanResults = wifiManager.getScanResults();
//
//                for (ScanResult sr: scanResults) {
//                    ssid_list.add(sr.SSID);
//                }
//
//                adapter = new ArrayAdapter<>(MainActivity.this,
//                        android.R.layout.simple_spinner_dropdown_item, ssid_list);
//                ssid_spinner.setAdapter(adapter);

                if (flag) {
                    flag = false;
                    startScanning();
                }

            }
        });


        plot = findViewById(R.id.plot_button);
        plot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_ssid.getText().toString() != "") {
                    wifi_name = et_ssid.getText().toString();
                }
                else if (wifi_name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter wifi name", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent plotIntent = new Intent(MainActivity.this, WifiPlotActivity.class);
                plotIntent.putExtra("WIFI_SSID", wifi_name);
                startActivity(plotIntent);
            }
        });

    }


    public void startScanning () {
        final int delay = 1000;
        final Handler wifiHandler = new Handler();

        wifiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                scanWifiSignal(wifi_name);
                wifiHandler.postDelayed(this, delay);
            }
        }, delay);
    }


    public void scanWifiSignal (String wifiSSID) {

        WifiManager wifiManager = (WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);

        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();
        Log.e("wifi scan", ""+scanResults.size());


        if (scanResults.size() == 0) {
            return;
        }

//        ssid_list.clear();
        ScanResult result = null;
        for (ScanResult s: scanResults) {
//            ssid_list.add(s.SSID);

            if (s.SSID.equals(wifiSSID)) {
                result = s;
                break;
            }
        }

//        adapter = new ArrayAdapter<>(MainActivity.this,
//                android.R.layout.simple_spinner_dropdown_item, ssid_list);
//        ssid_spinner.setAdapter(adapter);
//        ssid_list.clear();

        if (result == null) {
            if (scans_done < scan_limit) {
                Toast.makeText(getApplicationContext(), "no signal", Toast.LENGTH_SHORT).show();
                scans_done += 1;
            }

            return;
        }

        scans_done = 0;

        if (result.level > max_val) {
            max_val = result.level;
            peak_value.setText("Max: "+max_val+" dbm");
        }

        ssid.setText(result.SSID);
        timestamp.setText(""+result.timestamp);
        level.setText("Curr: "+result.level+" dbm");
    }

}
