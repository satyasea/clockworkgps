package com.blake.where;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blake.where.clockpunch.ClockPunchTask;
import com.blake.where.location.WhereActivity;
import com.blake.where.state.ClockStateTask;


import java.util.Date;

public class ClockWorkMainActivity extends Activity implements OnTaskCompleted {


    boolean isLoggedIn = false;

    ImageView statusColor;

    private TextView status,result;
    private TextView txt;
    private TextView welcome;
    private String[] coords = new String[2];
    static int ENTRY_TYPE_LOGIN = 0;
    static int ENTRY_TYPE_LOGOUT = 1;

    Button login;
    Button logout;

    ClockWorkUserEntry entry;




    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        status = new TextView(this);
        result  = new TextView(this);
        txt = (TextView) findViewById(R.id.txt);
        txt.setVisibility(View.VISIBLE);

        statusColor=(ImageView) findViewById(R.id.img);
        welcome = (TextView) findViewById(R.id.hello);
        login = (Button) findViewById(R.id.btn_login);
        logout = (Button) findViewById(R.id.btn_logout);
      //  quit = (Button) findViewById(R.id.btn_quit);

        //build button activity anon class
        Button.OnClickListener l = new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (v == login){
                    login();

                } else if (v == logout)
                {
                    logout();
                }/*else if (v == quit){
                    finish();
                }*/
            }
        };

        login.setOnClickListener(l);
        logout.setOnClickListener(l);

        //get worker id from previous loginactivity
        Intent intent = getIntent();
        String id = intent.getStringExtra("worker_id");
        if(id.length()==0){
            txt.setText("Login Fail!");
        }else {
            txt.setText(id);
            entry = new ClockWorkUserEntry(Integer.valueOf(id));

        }

        //check app login state / status, results are received by onTaskCompleted(String value)
        new ClockStateTask(this,this).execute(id);
    }

    /*
    Called after stopping, calls onStart()
    */
    @Override
    public void onRestart(){
        super.onRestart();
    }
    /*
    Called by methods onCreate() and onRestart(),
                  if activity goes to front it calls onResume().
        if activity is hidden it calls onStop()
     */
    @Override
    public void onStart(){
        super.onStart();
    }
    /*
    activity will interact with user, will receive user input
    calls onPause()
     */
    @Override
    public void onResume(){
        super.onResume();
       //todo rebuild ui
        //check app login state / status

    }
    /*
    called by onResume(),
    do short tasks before resuming previous activity - save changes etc
        if activity goes to front it calls onResume().
    if activity is hidden it calls onStop()
 */
    @Override
    public void onPause(){
        super.onPause();
    }
    /*
    activity not visible, covered by other activity
    calls onRestart() if activity is coming back to user
    calls onDestroy() if activity is going away
     */
    @Override
    public void onStop(){
        super.onStop();
    }
    /*
    called by finish(), or because system destroyed it.
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    //end lifecycle overrides

    private void login(){
        if(isLoggedIn) return;
        //todo redo entry object
        entry.setEntryType(ENTRY_TYPE_LOGIN);
        entry.setEntryTime(new Date());
        Intent i = new Intent(this, WhereActivity.class);
        startActivityForResult(i, 0);
    }


    private void logout(){
        if(!isLoggedIn) return;
        entry.setEntryType(ENTRY_TYPE_LOGOUT);
        entry.setEntryTime(new Date());
        Intent i = new Intent(this, WhereActivity.class);
        startActivityForResult(i, 1);
    }


    /*
here are the results from calling whereactivity for gps coordinates
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==0){
                if(null!=data){
                    coords[0] = data.getStringExtra("lat");
                    coords[1]= data.getStringExtra("long");
                    entry.setLat(coords[0]);
                    entry.setLongit(coords[1]);
                    new ClockPunchTask(this, entry).execute("");
                }
                isLoggedIn=true;
                setUIClockedIn();

            }
            if(requestCode==1){
                if(null!=data){
                    coords[0] = data.getStringExtra("lat");
                    coords[1]= data.getStringExtra("long");
                    entry.setLat(coords[0]);
                    entry.setLongit(coords[1]);
                    new ClockPunchTask(this, entry).execute("");
                }
                isLoggedIn=false;
                setUIClockedOut();
            }

        }else {
            isLoggedIn=false;
            setUILocationFailed();
        }
    }

    private void setUIClockedIn(){
        statusColor.setImageResource(R.drawable.green_square);
        statusColor.setVisibility(View.VISIBLE);
        welcome.setText("Clocked In");
        welcome.setTextColor(Color.GREEN);
        welcome.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.VISIBLE);
       // quit.setVisibility(View.INVISIBLE);
    }

    private void setUIClockedOut(){
        statusColor.setImageResource(R.drawable.red_square);
        statusColor.setVisibility(View.VISIBLE);
        welcome.setText("Clocked Out");
        welcome.setTextColor(Color.RED);
        welcome.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);

    }

    private void setUILocationFailed(){
        welcome.setText("Exit and Enable Location.");
        welcome.setTextColor(Color.YELLOW);
        welcome.setVisibility(View.VISIBLE);
        statusColor.setImageResource(R.drawable.orange_square);
        statusColor.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.INVISIBLE);

    }
    // there is no data from db, either because it failed or was empty
    private void setUINoLogData(){
        statusColor.setImageResource(R.drawable.orange_square);
        statusColor.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        welcome.setText("Clock data unavailable.");
        welcome.setTextColor(Color.YELLOW);
        welcome.setVisibility(View.VISIBLE);
    }
/*
   results of webservicetask check for application state (eg, last login type)

 */
    @Override
    public void onTaskCompleted(String value) {
        if(value.length()>0){
            if(Integer.parseInt(value)==0){
                isLoggedIn=true;
            }else if(Integer.parseInt(value)==1) {
                isLoggedIn = false;
            }
            if(isLoggedIn){
                setUIClockedIn();
            }
            else {
                setUIClockedOut();
            }
        } else {
            setUINoLogData();
        }

    }




}
