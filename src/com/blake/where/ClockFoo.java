package com.blake.where;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by blake on 7/30/14.
 */
public class ClockFoo {


    static String TARGET_EMAIL="saltyfog@gmail.com";
    static String TARGET_PHONE="8325881755";



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

    private void sendSMS(Context c, ClockWorkUserEntry clock){
        SimpleDateFormat ft =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String message = "ClockWork:" + clock.getUsername() + " " + (clock.getEntryType() == 0 ? "Login " : "Logout ") + ft.format(clock.getEntryTime());
        String phoneNo=TARGET_PHONE;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(c, "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(c,
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    private void sendEntryEmail(ClockWorkUserEntry clock, Activity src){
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{TARGET_EMAIL});
        SimpleDateFormat ft =new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        String text = "Clock Entry:" + clock.getUsername() + " " + (clock.getEntryType() == 0 ? "Login " : "Logout ") + ft.format(clock.getEntryTime());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ClockWork Entry: " + clock.getUsername() + " - " + (clock.getEntryType() == 0 ? "Login " : "Logout "));
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        src.startActivity(Intent.createChooser(emailIntent, "Mailing ClockWork Entry..."));
    }



}
