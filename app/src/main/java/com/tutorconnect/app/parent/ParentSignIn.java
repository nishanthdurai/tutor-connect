package com.tutorconnect.app.parent;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.model.ParentTutor;
import com.tutorconnect.app.student.ParentPasswordChangeActivity;

import java.util.regex.Pattern;

public class ParentSignIn extends AppCompatActivity {
    EditText et_email, et_password;
    Button btn_login;

    TextView tv_forgotPassword;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_signin);

        setTitle("Parent");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_forgotPassword = findViewById(R.id.tv_forgotPassword);

        dbReference = FirebaseDatabase.getInstance().getReference();

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParentSignIn.this, ParentResetPasswordActivity.class));
            }
        });

        progressDialog = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

    }

    private void performLogin() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

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
        } else {
            progressDialog.setMessage("Login in to your Account....");
            progressDialog.setTitle("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // validate credentials with email and password
            Query emailQuery = dbReference.child("parents").orderByChild("email").equalTo(email);
            emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot tutorSnapshot : dataSnapshot.getChildren()) {
                            ParentTutor parent = tutorSnapshot.getValue(ParentTutor.class);
                            if (parent != null && password.equals(parent.getPassword())) {
                                // check if password is valid
                                Toast.makeText(ParentSignIn.this, "SignIn success.", Toast.LENGTH_SHORT).show();
                                sendUserToMainActivity(parent);
                                return; // Exit once a match is found
                            }
                        }
                        Toast.makeText(ParentSignIn.this, "Invalid password..!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ParentSignIn.this, "Email is not registered..!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Log.e("Firebase", "Database error", databaseError.toException());
                    Toast.makeText(ParentSignIn.this, "Error validating credentials, " + "try again later..!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendUserToMainActivity(ParentTutor parent) {
        Class<? extends AppCompatActivity> targetActivity = parent.getRequirePasswordChange() ?
                ParentPasswordChangeActivity.class : ParentDashboard.class;

        Intent intent = new Intent(ParentSignIn.this, targetActivity);
        intent.putExtra("parentId", parent.getId());
        intent.putExtra("parentName", parent.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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