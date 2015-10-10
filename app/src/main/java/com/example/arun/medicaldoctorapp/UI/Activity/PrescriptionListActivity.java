package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.arun.medicaldoctorapp.Constants;
import com.example.arun.medicaldoctorapp.R;
import com.example.arun.medicaldoctorapp.UI.Adapter.PrescriptionAdapter;

import butterknife.Bind;
import butterknife.OnClick;

public class PrescriptionListActivity extends BaseActivity
{

    @Bind(R.id.prescription_recycler) RecyclerView mRecyclerView;
    @Bind(R.id.progress_view) ProgressBar progressLoading;

    private PrescriptionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        manager.delegate = this;
        manager.getAllPrescriptionsFromCurrentPatient();
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_prescription_list;
    }

    @Override
    protected void setupToolbar()
    {
        getSupportActionBar().setTitle("My Prescriptions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setupLayout()
    {
        progressLoading.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new PrescriptionAdapter(PrescriptionListActivity.this, null);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void processFinish(String result, int type)
    {
        Log.d("Process", "Process finished");
        if (type == Constants.TYPE_RECIEVED_PRESCRIPTIONS)
        {
            progressLoading.setVisibility(View.INVISIBLE);
            Log.d("Process", "Adding items");
            mAdapter.addItems(manager.currentDoctorPrescriptions);
        }
    }

    @OnClick (R.id.fab_addPrescription)
    void AddPrescription()
    {
        gotoPrescriptionActivity();
    }

    private void gotoPrescriptionActivity()
    {
        Intent intent = new Intent(PrescriptionListActivity.this, NewPrescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
