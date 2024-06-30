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
import com.tutorconnect.app.model.StudentTutor;

import java.util.UUID;
import java.util.regex.Pattern;

public class AddStudent extends AppCompatActivity {

    EditText et_email, et_password, et_confirmPassword, et_name, et_subject;
    Button btn_Register;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbInstance;

    Intent intent;
    String tutorId = "";
    String tutorEmail = "";
    String tutorPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        setTitle("Tutor");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        et_name = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_subject = findViewById(R.id.et_subject);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        btn_Register = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);

        intent = getIntent();
        tutorId = intent.getStringExtra("teacherId");
        tutorEmail = intent.getStringExtra("email");
        tutorPassword = intent.getStringExtra("password");

        dbInstance = FirebaseDatabase.getInstance().getReference();

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });
    }

    private void validateEmailExistenceAndAddStudent(String name, String email, String password, String subject) {
        progressDialog.setMessage("Adding Student....");
        progressDialog.setTitle("Adding");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Query emailQuery = dbInstance.child("students").orderByChild("email").equalTo(email);
        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(AddStudent.this, "Student with the same email already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    // create new record for user in firebase-realtime-database
                    String key = UUID.randomUUID().toString();
                    StudentTutor tutor = new StudentTutor(name, subject, email, password, key, tutorId);
                    dbInstance.child("students").child(key).setValue(tutor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(AddStudent.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                                sendUserToMainActivity();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddStudent.this, "Error adding student, try again later..!", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Registration failed", task.getException());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddStudent.this, "Error adding student, try again later..!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("Firebase", "Database error", databaseError.toException());
                Toast.makeText(AddStudent.this, "Error Adding student, try again later..!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PerformAuth() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();
        String name = et_name.getText().toString();
        String subject = et_subject.getText().toString();

        if (name.isEmpty()) {
            et_subject.setError("Please Enter Name");
            return;
        } else if (email.isEmpty()) {
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
        } else if (subject.isEmpty()) {
            et_subject.setError("Please Enter Subject");
            return;
        } else {
            validateEmailExistenceAndAddStudent(name, email, password, subject);

//            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        progressDialog.dismiss();
//
//                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                        String userId = firebaseUser.getUid();
//
//                        reference = FirebaseDatabase.getInstance().getReference().child(TutorSignUp.STUDENTS_USER).child(teacherId);
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        hashMap.put("id", userId);
//                        hashMap.put("studentName", username);
//                        hashMap.put("email", email);
//                        hashMap.put("password", password);
//                        hashMap.put("studentSubject", subject);
//
//                        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    loginTeacher(teacherEmail,teacherPassword);
//                                    sendUserToMainActivity();
//                                }
//                            }
//                        });
//                        Toast.makeText(AddStudent.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressDialog.dismiss();
//                        Log.d("TAG", "onComplete: "+ task.getException());
//                        Toast.makeText(AddStudent.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }

/*
    private void loginTeacher(String teacherEmail, String teacherPassword) {
        mAuth.signInWithEmailAndPassword(teacherEmail, teacherPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendUserToMainActivity();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }
*/

    private void sendUserToMainActivity() {
        Intent intent = new Intent(AddStudent.this, TutorDashboard.class);
        intent.putExtra("email", tutorEmail);
        intent.putExtra("password", tutorPassword);
        intent.putExtra("tutorId", tutorId);
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