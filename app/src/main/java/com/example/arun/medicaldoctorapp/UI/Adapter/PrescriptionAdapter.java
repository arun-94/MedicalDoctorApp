package com.example.arun.medicaldoctorapp.UI.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arun.medicaldoctorapp.ImageUtility;
import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.example.arun.medicaldoctorapp.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.VHMedicineItem>
{

    private final Context mContext;
    private List<Prescription> mData;
    // private List<Prescription> arraylist;

    public PrescriptionAdapter(Context context, ArrayList<Prescription> prescriptions)
    {
        mContext = context;
        if (prescriptions == null)
        {
            mData = new ArrayList<>();
        }
        else
        {
            mData = prescriptions;
            notifyDataSetChanged();
        }
    }

    public void addItems(ArrayList<Prescription> prescriptionArrayList)
    {
        Log.d("Process", "size of prescription : "+  prescriptionArrayList.size());
        mData.clear();
        //arraylist.clear();
        /*for(int i = 0; i < prescriptionArrayList.size(); i++)
        {
            add(prescriptionArrayList.get(i));
        }*/
        mData.addAll(prescriptionArrayList);
        //arraylist.addAll(newStores);
        notifyDataSetChanged();
        Log.d("Process", "Added to adapter");
    }

    public void add(Prescription s, int position)
    {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void add(Prescription s)
    {
        Log.d("process", "Size of list : " +  getItemCount());
        mData.add(s);
        notifyDataSetChanged();
    }

    public void remove(int position)
    {
        if (position < getItemCount())
        {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public VHMedicineItem onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_prescription, parent, false);
        return new VHMedicineItem(view);
    }

    @Override
    public void onBindViewHolder(final VHMedicineItem holder, final int position)
    {
        Prescription prescription = mData.get(position);

        if (prescription != null)
        {
            ((VHMedicineItem) holder).doctorName.setText(prescription.getPatientID().getString("name"));
            String medicineCSV = "";
            for(int i = 0; i < prescription.getMedicineList().size() - 1; i++) {
                medicineCSV += prescription.getMedicineList().get(i).getMedicine().getMedicineName() + ", ";
            }

            medicineCSV += prescription.getMedicineList().get(prescription.getMedicineList().size() - 1).getMedicine().getMedicineName();

            ((VHMedicineItem) holder).medicineNames.setText(medicineCSV);

            Date d1 = prescription.getCreatedAt();
            ((VHMedicineItem) holder).prescriptionDate.setText("" + d1.getDate() + "/" + d1.getMonth() + "/2015");

            ParseFile pf = prescription.getPatientID().getParseFile("profile_pic");
            pf.getDataInBackground(new GetDataCallback()
            {
                @Override
                public void done(byte[] bytes, ParseException e)
                {
                    if(e == null)
                    {
                        Bitmap bm = ImageUtility.decodeSampledBitmapFromByte(mContext, bytes);
                        holder.doctorPicture.setImageBitmap(bm);
                    }
                    else {
                        Log.e("PrescriptionAdapter", e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

/*    public ArrayList<Prescription> filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        mData.clear();
        if (charText.length() == 0)
        {
            mData.addAll(arraylist);
        }
        else
        {
*//*            for (Prescription wp : arraylist)
            {
                if (wp.getMedicineName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mData.add(wp);
                }
            }*//*
        }
        notifyDataSetChanged();

        return (ArrayList) mData;
    }*/


    public static class VHMedicineItem extends RecyclerView.ViewHolder
    {
        TextView doctorName;
        TextView medicineNames;
        TextView prescriptionDate;
        CircleImageView doctorPicture;

        public VHMedicineItem(View view)
        {
            super(view);
            doctorName = (TextView) view.findViewById(R.id.prescription_patient_name);
            medicineNames = (TextView) view.findViewById(R.id.prescription_details);
            prescriptionDate = (TextView) view.findViewById(R.id.prescription_date);
            doctorPicture = (CircleImageView) view.findViewById(R.id.prescription_profile_pic);
        }
    }
}
