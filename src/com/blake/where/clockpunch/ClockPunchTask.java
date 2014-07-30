package com.blake.where.clockpunch;


import android.content.Context;
import android.os.AsyncTask;
import com.blake.where.ClockWorkUserEntry;

public class ClockPunchTask extends AsyncTask<String,Void,String>{

    private int userId;
    private Context context;
    private int entry_type;
    private String latitude;
    private String longitude;


    public ClockPunchTask(Context context, ClockWorkUserEntry entry) {
        if(context == null || entry.getId() == 0 || entry.getLat() == null || entry.getLongit() == null){
            return;
        }
        this.context = context;
        this.userId= entry.getId();
        this.entry_type=entry.getEntryType();
        this.latitude=entry.getLat();
        this.longitude=entry.getLongit();
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
            return ClockPunchWebService.postClockPunchEntry(Integer.valueOf(this.userId), entry_type, latitude, longitude);

    }


    @Override
    protected void onPostExecute(String result){
    }

}