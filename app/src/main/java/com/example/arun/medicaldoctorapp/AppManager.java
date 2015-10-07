package com.example.arun.medicaldoctorapp;

import android.app.Application;

import com.example.arun.medicaldoctorapp.ParseObjects.Doctor;
import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.ParseObjects.Patient;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.parse.Parse;
import com.parse.ParseObject;

public class AppManager extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        parseInit();
    }

    private void parseInit() {
        ParseObject.registerSubclass(Doctor.class);
        ParseObject.registerSubclass(Medicine.class);
        ParseObject.registerSubclass(Patient.class);
        ParseObject.registerSubclass(Prescription.class);
        Parse.initialize(this, "cvSyybrwMKzHdVwpvUQ2ftclezIYsunMzz6UtUP7", "szykRYtChmUBK1FhyapdUrHI2lGOECH3dIjUCL1c");

    }
}
