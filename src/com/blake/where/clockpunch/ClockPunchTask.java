package com.blake.where.clockpunch;


import android.content.Context;
import android.os.AsyncTask;
public class ClockPunchTask extends AsyncTask<String,Void,String>{

    private String userId;
    private Context context;
    private int entry_type;
    private String latitude;
    private String longitude;

    public ClockPunchTask(Context context, int userId, int entry_type, String latitude, String longitude) {
        if(context == null || userId == 0 || latitude == null || longitude == null){
            return;
        }
        this.context = context;
        this.userId= String.valueOf(userId);
        this.entry_type=entry_type;
        this.latitude=latitude;
        this.longitude=longitude;
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