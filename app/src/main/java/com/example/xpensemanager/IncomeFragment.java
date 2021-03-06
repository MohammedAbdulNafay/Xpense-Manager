package com.example.xpensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
public class IncomeFragment extends Fragment
{

    // Firebase Database

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;

    // Recycler view

    private RecyclerView recyclerView;

    // Text View

    private TextView totalIncome;


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

    public IncomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_income, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();

        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        totalIncome = myView.findViewById(R.id.income_txt_result);

        recyclerView = myView.findViewById(R.id.recycler_id_income);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        mIncomeDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                double totalAmount = 0.00;

                for(DataSnapshot mySnapshot : snapshot.getChildren())
                {

                    Data data = mySnapshot.getValue(Data.class);

                    totalAmount += data.getAmount();

                    totalIncome.setText(String.format("%.2f", totalAmount));

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
                        .setQuery(mIncomeDatabase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position, @NonNull final Data data)
            {

                myViewHolder.setAmount(data.getAmount(), R.id.amount_txt_income);
                myViewHolder.setDate(data.getDate(), R.id.date_txt_income);
                myViewHolder.setNote(data.getNote(), R.id.note_txt_income);
                myViewHolder.setType(data.getType(), R.id.type_txt_income);

                myViewHolder.mView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        positionKey = getRef(position).getKey();

                        updAmount = data.getAmount();
                        updNote = data.getNote();
                        updType = data.getType();

                        DataHandler.updateDataItem(getActivity(), R.layout.update_data_item, R.id.amount_edt, R.id.type_edt, R.id.note_edt, R.id.btn_upd_update, R.id.btn_upd_delete, updAmount, updNote, updType, positionKey, mIncomeDatabase);

                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false);
                MyViewHolder holder = new MyViewHolder(myView);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.startListening();

    }
}