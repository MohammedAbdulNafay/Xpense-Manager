package com.example.xpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity
{

    private EditText mEmail;
    private Button btnReset;
    private TextView mSignupHere;
    private TextView mSignIn;

    //Firebase ...

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.email_reset);
        btnReset = findViewById(R.id.btn_reset);
        mSignupHere = findViewById(R.id.signup_reg);
        mSignIn = findViewById(R.id.signin_here);

        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = mEmail.getText().toString().trim();

                mAuth.sendPasswordResetEmail(email)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(getApplicationContext(), "Password reset email sent!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetActivity.this, MainActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
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

        //Login activity...

        mSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

}