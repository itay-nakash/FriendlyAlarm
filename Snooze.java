package com.example.itay.newfrindlyalarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by itay on 17/9/17.
 */

public class Snooze {

    Context context;
    Dialog dialog;
    AlarmManager alarm_manager;
    private boolean operated;
    String alarmTimeAfterSnooze;

    public Snooze(Context context, AlarmManager alarmManager) {
        //deafult safty value
        this.context = context;
        this.alarm_manager = alarmManager;
        this.dialog = new Dialog(context);
        //deafult safty value
        this.operated = false;
        alarmTimeAfterSnooze = "";
    }


    public void operate(int snoozeTime) {
        Calendar calendar=Calendar.getInstance();
        settingNewIntentForSnooze(snoozeTime);
        alarmTimeAfterSnooze=alarmTimeAfterSnooze(calendar,snoozeTime);
        Toast toast = Toast.makeText(context, "the alarm is set to: " + alarmTimeAfterSnooze, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void settingNewIntentForSnooze(int snoozeTime) {
        PendingIntent alarmPendingIntent=null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + snoozeTime);
        Intent snoozeIntent = new Intent(context, Alarm_Reciver.class);
        snoozeIntent.putExtra("click_status", true);
        alarmPendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, alarmPendingIntent.FLAG_ONE_SHOT);
        //tells to phone to set the alarm
        alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmPendingIntent);
        //sends the user to mainActivity after the alarm is off
        Intent backToMainActivity = new Intent(context, MainActivity.class);
        context.startActivity(backToMainActivity);
    }
    //same method as in SingleAlarm class
    public String alarmTimeAfterSnooze(Calendar calendar,int snoozeTime) {
        int int_hour, int_minute;
        String hour, minute;
        int_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int_minute = calendar.get(Calendar.MINUTE);
        //checking if I should add the snooze to the hours or minutes.
        while (snoozeTime > 60) {
            int_hour++;
            snoozeTime -= 60;
        }
        if(snoozeTime+int_minute >= 60)
        {
            int_minute=int_minute+snoozeTime-60;
            int_hour++;
        }else{
            int_minute+=snoozeTime;
        }
        hour = String.valueOf(int_hour);
        minute = String.valueOf(int_minute);
        if (int_hour < 10) {
            hour = "0" + hour;
        }
        if (int_minute < 10)
            minute = "0" + minute;
        return  (hour + ":" + minute);
    }




    //some Getters and Setters...

    public boolean isOperated() {
        return operated;
    }

    public void setOperated(boolean operated) {
        this.operated = operated;
    }
}


