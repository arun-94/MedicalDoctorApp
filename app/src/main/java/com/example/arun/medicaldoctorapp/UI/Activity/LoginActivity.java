package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.arun.medicaldoctorapp.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{
    @Bind(R.id.progress_view) ProgressBar progressLoading;
    @Bind(R.id.loginButton) Button loginButton;
    private String LOG_TAG = "LoginActivity";
    ConnectivityManager cm;
    NetworkInfo ni;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        manager.delegate = this;
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
        progressLoading.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.loginButton)
    void onLoginClick()
    {
        progressLoading.setVisibility(View.VISIBLE);

        loginButton.setText("");

        if ((ni != null) && (ni.isConnected()))
        {
            ParseUser.logInInBackground("TestDoctor", "test123", new LogInCallback()
            {
                public void done(ParseUser user, ParseException e)
                {
                    progressLoading.setVisibility(View.INVISIBLE);
                    loginButton.setText("login with Test Credentials");
                    if (user != null)
                    {
                        gotoMainActivity();
                    }
                    else
                    {
                        Log.e("Login Failed", e.getMessage());
                        Toast.makeText(LoginActivity.this, "No Internet. Login Failed. Switching to Offline access.", Toast.LENGTH_SHORT).show();
                        gotoMainActivityOffline();
                        // Signup failed. Look at the ParseException to see what happened.
                    }
                }
            });
        }
        else
        {
            Toast.makeText(LoginActivity.this, "No Internet. Switching to Offline Mode.", Toast.LENGTH_SHORT).show();
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
