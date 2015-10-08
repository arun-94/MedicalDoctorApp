package com.example.arun.medicaldoctorapp.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("Prescription")
public class Prescription extends ParseObject
{
    public Prescription()
    {
    }

    public ParseUser getDoctorID()
    {
        return (ParseUser) get("doctor_id");
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Medicine> getMedicineList()
    {
        return (ArrayList<Medicine>) get("medicine_ids");
    }

    public void setDoctorID(ParseUser doctor)
    {
        put("doctor_id", doctor);
    }

    public void putMedicineList(ArrayList<Medicine> medicines) {
        put("medicine_ids", medicines);
    }

    public static ParseQuery<Prescription> getQuery()
    {
        return ParseQuery.getQuery(Prescription.class);
    }



}
