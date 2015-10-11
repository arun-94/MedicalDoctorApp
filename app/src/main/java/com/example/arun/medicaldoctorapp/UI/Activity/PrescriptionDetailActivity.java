package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arun.medicaldoctorapp.Constants;
import com.example.arun.medicaldoctorapp.ImageUtility;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.example.arun.medicaldoctorapp.ParseObjects.User;
import com.example.arun.medicaldoctorapp.R;
import com.example.arun.medicaldoctorapp.UI.Adapter.PrescribedMedicineAdapter;
import com.example.arun.medicaldoctorapp.UI.Custom.RecyclerItemClickListener;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Puneet on 10-10-2015.
 */
public class PrescriptionDetailActivity extends BaseActivity
{
    @Bind(R.id.prescription_recycler) RecyclerView mRecyclerView;
    PrescribedMedicineAdapter mAdapter;
    @Bind(R.id.prescription_age_gender) TextView ageGender;
    @Bind(R.id.prescription_name) TextView name;
    @Bind(R.id.prescription_date) TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manager.delegate = this;
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_prescription_details;
    }

    @Override
    protected void setupToolbar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setupLayout()
    {
        String s = "M";
        if(!ParseUser.getCurrentUser().getBoolean("is_male") == true)
            s = "F";
        ageGender.setText(ParseUser.getCurrentUser().getInt("age") + "/" + s);
        name.setText(ParseUser.getCurrentUser().getString("name"));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new PrescribedMedicineAdapter(PrescriptionDetailActivity.this, manager.selectedPrescription.getMedicineList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PrescriptionDetailActivity.this, new RecyclerItemClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick(View view, int itempos)
            {
                Intent productListIntent = new Intent(PrescriptionDetailActivity.this, PrescriptionDetailActivity.class);
                startActivity(productListIntent);
            }
        }));
    }

    @OnClick(R.id.button_call_doctor)
    void callDoctor()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+91 " + manager.selectedPrescription.getPatientID().get("phone")));
        startActivity(callIntent);
    }

    @OnClick(R.id.button_set_reminder)
    void setReminder()
    {
        final Prescription newPresc = new Prescription();
        newPresc.setDoctorID(manager.selectedPrescription.getDoctorID());
        newPresc.setPatientID(manager.selectedPrescription.getPatientID());
        newPresc.putMedicineList(manager.selectedPrescription.getMedicineList());
        newPresc.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                Toast.makeText(PrescriptionDetailActivity.this, "Prescription sent!", Toast.LENGTH_SHORT).show();
                manager.addPrescription(newPresc);
            }
        });
    }

    @OnClick(R.id.button_share_prescription)
    void sharePresc()
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "Order for " + manager.selectedPrescription.getPatientID().get("name"));
        String content = "";
        for (int index = 0; index < manager.selectedPrescription.getMedicineList().size(); index++)
        {
            content = content.concat(manager.selectedPrescription.getMedicineList().get(index).getMedicine().getMedicineName() + "\n");
        }
        content = content.concat("\n\n\n - Sent via ePrescription Android app");
        i.putExtra(Intent.EXTRA_TEXT, content);
        try
        {
            startActivity(Intent.createChooser(i, "Send to Chemist"));
        }
        catch (ActivityNotFoundException ex)
        {
            Toast.makeText(PrescriptionDetailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processFinish(String result, int type)
    {

        if (type == Constants.TYPE_PRESCRIPTION_ADDED)
        {
            manager.pushPrescriptionToPatient((User) manager.selectedPrescription.getPatientID());
        }
    }
}

