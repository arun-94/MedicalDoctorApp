package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.arun.medicaldoctorapp.AppManager;
import com.example.arun.medicaldoctorapp.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{
    private String LOG_TAG = "LoginActivity";
    AppManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ParseFacebookUtils.initialize(this);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_login;
    }

    @Override
    protected void setupToolbar()
    {

    }

    @Override
    protected void setupLayout()
    {

    }

    @OnClick(R.id.loginButton)
    void onLoginClick()
    {
        ParseUser.logInInBackground("TestDoctor", "test123", new LogInCallback()
        {
            public void done(ParseUser user, ParseException e)
            {
                if (user != null)
                {
                    gotoMainActivity();
                }
                else
                {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    private void gotoMainActivity()
    {
        // manager.fetchDataFromParse();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
