package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.arun.medicaldoctorapp.ParseObjects.Doctor;
import com.example.arun.medicaldoctorapp.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{
    private String LOG_TAG = "LoginActivity";

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

    @OnClick(R.id.fbLoginButton)
    private void onLoginClick()
    {
        List<String> permissions = Arrays.asList("public_profile", "user_friends", "user_birthday");

        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback()
        {
            @Override
            public void done(ParseUser user, ParseException err)
            {
                //Log.d("ho", "Here");
                if (user == null)
                {
                    Log.d(LOG_TAG, "Uh oh. The user cancelled the Facebook login.");
                }
                else
                {
                    if (user.isNew())
                    {
                        Log.d(LOG_TAG, "User signed up and logged in through Facebook!");
                        fetchDataFromFB();
                    }
                    else
                    {
                        Log.d("ho", "User logged in through Facebook!");
                        fetchDataFromFB();
                    }
                }
            }
        });
    }

    private void fetchDataFromFB()
    {
        final ParseUser currentUser = Doctor.getCurrentUser();
        if (!currentUser.getUsername().equals(""))
        {
            gotoMainActivity();
        }

        if (currentUser.isAuthenticated())
        {
            makeMeRequest();
        }

    }

    private void makeMeRequest()
    {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback()
        {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse)
            {
                if (jsonObject != null)
                {
                    ParseUser currentUser = Doctor.getCurrentUser();
                    try
                    {
                        //userProfile.put("facebookId", jsonObject.getLong("id"));
                        currentUser.put("name", jsonObject.getString("name"));

                        if (jsonObject.getString("gender") != null)
                        {
                            if(jsonObject.getString("gender").equals("male"))
                                currentUser.put("is_male", true);
                            else
                                currentUser.put("is_male", false);
                        }

                        currentUser.saveInBackground();
                    } catch (JSONException e)
                    {
                        Log.d(LOG_TAG, "Error parsing returned user data. " + e);
                    }

                    gotoMainActivity();
                }
                else
                {
                    if (graphResponse.getError() != null)
                    {
                        switch (graphResponse.getError().getCategory())
                        {
                            case LOGIN_RECOVERABLE:
                                Log.d(LOG_TAG, "Authentication error: " + graphResponse.getError());
                                break;

                            case TRANSIENT:
                                Log.d(LOG_TAG, "Transient error. Try again. " + graphResponse.getError());
                                break;

                            case OTHER:
                                Log.d(LOG_TAG, "Some other error: " + graphResponse.getError());
                                break;
                        }
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,birthday");
        request.setParameters(parameters);

        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void gotoMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
