package com.blake.where;

 import android.content.Context;
 import android.os.AsyncTask;


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
            return RestWebServices.obtainLastEntryType(string1);
        }
        //use this method for single results
        else if (method== RestWebServices.POST_RETRIEVE_ROW){
                return RestWebServices.postVerifyUserObtainWorkerId(string1, string2);
        //use this method for multiple results
        }else if(method==RestWebServices.POST_RETRIEVE_ROWS){
            //show list of log entries
            return  RestWebServices.postSelectRowsAsJSONString();
        }
        else if(method==RestWebServices.POST_INSERT_ROW){
            //insert log intry
            return RestWebServices.postEntry(103, 0, "124.6", "12.33");
        }
        else return "";
    }


    @Override
    protected void onPostExecute(String result){
        //hand off result to caller (LoginActivity)
            listener.onTaskCompleted(result);
    }
}