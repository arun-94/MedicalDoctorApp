package com.example.arun.medicaldoctorapp.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.ParseObjects.PrescribedMedicine;
import com.example.arun.medicaldoctorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.VHMedicineItem>
{

    private final Context mContext;
    private List<PrescribedMedicine> mData;
    private List<PrescribedMedicine> arraylist;

    public MedicineAdapter(Context context)
    {
        mContext = context;
        mData = new ArrayList<PrescribedMedicine>();
    }

    public void addItems(ArrayList<PrescribedMedicine> prescribedMedicines)
    {
        mData.clear();
        arraylist.clear();
        mData.addAll(prescribedMedicines);
        arraylist.addAll(prescribedMedicines);
        this.notifyDataSetChanged();
    }

    public void add(PrescribedMedicine s, int position)
    {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void add(PrescribedMedicine s)
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
        final View view = LayoutInflater.from(mContext).inflate(R.layout.card_prescribed_medicine, parent, false);
        return new VHMedicineItem(view);
    }

    @Override
    public void onBindViewHolder(VHMedicineItem holder, final int position)
    {
        PrescribedMedicine medicine = mData.get(position);

        if (medicine != null)
        {

            ((VHMedicineItem) holder).medicineName.setText(medicine.getMedicine().getMedicineName());
            ((VHMedicineItem) holder).duration.setText(medicine.getDuration());
            ((VHMedicineItem) holder).quantity.setText(medicine.getQuantity());
            ((VHMedicineItem) holder).morning.setText("" + medicine.getTimesADay().get(0));
            ((VHMedicineItem) holder).afternoon.setText("" + medicine.getTimesADay().get(1));
            ((VHMedicineItem) holder).evening.setText("" + medicine.getTimesADay().get(2));
        }
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }

    public ArrayList<Medicine> filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        mData.clear();
        if (charText.length() == 0)
        {
            mData.addAll(arraylist);
        }
        else
        {
            for (PrescribedMedicine wp : arraylist)
            {
                if (wp.getMedicine().getMedicineName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    mData.add(wp);
                }
            }
        }
        notifyDataSetChanged();

        return (ArrayList) mData;
    }


    public static class VHMedicineItem extends RecyclerView.ViewHolder
    {
        public final TextView medicineName;
        public final TextView duration;
        public final TextView quantity;
        public final TextView morning;
        public final TextView afternoon;
        public final TextView evening;


        public VHMedicineItem(View view)
        {
            super(view);
            this.medicineName = (TextView) view.findViewById(R.id.prescribed_name);
            this.duration = (TextView) view.findViewById(R.id.prescribed_duration);
            this.quantity = (TextView) view.findViewById(R.id.prescribed_quantity);
            this.morning = (TextView) view.findViewById(R.id.textview_morning);
            this.afternoon = (TextView) view.findViewById(R.id.textview_afternoon);
            this.evening = (TextView) view.findViewById(R.id.textview_evening);
        }
    }
}
