package com.blake.where;


import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class ClockPunchTask extends AsyncTask<String,Void,String>{

    private String userId;
    private List<String> dbRows;
    private Context context;
    private int method = 0;
    private int entry_type = 0;

    private String latitude;
    private String longitude;

    private String status;
    private String results;

    //flag 0 means get and 1 means post.(By default it is get.)
    public ClockPunchTask(Context context, int userId,
                          int entry_type, int flag, List<String> rows, String latitude, String longitude) {
        this.context = context;
        this.userId= String.valueOf(userId);

        this.entry_type=entry_type;
        this.method = flag;
        this.dbRows = rows;
      //  this.results = results;
      //  this.status = status;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    protected void onPreExecute(){

    }
    @Override
    protected String doInBackground(String... arg0) {
        String userId = arg0[0];
        String password = arg0[1];
        String username = arg0[2];
        if(method == RestWebServices.GET_RETRIEVE_ROW){
            return RestWebServices.getData(username, password);
        }
        else if (method== RestWebServices.POST_RETRIEVE_ROW){
                return RestWebServices.postVerifyUserObtainWorkerId(username, password);

        }else if(method==RestWebServices.POST_RETRIEVE_ROWS){
            //show list of log entries

            return  RestWebServices.postSelectRowsAsJSONString();

        }
        else if(method==RestWebServices.POST_INSERT_ROW){
            return RestWebServices.postEntry(Integer.valueOf(userId), entry_type, latitude, longitude);
         //   postEntry(int worker_id, int entry_type, String latitude, String longitude)

        }
        else return "";
    }


    @Override
    protected void onPostExecute(String result){
       // this.statusField.setText("Login Successful");
        status="Login Successful";
    // Todo: here are the server results prints db rows from json
        if(method==RestWebServices.POST_RETRIEVE_ROWS){
            dbRows=  RestWebServices.getResultsJSONAsList(result);
            System.out.println(dbRows.toString());
           // this.roleField.setText(dbRows.toString());
            results = dbRows.toString();
        }else{
            results = result;
        }



    }

}