package com.example.arun.medicaldoctorapp.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arun.medicaldoctorapp.ParseObjects.Prescription;
import com.example.arun.medicaldoctorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.VHMedicineItem>
{

    private final Context mContext;
    private List<Prescription> mData;
    private List<Prescription> arraylist;

    public PrescriptionAdapter(Context context, ArrayList<Prescription> prescriptions)
    {
        mContext = context;
        if (prescriptions == null)
        {
            mData = new ArrayList<Prescription>();
        }
        else
        {
            mData = prescriptions;
        }
    }

    public void addItems(ArrayList<Prescription> newStores)
    {
        mData.clear();
        arraylist.clear();
        mData.addAll(newStores);
        arraylist.addAll(newStores);
        this.notifyDataSetChanged();
    }

    public void add(Prescription s, int position)
    {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void add(Prescription s)
    {
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
    public void onBindViewHolder(VHMedicineItem holder, final int position)
    {
        Prescription prescription = mData.get(position);

        if (prescription != null)
        {
            //((VHMedicineItem) holder).storeCategoryImage.setI(storeItem.getUser().getUsername());
        }
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public ArrayList<Prescription> filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        mData.clear();
        if (charText.length() == 0)
        {
            mData.addAll(arraylist);
        }
        else
        {
/*            for (Prescription wp : arraylist)
            {
                if (wp.getMedicineName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mData.add(wp);
                }
            }*/
        }
        notifyDataSetChanged();

        return (ArrayList) mData;
    }


    public static class VHMedicineItem extends RecyclerView.ViewHolder
    {
        public VHMedicineItem(View view)
        {
            super(view);

        }
    }
}
