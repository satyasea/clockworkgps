package com.blake.where;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClockWorkMainActivity extends Activity implements OnTaskCompleted {

    Button login;
    Button logout;
   // Button quit;
    boolean isLoggedIn = false;
    static String TARGET_EMAIL="saltyfog@gmail.com";
    static String TARGET_PHONE="8325881755";
    ImageView statusColor;
    private List<String> dbRows= new ArrayList<String>();
    private TextView status,result;
    private TextView txt;
    private TextView welcome;
    private String[] coords = new String[2];
    static int ENTRY_TYPE_LOGIN = 0;
    static int ENTRY_TYPE_LOGOUT = 1;


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
     //   quit.setOnClickListener(l);

        //get worker id from previous loginactivity
        Intent intent = getIntent();
        String message = intent.getStringExtra("worker_id");
        if(message.length()==0){
            txt.setText("Login Fail!");
        }else {
            txt.setText( message);
        }

        //check app login state / status, results are received by onTaskCompleted(String value)
        new WebServiceTask(this,this,RestWebServices.POST_RETRIEVE_TYPE_ROW).execute(message,"");
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
        //get worker id from previous loginactivity
        /*
        Intent intent = getIntent();
        String message = intent.getStringExtra("worker_id");
        if(message.length()==0){
            txt.setText("Login Fail!");
        }else {
            txt.setText(message);
        }
        new WebServiceTask(this,this,RestWebServices.POST_RETRIEVE_TYPE_ROW).execute(message,"");
        */

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
        ClockWorkUserEntry entry = new ClockWorkUserEntry();
        entry = new ClockWorkUserEntry();
        entry.setEntryType(ENTRY_TYPE_LOGIN);
        entry.setEntryTime(new Date());
        Intent i = new Intent(this, WhereActivity.class);
        startActivityForResult(i, 0);

    }


    private void logout(){
        if(!isLoggedIn) return;
        //todo redo entry object
        ClockWorkUserEntry entry = new ClockWorkUserEntry();
        entry.setEntryType(1);
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
                    new ClockPunchTask(this, Integer.valueOf(txt.getText().toString()), ENTRY_TYPE_LOGIN,  coords[0], coords[1]).execute("");
                }
                isLoggedIn=true;
                setUIClockedIn();

            }
            if(requestCode==1){
                if(null!=data){
                    coords[0] = data.getStringExtra("lat");
                    coords[1]= data.getStringExtra("long");
                    new ClockPunchTask(this, Integer.valueOf(txt.getText().toString()), ENTRY_TYPE_LOGOUT, coords[0], coords[1]).execute("");
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
      //  quit.setVisibility(View.VISIBLE);
    }

    private void setUILocationFailed(){
        welcome.setText("Exit and Enable Location.");
        welcome.setTextColor(Color.YELLOW);
        welcome.setVisibility(View.VISIBLE);
        statusColor.setImageResource(R.drawable.orange_square);
        statusColor.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.INVISIBLE);
      //  quit.setVisibility(View.VISIBLE);

    }

    private void setUINoLogData(){
        // there is no data from db, either because it failed or was empty
        statusColor.setImageResource(R.drawable.orange_square);
        statusColor.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
     //   quit.setVisibility(View.VISIBLE);
        welcome.setText("Clock data unavalable.");
        welcome.setTextColor(Color.YELLOW);
        welcome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskCompleted(String value) {
        //results of webservicetask check for application state (eg, last login type)
      //  Toast.makeText(this, "login status: " + value, Toast.LENGTH_LONG).show();
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

    private void sendSMS(ClockWorkUserEntry clock, long time){
        SimpleDateFormat ft =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String message = "ClockWork:" + clock.getUsername() + " " + (clock.getEntryType() == 0 ? "Login " : "Logout ") + ft.format(clock.getEntryTime()) + " Total: "+time + " sec.";
        String phoneNo=TARGET_PHONE;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        //    Toast.makeText(getApplicationContext(), "SMS sent.",                   Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        //    Toast.makeText(getApplicationContext(),                   "SMS faild, please try again.",                Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void sendSMS(ClockWorkUserEntry clock){
        SimpleDateFormat ft =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String message = "ClockWork:" + clock.getUsername() + " " + (clock.getEntryType() == 0 ? "Login " : "Logout ") + ft.format(clock.getEntryTime());
        String phoneNo=TARGET_PHONE;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void sendEntryEmail(ClockWorkUserEntry clock){
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{TARGET_EMAIL});
        SimpleDateFormat ft =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String text = "Clock Entry:" + clock.getUsername() + " " + (clock.getEntryType() == 0 ? "Login " : "Logout ") + ft.format(clock.getEntryTime());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ClockWork Entry: " + clock.getUsername() + " - " + (clock.getEntryType() == 0 ? "Login " : "Logout "));
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        this.startActivity(Intent.createChooser(emailIntent, "Mailing ClockWork Entry..."));
    }

/*
    private void showEntries(){
        List<ClockWorkUserEntry> entries = db.getAllEntries();
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat ft =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        for(ClockWorkUserEntry clock: entries){
            sb.append("Clock Entry:" + clock.getUsername() + " " + (clock.getEntryType() == 0 ? "Login " : "Logout ") + ft.format(clock.getEntryTime()));
            sb.append("\n");
        }
        if(entries.size() > 0 && entries.get(entries.size()-1).getEntryType()==0){
            isLoggedIn=true;
        }
        String status = (isLoggedIn ? "Logged In " : "Logged Out");
        sb.append("\n" + "Logged in? " + status + "\n");
       if(isLoggedIn) txt.setTextColor(Color.GREEN);
        else txt.setTextColor(Color.RED);
        txt.setText(sb.toString());
    }
    */
/*
    private long calculateTotalTime(){

        List<ClockWorkUserEntry> entries = db.getAllEntries();

        List<Long> workSeconds = new ArrayList<Long>();
        long returnVal=0l;
        if(entries.size()>1){
            for(int i = 0; i < entries.size();){
                ClockWorkUserEntry login = entries.get(i);
                if(entries.get(i+1)!=null){
                    ClockWorkUserEntry logout = entries.get(i+1);
                    i +=2;
                    workSeconds.add(     (logout.getEntryTime().getTime() - login.getEntryTime().getTime() )/ 1000    );
                }

            }
            for(Long seconds: workSeconds){
                returnVal+=seconds;
            }
        }
        return returnVal;
    }
*/


}
