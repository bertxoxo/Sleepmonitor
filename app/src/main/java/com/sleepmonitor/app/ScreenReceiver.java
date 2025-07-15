package com.sleepmonitor.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SleepService.class);
        i.putExtra("event", intent.getAction());
        context.startService(i);
    }
}
