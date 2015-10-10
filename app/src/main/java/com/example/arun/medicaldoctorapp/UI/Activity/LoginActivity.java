package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

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
    ConnectivityManager cm;
    NetworkInfo ni;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
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


        if ((ni != null) && (ni.isConnected()))
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
                        Toast.makeText(LoginActivity.this, "No Internet. Login Failed.", Toast.LENGTH_SHORT).show();
                        gotoMainActivityOffline();
                        // Signup failed. Look at the ParseException to see what happened.
                    }
                }
            });
        }
        else {
            Toast.makeText(LoginActivity.this, "No Internet. Offline Mode.", Toast.LENGTH_SHORT).show();
            gotoMainActivityOffline();
        }
    }

    private void gotoMainActivityOffline()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivityOffline.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void gotoMainActivity()
    {
        // manager.fetchDataFromParse();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
