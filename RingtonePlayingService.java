package com.example.itay.newfrindlyalarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by itay on 9/12/17.
 */

public class RingtonePlayingService extends Service{
    static MediaPlayer mediaAlarm;// made static to change from any object the same music.
    private boolean isRunning, buttonStatus;
    private SingleAlarm currentAlarm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   RingtoneHelper ringtoneHelper=new RingtoneHelper();
        currentAlarm=HandlerPassInByte.reciveAlarmAsByte(intent);
        getIntentsValues(intent);
        if (buttonStatus) {
            // creat an instance of the media player
            mediaAlarm = new MediaPlayer();
            Uri uri=RingtoneManager.getValidRingtoneUri(this);
            mediaAlarm.setAudioStreamType(AudioManager.STREAM_ALARM);
            try {
                mediaAlarm.setDataSource(this, uri);
                mediaAlarm.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            notification();
        }
        if(MainActivity.isRunning){
            Intent alarmStarted=new Intent(this,AlarmPage.class);
            alarmStarted.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            HandlerPassInByte.addByteData(alarmStarted,currentAlarm);
            startActivity(alarmStarted);
        }
        ManageAlarm(buttonStatus);
        return START_NOT_STICKY;
    }
    public void getIntentsValues(Intent intent){
        buttonStatus = intent.getExtras().getBoolean("click_status");
        currentAlarm=HandlerPassInByte.reciveAlarmAsByte(intent);
    }
//    public Uri ringtoneToUri(){
//
//    }
    public void ManageAlarm(boolean buttonStatus){
        //if theres no music and the button clicked
        if(!this.isRunning&&buttonStatus) {
            mediaAlarm.start();
            this.isRunning=true;
            //moving to the AlarmPage
            Intent alarmStarted=new Intent(this,AlarmPage.class);
            HandlerPassInByte.addByteData(alarmStarted,currentAlarm);
            alarmStarted.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(alarmStarted);
        }
        // there is music and button unclicked.
        else if(this.isRunning&&!buttonStatus)
        {
            mediaAlarm.stop();
            mediaAlarm.reset();
            this.isRunning=false;
        }

    }
    //not sure it should be here
       public void notification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder=setNotificationSettings(builder);
        //intent for the pending intent
        Intent notifiIntent= new Intent(this,AlarmPage.class);
        notifiIntent.putExtra("click_status",currentAlarm.getAlarmIndex());
        TaskStackBuilder stackBuilder= TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AlarmPage.class);
        stackBuilder.addNextIntent(notifiIntent);
        //pending intent for this activity- so the notification manage here.
        PendingIntent notifiPendingIntent= stackBuilder.getPendingIntent(currentAlarm.getAlarmIndex(), PendingIntent.FLAG_UPDATE_CURRENT);
        //connect the bulder to the pending intent
        builder.setContentIntent(notifiPendingIntent);
        //initialize notifiManager
        NotificationManager notifiManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //operatin the specific notifi
        notifiManager.notify(currentAlarm.getAlarmIndex(),builder.build());
    }
    public NotificationCompat.Builder setNotificationSettings(NotificationCompat.Builder builder){
        String textForNotification=currentAlarm.getAlarmMessage();
        builder.setSmallIcon(R.drawable.alarmclock);
        builder.setContentTitle("Alarm");
        builder.setContentText(textForNotification);
        builder.setAutoCancel(true);
        return builder;
    }
    public void onDestroy() {
        // Tell the user we stopped
        super.onDestroy();
        Toast.makeText(this, "on destroyed called", Toast.LENGTH_SHORT).show();
        this.isRunning=false;
    }
}
