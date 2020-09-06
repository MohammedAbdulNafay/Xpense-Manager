package com.example.xpensemanager.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Date;

public class DataHandler
{

    public static void updateDataItem(final FragmentActivity fragmentActivity, int layoutId, int amountId, int typeId, int noteId, int updateButtonId, int deleteButtonId, double updAmount, String updNote, String updType, final String positionKey, final DatabaseReference databaseReference)
    {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(fragmentActivity);
        LayoutInflater inflater = LayoutInflater.from(fragmentActivity);
        View myView = inflater.inflate(layoutId, null);
        myDialog.setView(myView);

        final EditText editAmount = myView.findViewById(amountId);
        final EditText editType = myView.findViewById(typeId);
        final EditText editNote = myView.findViewById(noteId);

        // set data updated

        editAmount.setText(String.format("%.2f", updAmount));
        editAmount.setSelection(String.valueOf(updAmount).length());

        editNote.setText(updNote);
        editNote.setSelection(updNote.length());

        editType.setText(updType);
        editType.setSelection(updType.length());

        Button btnUpdate = myView.findViewById(updateButtonId);
        Button btnDelete = myView.findViewById(deleteButtonId);

        final AlertDialog dialog = myDialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String updNote = editNote.getText().toString().trim();
                String updType = editType.getText().toString().trim();
                Double updAmount = Double.parseDouble(editAmount.getText().toString().trim());

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(updAmount, updType, updNote, positionKey, mDate);

                databaseReference.child(positionKey).setValue(data);

                dialog.dismiss();

                Toast.makeText(fragmentActivity, "Data updated!!", Toast.LENGTH_SHORT).show();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                databaseReference.child(positionKey).removeValue();

                dialog.dismiss();

                Toast.makeText(fragmentActivity, "Data deleted!!", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();
    }
}
