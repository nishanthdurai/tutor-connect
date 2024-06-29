package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.model.Tutor;

import java.util.UUID;
import java.util.regex.Pattern;

public class TutorSignUp extends AppCompatActivity {

    public static final String TUTOR_USERS = "TutorUsers";
    public static final String STUDENTS_USER = "StudentUsers";
    public static final String PARENTS_USER = "ParentsUsers";
    public static final String ATTENDANCE = "Attendance";
    public static final String REMARKS = "Remarks";

    EditText et_email, et_password, et_confirmPassword;
    Button btn_Register;
    TextView tv_loginBtn;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up);

        setTitle("Tutor");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        btn_Register = findViewById(R.id.btn_register);
        tv_loginBtn = findViewById(R.id.tv_loginButton);

        progressDialog = new ProgressDialog(this);

        dbInstance = FirebaseDatabase.getInstance().getReference();

        tv_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorSignUp.this, TutorSignIn.class));
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });

    }

    private void verifyEmailExistence(String email, String password) {
        progressDialog.setMessage("Creating your Account....");
        progressDialog.setTitle("Creating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Query emailQuery = dbInstance.child("tutors").orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(TutorSignUp.this, "Email already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    // create new record for user in firebase-realtime-database
                    String key = UUID.randomUUID().toString();
                    Tutor tutor = new Tutor(email, password, key);
                    dbInstance.child("tutors").child(key).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(TutorSignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TutorSignUp.this, TutorSignIn.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(TutorSignUp.this, "Error registering user, try again later..!", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Registration failed", task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(TutorSignUp.this, "Error registering user, try again later..!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("Firebase", "Database error", databaseError.toException());
                Toast.makeText(TutorSignUp.this, "Error registering user, try again later..!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PerformAuth() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();

        if (email.isEmpty()) {
            et_email.setError("Please Enter Email");
            return;
        } else if (!pat.matcher(email).matches()) {
            et_email.setError("Please Enter a valid Email");
            return;
        } else if (password.isEmpty()) {
            et_password.setError("Please input Password");
            return;
        } else if (password.length() < 6) {
            et_password.setError("Password too short");
            return;
        } else if (!confirmPassword.equals(password)) {
            et_confirmPassword.setError("Password doesn't matches");
            return;
        } else {
            verifyEmailExistence(email, password);
        }
    }

    private void sendUserToMainActivity(String userId, String email, String password) {
        Intent intent = new Intent(TutorSignUp.this, TutorRealDashboard.class);
        intent.putExtra("email", email);
        intent.putExtra("userId", email);
        intent.putExtra("password", password);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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