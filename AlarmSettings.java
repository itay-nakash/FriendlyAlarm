package com.example.itay.newfrindlyalarm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlarmSettings extends AppCompatActivity {

    Switch snoozeSwitch;
    Button ringtoneBtn;
    CheckBox[] checkBoxes=new CheckBox[6];
    static ArrayList<Integer> snoozeTimes=new ArrayList<>();
    static boolean snooze=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initializeVaribles();
        initializeCheckBoxArray();
        final SharedPreferences examplePrefs = getSharedPreferences("pref",0);
        final SharedPreferences.Editor editor = examplePrefs.edit();
        setListeners(editor);
    }

    public void initializeVaribles(){
        snoozeSwitch=(Switch)findViewById(R.id.switch_snooze);
        ringtoneBtn=(Button)findViewById(R.id.buttonRingtone);
        checkBoxes[0]=(CheckBox)findViewById(R.id.checkBox_3);
        checkBoxes[1]=(CheckBox)findViewById(R.id.checkBox_5);
        checkBoxes[2]=(CheckBox)findViewById(R.id.checkBox_7);
        checkBoxes[3]=(CheckBox)findViewById(R.id.checkBox10);
        checkBoxes[4]=(CheckBox)findViewById(R.id.checkBox15);
        checkBoxes[5]=(CheckBox)findViewById(R.id.checkBox20);
    }
    //hardcoded but i think i had no choice
    public void initializeCheckBoxArray(){
        if(checkBoxes[0].isChecked())
            snoozeTimes.add(3);
        if(checkBoxes[1].isChecked())
            snoozeTimes.add(5);
        if(checkBoxes[2].isChecked())
            snoozeTimes.add(7);
        if(checkBoxes[3].isChecked())
            snoozeTimes.add(10);
        if(checkBoxes[4].isChecked())
            snoozeTimes.add(15);
        if(checkBoxes[5].isChecked())
            snoozeTimes.add(20);
    }
    public void setListeners(final SharedPreferences.Editor editor){
        snoozeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("checkedkey", snoozeSwitch.isChecked());
                editor.commit();
                if(snoozeSwitch.isChecked()){
                    snooze=true;
                }
                else {
                    snooze=false;
                }

            }
        });
    }

}
