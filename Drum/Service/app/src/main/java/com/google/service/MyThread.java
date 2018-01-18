package com.google.service;

import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Val on 1/17/2018.
 */

public class MyThread implements Runnable {

    private Thread thread;
    private boolean flag;
    private int count;
    private MyService myService;

    public MyThread(MyService myService, int count) {
        this.myService = myService;
        this.count = count;
        this.flag = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        Intent i = new Intent("com.google.MyService");
        i.putExtra(MainActivity.CODE, MainActivity.CODE_TIME);
        Calendar calendar = Calendar.getInstance();
        i.putExtra(MainActivity.TIME, calendar.get(Calendar.HOUR_OF_DAY) + " : " +
                                        calendar.get(Calendar.MINUTE) + " : " +
                                        calendar.get(Calendar.SECOND));
        myService.sendBroadcast(i);
        while(flag) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (flag) {
                count++;
                i.putExtra(MainActivity.CODE, MainActivity.CODE_COUNT_SHOW);
                i.putExtra(MainActivity.COUNT, count);
                myService.sendBroadcast(i);
            }
        }
    }

    public void stop() {
        this.flag = false;
    }
}
