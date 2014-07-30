package com.blake.where.unused;

 import android.content.Context;
 import android.os.AsyncTask;
 import com.blake.where.clockpunch.ClockPunchWebService;
 import com.blake.where.state.ClockStateWebService;
 import com.blake.where.login.LoginWebService;
 import com.blake.where.OnTaskCompleted;


public class WebServiceTask extends AsyncTask<String,Void,String>{

    private Context context;
    private int method = 0;
    private OnTaskCompleted listener;

    //flag 0 means get and 1 means post.(By default it is get.)
    public WebServiceTask(Context context, OnTaskCompleted listener, int flag) {
        this.context = context;
        method = flag;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        String string1 = arg0[0];
        String string2  = arg0[1];
        //get method, for testing only
        if(method == RestWebServices.GET_RETRIEVE_ROW){
            return RestWebServices.getData(string1, string2);
        }
       else if(method==RestWebServices.POST_RETRIEVE_TYPE_ROW){
            return ClockStateWebService.obtainLastEntryType(string1);
        }
        //use this method for single results
        else if (method== RestWebServices.POST_RETRIEVE_ROW){
                return LoginWebService.postVerifyUserObtainWorkerId(string1, string2);
        //use this method for multiple results
        }else if(method==RestWebServices.POST_RETRIEVE_ROWS){
            //show list of log entries
            return  RestWebServices.postSelectRowsAsJSONString();
        }
        else if(method==RestWebServices.POST_INSERT_ROW){
            //insert log intry
            return ClockPunchWebService.postClockPunchEntry(103, 0, "124.6", "12.33");
        }
        else return "";
    }


    @Override
    protected void onPostExecute(String result){
        //hand off result to caller (LoginActivity)
            listener.onTaskCompleted(result);
    }
}