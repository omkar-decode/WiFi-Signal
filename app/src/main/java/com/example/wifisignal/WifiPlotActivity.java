package com.example.wifisignal;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

public class WifiPlotActivity extends AppCompatActivity {

    private String wifi_name;
    int max_val = -128;

    private final Handler handler = new Handler();
    private Runnable timer;
    private LineGraphSeries<DataPoint> plot;
    private TextView tv_peak_value;

    final int delay = 1000;
    double x_value = -1.0;
    int x_limit = 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_plot);

        tv_peak_value = findViewById(R.id.tv_plot_peak_value);

        wifi_name = getIntent().getStringExtra("WIFI_SSID");
        Log.e("received SSID", wifi_name);

        TextView header = findViewById(R.id.activity_plot_heading);
        header.setText(wifi_name);

        GraphView graph = findViewById(R.id.graph);
        plot = new LineGraphSeries<>();
        graph.addSeries(plot);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("time (sec) --->");
        gridLabel.setVerticalAxisTitle("signal strength (dbm)  --->");

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(x_limit);
        graph.getViewport().setMinY(-140);
        graph.getViewport().setMaxY(-20);


    }

    @Override
    protected void onResume() {
        super.onResume();

        timer = new Runnable() {
            @Override
            public void run() {
                x_value += (delay/1000.0);
                if (x_value <= x_limit) {
                    plot.appendData(new DataPoint(x_value, wifiStrength()),
                            false, 1000);
                }
                else {
                    plot.appendData(new DataPoint(x_value, wifiStrength()),
                            true, 1000);
                }

                handler.postDelayed(this, delay);
            }
        };
        handler.postDelayed(timer, delay);
    }

    public double wifiStrength () {
        WifiManager wifiManager = (WifiManager) getApplicationContext().
                getSystemService(Context.WIFI_SERVICE);

        wifiManager.startScan();
        List<ScanResult> scanResults = wifiManager.getScanResults();

        ScanResult result = null;
        for (ScanResult sr: scanResults) {
            if (sr.SSID.equals(wifi_name)) {
                result = sr;
                break;
            }
        }

        if (result == null) {
            return -127d;
        }

        if (result.level > max_val) {
            max_val = result.level;
            tv_peak_value.setText("Peak: "+max_val+" dbm");
        }

        return (double) result.level;
    }
}
