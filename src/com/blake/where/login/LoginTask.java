package com.blake.where.login;

import android.content.Context;
import android.os.AsyncTask;
import com.blake.where.OnTaskCompleted;


public class LoginTask extends AsyncTask<String,Void,String>{

    private Context context;
    private OnTaskCompleted listener;


    public LoginTask(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        String phone = arg0[0];
        String pass  = arg0[1];
        return LoginWebService.postVerifyUserObtainWorkerId(phone,pass);

    }


    @Override
    protected void onPostExecute(String result){
        //hand off result to caller (LoginActivity)
        listener.onTaskCompleted(result);
    }
}