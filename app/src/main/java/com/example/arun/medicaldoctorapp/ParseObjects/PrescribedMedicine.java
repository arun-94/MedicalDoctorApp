package com.example.arun.medicaldoctorapp.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

@ParseClassName("PrescribedMedicine")
public class PrescribedMedicine extends ParseObject
{
    public PrescribedMedicine(){}

    public String getDuration() {
        return getString("duration");
    }

    public void setDuration(String duration) {
        put("duration", duration);
    }

    public String getQuantity() {
        return getString("quantity");
    }

    public void setQuantity(String quantity) {
        put("quantity", quantity);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Integer> getTimesADay() {
        return (ArrayList<Integer>) get("times_a_day");
    }

    public void setTimesADay(ArrayList<Integer> timesADay) {
        put("times_a_day", timesADay);
    }

    public void setMedicine(Medicine medicine) {
        put("medicine", medicine);
    }

    public void setNotes(String notes) {
        put("notes", notes);
    }

    public String getNotes() {
        return getString("notes");
    }

    public void setType(Boolean type) {
        put("is_capsule", type);
    }

    public boolean isCapsule() {
        return getBoolean("is_capsule");
    }

    public static ParseQuery<PrescribedMedicine> getQuery()
    {
        return ParseQuery.getQuery(PrescribedMedicine.class);
    }
}
