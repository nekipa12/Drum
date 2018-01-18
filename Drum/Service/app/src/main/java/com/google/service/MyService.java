package com.google.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;


public class MyService extends Service {

    private MyThread myThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(startId != 1) {
            myThread.stop();
        }
        int count = intent.getIntExtra(MainActivity.COUNT, 0);
        myThread = new MyThread(this, count);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        myThread.stop();
        super.onDestroy();
    }
}
