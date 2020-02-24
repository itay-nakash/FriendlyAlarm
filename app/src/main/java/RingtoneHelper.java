package com.example.itay.newfrindlyalarm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by itay on 11/10/17.
 */

class RingtoneHelper {
    Context context;

    RingtoneHelper(Context context) {
        this.context = context;
    }

    public List<Ringtone> fetchAvailableRingtones(Context context) {

        List<Ringtone> ringtones = new ArrayList<>();
        RingtoneManager mgr = new RingtoneManager(context);
        mgr.setType(RingtoneManager.TYPE_ALARM);
        int n = mgr.getCursor().getCount();
        for (int i = 0; i < n; i++) {
            ringtones.add(mgr.getRingtone(i));
        }
        return ringtones;
    }

    public void changeRingtone(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("randomizer", Context.MODE_PRIVATE);
        if (!preferences.getBoolean("active", false))
            return;

        RingtoneManager mgr = new RingtoneManager(context);
        Random random = new Random(System.currentTimeMillis());

        int n = random.nextInt(mgr.getCursor().getCount());

        RingtoneManager.setActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_RINGTONE, mgr.getRingtoneUri(n));
    }

    public HashMap<String, Ringtone> ringtonesToMap(List<Ringtone> ringtones, Context context) {
        HashMap<String, Ringtone> ringtonesMap = new HashMap<>();
        for (int i = 0; i < ringtones.size(); i++) {
            ringtonesMap.put(ringtones.get(i).getTitle(context), ringtones.get(i));
        }
        return ringtonesMap;
    }


}



//    public void ringtoneTest(){
//        RingtoneManager
//                mRingtoneManager2 = new RingtoneManager(this); //adds ringtonemanager
//        mRingtoneManager2.setType(RingtoneManager.TYPE_RINGTONE); //sets the type to ringtones
//        mRingtoneManager2.setIncludeDrm(true); //get list of ringtones to include DRM
//
//        Cursor mCursor2 = mRingtoneManager2.getCursor(); //appends my cursor to the ringtonemanager
//
//        startManagingCursor(mCursor2); //starts the cursor query
//
//        //prints output for diagnostics
//        String test = mCursor2.getString(mCursor2.getColumnIndexOrThrow(RingtoneManager.EXTRA_RINGTONE_TITLE));
//
//        String[] from = {mCursor2.getColumnName(RingtoneManager.TITLE_COLUMN_INDEX)}; // get the list items for the listadapter could be TITLE or URI
//
//        int[] to = {android.R.id.text1};
//
//        // create simple cursor adapter
//        SimpleCursorAdapter adapter =
//                new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mCursor2, from, to );
//        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
//        // get reference to our spinner
//        Spinner s = (Spinner) findViewById( R.id.spinner1);
//        s.setAdapter(adapter);
//    }




//    @Override
//    public void onItemSelected(AdapterView<?> arg0, View arg1,
//                               int arg2, long arg3) {
//        // TODO Auto-generated method stub
//        int pos = s.getSelectedItemPosition();
//        Toast.makeText(this, pos, Toast.LENGTH_SHORT).show();
//    }
//}

