package com.example.androidprojecttutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class tutordashboard extends AppCompatActivity {

    CardView btn_viewStudents;


    Intent intent;

    String tutorId, email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutordashboard);


        setTitle("Tutor");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btn_viewStudents = findViewById(R.id.cv_viewStudents);


        intent = getIntent();
        email = intent.getStringExtra("email");
        tutorId = intent.getStringExtra("tutorId");
        password = intent.getStringExtra("password");

        btn_viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutordashboard.this, tutorstudentinfo.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("tutorId", tutorId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });








    }
}