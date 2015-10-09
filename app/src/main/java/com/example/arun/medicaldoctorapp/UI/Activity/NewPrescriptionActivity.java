package com.example.arun.medicaldoctorapp.UI.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.R;
import com.example.arun.medicaldoctorapp.UI.Adapter.MedicineAdapter;

import butterknife.Bind;
import butterknife.OnClick;

public class NewPrescriptionActivity extends BaseActivity
{
    @Bind(R.id.medicine_recycler) RecyclerView mRecyclerView;
    @Bind(R.id.editText_patient_phone_number) EditText etPhoneNum;

    private MedicineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_new_prescription;
    }

    @Override
    protected void setupToolbar()
    {
        toolbar.setTitle("New Prescription");
    }

    @Override
    protected void setupLayout()
    {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new MedicineAdapter(NewPrescriptionActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        etPhoneNum.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() >= 8)
                {
                    if (isValidPhoneNumber(s))
                    {
                        manager.searchPatientByPhoneNumber(s.toString());
                    }
                }
            }
        });
    }

    @OnClick(R.id.fab_addMedicine)
    void showAddMedicineDialog()
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Prescribe New Medicine");
        View customDialogView = inflater.inflate(R.layout.popup_add_medicine, null, false);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //@Bind EditText
                mAdapter.add(new Medicine());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
            }
        });
        builder.setView(customDialogView);
        builder.create();
        builder.show();
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber)
    {
        if (!TextUtils.isEmpty(phoneNumber))
        {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }
}
