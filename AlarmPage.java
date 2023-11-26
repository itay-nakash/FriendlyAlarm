package com.example.itay.newfrindlyalarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class AlarmPage extends AppCompatActivity {

    Button button_turn_alarm_off;
    AlarmManager alarm_manager;
    SingleAlarm currentAlarm;
    //Dialog dialog;
    Snooze snooze;
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_page);
        MainActivity.isRunning=false;
        alarm_manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent=getIntent();
        currentAlarm=HandlerPassInByte.reciveAlarmAsByte(intent);
        checkAlarmOff();
        checkSnooze(this,alarm_manager);
    }
    public void initializeImageView(){
        imageView1=(ImageView) findViewById(R.id.snooze1);
        imageView2=(ImageView) findViewById(R.id.snooze2);
        imageView3=(ImageView) findViewById(R.id.snooze3);
        imageView4=(ImageView) findViewById(R.id.snooze4);
        imageView5=(ImageView) findViewById(R.id.snooze5);
        imageView6=(ImageView) findViewById(R.id.snooze6);
    }
    public void setPlaceAndResourceImageView(){



    }
    public void checkAlarmOff(){
        button_turn_alarm_off = (Button) findViewById(R.id.button_alarm_off);
        button_turn_alarm_off.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopAlarm();
                Intent backToMainActivity = new Intent(AlarmPage.this, MainActivity.class);
                backToMainActivity.putExtra("remove alarm id",currentAlarm.getAlarmIndex());
                startActivity(backToMainActivity);
            }
        });
    }
    public void checkSnooze(Context context,AlarmManager alarm_manager){
        ArrayList<Integer> checkBoxesTimes=currentAlarm.getSnoozeTimes();
        for(int i=0;i<checkBoxesTimes.size();i++){

        }



    }
    public void stopAlarm() {
        NotificationManager notifiManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int alarmId= getIntent().getExtras().getInt("special id");
        //String alarmMessage= getIntent().getExtras().getString("alarm message");
        notifiManager.cancel(alarmId);
        Intent cancelIntent = new Intent(AlarmPage.this, Alarm_Reciver.class);
        cancelIntent.putExtra("click_status", false);
        HandlerPassInByte.addByteData(cancelIntent,currentAlarm);
        AlarmPage.this.sendBroadcast(cancelIntent);
    }

    public void operateSnooze(int snoozeTime){
        snooze.operate(snoozeTime);
    }

    public int getPicId(int numForSnooze){
        int id=0;
        switch (numForSnooze) {
            case 3:  id = R.drawable.number3;
                break;
            case 5:  id = R.drawable.snooze5;
                break;
            case 7:  id=R.drawable.number7;
                break;
            case 10: id=R.drawable.snooze10;
                break;
            case 15: id=R.drawable.number15;
                break;
            case 20: id=R.drawable.number20;
                break;
        }
        return id;
    }

      /*
        button_snooze=(Button) findViewById(R.id.button_snooze);
        button_snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snooze= new Snooze(context,alarm_manager);
                if(AlarmSettings.snooze) {
                    stopAlarm();
                }
                else{
                    Toast toast;
                    toast = Toast.makeText(AlarmPage.this, "Snooze option is off ", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1250);//creating a toast to the user
                    toast.show();

                }
            }
        }); */

    /* public void activateDialog()
    {
        this.dialog = new Dialog(this);
        dialog.setContentView(R.layout.snooze_dialog);
        dialog.setTitle("Time for snooze");
        dialog.setCancelable(true);
        dialog.show();
    }
    public void checkUserSnoozeTime(){

        ImageButton snooze1 =(ImageButton) dialog.findViewById(R.id.snooze1);
        snooze1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operateSnooze(1);
            }
        });
        ImageButton snooze5 = (ImageButton)dialog.findViewById(R.id.snooze5);
        snooze5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operateSnooze(5);
            }
        });
        ImageButton snooze10 =(ImageButton) dialog.findViewById(R.id.snooze10);
        snooze10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operateSnooze(10);
            }
        });
    } */

}
