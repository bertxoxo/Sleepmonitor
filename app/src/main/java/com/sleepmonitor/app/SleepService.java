package com.sleepmonitor.app;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class SleepService extends Service {
    private boolean screenLocked = false;
    private long lastLockedTime = 0;
    private Handler handler = new Handler();
    private String userName = "John";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra("screen_event");
        if (action == null) return START_STICKY;

        switch (action) {
            case Intent.ACTION_SCREEN_OFF:
                screenLocked = true;
                lastLockedTime = System.currentTimeMillis();
                break;
            case Intent.ACTION_USER_PRESENT:
                if (screenLocked) {
                    screenLocked = false;
                    sendToAdmin(userName + " is awake");
                }
                break;
        }

        checkSleepPattern();
        return START_STICKY;
    }

    private void checkSleepPattern() {
        handler.postDelayed(() -> {
            long current = System.currentTimeMillis();
            long diff = current - lastLockedTime;
            if (screenLocked && diff > 3600000 && isBetween11PMto11AM()) {
                sendToAdmin(userName + " is sleeping");
            }
        }, 60000);
    }

    private boolean isBetween11PMto11AM() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour >= 23 || hour < 11;
    }

    private void sendToAdmin(String msg) {
        Log.d("ADMIN_NOTIFY", msg);
        // TODO: Add Firebase notification here
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
