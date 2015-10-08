package com.example.arun.medicaldoctorapp.UI.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arun.medicaldoctorapp.ParseObjects.Medicine;
import com.example.arun.medicaldoctorapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.VHMedicineItem>
{

    private final Context mContext;
    private List<Medicine> mData;
    private List<Medicine> arraylist;

    public MedicineAdapter(Context context)
    {
        mContext = context;
        mData = new ArrayList<Medicine>();
    }

    public void addItems(ArrayList<Medicine> newStores)
    {
        mData.clear();
        arraylist.clear();
        mData.addAll(newStores);
        arraylist.addAll(newStores);
        this.notifyDataSetChanged();
    }

    public void add(Medicine s, int position)
    {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void add(Medicine s)
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
        Medicine store = mData.get(position);

        if (store != null)
        {
            //((VHMedicineItem) holder).storeCategoryImage.setI(storeItem.getUser().getUsername());
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
            for (Medicine wp : arraylist)
            {
                if (wp.getMedicineName().toLowerCase(Locale.getDefault()).contains(charText))
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
/*        public final TextView storeName;
        public final TextView storeLastUpdatedAt;
        public final TextView storePhotographerName;
        public final ImageView storeCategoryImage;
        public final ImageView storeExtraButton;*/

        public VHMedicineItem(View view)
        {
            super(view);

        }
    }
}
