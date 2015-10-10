package com.example.arun.medicaldoctorapp.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("_User")
public class User extends ParseUser
{
    public ParseFile getSignature() {
        return (ParseFile) get("signature");
    }

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

    public String getName() {return getString("name");}

    public boolean isDoctor() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Prescription> getPrescriptionList() {
        return (ArrayList<Prescription>) get("prescription_list");
    }

    public void addPrescriptionToList(Prescription prescription) {
        addUnique("prescription_list", prescription);
    }

    public void setSignature( ParseFile signature) {
        put("signature", signature);
    }

}
