package com.tutorconnect.app.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tutorconnect.app.R;

public class TutorRealDashboard extends AppCompatActivity {

    CardView btn_viewStudents;
    CardView btn_viewAssignment;
    CardView btn_viewNotes;
    CardView btn_uploadNotes;
    CardView btn_uploadAssignment;

    Intent intent;

    String tutorId, email, password;

    private boolean doubleBackToExitPressedOnce = false;
    private Handler handler = new Handler();
    private Runnable resetDoubleBackToExitPressedOnce = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_real_dashboard);

        setTitle("Tutor");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btn_viewStudents = findViewById(R.id.cv_viewStudents);
        btn_viewAssignment = findViewById(R.id.cv_viewAssignment);
        btn_viewNotes = findViewById(R.id.cv_viewNotes);
        btn_uploadNotes = findViewById(R.id.cv_uploadNotes);
        btn_uploadAssignment = findViewById(R.id.cv_uploadAssignment);

        intent = getIntent();
        email = intent.getStringExtra("email");
        tutorId = intent.getStringExtra("tutorId");
        password = intent.getStringExtra("password");

        btn_viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, TutorDashboard.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("tutorId", tutorId);
                startActivity(intent);
            }
        });

        btn_viewAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, ViewAssignment.class);
                intent.putExtra("tutorId", tutorId);
                startActivity(intent);
            }
        });

        btn_uploadNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, AddNotes.class);
                intent.putExtra("tutorId", tutorId);
                startActivity(intent);
            }
        });
        btn_uploadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, UploadAssignment.class);
                intent.putExtra("userId", tutorId);
                startActivity(intent);
            }
        });
        btn_viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorRealDashboard.this, ViewNotes.class);
                intent.putExtra("userId", tutorId);
                startActivity(intent);
            }
        });
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        handler.postDelayed(resetDoubleBackToExitPressedOnce, 2000); // 2 seconds delay to reset
    }
}