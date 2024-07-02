package com.example.androidprojecttutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class tutorsignin extends AppCompatActivity {

   EditText et_email, et_password;
       Button btn_login;
        TextView tv_registerBtn;

        TextView tv_forgotPassword;

      DatabaseReference dbInstance;


       ProgressDialog progressDialog;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tutorsignin);

            setTitle("Tutor");

            // Enable the Up button
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            et_email = findViewById(R.id.et_email);
            et_password = findViewById(R.id.et_password);
            btn_login = findViewById(R.id.btn_login);
            tv_registerBtn = findViewById(R.id.tv_registerButton);
            tv_forgotPassword = findViewById(R.id.tv_forgotPassword);

            tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(tutorsignin.this, tutorreset.class));
                }
            });

            progressDialog = new ProgressDialog(this);

            dbInstance = FirebaseDatabase.getInstance().getReference();

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performLogin();
                }
            });

            tv_registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(tutorsignin.this, tutorsignup.class));
                }
            });

        }

        private void performLogin() {
            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            if (email.isEmpty()) {
                et_email.setError("Please Enter Email");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_email.setError("Please Enter a valid Email");
                return;
            } else if (password.isEmpty()) {
                et_password.setError("Please input Password");
                return;
            } else if (password.length() < 6) {
                et_password.setError("Password too short");
                return;
            } else {
                progressDialog.setMessage("Validating credentials....");
                progressDialog.setTitle("Loading");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                // validate credentials with email and password
                Query emailQuery = dbInstance.child("tutors").orderByChild("email").equalTo(email);
                emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
                                Tutor tutor = tutorSnapshot.getValue(Tutor.class);
                                if (tutor != null && password.equals(tutor.getPassword())) {
                                    // check if password is valid
                                    Toast.makeText(tutorsignin.this, "SignIn success.", Toast.LENGTH_SHORT).show();
                                    sendUserToMainActivity(tutor.getKey(), tutor.getEmail(), tutor.getPassword());
                                    return; // Exit once a match is found
                                }
                            }

                            Toast.makeText(tutorsignin.this, "Invalid password..!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(tutorsignin.this, "Email is not registered, " + "try signing up..!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(tutorsignin.this, tutorsignup.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Log.e("Firebase", "Database error", databaseError.toException());
                        Toast.makeText(tutorsignin.this, "Error validating credentials, " + "try again later..!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        private void sendUserToMainActivity(String userId, String email, String password) {
            Intent intent = new Intent(tutorsignin.this, tutordashboard.class);
            intent.putExtra("email", email);
            intent.putExtra("userId", userId);
            intent.putExtra("password", password);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }


//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            switch (item.getItemId()) {
//                case android.R.id.home:
//                    // Handle the back button action
//                    onBackPressed();
//                    return true;
//                default:
//                    return super.onOptionsItemSelected(item);
//            }
    }
    //}




