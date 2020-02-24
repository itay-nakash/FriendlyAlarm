package com.example.itay.newfrindlyalarm;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by itay on 25/9/17.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MyAlarmViewHolder> {

    ArrayList<SingleAlarm> alarmsList = new ArrayList<>();
    private LayoutInflater inflator;
    private int numOfItems;


    public AlarmAdapter(Context context, ArrayList<SingleAlarm> alarmsList) {
        inflator = LayoutInflater.from(context);
        this.numOfItems = alarmsList.size();
        this.alarmsList = alarmsList;
    }

    @Override
    public MyAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_row, parent, false);
        MyAlarmViewHolder alarmViewHolder = new MyAlarmViewHolder(view);
        return alarmViewHolder;
    }

    @Override
    public void onBindViewHolder(MyAlarmViewHolder holder, final int position) {

        String alarmText = alarmsList.get(position).getStringAlarmTime();
        String alarmDay = alarmsList.get(position).convertDayToString();
        holder.timeText.setText(alarmText);
        holder.alarmDayText.setText(alarmDay);
        holder.alarmMessage.setText(alarmsList.get(position).getAlarmMessage());
        if(!alarmsList.get(position).isRepeatAlarm())
            holder.repeatButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return numOfItems;
    }

    public void setNewAlarms(ArrayList<SingleAlarm> newAlarms){
        // oldAlarms is the list of items currently displayed by the adapter
        AlarmDiffCallBack diffCallBack = new AlarmDiffCallBack(alarmsList, newAlarms);

        // Second parameter is to detect "movement". If your list is always sorted the same way, you can set it to false to optimize
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallBack, true);

        this.alarmsList.clear();
        this.alarmsList.addAll(newAlarms);
        // This will notify the adapter of what is new data, and will animate/update it for you ("this" being the adapter)
        diffResult.dispatchUpdatesTo(this);
    }

    class MyAlarmViewHolder extends RecyclerView.ViewHolder {

        TextView timeText, alarmDayText, alarmMessage;
        ImageView repeatButton;

        public MyAlarmViewHolder(View itemView) {
            super(itemView);
            timeText = (TextView) itemView.findViewById(R.id.textViewAlarmTime);
            alarmDayText = (TextView) itemView.findViewById(R.id.textViewDeatiles);
            alarmMessage=(TextView) itemView.findViewById(R.id.alarmMessage);
            repeatButton=(ImageView) itemView.findViewById(R.id.imageViewRepeat);

            //Lisener for the repeat picture
            repeatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(repeatButton.getVisibility()==View.VISIBLE){
                        repeatButton.setVisibility(View.INVISIBLE);
                    }else{
                        repeatButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
