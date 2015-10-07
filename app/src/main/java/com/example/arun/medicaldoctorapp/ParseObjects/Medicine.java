package com.example.arun.medicaldoctorapp.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Medicine")
public class Medicine extends ParseObject
{
    public Medicine()
    {
    }

    public String getMedicineName()
    {
        return getString("medicine_name");
    }

    public String getMedicineUrl() {
        return "www.drugs.com/" + getMedicineName() + ".html";
    }
}
