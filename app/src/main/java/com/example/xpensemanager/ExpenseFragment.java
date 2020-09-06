package com.example.xpensemanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpensemanager.Model.Data;
import com.example.xpensemanager.Model.DataHandler;
import com.example.xpensemanager.Model.MyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment
{

    // Firebase database

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;

    // Recycle view

    private RecyclerView recyclerView;

    // Text View

    private TextView totalExpense;

    // Update or Delete text

    private EditText editAmount;
    private EditText editType;
    private EditText editNote;

    // Button

    private Button btnUpdate;
    private Button btnDelete;

    // Data Item value

    private String updType;
    private String updNote;
    private double updAmount;

    private String positionKey;

    public ExpenseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        totalExpense = myView.findViewById(R.id.expense_txt_result);

        recyclerView = myView.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                double totalAmount = 0.00;

                for(DataSnapshot mySnapshot : snapshot.getChildren())
                {

                    Data data = mySnapshot.getValue(Data.class);

                    totalAmount += data.getAmount();

                    totalExpense.setText(String.format("%.2f", totalAmount));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        return myView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position, @NonNull final Data data)
            {

                /*myViewHolder.setAmount(data.getAmount());
                myViewHolder.setDate(data.getDate());
                myViewHolder.setNote(data.getNote());
                myViewHolder.setType(data.getType());*/

                myViewHolder.setAmount(data.getAmount(), R.id.amount_txt_expense);
                myViewHolder.setDate(data.getDate(), R.id.date_txt_expense);
                myViewHolder.setNote(data.getNote(), R.id.note_txt_expene);
                myViewHolder.setType(data.getType(), R.id.type_txt_expense);

                myViewHolder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        positionKey = getRef(position).getKey();

                        updAmount = data.getAmount();
                        updNote = data.getNote();
                        updType = data.getType();

                        //updateDataItem();
                        DataHandler.updateDataItem(getActivity(), R.layout.update_data_item, R.id.amount_edt, R.id.type_edt, R.id.note_edt, R.id.btn_upd_update, R.id.btn_upd_delete, updAmount, updNote, updType, positionKey, mExpenseDatabase);
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false);
                MyViewHolder holder = new MyViewHolder(myView);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.startListening();
    }

    /*public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setType(String type)
        {
            TextView mType = mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        public void setNote(String note)
        {
            TextView mNote = mView.findViewById(R.id.note_txt_expene);
            mNote.setText(note);
        }

        public void setDate(String date)
        {
            TextView mDate = mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        public void setAmount(double amount)
        {
            TextView mAmount = mView.findViewById(R.id.amount_txt_expense);

            mAmount.setText(String.format("%.2f", amount));
        }
    }*/

    /*private void updateDataItem()
    {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.update_data_item, null);
        myDialog.setView(myView);

        editAmount = myView.findViewById(R.id.amount_edt);
        editType = myView.findViewById(R.id.type_edt);
        editNote = myView.findViewById(R.id.note_edt);

        // set data updated

        editAmount.setText(String.valueOf(updAmount));
        editAmount.setSelection(String.valueOf(updAmount).length());

        editNote.setText(updNote);
        editNote.setSelection(updNote.length());

        editType.setText(updType);
        editType.setSelection(updType.length());

        btnUpdate = myView.findViewById(R.id.btn_upd_update);
        btnDelete = myView.findViewById(R.id.btn_upd_delete);

        final AlertDialog dialog = myDialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                updNote = editNote.getText().toString().trim();
                updType = editType.getText().toString().trim();
                updAmount = Double.parseDouble(editAmount.getText().toString().trim());

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(updAmount, updType, updNote, positionKey, mDate);

                mExpenseDatabase.child(positionKey).setValue(data);

                dialog.dismiss();

                Toast.makeText(getContext(), "Data updated!!", Toast.LENGTH_SHORT).show();

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                mExpenseDatabase.child(positionKey).removeValue();

                dialog.dismiss();

                Toast.makeText(getContext(), "Data deleted!!", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();


    }*/

}