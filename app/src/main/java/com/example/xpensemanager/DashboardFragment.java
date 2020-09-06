package com.example.xpensemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpensemanager.Model.Data;
import com.example.xpensemanager.Model.MyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
public class DashboardFragment extends Fragment
{

    // Floating button

    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    // Floating button textview

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    private boolean isOpen = false;

    // Animation

    private Animation fadeOpen, fadeClose;

    // Firebase database

   private FirebaseAuth mAuth;
   private DatabaseReference mIncomeDatabase;
   private DatabaseReference mExpenseDatabase;

   // Dashboard Income and Expense total

    private TextView totalIncomeResult;
    private TextView totalExpenseResult;

    // Recycler views

    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();

        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        // Connect floating button to the layout

        fab_main_btn = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myView.findViewById(R.id.income_ft_btn);
        fab_expense_btn = myView.findViewById(R.id.expense_ft_btn);

        // Connect floating text

        fab_income_txt = myView.findViewById(R.id.income_ft_text);
        fab_expense_txt = myView.findViewById(R.id.expense_ft_text);

        // Total Income and Expense

        totalIncomeResult = myView.findViewById(R.id.income_set_result);
        totalExpenseResult = myView.findViewById(R.id.expense_set_result);

        // Recycler views

        mRecyclerIncome = myView.findViewById(R.id.recycler_income);
        mRecyclerExpense = myView.findViewById(R.id.recycler_expense);

        // Animation connect

        fadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        fadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);

        fab_main_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                addData();

                ftAnimation();

            }
        });

        // Calculate total income

        mIncomeDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                double totalIncome = 0.00;

                for(DataSnapshot mySnapshot : snapshot.getChildren())
                {

                    Data data = mySnapshot.getValue(Data.class);
                    totalIncome += data.getAmount();

                    totalIncomeResult.setText(String.format("%.2f", totalIncome));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // Calculate total expense

        mExpenseDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                double totalExpense = 0.00;

                for(DataSnapshot mySnapshot : snapshot.getChildren())
                {

                    Data data = mySnapshot.getValue(Data.class);
                    totalExpense += data.getAmount();

                    totalExpenseResult.setText(String.format("%.2f", totalExpense));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // Recycler views

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        layoutManagerIncome.setStackFromEnd(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);


        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);

        return myView;
    }

    // Floating button animation

    private void ftAnimation()
    {
        if (isOpen)
        {

            fab_income_btn.startAnimation(fadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.startAnimation(fadeClose);
            fab_expense_btn.setClickable(false);

            fab_income_txt.startAnimation(fadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.startAnimation(fadeClose);
            fab_expense_txt.setClickable(false);

            isOpen = false;

        }
        else
        {

            fab_income_btn.startAnimation(fadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.startAnimation(fadeOpen);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(fadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.startAnimation(fadeOpen);
            fab_expense_txt.setClickable(true);

            isOpen = true;
        }
    }

    private void addData()
    {
        // FAB button income

        fab_income_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dataInsert(mIncomeDatabase);
            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dataInsert(mExpenseDatabase);
            }
        });
    }

    /*public void incomeDataInsert()
    {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText editAmount = myView.findViewById(R.id.amount_edt);
        final EditText editType = myView.findViewById(R.id.type_edt);
        final EditText editNote = myView.findViewById(R.id.note_edt);

        Button btnSave = myView.findViewById(R.id.btn_save);
        Button btnCancel = myView.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount))
                {
                    editAmount.setError("Required field...");
                    return;
                }

                if(TextUtils.isEmpty(type))
                {
                    editType.setError("Required field...");
                    return;
                }

                if(TextUtils.isEmpty(note))
                {
                    editNote.setError("Required field...");
                    return;
                }

                int intAmount = Integer.parseInt(amount);

                String id = mIncomeDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(intAmount, type, note, id, mDate);

                mIncomeDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();

                ftAnimation();

                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void expenseDataInsert()
    {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText editAmount = myView.findViewById(R.id.amount_edt);
        final EditText editType = myView.findViewById(R.id.type_edt);
        final EditText editNote = myView.findViewById(R.id.note_edt);

        Button btnSave = myView.findViewById(R.id.btn_save);
        Button btnCancel = myView.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount))
                {
                    editAmount.setError("Required field...");
                    return;
                }

                if(TextUtils.isEmpty(type))
                {
                    editType.setError("Required field...");
                    return;
                }

                if(TextUtils.isEmpty(note))
                {
                    editNote.setError("Required field...");
                    return;
                }

                int intAmount = Integer.parseInt(amount);

                String id = mExpenseDatabase.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(intAmount, type, note, id, mDate);

                mExpenseDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }*/

    @Override
    public void onStart()
    {
        super.onStart();

        // Income Recycler adapter

        FirebaseRecyclerOptions<Data> incomeOptions = new FirebaseRecyclerOptions.Builder<Data>().setQuery(mIncomeDatabase, Data.class).build();

        /*FirebaseRecyclerAdapter<Data, IncomeViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(incomeOptions)
         {
            @Override
            protected void onBindViewHolder(@NonNull IncomeViewHolder myViewHolder, final int position, @NonNull final Data data)
            {

                myViewHolder.setIncomeAmount(data.getAmount());
                myViewHolder.setIncomeDate(data.getDate());
                myViewHolder.setIncomeType(data.getType());

            }

            @NonNull
            @Override
            public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
             {
                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income, parent, false);
                IncomeViewHolder holder = new IncomeViewHolder(myView);
                return holder;
            }*/

        FirebaseRecyclerAdapter<Data, MyViewHolder> incomeAdapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(incomeOptions)
        {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position, @NonNull final Data data)
            {

                myViewHolder.setAmount(data.getAmount(), R.id.amount_income_ds);
                myViewHolder.setDate(data.getDate(), R.id.date_income_ds);
                myViewHolder.setType(data.getType(), R.id.type_income_ds);

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_income, parent, false);
                MyViewHolder holder = new MyViewHolder(myView);
                return holder;
            }
        };

        mRecyclerIncome.setAdapter(incomeAdapter);

        incomeAdapter.startListening();

        // Expense Recycler adapter

        /*FirebaseRecyclerOptions<Data> expenseOptions =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, ExpenseViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(expenseOptions)
        {
            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder myViewHolder, final int position, @NonNull final Data data)
             {

                myViewHolder.setExpenseAmount(data.getAmount());
                myViewHolder.setExpenseDate(data.getDate());
                myViewHolder.setExpenseType(data.getType());

            }

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
             {
                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_expense, parent, false);
                ExpenseViewHolder holder = new ExpenseViewHolder(myView);
                return holder;
            }*/

            FirebaseRecyclerOptions<Data> expenseOptions =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> expenseAdapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(expenseOptions)
        {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position, @NonNull final Data data)
            {

                myViewHolder.setAmount(data.getAmount(), R.id.amount_expense_ds);
                myViewHolder.setDate(data.getDate(), R.id.date_expense_ds);
                myViewHolder.setType(data.getType(), R.id.type_expense_ds);

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_expense, parent, false);
                MyViewHolder holder = new MyViewHolder(myView);
                return holder;
            }
        };

        mRecyclerExpense.setAdapter(expenseAdapter);

        expenseAdapter.startListening();
    }

    // For Income data

    /*public static class IncomeViewHolder extends RecyclerView.ViewHolder
     {

        View mIncomeView;

        public IncomeViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mIncomeView = itemView;
        }

        public void setIncomeType(String type)
        {
            TextView mType = mIncomeView.findViewById(R.id.type_income_ds);
            mType.setText(type);
        }

        public void setIncomeAmount(double amount)
        {
            TextView mAmount = mIncomeView.findViewById(R.id.amount_income_ds);
            mAmount.setText(String.format("%.2f", amount));
        }

        public void setIncomeDate(String date) {
            TextView mDate = mIncomeView.findViewById(R.id.date_income_ds);
            mDate.setText(date);
        }
    }*/

    // For Expense data

    /*public static class ExpenseViewHolder extends RecyclerView.ViewHolder
    {

        View mExpenseView;

        public ExpenseViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mExpenseView = itemView;
        }

        public void setExpenseType(String type)
        {
            TextView mType = mExpenseView.findViewById(R.id.type_expense_ds);
            mType.setText(type);
        }

        public void setExpenseAmount(double amount)
        {
            TextView mAmount = mExpenseView.findViewById(R.id.amount_expense_ds);
            mAmount.setText(String.format("%.2f", amount));
        }

        public void setExpenseDate(String date)
        {
            TextView mDate = mExpenseView.findViewById(R.id.date_expense_ds);
            mDate.setText(date);
        }
    }*/

    public void dataInsert(final DatabaseReference databaseReference)
    {

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText editAmount = myView.findViewById(R.id.amount_edt);
        final EditText editType = myView.findViewById(R.id.type_edt);
        final EditText editNote = myView.findViewById(R.id.note_edt);

        Button btnSave = myView.findViewById(R.id.btn_save);
        Button btnCancel = myView.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String type = editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if(TextUtils.isEmpty(amount))
                {
                    editAmount.setError("Required field...");
                    return;
                }

                if(TextUtils.isEmpty(type))
                {
                    editType.setError("Required field...");
                    return;
                }

                if(TextUtils.isEmpty(note))
                {
                    editNote.setError("Required field...");
                    return;
                }

                int intAmount = Integer.parseInt(amount);

                String id = databaseReference.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(intAmount, type, note, id, mDate);

                databaseReference.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}