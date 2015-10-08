package com.example.arun.medicaldoctorapp.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.arun.medicaldoctorapp.R;

public class MainActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        ParsePush parsePush = new ParsePush();
//        ParseQuery pQuery = ParseInstallation.getQuery();
//        pQuery.whereEqualTo("username", "123");
//        parsePush.sendMessageInBackground("Yo bro", pQuery);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void setupToolbar()
    {

    }

    @Override
    protected void setupLayout()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
