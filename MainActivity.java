package com.example.itay.newfrindlyalarm;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.Ringtone;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static boolean isRunning = false;
    Spinner ringtonesSpinner;
    RingtoneHelper ringtoneHelper;
    Context context;
    List<Ringtone> ringtones;
    TimePicker alarmTimePicker;
    AlarmManager alarm_manager;
    boolean repeatAlarm = false;
    ArrayList<SingleAlarm> alarmsList = new ArrayList();
    CheckBox[] checkBoxes = new CheckBox[7];
    Button buttonSettings, buttonAddAlarm;
    EditText alarmMessage;
    HashMap<String, Ringtone> ringtoneHashMap;
    ToggleButton repeatButton;
    AlarmHandler alarmHandler;
    private boolean firstRun = true;//help me know how to set the checkbox
    private RecyclerView showAlarmView;
    private AlarmAdapter alarmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        initializeVariables();
        initializeListener();
        alarmsList=alarmHandler.getTheAlarmListFromJson(alarmsList);
        removeCancledAlarm();
        creatAdapt();
        swipeRecycleView();
        addAlarm(this);
    }

    public void initializeVariables() {
        initializeTimePicker();
        initializeCheckBoxes();
        initializeRingtoneSpinner();
        alarmHandler = new AlarmHandler(context);
        isRunning = true;
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMessage = (EditText) findViewById(R.id.alarmMessage);
        creatAdapt();
        showAlarmView.setLayoutManager(new LinearLayoutManager(this));
        //initialize my buttons
        buttonSettings = (Button) findViewById(R.id.buttonSetting);
        //   button_to_frindlyalarm = (Button) findViewById(R.id.button_to_frindlyalarm);
        //   button_to_animals = (Button) findViewById(R.id.button_to_animals);
        buttonAddAlarm = (Button) findViewById(R.id.button_add_alarm);
    }
    public void initializeRingtoneSpinner(){
        ringtonesSpinner= (Spinner) findViewById(R.id.ringtonesSpinner);
        ringtoneHelper=new RingtoneHelper(context);
        List<Ringtone> ringtones=ringtoneHelper.fetchAvailableRingtones(MainActivity.this);
        HashMap<String, Ringtone> ringtoneHashMap= ringtoneHelper.ringtonesToMap(ringtones,this);
        //ringtonesSpinner=ringtoneHelper.ringtoneToSpinner(ringtonesSpinner,ringtones,ringtoneHashMap);
        ringtoneToSpinner();
    }
    public void ringtoneToSpinner(){
        ringtonesSpinner= (Spinner) findViewById(R.id.ringtonesSpinner);
        ringtoneHelper=new RingtoneHelper(context);
        ringtones=ringtoneHelper.fetchAvailableRingtones(MainActivity.this);
        ringtoneHashMap= ringtoneHelper.ringtonesToMap(ringtones,this);
        ArrayList<String> keysList=new ArrayList<>();
        keysList.addAll(ringtoneHashMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item,
                keysList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ringtonesSpinner.setAdapter(adapter);
    }

    public void initializeCheckBoxes() {
        checkBoxes[0] = (CheckBox) findViewById(R.id.checkBoxDay1);
        checkBoxes[1] = (CheckBox) findViewById(R.id.checkBoxDay2);
        checkBoxes[2] = (CheckBox) findViewById(R.id.checkBoxDay3);
        checkBoxes[3] = (CheckBox) findViewById(R.id.checkBoxDay4);
        checkBoxes[4] = (CheckBox) findViewById(R.id.checkBoxDay5);
        checkBoxes[5] = (CheckBox) findViewById(R.id.checkBoxDay6);
        checkBoxes[6] = (CheckBox) findViewById(R.id.checkBoxDay7);
     //   if(firstRun)
        setTodayCheckBoxTrue();
    }

    public void removeCancledAlarm(){
        // You can be pretty confident that the intent will not be null here.
        Intent intent = getIntent();
        if(intent.hasExtra("remove alarm id")){
            int id=intent.getExtras().getInt("remove alarm id");
            alarmsList.remove(id);
        }
    }

    //not tested yet
    public void setTodayCheckBoxTrue() {
        Calendar current = Calendar.getInstance();
        int dayToday = current.get(Calendar.DAY_OF_WEEK);
        //-1 since arrays starts at 0
        int todayInNum=dayToday-1;
        checkBoxes[todayInNum].setChecked(true);
    }

    public void initializeListener() {
        final Intent intentSettings = new Intent(this.context, AlarmSettings.class);//intents for buttons
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intentSettings);
            }
        });
        repeatButton = (ToggleButton) findViewById(R.id.repeateButton);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatAlarm)
                    repeatAlarm = false;
                else
                    repeatAlarm = true;
            }
        });
    }

    public void initializeTimePicker() {
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        alarmTimePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmTimePicker.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            alarmTimePicker.setMinute(Calendar.getInstance().get(Calendar.MINUTE) + 1);
        } else {
            alarmTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            alarmTimePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
        }
    }

    public Calendar timePickerToCalendar(TimePicker alarmTimePicker) {
        Calendar calendar = Calendar.getInstance();
        //getting the time the user chose in the timepicker to the calender varible
        if (android.os.Build.VERSION.SDK_INT <= 23) {// Checking the API to choose between gethour and getcurrenthour.
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
        }
        return calendar;
    }


    public void addAlarm(final Context context) {
        //creating a new alarm and set the relevant varible to the addAlarm function
        buttonAddAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //the calndar is without the right day, the day is added in the handler(int the loop)
                Calendar calendarForAlarm = timePickerToCalendar(alarmTimePicker);
                      String spinnnerText=ringtonesSpinner.getSelectedItem().toString();
                Ringtone ringtoneToAlarm=ringtoneHashMap.get(spinnnerText);
                alarmsList = alarmHandler.addAlarm(checkBoxes,
                        alarmMessage,
                        alarmsList,
                        alarm_manager,
                        calendarForAlarm,
                        repeatAlarm,
                        ringtoneToAlarm);
                alarmsList=alarmHandler.sortAlarms(alarmsList);
                 creatAdapt();
            }
        });
    }
    public Ringtone getRingtoneFromSpinner(){
        String spinnnerText=ringtonesSpinner.getSelectedItem().toString();
        Ringtone returnRingtone=ringtoneHashMap.get(spinnnerText);
        return returnRingtone;
    }

    public void updateRecycleView(ArrayList<SingleAlarm> newAlarmsList){}
    public void creatAdapt() {
        showAlarmView = (RecyclerView) findViewById(R.id.alarm_list);
        alarmAdapter = new AlarmAdapter(this, alarmsList);
        showAlarmView.setAdapter(alarmAdapter);
    }
    public void swipeRecycleView() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.LEFT) {
                    int position = viewHolder.getAdapterPosition();
                    //cancelAlarm(position);
                    int alarmIndex=alarmsList.get(position).getAlarmIndex();
                    alarmHandler.cancelAlarm(alarmIndex,alarm_manager);
                    alarmsList.remove(viewHolder.getAdapterPosition());
                    alarmHandler.saveAlarmListToJson(alarmsList);
                  //  alarmAdapter.setNewAlarms(alarmsList);
                    creatAdapt();
                }
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    int position = viewHolder.getAdapterPosition();
                    creatAdapt();
                    //edit the alarm
                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Paint p = new Paint();
                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                    if (dX < 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(showAlarmView);
    }
    //getters and setters

    public void alarmsListRemoveOne() {
        alarmsList.remove(alarmsList.size());
    }
    public void saveNewAlarmLiset(ArrayList<SingleAlarm> alarmsList){
        alarmHandler.saveAlarmListToJson(alarmsList);

    }
}