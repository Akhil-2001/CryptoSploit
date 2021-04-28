package com.example.cryptosploit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText regName, regEmail, regPass, regPassConf;
    Button regUserBtn;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regName = findViewById(R.id.editTextTextPersonName);
        regEmail = findViewById(R.id.editTextTextPersonName2);
        regPass = findViewById(R.id.editTextTextPersonName3);
        regPassConf = findViewById(R.id.editTextTextPersonName4);
        regUserBtn = findViewById(R.id.button2);

        fAuth = FirebaseAuth.getInstance();

        regUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = regName.getText().toString();
                String Email = regEmail.getText().toString();
                String Pass = regPass.getText().toString();
                String confPass = regPassConf.getText().toString();

                if(Name.isEmpty()) {
                    regName.setError("Full Name is required");
                    return;
                }

                if(Email.isEmpty()) {
                    regEmail.setError("Email Address is required");
                    return;
                }

                if(Pass.isEmpty()) {
                    regName.setError("Password is required");
                    return;
                }

                if(confPass.isEmpty()) {
                    regName.setError("Password confirmation is required");
                    return;
                }

                if(!Pass.equals(confPass)) {
                    regPassConf.setError("Passwords do not match");
                    return;
                }

                Toast.makeText(Register.this, "Data Validated", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(Email,Pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}