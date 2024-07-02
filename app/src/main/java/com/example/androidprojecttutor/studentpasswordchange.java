package com.example.androidprojecttutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class studentpasswordchange extends AppCompatActivity {

    EditText et_confirm_password, et_password;
    Button btn_change_password;

    DatabaseReference dbReference;

    ProgressDialog progressDialog;

    Intent intent;

    String studentName = "";
    String studentId = "";
    String tutorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_studentpasswordchange);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        intent = getIntent();
        studentName = intent.getStringExtra("studentName");
        studentId = intent.getStringExtra("studentId");
        tutorId = intent.getStringExtra("tutorId");

        setTitle("Welcome " + studentName);

        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_password = findViewById(R.id.et_password);
        btn_change_password = findViewById(R.id.btn_change);

        dbReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performPasswordChange();
            }
        });

    }

    private void performPasswordChange() {
        String password = et_password.getText().toString();
        String confirmPassword = et_confirm_password.getText().toString();

        if (password.isEmpty()) {
            et_password.setError("Please Enter Password");
            return;
        } else if (confirmPassword.isEmpty()) {
            et_confirm_password.setError("Please Confirm Password");
            return;
        } else if (password.length() < 6) {
            et_password.setError("Password too short");
            return;
        } else if (!password.equals(confirmPassword)) {
            et_password.setError("Password mismatch");
            et_confirm_password.setError("Password mismatch");
            return;
        } else {
            progressDialog.setMessage("Validating credentials....");
            progressDialog.setTitle("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // update password and mark require password change to false
            // Create a map of the new values
            Map<String, Object> userValues = new HashMap<>();
            userValues.put("password", password);
            userValues.put("requirePasswordChange", false);

            dbReference.child("students").child(studentId).updateChildren(userValues)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Update successful
                            Toast.makeText(studentpasswordchange.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                            onPasswordChangedSuccessfully();
                        } else {
                            // Update failed
                            Toast.makeText(studentpasswordchange.this, "Failed to change password, try again later", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(studentpasswordchange.this, "Failed to change password, try again later", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void onPasswordChangedSuccessfully() {
        Intent intent = new Intent(studentpasswordchange.this, Studentdashboard.class);
        intent.putExtra("studentName", studentName);
        intent.putExtra("studentId", studentId);
        intent.putExtra("tutorId", tutorId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
