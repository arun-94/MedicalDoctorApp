package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arun.medicaldoctorapp.R;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
{
    @Bind(R.id.home_doctor_name) TextView doctorName;

    @OnClick(R.id.home_fab_new_prescription)
    void addPrescription()
    {
        gotoPrescriptionActivity();
    }

    @OnClick(R.id.home_fab_view_prescriptions)
    void viewPrescriptions() {gotoViewPrescriptionActivity(); }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
        doctorName.setText(ParseUser.getCurrentUser().getString("name"));
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

    private void gotoPrescriptionActivity()
    {
        Intent intent = new Intent(MainActivity.this, NewPrescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void gotoViewPrescriptionActivity()
    {
        Intent intent = new Intent(MainActivity.this, PrescriptionListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.home_fab_settings)
    void click1()
    {
        Toast.makeText(MainActivity.this, "No settings to show", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.home_fab_patients)
    void click2()
    {
        Toast.makeText(MainActivity.this, "Viewing patient currently unavailable", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.home_fab_signature)
    void click3()
    {
        Toast.makeText(MainActivity.this, "Adding signature coming soon", Toast.LENGTH_SHORT).show();
    }
}
