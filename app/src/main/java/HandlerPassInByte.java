package com.example.itay.newfrindlyalarm;

import android.content.Intent;

import com.example.itay.newfrindlyalarm.SingleAlarm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by itay on 6/10/17.
 */

public class HandlerPassInByte {

    //gets the intent the activity want to add data to
    public static void addByteData(Intent myIntent,SingleAlarm alarm) {
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
    //gets the intent the activity recived
    public static SingleAlarm reciveAlarmAsByte(Intent intent){

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
