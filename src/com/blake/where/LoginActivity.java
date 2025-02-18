package com.blake.where;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.blake.where.login.ClockworkUserBean;
import com.blake.where.login.LoginTask;
import com.blake.where.login.LoginUtil;

public class LoginActivity extends Activity implements OnTaskCompleted {

    private EditText phoneField,passwordField;
    private TextView message;

    /*
    called when activity is first created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneField = (EditText)findViewById(R.id.editText1);
        passwordField = (EditText)findViewById(R.id.editText2);
        message = (TextView)findViewById(R.id.textView3);
        phoneField.setFocusable(true);
        //next calls onStart()
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void quit(View v){
        finish();
    }

    public void loginPost(View view){
        String phone = phoneField.getText().toString();
        String password = passwordField.getText().toString();
        boolean fail = false;
        StringBuffer sb = new StringBuffer();
        if(phone.length()<10 ){
            sb.append("Phone 10 digits!");
            message.setTextColor(Color.RED);
            fail=true;
        }
        if(password.length()<6){
            sb.append(" PassWord 6 digits!");
            message.setTextColor(Color.RED);
            fail = true;
        }
        if(fail){
            message.setText(sb.toString());
            phoneField.setText("");
            passwordField.setText("");
            return;
        }else{
            new LoginTask(this,this).execute(phone, password);
        }
    }

    @Override
    public void onTaskCompleted(String value) {

        if(value.length()==0){
            message.setText("No worker found.");
            message.setTextColor(Color.RED);
            phoneField.setText("");
            phoneField.setFocusable(true);
            passwordField.setText("");
            return;
        }
        Toast.makeText(this, "web service data item: " + value, Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, ClockWorkMainActivity.class);
        ClockworkUserBean bean = LoginUtil.getUserBeanFromResultsJSON(value);
        i.putExtra("worker_id", String.valueOf(bean.getWorkerId()));
        i.putExtra("tsheets_id", String.valueOf(bean.getTsheetsId()));
        i.putExtra("fname", String.valueOf(bean.getFname()));
        this.startActivity(i);
    }

}
