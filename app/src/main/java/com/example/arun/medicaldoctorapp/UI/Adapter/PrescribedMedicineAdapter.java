package com.example.arun.medicaldoctorapp.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.arun.medicaldoctorapp.ParseObjects.PrescribedMedicine;
import com.example.arun.medicaldoctorapp.R;

import java.util.ArrayList;
import java.util.List;

public class PrescribedMedicineAdapter extends RecyclerView.Adapter<PrescribedMedicineAdapter.VHMedicineItem>
{

    private final Context mContext;
    private List<PrescribedMedicine> mData;
    // private List<PrescribedMedicine> arraylist;

    public PrescribedMedicineAdapter(Context context, ArrayList<PrescribedMedicine> prescriptions)
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

    public void addItems(ArrayList<PrescribedMedicine> prescriptionArrayList)
    {
        Log.d("Process", "size of prescription : " + prescriptionArrayList.size());
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

    public void add(PrescribedMedicine s, int position)
    {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void add(PrescribedMedicine s)
    {
        Log.d("process", "Size of list : " + getItemCount());
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
        final View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_prescribed_medicine, parent, false);
        return new VHMedicineItem(view);
    }

    @Override
    public void onBindViewHolder(final VHMedicineItem holder, final int position)
    {
        PrescribedMedicine medicine = mData.get(position);

        if (medicine != null)
        {
            ((VHMedicineItem) holder).medicineName.setText(medicine.getMedicine().getMedicineName());
            //((VHMedicineItem) holder).medicineDate.setText(""+medicine.getDate());
            ((VHMedicineItem) holder).medicineDuration.setText("" + medicine.getDuration());
            ((VHMedicineItem) holder).medicineQuantity.setText("" + medicine.getQuantity());
            ((VHMedicineItem) holder).morning.setText("" + medicine.getTimesADay().get(0));
            ((VHMedicineItem) holder).afternoon.setText("" + medicine.getTimesADay().get(1));
            ((VHMedicineItem) holder).evening.setText("" + medicine.getTimesADay().get(2));
            if (medicine.getNotes().equals("") || medicine.getNotes() == null)
            {
                ((VHMedicineItem) holder).notesLayout.setVisibility(View.GONE);
            }
            else
            {
                ((VHMedicineItem) holder).medicineNotes.setText("" + medicine.getNotes());
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

/*    public ArrayList<PrescribedMedicine> filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        mData.clear();
        if (charText.length() == 0)
        {
            mData.addAll(arraylist);
        }
        else
        {
*//*            for (PrescribedMedicine wp : arraylist)
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
        TextView medicineName;
        TextView medicineDuration;
        TextView medicineNotes;
        TextView medicineQuantity;
        TextView morning;
        TextView afternoon;
        TextView evening;
        View notesLayout;

        public VHMedicineItem(View view)
        {
            super(view);
            medicineName = (TextView) view.findViewById(R.id.prescribed_name);
            medicineDuration = (TextView) view.findViewById(R.id.prescribed_duration);
            medicineQuantity = (TextView) view.findViewById(R.id.prescribed_quantity);
            medicineNotes = (TextView) view.findViewById(R.id.prescribed_notes);
            morning = (TextView) view.findViewById(R.id.textview_morning);
            evening = (TextView) view.findViewById(R.id.textview_evening);
            afternoon = (TextView) view.findViewById(R.id.textview_afternoon);
            notesLayout = (View) view.findViewById(R.id.notes_layout);
        }
    }
}
