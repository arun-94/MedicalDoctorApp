package com.example.arun.medicaldoctorapp.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arun.medicaldoctorapp.Constants;
import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.ParseObjects.PrescribedMedicine;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.example.arun.medicaldoctorapp.ParseObjects.User;
import com.example.arun.medicaldoctorapp.R;
import com.example.arun.medicaldoctorapp.UI.Adapter.MedicineAdapter;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class NewPrescriptionActivityOffline extends BaseActivity
{

    @Bind(R.id.medicine_recycler) RecyclerView mRecyclerView;
    @Bind(R.id.editText_patient_phone_number) EditText etPhoneNum;
    @Bind(R.id.progress_validate_patient) ProgressBar progressValidation;
    @Bind(R.id.imageview_validation) ImageView imageValidation;
    @Bind(R.id.patient_profile_layout) View patientProfileLayout;
    @Bind(R.id.phonenumber_layout) View phoneNumberLayout;
    @Bind(R.id.textView_number_label) TextView tvPhoneLabel;


    @Bind(R.id.patient_name) TextView patientName;
    @Bind(R.id.patient_phone) TextView patientPhone;
    @Bind(R.id.person_amount) TextView personAmount;

    private MedicineAdapter mAdapter;
    private Prescription prescription;
    private User patient;
    private ArrayList<PrescribedMedicine> prescribedMedicines;
    private ArrayList<String> medicineNames;
    private ArrayList<String> durationList;


    @NotEmpty AutoCompleteTextView etMedicineName;
    @NotEmpty  AutoCompleteTextView etMedicineDuration;
    EditText etMedicineNotes;
    TextView tvQuantity;
    TextView tvLabel;
    CheckBox checkMorning;
    CheckBox checkAfternoon;
    CheckBox checkEvening;
    RadioGroup radioMedicineType;
    SeekBar seekbarQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        prescription = new Prescription();
        medicineNames = new ArrayList<>();
        durationList = new ArrayList<>();
        prescribedMedicines = new ArrayList<>();
        setDurationList();
        super.onCreate(savedInstanceState);
        manager.delegate = this;


    }

    private void setDurationList()
    {
        durationList.add("1 Day");
        durationList.add("1 Week");
        durationList.add("2 Weeks");
        durationList.add("3 Weeks");
        durationList.add("4 Weeks");
        durationList.add("1 Month");
        durationList.add("3 Months");
        durationList.add("2 Days");
        durationList.add("3 Days");
        durationList.add("4 Days");
        durationList.add("5 Days");
        durationList.add("6 Days");

    }

    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_new_prescription;
    }

    @Override
    protected void setupToolbar()
    {
        getSupportActionBar().setTitle("New Prescription (Offline)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber)
    {
        if (!TextUtils.isEmpty(phoneNumber))
        {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    @Override
    protected void setupLayout()
    {

        progressValidation.setVisibility(View.GONE);
        imageValidation.setVisibility(View.GONE);
        patientProfileLayout.setVisibility(View.GONE);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new MedicineAdapter(NewPrescriptionActivityOffline.this);
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
                if (s.length() >= 10)
                {
                    if (isValidPhoneNumber(s))
                    {
                        progressValidation.setVisibility(View.VISIBLE);
                        manager.searchPatientByPhoneNumberOffline(s.toString());
                    }
                }
            }
        });
    }

    @OnClick(R.id.fab_addMedicine)
    void showAddMedicineDialog()
    {
        ArrayList<Integer> timesADay = new ArrayList<>(3);
        timesADay.add(1);
        timesADay.add(0);
        timesADay.add(1);

        PrescribedMedicine medicine = new PrescribedMedicine();

        Medicine medicine1 = new Medicine();
        medicine1.put("medicine_name", "XYZ Medicine");

        medicine.setMedicine(medicine1);
        medicine.setDuration("x days");
        medicine.setNotes("Take daily");
        medicine.setQuantity("100ml");
        medicine.setTimesADay(timesADay);
        mAdapter.add(medicine);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prescription_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        if (id == R.id.action_save)
        {
            Toast.makeText(NewPrescriptionActivityOffline.this, "Offline Mode. Can't save.", Toast.LENGTH_LONG).show();
        }
        else if (id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void processFinish(String result, int type)
    {
        if (type == Constants.TYPE_VALID_NUMBER)
        {
            progressValidation.setVisibility(View.GONE);
            imageValidation.setVisibility(View.VISIBLE);
            patientProfileLayout.setVisibility(View.VISIBLE);
            phoneNumberLayout.setVisibility(View.GONE);
            tvPhoneLabel.setVisibility(View.GONE);
            //Set here tick image

            patientName.setText(manager.selectedPatient.getName());
            patientPhone.setText(manager.selectedPatient.getPhone());

            String patientGender;
            if(manager.selectedPatient.isMale())
                patientGender = "M";
            else
                patientGender = "F";
            personAmount.setText(manager.selectedPatient.getAge() + "/" + patientGender);
        }
        else if (type == Constants.TYPE_INVALID_NUMBER)
        {
            progressValidation.setVisibility(View.GONE);
            imageValidation.setVisibility(View.VISIBLE);
            //Set here X image
        }
        else if(type == Constants.TYPE_PRESCRIPTION_ADDED) {
            progressValidation.setVisibility(View.GONE);
            manager.pushPrescriptionToPatient(manager.selectedPatient);
            gotoMainActivity();
        }
    }

    private void gotoMainActivity()
    {
        // manager.fetchDataFromParse();

        Intent intent = new Intent(NewPrescriptionActivityOffline.this, MainActivityOffline.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
