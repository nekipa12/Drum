package com.google.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLastTime, tvCount;
    private Button btnStart, btnStop;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver broadcastReceiver;
    private int count;
    public static final String TIME = "time";
    public static final String COUNT = "count";
    public static final String CODE = "code";
    public static final int CODE_TIME = 0;
    public static final int CODE_COUNT_SHOW = 1;
    public static final int CODE_COUNT_SAVE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        tvLastTime = findViewById(R.id.tvLastTime);
        tvCount = findViewById(R.id.tvCount);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        sharedPreferences = getPreferences(MODE_PRIVATE);

        tvCount.setText(String.valueOf(getCountFromPreferences()));
        tvLastTime.setText(getTimeFromPreferences());

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int code = intent.getIntExtra(CODE, -1);
                switch (code) {
                    case CODE_TIME:
                        String date = intent.getStringExtra(TIME);
                        writeToPreferences(date, TIME);
                        tvLastTime.setText(date);
                        break;
                    case CODE_COUNT_SHOW:
                        tvCount.setText(String.valueOf(intent.getIntExtra(COUNT, -1)));
                        break;
                }
            }
        };

        IntentFilter intFilt = new IntentFilter("com.google.MyService");
        registerReceiver(broadcastReceiver, intFilt);
    }

    private void writeToPreferences(String data, String stat) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        if (stat.equals(TIME)) {
            ed.putString(TIME, data);
        } else if (stat.equals(COUNT)) {
            ed.putInt(COUNT, Integer.parseInt(data));
        }
        ed.apply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                count = getCountFromPreferences();
                startService(new Intent(this, MyService.class).putExtra(COUNT, count));
                break;
            case R.id.btnStop:
                stopService(new Intent(this, MyService.class));
                saveValuesBeforeDestroying();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, MyService.class));
        unregisterReceiver(broadcastReceiver);
        saveValuesBeforeDestroying();
        super.onDestroy();
    }

    private void saveValuesBeforeDestroying () {
        String count = tvCount.getText().toString();
        writeToPreferences(count, COUNT);
    }

    private int getCountFromPreferences() {
        return sharedPreferences.getInt(COUNT, 0);
    }
    private String getTimeFromPreferences() {
        return sharedPreferences.getString(TIME, "0");
    }
}
