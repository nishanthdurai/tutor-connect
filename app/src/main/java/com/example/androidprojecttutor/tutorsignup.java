package com.example.androidprojecttutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Patterns;
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

public class tutorsignup extends AppCompatActivity {



    EditText et_email, et_password, et_confirmPassword, et_username;
    Button btn_Register;

    TextView tv_loginBtn;
    FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorsignup);

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        btn_Register = findViewById(R.id.btn_register);
        tv_loginBtn = findViewById(R.id.tv_loginButton);

        mAuth = FirebaseAuth.getInstance();


        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuth();
            }
        });


        tv_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tutorsignup.this,tutorsignin.class);
                intent.putExtra("name" ,et_email.getText().toString().trim());
                intent.putExtra("password" ,et_password.getText().toString().trim());

                startActivity(intent);
            }
        });
    }


    private void PerformAuth() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();
        String username = et_username.getText().toString();



        if(username.isEmpty() ||email.isEmpty()|| password.isEmpty()||confirmPassword.isEmpty()){
            Toast.makeText(this, "please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()< 8){
            et_password.setError("password too short ");
            et_password.requestFocus();
            return;
        }

        if(!confirmPassword.equals(password)){
            et_confirmPassword.setError("password should be more than 8 char ");
            et_confirmPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("please put valid email");
            et_email.requestFocus();
            return;

        }


        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                if (task.isSuccessful()) {
                    Toast.makeText(tutorsignup.this, "registered account", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(tutorsignup.this, tutorsignin.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(tutorsignup.this, "failed!contact admin", Toast.LENGTH_SHORT).show();
                }

            }
            });
        }
}