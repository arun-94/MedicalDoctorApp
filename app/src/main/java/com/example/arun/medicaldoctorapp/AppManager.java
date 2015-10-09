package com.example.arun.medicaldoctorapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.arun.medicaldoctorapp.ParseObjects.User;
import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
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

    public ArrayList<Prescription> currentDoctorPrescriptions = new ArrayList<>();
    public ArrayList<Medicine> medicinesList = new ArrayList<>();
    public User selectedPatient;

    @Override
    public void onCreate()
    {
        super.onCreate();
        parseInit();
        fetchAllDataFromParse();
    }

    private void parseInit()
    {
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Medicine.class);
        ParseObject.registerSubclass(PrescribedMedicine.class);
        ParseObject.registerSubclass(Prescription.class);
        Parse.initialize(this, "cvSyybrwMKzHdVwpvUQ2ftclezIYsunMzz6UtUP7", "szykRYtChmUBK1FhyapdUrHI2lGOECH3dIjUCL1c");
    }

    public void fetchAllDataFromParse()
    {
        //refreshUserDataAndFetchNewPrescriptionList();
        fetchMedicineList();
    }

    public void fetchMedicineList()
    {

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

    private void refreshUserDataAndFetchNewPrescriptionList()
    {
        final ParseUser currentUser = User.getCurrentUser();
        currentUser.fetchInBackground(new GetCallback<User>()
        {
            @Override
            public void done(final User parseObject, ParseException e)
            {
                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.include("prescription_list");

                query.getInBackground(currentUser.getObjectId(), new GetCallback<ParseUser>()
                {

                    @Override
                    public void done(ParseUser parseUser, ParseException e)
                    {

                        User doctor = (User) parseUser;
                        currentDoctorPrescriptions.addAll(doctor.getPrescriptionList());
                    }
                });
            }
        });
    }

    public void addPrescription(final Prescription p)
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>()
        {
            @Override
            public void done(ParseUser parseUser, ParseException e)
            {
                parseUser.addUnique("prescription_list", p);
                parseUser.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if (e == null)
                        {

                        }
                        else
                        {
                            Log.e("AppManager Doctor", e.getMessage());
                        }
                    }
                });
            }
        });
        //patient.saveInBackground();
    }

    public void searchPatientByPhoneNumber(String phoneNo)
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("phone", phoneNo);

        query.findInBackground(new FindCallback<ParseUser>()
        {

            @Override
            public void done(List<ParseUser> list, ParseException e)
            {
                Log.d("PATIENT", "Size of list returned is " + list.size());
                if(list.size() == 0)
                    Log.d("PATIENT", "No patient with that number");
                else
                {
                    selectedPatient = (User) list.get(0);
                    Log.d("PATIENT", "Selected Patient class " + selectedPatient.getClassName());
                    Log.d("PATIENT", "The patient from list is " + list.get(0).getUsername() + "Type is " + list.get(0).getClassName());
                    Log.d("PATIENT", "The patient is " + list.get(0).getUsername() + "Type is " + list.get(0).getClassName());
                }
            }
        });
    }

    public void saveSignature(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] byteArray = stream.toByteArray();
        final ParseFile pf = new ParseFile(byteArray);

        pf.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                final User doctor = (User) ParseUser.getCurrentUser();
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

    public void pushPrescriptionToPatient(User patient)
    {

        ParseQuery pQuery = ParseInstallation.getQuery();
        pQuery.whereEqualTo("username", patient.getUsername());

        try
        {
            JSONObject data = new JSONObject("{\"alert\":\"You have a new prescription!\"," + "\"uri\": \"com.example.arun.medicaldoctorapp.PrescriptionActivity\"}"); // Add uri latter

            ParsePush parsePush = new ParsePush();
            parsePush.setQuery(pQuery);
            parsePush.setMessage("You have a new prescription");
            parsePush.setData(data);
            parsePush.sendInBackground();

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


    }
}
