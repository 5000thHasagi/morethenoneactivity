package com.example.morethenoneactivityapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class LoginActivity extends Activity implements View.OnClickListener, GetDataAsyncTask.AsyncResponse {
    // --------------------------------------
    private EditText etLogin;
    private EditText etPassword;
    private Button buttonSub;

    private String login, password, urlLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.eTLogin);
        etPassword = findViewById(R.id.eTPassword);
        buttonSub = findViewById(R.id.buttonSubmit);

        buttonSub.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        login = etLogin.getText().toString();
        password = etPassword.getText().toString();
        urlLink = "http://lofghrps.beget.tech/script_two_activities.php";

        new GetDataAsyncTask(this).execute(login, password, urlLink);
    }

    @Override
    public void proccessFinish(String result) {

        if(!result.equals("User not found")){
            Intent intent = new Intent();
            intent.putExtra(MainActivity.FULLUSERNAME, result);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }

        //Intent intent = new Intent(this, LoginActivity.class);
        //intent.putExtra("fullname", result);
        //intent.putExtra("password", etPassword.getText().toString());
        //startActivity(intent);
    }


//------------------------------------

}
