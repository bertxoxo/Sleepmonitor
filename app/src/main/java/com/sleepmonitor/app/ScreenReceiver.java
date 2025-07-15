package com.sleepmonitor.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = new Intent(context, SleepService.class);
        serviceIntent.putExtra("screen_event", action);
        context.startService(serviceIntent);
    }
}
