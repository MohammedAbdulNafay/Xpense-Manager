package com.example.xpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity
{

    private EditText mEmail;
    private EditText mPass;
    private Button btnReg;
    private TextView mSignin;

    //private ProgressDialog mDialog;  Deprecated

    private ProgressBar mProgressBar;

    //Firebase ...

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        //mDialog = new ProgressDialog(this);
        mProgressBar = new ProgressBar(this);

        registration();
    }


    private void registration()
    {
        mEmail = findViewById(R.id.email_reg);
        mPass = findViewById(R.id.password_reg);
        btnReg = findViewById(R.id.btn_reg);
        mSignin = findViewById(R.id.signin_here);
        mProgressBar = findViewById(R.id.progressbar_reg);

        btnReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email Required...");
                    Toast.makeText(getApplicationContext(),"Email is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(pass))
                {
                    mPass.setError("Password Required...");
                    Toast.makeText(getApplicationContext(),"Password is required", Toast.LENGTH_LONG).show();
                    return;
                }

                //mDialog.setMessage("Processing...");
                //mDialog.show();
                mProgressBar.setVisibility(View.VISIBLE);


                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            //mDialog.dismiss();
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Registration Successful ...", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                        }
                        else
                        {
                            //mDialog.dismiss();
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Registration Failed ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

}