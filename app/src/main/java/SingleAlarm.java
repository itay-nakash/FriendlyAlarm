package com.example.itay.newfrindlyalarm;

import android.app.AlarmManager;
import android.content.Context;
import android.media.Ringtone;
import android.view.Gravity;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by itay on 16/9/17.
 */

public class SingleAlarm implements Serializable{
    public static ArrayList<Integer>  snoozeTimes = new ArrayList<>();
    private boolean userChoiceNextWeek;
    private boolean repeatAlarm;
    private Calendar calendar;
    private String alarmMessage;
    private String stringAlarmTime;
    private int dayForAlarm;
    private int alarmIndex;


    public SingleAlarm(int dayForAlarm,
                       int alarmIndex,
                       boolean repeatAlarm,
                       boolean userChoiceNextWeek,
                       String alarmMessage,
                       Calendar calendar,
                       ArrayList<Integer> snoozeTimes) {

        this.calendar = calendar;
        this.userChoiceNextWeek = userChoiceNextWeek;
        stringAlarmTime = new String();
        this.alarmIndex = alarmIndex;
        this.dayForAlarm = dayForAlarm;
        this.repeatAlarm = repeatAlarm;
        this.alarmMessage = alarmMessage;
        this.snoozeTimes = snoozeTimes;
    }

    //not worked
    public void initializeSnoozeTimes() {
    }


    public void newAlarmAnnounce(Context context) {
        Calendar current = Calendar.getInstance();
        long calenderDifferent = calendar.getTimeInMillis() - current.getTimeInMillis();
        int difInHours = (int) calenderDifferent / (1000 * 60 * 60);
        Toast toast;
        stringAlarmTime = calenderToString();
        String day = convertDayToString();
        if (repeatAlarm)
            toast = Toast.makeText(context, "the alarm is set to every: " + day + " " + stringAlarmTime, Toast.LENGTH_SHORT);//creating a toast to the user
        else {
            if ((difInHours < 24) && (!userChoiceNextWeek)) {
                //adding one to upsize upword the /
                int difInMinutes = (int) calenderDifferent / (1000 * 60) + 1;
                while (difInMinutes > 60)
                    difInMinutes -= 60;
                String fromNow = setsZeroToStrings(difInHours, difInMinutes);
                toast = Toast.makeText(context, "the alarm is set to: " + fromNow + " hours from now", Toast.LENGTH_SHORT);//creating a toast to the user

            } else if (userChoiceNextWeek)
                toast = Toast.makeText(context, "the alarm is set to next: " + day + " " + stringAlarmTime, Toast.LENGTH_SHORT);//creating a toast to the user
            else {
                toast = Toast.makeText(context, "the alarm is set to: " + day + " " + stringAlarmTime, Toast.LENGTH_SHORT);//creating a toast to the user
            }
        }
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1250);//creating a toast to the user
        toast.show();//creating a toast to the user
    }

    public String setsZeroToStrings(int hour, int minute) {
        String time = "";
        if (hour < 10) {
            if (hour == 0)
                time = "00";
            else
                time = "0" + hour;
        } else {
            time += hour;
        }
        if (minute < 10) {
            time += ":0" + minute;
        } else {
            time += ":" + minute;
        }
        return time;
    }

    public String calenderToString() {
        int int_hour, int_minute;
        int_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int_minute = calendar.get(Calendar.MINUTE);
        stringAlarmTime = setsZeroToStrings(int_hour, int_minute);
        return stringAlarmTime;
    }

    public String convertDayToString() {
        String day = "error";
        switch (dayForAlarm) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }








    //setters and getters
    public ArrayList<Integer> getSnoozeTimes() {
        return snoozeTimes;
    }

    public void setSnoozeTimes(ArrayList<Integer> snoozeTimes) {
        this.snoozeTimes = snoozeTimes;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getStringAlarmTime() {
        return stringAlarmTime;
    }

    public void setStringAlarmTime(String stringAlarmTime) {
        this.stringAlarmTime = stringAlarmTime;
    }

    public boolean isUserChoiceNextWeek() {
        return userChoiceNextWeek;
    }

    //Setter's and Getter's
    public void setUserChoiceNextWeek(boolean userChoiceOk) {
        this.userChoiceNextWeek = userChoiceOk;
    }

    public boolean isRepeatAlarm() {
        return repeatAlarm;
    }

    public void setRepeatAlarm(boolean repeatAlarm) {
        this.repeatAlarm = repeatAlarm;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public int getAlarmIndex() {
        return alarmIndex;
    }

    //    @Override
//    public int compareTo(SingleAlarm singleAlarmForCompare) {
//        Calendar calendar2 = singleAlarmForCompare.getCalendar();
//        int dif = (int) (this.calendar.getTimeInMillis() - calendar2.getTimeInMillis());
//        return dif;
//    }

}

