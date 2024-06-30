package com.tutorconnect.app.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.tutor.ParentAdapter;
import com.tutorconnect.app.model.ParentTutor;

import java.util.ArrayList;
import java.util.List;

public class ViewParent extends AppCompatActivity {

    Intent intent;

    String tutorId = "";
    String studentId = "";
    String studentName = "";

    DatabaseReference dbReference;

    List<ParentTutor> parentList = new ArrayList<>();
    RecyclerView recyclerView;
    ParentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.view_parents);

        setTitle("Parent - " + studentName);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.rv_showAllParents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewParent.this));

        intent = getIntent();
        tutorId = intent.getStringExtra("tutorId");
        studentId = intent.getStringExtra("studentId");
        studentName = intent.getStringExtra("studentName");

        dbReference = FirebaseDatabase.getInstance().getReference();

        getAllParents();
    }

    private void getAllParents() {
        Query emailQuery = dbReference.child("parents")
                .orderByChild("studentId").equalTo(studentId);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ParentTutor model = dataSnapshot.getValue(ParentTutor.class);
                    Log.d("TAG", "onDataChange: " + model.getName());
                    parentList.add(model);
                }
                mAdapter = new ParentAdapter(ViewParent.this, parentList, tutorId);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}