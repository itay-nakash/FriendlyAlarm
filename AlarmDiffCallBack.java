package com.example.itay.newfrindlyalarm;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.itay.newfrindlyalarm.SingleAlarm;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AlarmDiffCallBack extends DiffUtil.Callback{

    private final ArrayList<SingleAlarm> oldList;
    private final  ArrayList<SingleAlarm> newList;

    public AlarmDiffCallBack( ArrayList<SingleAlarm> oldList, ArrayList<SingleAlarm> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getAlarmIndex() == newList.get(newItemPosition).getAlarmIndex();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getAlarmIndex() == newList.get(newItemPosition).getAlarmIndex();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}