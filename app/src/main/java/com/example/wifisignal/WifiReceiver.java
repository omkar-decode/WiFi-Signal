package com.example.wifisignal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WifiReceiver extends BroadcastReceiver {

//    List<String> ts = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Intent action: " + intent.getAction(), Toast.LENGTH_LONG).show();

//        WifiManager wifiManager = (WifiManager) context.getApplicationContext().
//                getSystemService(Context.WIFI_SERVICE);
//
//        List<ScanResult> scanResults = wifiManager.getScanResults();
//        wifiManager.getScanResults();
//        ts.add(String.valueOf(scanResults.get(0).timestamp));

    }
}
