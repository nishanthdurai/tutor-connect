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
import com.tutorconnect.app.model.ParentTutor;
import com.tutorconnect.app.model.Tutor;

import java.util.UUID;
import java.util.regex.Pattern;

public class AddParent extends AppCompatActivity {

    EditText et_email, et_password, et_confirmPassword, et_name;
    Button btn_Register;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbReference;

    Intent intent;
    String tutorId = "";
    String studentId = "";
    String studentName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);

        intent = getIntent();
        tutorId = intent.getStringExtra("tutorId");
        studentId = intent.getStringExtra("studentId");
        studentName = intent.getStringExtra("studentName");

        setTitle("Add parent - " + tutorId);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        et_name = findViewById(R.id.parent_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirmPassword = findViewById(R.id.et_confirmPassword);
        btn_Register = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);

        dbReference = FirebaseDatabase.getInstance().getReference();

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });

    }

    private void PerformAuth() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirmPassword.getText().toString();
        String name = et_name.getText().toString();

        if (name.isEmpty()) {
            et_email.setError("Please Enter Name");
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
        } else {
            verifyEmailExistenceAndAddParent(name, email, password, tutorId, studentId);
        }
    }

    private void verifyEmailExistenceAndAddParent(String name, String email,
                                                  String password, String tutorId, String studentId) {
        progressDialog.setMessage("Adding parent....");
        progressDialog.setTitle("Adding");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String compositeKey = tutorId + "_" + studentId;

        Query emailQuery = dbReference.child("parents").orderByChild("email")
                .equalTo(email);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean compositeKeyFound = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ParentTutor parent = snapshot.getValue(ParentTutor.class);
                    if (parent != null && compositeKey.equals(parent.getCompositeKey())) {
                        compositeKeyFound = true;
                        break;
                    }
                }

                progressDialog.dismiss();
                if (compositeKeyFound) {
                    Toast.makeText(AddParent.this, "Parent with the same email exists.", Toast.LENGTH_SHORT).show();
                } else {
                    addNewParent(name, email, password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Log.e("Firebase", "Database error", databaseError.toException());
                Toast.makeText(AddParent.this, "Error adding parent, try again later..!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewParent(String name, String email, String password) {
        // create new record for user in firebase-realtime-database
        String id = UUID.randomUUID().toString();
        String compositeKey = tutorId + "_" + studentId;
        ParentTutor tutor = new ParentTutor(name, email, password, id, studentId, tutorId, compositeKey);
        dbReference.child("parents").child(id).setValue(tutor)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(AddParent.this, "Parent added successfully", Toast.LENGTH_SHORT).show();
                            sendUserToMainActivity();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(AddParent.this, "Error adding parent, try again later..!", Toast.LENGTH_SHORT).show();
                            Log.e("Firebase", "Registration failed", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddParent.this, "Error adding parent, try again later..!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(AddParent.this, ViewParent.class);
        intent.putExtra("tutorId", tutorId);
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Handle the back button action
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}