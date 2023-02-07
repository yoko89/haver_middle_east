package com.neklaway.havermiddle_east.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.neklaway.havermiddle_east.activity.visaLog.VisaAlert;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        VisaAlert.enqueueWork(context,VisaAlert.class,1234,intent);
    }
}
