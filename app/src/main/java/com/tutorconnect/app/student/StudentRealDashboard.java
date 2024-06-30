package com.tutorconnect.app.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tutorconnect.app.R;

public class StudentRealDashboard extends AppCompatActivity {

    CardView btn_viewAssignment;
    CardView btn_viewNotes;
    CardView btn_viewRemarks;

    Intent intent;
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_real_dashboard);

        setTitle("Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btn_viewAssignment = findViewById(R.id.cv_viewAssignment);
        btn_viewNotes = findViewById(R.id.cv_viewNotes);
        btn_viewRemarks = findViewById(R.id.cv_viewRemarks);

        intent = getIntent();
        name = intent.getStringExtra("StudentSignin");

        btn_viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, StudentViewNotes.class);
                intent.putExtra("studentName", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btn_viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, ViewAssignment.class);
                intent.putExtra("studentName", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btn_viewRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentRealDashboard.this, StudentDashboard.class);
                intent.putExtra("studentName", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
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