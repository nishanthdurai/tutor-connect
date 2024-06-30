package com.tutorconnect.app.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.tutor.StudentAdapter;
import com.tutorconnect.app.model.StudentTutor;

import java.util.ArrayList;
import java.util.List;

public class TutorDashboard extends AppCompatActivity {

    ImageView iv_addUser;
    FloatingActionButton fab_addAss;

    Intent intent;

    String tutorEmail = "";
    String tutorPassword = "";
    String tutorKey = "";

    DatabaseReference dbReference;

    List<StudentTutor> mList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_dashboard);

        setTitle("Tutor");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        iv_addUser = findViewById(R.id.iv_addUser);

        recyclerView = findViewById(R.id.rv_showAllSubject);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TutorDashboard.this));

        intent = getIntent();
        tutorEmail = intent.getStringExtra("email");
        tutorPassword = intent.getStringExtra("password");
        tutorKey = intent.getStringExtra("tutorId");

        dbReference = FirebaseDatabase.getInstance().getReference();

        getAllStudent();

        iv_addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorDashboard.this, AddStudent.class);
                intent.putExtra("teacherId", tutorKey);
                intent.putExtra("email", tutorEmail);
                intent.putExtra("password", tutorPassword);
                startActivity(intent);
            }
        });
    }

    private void getAllStudent() {
        Query emailQuery = dbReference.child("students")
                .orderByChild("tutorId").equalTo(tutorKey);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StudentTutor model = dataSnapshot.getValue(StudentTutor.class);
                    Log.d("TAG", "onDataChange: " + model.getName());
                    mList.add(model);
                }
                mAdapter = new StudentAdapter(TutorDashboard.this, mList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}