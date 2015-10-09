package com.example.arun.medicaldoctorapp.UI.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.ParseObjects.PrescribedMedicine;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.example.arun.medicaldoctorapp.ParseObjects.User;
import com.example.arun.medicaldoctorapp.R;
import com.example.arun.medicaldoctorapp.UI.Adapter.MedicineAdapter;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.OnClick;

public class NewPrescriptionActivity extends BaseActivity
{
    @Bind(R.id.medicine_recycler) RecyclerView mRecyclerView;
    @Bind(R.id.editText_patient_phone_number) EditText etPhoneNum;

    private MedicineAdapter mAdapter;
    private Prescription prescription;
    private User patient;
    private ArrayList<String> medicineNames;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prescription = new Prescription();
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
        medicineNames = new ArrayList<>();
        for (int i = 0; i < manager.medicinesList.size(); i++)
        {
            medicineNames.add(manager.medicinesList.get(i).getMedicineName());
        }
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

        final AutoCompleteTextView etMedicineName = (AutoCompleteTextView) customDialogView.findViewById(R.id.editText_medicine_name);
        final EditText etMedicineDuration = (EditText) customDialogView.findViewById(R.id.editText_medicine_duration);
        final EditText etMedicineNotes = (EditText) customDialogView.findViewById(R.id.editText_medicine_notes);
        final TextView tvQuantity = (TextView) customDialogView.findViewById(R.id.textview_quantity);
        final TextView tvLabel = (TextView) customDialogView.findViewById(R.id.textview_MG_or_ML);
        final CheckBox checkMorning = (CheckBox) customDialogView.findViewById(R.id.checkbox_morning);
        final CheckBox checkAfternoon = (CheckBox) customDialogView.findViewById(R.id.checkbox_afternoon);
        final CheckBox checkEvening = (CheckBox) customDialogView.findViewById(R.id.checkbox_evening);
        final RadioGroup radioMedicineType = (RadioGroup) customDialogView.findViewById(R.id.radiogroup_medicine_type);
        final SeekBar seekbarQuantity = (SeekBar) customDialogView.findViewById(R.id.seekbar_quantity);


        ArrayAdapter<String> localityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, medicineNames);

        etMedicineName.setAdapter(localityAdapter);
        etMedicineName.setValidator(new NamesValidator());
        etMedicineName.setOnFocusChangeListener(new FocusListener());

        seekbarQuantity.incrementProgressBy(50);
        seekbarQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser)
            {
                progress = progressValue;
                progress = progress / 50;
                progress = progress * 50;

                tvQuantity.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        radioMedicineType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                View radioButton = radioMedicineType.findViewById(checkedId);
                int checked = radioMedicineType.indexOfChild(radioButton);
                if (checked == 0)
                {
                    tvLabel.setText("MG");
                }
                else
                {
                    tvLabel.setText("ML");
                }
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                PrescribedMedicine prescribedMedicine = new PrescribedMedicine();
                //prescribedMedicine.set

                for (int i = 0; i < manager.medicinesList.size(); i++)
                {
                    if(manager.medicinesList.get(i).getMedicineName().contains(etMedicineName.getText().toString().trim())) {
                        prescribedMedicine.setMedicine(manager.medicinesList.get(i));
                        break;
                    }
                }

                prescribedMedicine.setDuration(etMedicineDuration.getText().toString());
                prescribedMedicine.setNotes(etMedicineNotes.getText().toString());

                ArrayList<Integer> timesADay = new ArrayList<Integer>();
                timesADay.add(boolToInt(checkMorning.isChecked()));
                timesADay.add(boolToInt(checkAfternoon.isChecked()));
                timesADay.add(boolToInt(checkEvening.isChecked()));

                prescribedMedicine.setType(tvLabel.getText().toString().contains("G"));

                prescribedMedicine.setTimesADay(timesADay);
                prescribedMedicine.setQuantity(tvQuantity.getText().toString() + tvLabel.getText().toString());
                //ArrayList<PrescribedMedicine> prescribedMedicineArrayList = new ArrayList<PrescribedMedicine>();
                //prescribedMedicineArrayList.add(prescribedMedicine);
                //prescription.putMedicineList(prescribedMedicineArrayList);

                //manager.addPrescription(prescription, manager.selectedPatient);

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

    public int boolToInt(boolean b)
    {
        return b ? 1 : 0;
    }

    class NamesValidator implements AutoCompleteTextView.Validator
    {

        @Override
        public boolean isValid(CharSequence text)
        {
            Collections.sort(medicineNames);
            if (Collections.binarySearch(medicineNames, text.toString()) > 0)
            {
                return true;
            }
            return false;
        }

        @Override
        public CharSequence fixText(CharSequence invalidText)
        {
            return "";
        }
    }

    class FocusListener implements View.OnFocusChangeListener
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            if (v.getId() == R.id.editText_medicine_name && hasFocus)
            {
                Log.v("Test", "Performing validation");
                ((AutoCompleteTextView) v).performValidation();
            }
        }
    }
}
