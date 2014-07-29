package com.blake.where;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class DisplayMessageActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get the message from the intent
        Intent intent = getIntent();
        String message1 = intent.getStringExtra("lat");
        String message2 = intent.getStringExtra("long");
      //  String message3 = intent.getStringExtra("loc");
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("lat: " + message1 + ", long: " + message2 );

        // Set the text view as the activity layout
        setContentView(textView);

    }
}