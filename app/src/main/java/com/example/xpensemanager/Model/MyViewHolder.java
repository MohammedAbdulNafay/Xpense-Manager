package com.example.xpensemanager.Model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder
{

    public View mView;

    public MyViewHolder(@NonNull View itemView)
    {
        super(itemView);
        mView = itemView;
    }

    public void setType(String type, int id)
    {
        TextView mType = mView.findViewById(id);
        mType.setText(type);
    }

    public void setNote(String note, int id)
    {
        TextView mNote = mView.findViewById(id);
        mNote.setText(note);
    }

    public void setDate(String date, int id)
    {
        TextView mDate = mView.findViewById(id);
        mDate.setText(date);
    }

    public void setAmount(double amount, int id)
    {
        TextView mAmount = mView.findViewById(id);
        mAmount.setText(String.format("%.2f", amount));
    }
}
