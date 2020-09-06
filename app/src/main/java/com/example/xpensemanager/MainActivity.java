package com.example.xpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
{

    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForgetPassword;
    private TextView mSignupHere;

    //private ProgressDialog mDialog;  Deprecated

    private ProgressBar mProgressBar;

    //Firebase ...

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        loginDetails();

    }

    private void loginDetails()
    {
        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        mForgetPassword = findViewById(R.id.forget_password);
        mSignupHere = findViewById(R.id.signup_reg);
        mProgressBar = findViewById(R.id.progressbar_login);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email Required...");
                    Toast.makeText(getApplicationContext(),"Email is required", Toast.LENGTH_LONG);
                    return;
                }

                if(TextUtils.isEmpty(pass))
                {
                    mPass.setError("Password Required...");
                    Toast.makeText(getApplicationContext(),"Password is required", Toast.LENGTH_LONG);
                    return;
                }

                //mDialog.setMessage("Processing...");
                //mDialog.show();
                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if(task.isSuccessful())
                        {
                            //mDialog.dismiss();
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Login Successful ...", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                        }
                        else
                        {
                            //mDialog.dismiss();
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Login Failed ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //Registration activity...

        mSignupHere.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });

        //Reset password activity...

        mForgetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), ResetActivity.class));
            }
        });

    }
}