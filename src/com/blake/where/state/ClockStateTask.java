package com.blake.where.state;

import android.content.Context;
import android.os.AsyncTask;
import com.blake.where.OnTaskCompleted;


public class ClockStateTask extends AsyncTask<String,Void,String>{

    private Context context;
    private OnTaskCompleted listener;

    //flag 0 means get and 1 means post.(By default it is get.)
    public ClockStateTask(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        String id = arg0[0];
            return ClockStateWebService.obtainLastEntryType(id);

    }


    @Override
    protected void onPostExecute(String result){
        //hand off result to caller (LoginActivity)
            listener.onTaskCompleted(result);
    }
}