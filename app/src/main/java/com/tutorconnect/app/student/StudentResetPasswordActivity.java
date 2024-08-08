package com.tutorconnect.app.student;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.tutorconnect.app.model.StudentTutor;

public class StudentResetPasswordActivity extends AppCompatActivity {
    EditText et_sendEmail;
    Button btn_reset;

    DatabaseReference dbInstance;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_reset_password);

        setTitle("Student - Reset password");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        et_sendEmail = findViewById(R.id.et_sendEmail);
        btn_reset = findViewById(R.id.btn_reset);

        dbInstance = FirebaseDatabase.getInstance().getReference();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_sendEmail.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(StudentResetPasswordActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    // check if the email present.
                    // validate credentials with email and password
                    Query emailQuery = dbInstance.child("students").orderByChild("email").equalTo(email);
                    emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    StudentTutor student = snapshot.getValue(StudentTutor.class);
                                    if (student != null) {
                                        sendEmail(email,
                                                "Your login password",
                                                "Your password for logging in is: " + student.getPassword());
                                    }
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(StudentResetPasswordActivity.this, "Email is not registered, " + "try signing up..!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Log.e("Firebase", "Database error", databaseError.toException());
                            Toast.makeText(StudentResetPasswordActivity.this, "Error validating email, " + "try again later..!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("An email has been sent to the registered email address with your password.")
                .setCancelable(false)
                .setPositiveButton("Got it, proceed to login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish(); // Close the activity
                    }
                })
                .show();
    }

    private void sendEmail(String email, String subject, String message) {
        new Thread(() -> {
            boolean result = MailHandler.sendEmail(email, subject, message);
            runOnUiThread(() -> {
                progressDialog.dismiss();
                if (result) {
                    showConfirmationDialog();// close the activity and go back.
                } else {
                    Toast.makeText(StudentResetPasswordActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
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