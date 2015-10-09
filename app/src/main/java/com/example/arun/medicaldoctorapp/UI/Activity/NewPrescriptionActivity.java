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
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.arun.medicaldoctorapp.ParseObjects.PrescribedMedicine;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.example.arun.medicaldoctorapp.ParseObjects.User;
import com.example.arun.medicaldoctorapp.R;
import com.example.arun.medicaldoctorapp.UI.Adapter.MedicineAdapter;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    private ArrayList<PrescribedMedicine> prescribedMedicines;
    private ArrayList<String> medicineNames;
    private ArrayList<String> durationList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        prescription = new Prescription();
        medicineNames = new ArrayList<>();
        durationList = new ArrayList<>();
        prescribedMedicines = new ArrayList<>();
        setDurationList();
        super.onCreate(savedInstanceState);
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
        toolbar.setTitle("New Prescription");
    }

    @Override
    protected void setupLayout()
    {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
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
        final AutoCompleteTextView etMedicineDuration = (AutoCompleteTextView) customDialogView.findViewById(R.id.editText_medicine_duration);
        final EditText etMedicineNotes = (EditText) customDialogView.findViewById(R.id.editText_medicine_notes);
        final TextView tvQuantity = (TextView) customDialogView.findViewById(R.id.textview_quantity);
        final TextView tvLabel = (TextView) customDialogView.findViewById(R.id.textview_MG_or_ML);
        final CheckBox checkMorning = (CheckBox) customDialogView.findViewById(R.id.checkbox_morning);
        final CheckBox checkAfternoon = (CheckBox) customDialogView.findViewById(R.id.checkbox_afternoon);
        final CheckBox checkEvening = (CheckBox) customDialogView.findViewById(R.id.checkbox_evening);
        final RadioGroup radioMedicineType = (RadioGroup) customDialogView.findViewById(R.id.radiogroup_medicine_type);
        final SeekBar seekbarQuantity = (SeekBar) customDialogView.findViewById(R.id.seekbar_quantity);


        ArrayAdapter<String> medicineNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, medicineNames);
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, durationList);


        etMedicineName.setAdapter(medicineNameAdapter);
        etMedicineName.setValidator(new NamesValidator());
        etMedicineName.setOnFocusChangeListener(new FocusListener());

        etMedicineDuration.setAdapter(durationAdapter);
        etMedicineDuration.setValidator(new DurationValidator());
        etMedicineDuration.setOnFocusChangeListener(new FocusListener());

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
                mAdapter.add(prescribedMedicine);
                prescribedMedicines.add(prescribedMedicine);
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

    class DurationValidator implements AutoCompleteTextView.Validator
    {

        @Override
        public boolean isValid(CharSequence text)
        {
            Collections.sort(durationList);
            if (Collections.binarySearch(durationList, text.toString()) > 0)
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            prescription.putMedicineList(prescribedMedicines);
            prescription.setDoctorID(ParseUser.getCurrentUser());
            prescription.setPatientID(manager.selectedPatient);
            prescription.saveInBackground(new SaveCallback()
            {
                @Override
                public void done(ParseException e)
                {
                    manager.addPrescription(prescription);
                }
            });

            /*Intent i = new Intent(BaseActivity.this, AboutActivity.class);
            startActivity(i);
            return true;*/
        }
        else if (id == android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
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
