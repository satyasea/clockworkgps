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
import com.blake.where.tsheets.TsheetCreateWebService;
import com.blake.where.tsheets.TsheetsCreateGeoTask;
import com.blake.where.tsheets.TsheetsCreateTask;
import com.blake.where.tsheets.TsheetsEditTask;

import java.util.Date;

public class ClockWorkMainActivity extends Activity implements OnTaskCompleted {

    ImageView statusColor;
    private TextView txt;
    private TextView welcome;
    Button login;
    Button logout;
    static int ENTRY_TYPE_LOGIN = 0;
    static int ENTRY_TYPE_LOGOUT = 1;
    private String[] coords = new String[2];
    boolean isLoggedIn = false;
    ClockWorkUserEntry entry;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        txt = (TextView) findViewById(R.id.txt);
        txt.setVisibility(View.VISIBLE);
        statusColor=(ImageView) findViewById(R.id.img);
        welcome = (TextView) findViewById(R.id.hello);
        login = (Button) findViewById(R.id.btn_login);
        logout = (Button) findViewById(R.id.btn_logout);

        //build button activity anon class
        Button.OnClickListener l = new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (v == login){
                    login();
                } else if (v == logout)  {
                    logout();
                }
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

    private void login(){
        if(isLoggedIn) return;
        entry.setEntryType(ENTRY_TYPE_LOGIN);
      //  entry.setEntryTime(new Date());
        Intent i = new Intent(this, WhereActivity.class);
        startActivityForResult(i, 0);
    }

    private void logout(){
        if(!isLoggedIn) return;
        entry.setEntryType(ENTRY_TYPE_LOGOUT);
       // entry.setEntryTime(new Date());
        Intent i = new Intent(this, WhereActivity.class);
        startActivityForResult(i, 1);
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
        //check app login state / status, results are received by onTaskCompleted(String value)
        new ClockStateTask(this,this).execute(String.valueOf(entry.getId()));

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

    /*
here are the results from calling whereactivity for gps coordinates
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(null!=data){
                coords[0] = data.getStringExtra("lat");
                coords[1]= data.getStringExtra("long");
                entry.setLat(coords[0]);
                entry.setLongit(coords[1]);
                /*
                todo: Here is where the clockpunchtask is executed after the entry is built with location data
                 */
                new ClockPunchTask(this, entry).execute("");
                //todo: tsheets

                int markId = 1636899;
                int blakeId = 1635347;
                if(requestCode==ENTRY_TYPE_LOGIN) {
                    new TsheetsCreateTask(this, blakeId).execute("");
                    new TsheetsCreateGeoTask(this, entry, blakeId).execute("");
                }else{
                    new TsheetsCreateGeoTask(this, entry, blakeId).execute("");
                    new TsheetsEditTask(this, blakeId).execute("");
                }
            }
            /*
            todo: After the clockpunchtask runs, then the UI state is reset based upon request code (login type),
            todo: should tighten this up and get a confirmation from the web service that the insert was successful
             */
            if(requestCode==ENTRY_TYPE_LOGIN){
                isLoggedIn=true;
                setUIClockedIn();
            }else{
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

}
