package com.tutorconnect.app.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
        setContentView(R.layout.activity_view_parent);

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

        setTitle("Parent - " + studentName);

        dbReference = FirebaseDatabase.getInstance().getReference();

        getAllParents();
    }

    private void getAllParents() {

        String compositeKey = tutorId + "_" + studentId;

        Query emailQuery = dbReference.child("parents")
                .orderByChild("compositeKey").equalTo(compositeKey);

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

    private void onClickAddNewParent() {
        Intent intent = new Intent(ViewParent.this, AddParent.class);
        intent.putExtra("tutorId", tutorId);
        intent.putExtra("studentId", studentId);
        intent.putExtra("studentName", studentName);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle the back button action
            onBackPressed();
            return true;
        } else if (id == R.id.add_new_student) {
            onClickAddNewParent();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tutor_dashboard_menu, menu);
        return true;
    }
}