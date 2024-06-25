package com.example.androidprojecttutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class tutorsignin extends AppCompatActivity {


    EditText et_email, et_password;

    Button btn_login;

    TextView tv_registerBtn;

    TextView tv_forgotPassword;

    FirebaseAuth mAuth;

    FirebaseUser currentUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorsignin);



        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_registerBtn = findViewById(R.id.tv_registerButton);
        tv_forgotPassword = findViewById(R.id.tv_forgotPassword);
        mAuth=FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });


        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(tutorsignin.this, tutorreset.class));
            }
        });



        tv_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(tutorsignin.this, tutorsignup.class));
            }
        });


    }
    private void performLogin() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(tutorsignin.this, "please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            et_password.setError("password too short ");
            et_password.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("please put valid email");
            et_email.requestFocus();
            return;

        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    Toast.makeText(tutorsignin.this, "login succesfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(tutorsignin.this, tutordashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(tutorsignin.this, "login failed", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}