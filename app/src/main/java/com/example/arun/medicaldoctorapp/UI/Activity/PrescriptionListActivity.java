package com.example.arun.medicaldoctorapp.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.example.arun.medicaldoctorapp.R;
import com.example.arun.medicaldoctorapp.UI.Adapter.MedicineAdapter;
import com.example.arun.medicaldoctorapp.UI.Adapter.PrescriptionAdapter;

import butterknife.Bind;
import butterknife.OnClick;

public class PrescriptionListActivity extends BaseActivity
{

    @Bind(R.id.prescription_recycler) RecyclerView mRecyclerView;
    private PrescriptionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_prescription_list;
    }

    @Override
    protected void setupToolbar()
    {
        toolbar.setTitle("My Prescriptions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setupLayout()
    {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new PrescriptionAdapter(PrescriptionListActivity.this, null);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick (R.id.fab_addPrescription)
    void AddPrescription()
    {
        mAdapter.add(new Prescription());
    }
}
