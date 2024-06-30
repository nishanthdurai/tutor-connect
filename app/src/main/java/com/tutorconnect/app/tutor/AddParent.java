package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tutorconnect.app.R;

import java.util.regex.Pattern;

public class AddParent extends AppCompatActivity {

    EditText et_email, et_password, et_confirmPassword, et_name, et_subject, et_child;
    Button btn_Register;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    DatabaseReference dbReference;

    Intent intent;
    String tutorKey ="";
    String teacherEmail="";
    String teacherPassword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parent);

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
        et_child = findViewById(R.id.et_child);
        btn_Register = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(this);

        intent = getIntent();
        tutorKey = intent.getStringExtra("teacherId");
        teacherEmail = intent.getStringExtra("email");
        teacherPassword = intent.getStringExtra("password");

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
        String subject = et_subject.getText().toString();
        String child = et_child.getText().toString();

        if (name.isEmpty()) {
            et_email.setError("Please Enter Name");
            return;
        }  else
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
        }else if (subject.isEmpty()) {
            et_subject.setError("Please Enter Subject");
            return;
        } else {
            progressDialog.setMessage("Creating Account....");
            progressDialog.setTitle("Creating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

//            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        progressDialog.dismiss();
//
//                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                        String userId = firebaseUser.getUid();
//
//                        dbReference = FirebaseDatabase.getInstance().getReference().child(TutorSignUp.PARENTS_USER);
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        hashMap.put("id", userId);
//                        hashMap.put("username", name);
//                        hashMap.put("email", email);
//                        hashMap.put("password", password);
//                        hashMap.put("subject", subject);
//                        hashMap.put("child", child);
//
//                        dbReference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    loginTeacher(teacherEmail,teacherPassword);
//                                    sendUserToMainActivity();
//                                }
//                            }
//                        });
//                        Toast.makeText(AddParent.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                    } else {
//                        progressDialog.dismiss();
//                        Log.d("TAG", "onComplete: "+ task.getException());
//                        Toast.makeText(AddParent.this, "Registration Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }

    private void loginTeacher(String teacherEmail, String teacherPassword) {
//        mAuth.signInWithEmailAndPassword(teacherEmail, teacherPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    sendUserToMainActivity();
//                } else {
//                    progressDialog.dismiss();
//                }
//            }
//        });
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(AddParent.this, TutorDashboard.class);
        intent.putExtra("email",teacherEmail);
        intent.putExtra("password",teacherPassword);
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