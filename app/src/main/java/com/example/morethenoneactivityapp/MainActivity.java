package com.example.morethenoneactivityapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    private TextView textView;

    private SharedPreferences sharedPreferences;
    private int code = 0;

    private Toast toast;
    public static String MY_PREF = "MORETHENONEACTIVITYAPP_PREFERENCES_FILE";
    public final static String FULLUSERNAME = "fullusername";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvViewFullName);

        String fullUserName;
        if ((fullUserName = loadUserFullNameFromMyPref()) == null){
            getUserFullNameFromLoginActivity();
        } else {
            textView.setText("Hi "+fullUserName);
        }
/*
        Intent intent = getIntent();
        //String login = intent.getStringExtra("login");
        String full_name = intent.getStringExtra("fullname");

        textView.setText("Hello "+full_name);

 */
    }
    private String loadUserFullNameFromMyPref(){

        sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String fullUserName = sharedPreferences.getString(FULLUSERNAME, "");
        if (fullUserName.isEmpty()){
            return null;
        } else {
            return fullUserName;
        }
    }
    // -------------------------------------------------------------------
     private void getUserFullNameFromLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, code);
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == code){
            if(resultCode == RESULT_OK ){
                String fullUserName = data.getStringExtra(FULLUSERNAME);
                saveFullUserNameInMyPref(fullUserName);
                textView.setText("Hi "+fullUserName);

            } else {
                int duration = Toast.LENGTH_SHORT;
                if (toast != null){
                    toast.cancel();

                }
                toast = Toast.makeText(this,"This User not Found!", duration);
                toast.show();
                getUserFullNameFromLoginActivity();
            }
        }
    }

    private void saveFullUserNameInMyPref(String data){
        sharedPreferences = getApplication().getSharedPreferences(MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FULLUSERNAME, data);
        editor.commit();

    }
}