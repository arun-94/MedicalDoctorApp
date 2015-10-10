package com.example.arun.medicaldoctorapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    public AsyncResponse delegate = null;
    ConnectivityManager cm;
    NetworkInfo ni;

    @Override
    public void onCreate()
    {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
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
        ParseUser.enableRevocableSessionInBackground();
    }

    public void fetchAllDataFromParse()
    {
        //refreshUserDataAndFetchNewPrescriptionList();
        fetchMedicineList();
    }

    public void fetchMedicineList()
    {
        if ((ni != null) && (ni.isConnected()))
        {
            ParseQuery<Medicine> query = Medicine.getQuery();
            query.setLimit(1000);
            query.findInBackground(new FindCallback<Medicine>()
            {

                @Override
                public void done(List<Medicine> list, ParseException e)
                {
                    medicinesList.addAll(list);
                }
            });
        }
    }

    public void getAllPrescriptionsFromCurrentPatient()
    {

        if ((ni != null) && (ni.isConnected()))
        {
            ParseQuery<Prescription> query = Prescription.getQuery();

            query.whereEqualTo("doctor_id", ParseUser.getCurrentUser());
            query.include("doctor_id");
            query.include("patient_id");
            query.include("medicine_ids");
            query.include("medicine_ids.medicine");
            query.findInBackground(new FindCallback<Prescription>()
            {
                @Override
                public void done(List<Prescription> list, ParseException e)
                {
                    if (e == null)
                    {
                        Log.d("Manager", "Size of list ; " + list.size());
                        currentDoctorPrescriptions.clear();
                        currentDoctorPrescriptions.addAll(list);
                        delegate.processFinish("manager", Constants.TYPE_RECIEVED_PRESCRIPTIONS);
                    }
                    else
                    {
                        Log.d("AppManager", e.getMessage());
                    }
                }
            });

        }

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
                            delegate.processFinish("added prescription", Constants.TYPE_PRESCRIPTION_ADDED);
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
                if (list.size() == 0)
                {
                    Log.d("PATIENT", "No patient with that number");
                    delegate.processFinish("error", Constants.TYPE_INVALID_NUMBER);
                }
                else
                {
                    selectedPatient = (User) list.get(0);
                    Log.d("PATIENT", "Selected Patient class " + selectedPatient.getClassName());
                    Log.d("PATIENT", "The patient from list is " + list.get(0).getUsername() + "Type is " + list.get(0).getClassName());
                    Log.d("PATIENT", "The patient is " + list.get(0).getUsername() + "Type is " + list.get(0).getClassName());
                    delegate.processFinish("manager", Constants.TYPE_VALID_NUMBER);
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
           // parsePush.setData(data);
            parsePush.sendInBackground();

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void searchPatientByPhoneNumberOffline(String s)
    {

        selectedPatient = new User();
        selectedPatient.put("is_male", true);
        selectedPatient.put("name", "Offline Patient");
        selectedPatient.put("phone", "9876543210");
        selectedPatient.put("is_doctor", false);
        selectedPatient.put("age", "xx");
        delegate.processFinish("error", Constants.TYPE_VALID_NUMBER);
    }
}

