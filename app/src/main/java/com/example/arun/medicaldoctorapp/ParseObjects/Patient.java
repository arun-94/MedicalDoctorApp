package com.example.arun.medicaldoctorapp.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("_User")
public class Patient extends ParseUser
{
    public boolean isMale() {
        return getBoolean("is_male");
    }

    public String getPhone() {
        return getString("phone");
    }

    public String getAddress() {
        return getString("address");
    }

    public int getAge() {
        return getInt("age");
    }

    public boolean isDoctor() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Prescription> getPrescriptionList() {
        return (ArrayList<Prescription>) get("prescription_list");
    }

    public void addPrescriptionToList(Prescription p)
    {
        addUnique("prescription_list", p);
    }
}
