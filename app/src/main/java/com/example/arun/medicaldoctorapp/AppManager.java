package com.example.arun.medicaldoctorapp;

import android.app.Application;
import android.graphics.Bitmap;

import com.example.arun.medicaldoctorapp.ParseObjects.Doctor;
import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.ParseObjects.Patient;
import com.example.arun.medicaldoctorapp.ParseObjects.PrescribedMedicine;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.facebook.FacebookSdk;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppManager extends Application
{

    public ArrayList<Prescription> currentDoctorPrescriptions;
    public ArrayList<Medicine> medicinesList;
    public Patient selectedPatient;

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
        ParseObject.registerSubclass(PrescribedMedicine.class);
        ParseObject.registerSubclass(Prescription.class);
        Parse.initialize(this, "cvSyybrwMKzHdVwpvUQ2ftclezIYsunMzz6UtUP7", "szykRYtChmUBK1FhyapdUrHI2lGOECH3dIjUCL1c");
    }

    public void fetchAllDataFromParse()
    {
        refreshUserDataAndFetchNewPrescriptionList();
        fetchMedicineList();
    }

    public void fetchMedicineList() {

        ParseQuery<Medicine> query = Medicine.getQuery();

        query.findInBackground(new FindCallback<Medicine>()
        {

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

    public void addPrescription(Prescription p, Patient patient)
    {
        final Doctor doctor = (Doctor) ParseUser.getCurrentUser();
        doctor.addPrescriptionToList(p);

        patient.addPrescriptionToList(p);

        doctor.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                doctor.fetchInBackground();
            }
        });

        patient.saveInBackground();
    }

    public void searchPatientByPhoneNumber(String phoneNo) {
        ParseQuery<ParseUser> query = Patient.getQuery();
        query.whereEqualTo("phone", phoneNo);

        query.findInBackground(new FindCallback<ParseUser>()
        {

            @Override
            public void done(List<ParseUser> list, ParseException e)
            {
                selectedPatient = (Patient) list.get(0);
            }
        });
    }

    public void saveSignature(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] byteArray = stream.toByteArray();
        final ParseFile pf = new ParseFile(byteArray);

        pf.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                final Doctor doctor = (Doctor) ParseUser.getCurrentUser();
                doctor.setSignature(pf);
                doctor.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        doctor.fetchInBackground();
                    }
                });
            }
        });
    }

    public void pushPrescriptionToPatient(Patient patient) {

        ParseQuery pQuery = ParseInstallation.getQuery();
        pQuery.whereEqualTo("username", patient.getUsername());

        try
        {
            JSONObject data = new JSONObject("{\"alert\":\"You have a new prescription!\"," +
                    "\"uri\": \"com.example.arun.medicaldoctorapp.PrescriptionActivity\"}"); // Add uri latter

            ParsePush parsePush = new ParsePush();
            parsePush.setQuery(pQuery);
            parsePush.setMessage("You have a new prescription");
            parsePush.setData(data);
            parsePush.sendInBackground();

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }
}
