package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.MailHandler;
import com.tutorconnect.app.R;
import com.tutorconnect.app.RandomOtp;
import com.tutorconnect.app.model.Tutor;

public class TutorResetPasswordActivity extends AppCompatActivity {
    EditText et_sendEmail, et_otp;
    Button btn_reset, btn_submit_otp;

    ProgressDialog progressDialog;
    String generatedOtp;

    DatabaseReference dbInstance;

    String tutorEmail = "";
    String tutorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_reset_password);

        setTitle("Tutor - Reset password");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        dbInstance = FirebaseDatabase.getInstance().getReference();

        et_sendEmail = findViewById(R.id.et_sendEmail);
        btn_reset = findViewById(R.id.btn_reset);
        et_otp = findViewById(R.id.et_otp);
        btn_submit_otp = findViewById(R.id.btn_submit_otp);

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_sendEmail.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(TutorResetPasswordActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    // check if the email present.
                    // validate credentials with email and password
                    Query emailQuery = dbInstance.child("tutors").orderByChild("email").equalTo(email);
                    emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
                                    Tutor tutor = tutorSnapshot.getValue(Tutor.class);
                                    if (tutor != null && email.equals(tutor.getEmail())) {
                                        tutorEmail = tutor.getEmail();
                                        tutorId = tutor.getId();
                                        String otp = RandomOtp.generateOTP(6);
                                        generatedOtp = otp;
                                        sendEmail(email,
                                                "Password for OTP resetting",
                                                "Your otp to reset password is: " + otp);
                                    }
                                }
                            } else {
                                Toast.makeText(TutorResetPasswordActivity.this, "Email is not registered, " + "try signing up..!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Log.e("Firebase", "Database error", databaseError.toException());
                            Toast.makeText(TutorResetPasswordActivity.this, "Error validating email, " + "try again later..!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = et_otp.getText().toString();
                if (otp.equals(generatedOtp)) {
                    Toast.makeText(TutorResetPasswordActivity.this, "OTP validated successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TutorResetPasswordActivity.this, TutorPasswordChangeActivity.class);
                    intent.putExtra("tutorEmail", tutorEmail);
                    intent.putExtra("tutorId", tutorId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    // Proceed to reset password or other actions
                } else {
                    Toast.makeText(TutorResetPasswordActivity.this, "Invalid OTP, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail(String email, String subject, String message) {
        new Thread(() -> {
            boolean result = MailHandler.sendEmail(email, subject, message);
            runOnUiThread(() -> {
                progressDialog.dismiss();
                if (result) {
                    // Show OTP input and submit button
                    et_otp.setVisibility(View.VISIBLE);
                    btn_submit_otp.setVisibility(View.VISIBLE);
                    Toast.makeText(TutorResetPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TutorResetPasswordActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Handle the back button action
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}