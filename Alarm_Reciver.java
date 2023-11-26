package com.example.itay.newfrindlyalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by itay on 9/11/17.
 */

public class Alarm_Reciver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent= new Intent(context, RingtonePlayingService.class);//intent for the service ringtone playing
        boolean button_status = intent.getExtras().getBoolean("click_status");
        SingleAlarm newAlarm=HandlerPassInByte.reciveAlarmAsByte(intent);
        //  SingleAlarm newAlarm=reciveAlarmAsByte(intent);
        serviceIntent.putExtra("click_status", button_status);//passing button status
        HandlerPassInByte.addByteData(serviceIntent,newAlarm);
        context.startService(serviceIntent);//start the ringtone service
    }

    public SingleAlarm reciveAlarmAsByte(Intent intent){

        ByteArrayInputStream bis = new ByteArrayInputStream(intent.getByteArrayExtra("alarm"));
        ObjectInput in = null;
        SingleAlarm alarm=null;
        try {
            in = new ObjectInputStream(bis);
            alarm = (SingleAlarm)in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return alarm;
    }

}
