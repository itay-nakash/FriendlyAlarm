package com.example.itay.newfrindlyalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by itay on 4/10/17.
 */

public class AlarmHandler {

    Context context;

    public AlarmHandler(Context context) {
        this.context = context;
    }


    private boolean userChoiceNew(List<SingleAlarm> alarmList, Calendar calendarTimeForAlarm) {
        boolean userChoiceNew = true;
        for (int i = 0; i < alarmList.size(); i++) {
            Calendar calendar2 = alarmList.get(i).getCalendar();
            if (calendarTimeForAlarm.get(Calendar.DAY_OF_WEEK) == calendar2.get(Calendar.DAY_OF_WEEK)
                    && (calendarTimeForAlarm.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY))
                    && (calendarTimeForAlarm.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE)))
                userChoiceNew = false;
        }
        return userChoiceNew;
    }

    public void alarmExistAnnounce() {

        Toast toast = Toast.makeText(context, "This alarm is already exists ", Toast.LENGTH_SHORT);//creating a toast to the user
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1250);//creating a toast to the user
        toast.show();
    }


    public boolean[] checkDayChoice(CheckBox[] checkBoxes) {
        boolean[] dayChosen = new boolean[7];
        //to make sure the user choose something
        boolean choosen = false;
        for (int i = 0; i < dayChosen.length; i++)
            dayChosen[i] = false;
        for (int i = 0; i < dayChosen.length; i++) {
            if (checkBoxes[i].isChecked()) {
                //adding one- array index starts at 0
                dayChosen[i] = true;
                choosen = true;
            }
        }
        if (!choosen) {
            Toast toast;
            toast = Toast.makeText(context, "You need to choose day for the alarm ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1250);
            toast.show();
        }
        return dayChosen;
    }

    public boolean addDayToCalender(Calendar calendar, int dayForAlarm) {
        boolean userChoiceNextWeek = false;
        Calendar current = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayForAlarm);
//        if (dayForAlarm < current.get(Calendar.DAY_OF_WEEK)) {
//            calendar.add(Calendar.DAY_OF_YEAR, 7);
//            userChoiceNextWeek=true;
//        }
        if (calendar.compareTo(current) <= 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            userChoiceNextWeek = true;
        }
        return userChoiceNextWeek;
    }


    public ArrayList<SingleAlarm> addAlarm(CheckBox[] checkBoxes,
                                           EditText alarmMessage,
                                           ArrayList<SingleAlarm> alarmsList,
                                           AlarmManager alarm_manager,
                                           Calendar calendar,
                                           boolean repeatAlarm,
                                           Ringtone ringtoneToAlarm) {
        ArrayList<Integer> snoozeSettings = AlarmSettings.snoozeTimes;
        boolean[] dayChosen = checkDayChoice(checkBoxes);
        String alarmMessageString = alarmMessage.getText().toString();
        for (int i = 0; i < dayChosen.length; i++) {
            if (dayChosen[i]) {
                //array starts at 0, days starts at 1
                int dayInNum = i + 1;
                addSingleAlarm(alarmsList
                        , calendar
                        , dayInNum
                        , repeatAlarm
                        , alarmMessageString
                        , alarm_manager
                        , snoozeSettings
                        , ringtoneToAlarm);
            }
        }
        //alarmsList=sortAlarms(alarmsList);
        //saves the alarms as jason
        saveAlarmListToJson(alarmsList);
        return alarmsList;
    }

    public void addSingleAlarm(ArrayList<SingleAlarm> alarmsList,
                               Calendar calendar,
                               int dayInNum,
                               boolean repeatAlarm,
                               String alarmMessageString,
                               AlarmManager alarmManager,
                               ArrayList<Integer> snoozeTimes,
                               Ringtone ringtoneToAlarm) {
        Calendar tempCalendar = (Calendar) calendar.clone();
        boolean nextWeek = addDayToCalender(tempCalendar, dayInNum);
        if (userChoiceNew(alarmsList, tempCalendar)) {
            int alarmIndex = alarmsList.size();
            SingleAlarm newAlarm = new SingleAlarm(dayInNum,
                    alarmIndex,
                    repeatAlarm,
                    nextWeek,
                    alarmMessageString,
                    tempCalendar,
                    snoozeTimes);
            setTheAlarm(newAlarm, alarmManager, ringtoneToAlarm);
            newAlarm.setSnoozeTimes(AlarmSettings.snoozeTimes);
            newAlarm.newAlarmAnnounce(context);
            alarmsList.add(newAlarm);
        } else {
            alarmExistAnnounce();
        }
    }


    public ArrayList<SingleAlarm> sortAlarms(ArrayList<SingleAlarm> alarmsList) {
        Collections.sort(alarmsList, new Comparator<SingleAlarm>() {
            public int compare(SingleAlarm obj1, SingleAlarm obj2) {
                // ## Ascending order
                return (obj1.getCalendar().compareTo(obj2.getCalendar()));
            }
        });
        return alarmsList;
    }


    public void setTheAlarm(SingleAlarm newAlarm, AlarmManager alarmManager,Ringtone ringtone) {

        final long timeMills = newAlarm.getCalendar().getTimeInMillis();
        PendingIntent alarm_pendingIntent = null;
        final Intent intentReciver = new Intent(this.context, Alarm_Reciver.class);
        //tells the reciver that it clicked- and clicked true(want to set on and not off).
        intentReciver.putExtra("click_status", true);
        //add the ringtone
       // intentReciver.putExtra(, ringtone);
        //tryin to add the singleAlarm object
        HandlerPassInByte.addByteData(intentReciver, newAlarm);
        addByteData(intentReciver, newAlarm);
        alarm_pendingIntent = PendingIntent.getBroadcast(context, newAlarm.getAlarmIndex(), intentReciver, alarm_pendingIntent.FLAG_UPDATE_CURRENT);
        if (newAlarm.isRepeatAlarm()) {
            final long weekInMillis = 1000 * 6 * 60 * 24 * 7;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeMills, weekInMillis, alarm_pendingIntent);
        } else
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeMills, alarm_pendingIntent);//tells to phone to set the alarm
    }

    public void addByteData(Intent myIntent, SingleAlarm alarm) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(alarm);
            out.flush();
            byte[] data = bos.toByteArray();
            myIntent.putExtra("alarm", data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void cancelAlarm(int alarmIndex, AlarmManager alarmManager) {
        PendingIntent cancelPendingIntent = null;
        final Intent intentReciverForCancel = new Intent(this.context, Alarm_Reciver.class);
        //tells the reciver that it clicked- and clicked true(want to set on and not off).
        intentReciverForCancel.putExtra("click_status", false);
        //adding speacil id for the notification. that way i can cancel it after it had been clicked.
        intentReciverForCancel.putExtra("special id", alarmIndex);
        cancelPendingIntent = cancelPendingIntent.getBroadcast(context, alarmIndex, intentReciverForCancel, cancelPendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(cancelPendingIntent);
    }

    //convert the alarmList to gson to save it
    public void saveAlarmListToJson(ArrayList<SingleAlarm> alarmsList) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(alarmsList);

        editor.putString("alarm_list", json);
        editor.commit();
    }

    public ArrayList<SingleAlarm> getTheAlarmListFromJson(ArrayList<SingleAlarm> alarmsList) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("alarm_list", null);
        if (json != null) {
            Type arrayType = new TypeToken<ArrayList<SingleAlarm>>() {
            }.getType();
            alarmsList = gson.fromJson(json, arrayType);
        }
        return alarmsList;
    }


}
