package com.example.arun.medicaldoctorapp;

import android.app.Application;

import com.example.arun.medicaldoctorapp.ParseObjects.Doctor;
import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.facebook.FacebookSdk;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AppManager extends Application
{

    public ArrayList<Prescription> currentDoctorPrescriptions;
    public ArrayList<Medicine> medicinesList;

    @Override
    public void onCreate()
    {
        super.onCreate();
        parseInit();
    }

    private void parseInit() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseObject.registerSubclass(Doctor.class);
        ParseObject.registerSubclass(Medicine.class);
        //ParseObject.registerSubclass(Patient.class);
        ParseObject.registerSubclass(Prescription.class);
        Parse.initialize(this, "cvSyybrwMKzHdVwpvUQ2ftclezIYsunMzz6UtUP7", "szykRYtChmUBK1FhyapdUrHI2lGOECH3dIjUCL1c");
    }

    public void fetchAllDataFromParse()
    {
        refreshUserDataAndFetchNewPrescriptionList();
        fetchMedicineList();
    }

    public void fetchMedicineList() {

        ParseQuery query = Medicine.getQuery();

        query.findInBackground(new FindCallback<Medicine>() {

            @Override
            public void done(List<Medicine> list, ParseException e)
            {
                medicinesList.addAll(list);
            }
        });
    }

    private void refreshUserDataAndFetchNewPrescriptionList() {
        final ParseUser currentUser = Doctor.getCurrentUser();
        currentUser.fetchInBackground(new GetCallback<Doctor>()
        {
            @Override
            public void done(final Doctor parseObject, ParseException e)
            {
                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.include("prescription_list");

                query.getInBackground(currentUser.getObjectId(), new GetCallback<ParseUser>()
                {

                    @Override
                    public void done(ParseUser parseUser, ParseException e)
                    {

                        Doctor doctor = (Doctor) parseUser;
                        currentDoctorPrescriptions.addAll(doctor.getPrescriptionList());
                    }
                });
            }
        });
    }
}
