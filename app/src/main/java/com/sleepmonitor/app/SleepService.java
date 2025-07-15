package com.sleepmonitor.app;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

public class SleepService extends Service {
    private boolean screenLocked = false;
    private long lastLockedTime = 0;
    private Handler handler = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra("event");

        if (action != null) {
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                screenLocked = true;
                lastLockedTime = System.currentTimeMillis();
            } else if (action.equals(Intent.ACTION_USER_PRESENT)) {
                if (screenLocked) {
                    screenLocked = false;
                    sendToFirebase("awake");
                }
            }
        }

        // Check every 1 min
        handler.postDelayed(() -> {
            if (screenLocked && isNight() && System.currentTimeMillis() - lastLockedTime >= 3600000) {
                sendToFirebase("sleeping");
            }
        }, 60000);

        return START_STICKY;
    }

    private boolean isNight() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return (hour >= 23 || hour < 11);
    }

    private void sendToFirebase(String status) {
        long timestamp = System.currentTimeMillis();
        FirebaseDatabase.getInstance().getReference("users")
                .child(MainActivity.USER_CODE)
                .push()
                .setValue(status + " at " + timestamp);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
