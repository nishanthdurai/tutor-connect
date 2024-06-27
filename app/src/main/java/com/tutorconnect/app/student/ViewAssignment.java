package com.tutorconnect.app.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.Model.NotesModel;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.studentViewAssignmentAdapter;

import java.util.ArrayList;

public class ViewAssignment extends AppCompatActivity {

    ArrayList<NotesModel> arrayList = new ArrayList<>();
    studentViewAssignmentAdapter adapter;
    RecyclerView rv_viewnotes;

    Intent intent;
    String childName = "";

    Button btn_markAsDone;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment2);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        intent = getIntent();
        childName = intent.getStringExtra("childName");

        rv_viewnotes = findViewById(R.id.rv_viewAssignment);
        rv_viewnotes.setHasFixedSize(true);
        rv_viewnotes.setLayoutManager(new LinearLayoutManager(ViewAssignment.this));

        btn_markAsDone= findViewById(R.id.btn_markAsDone);

        progressDialog = new ProgressDialog(ViewAssignment.this);
        btn_markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Marking Completed");
                progressDialog.setTitle("Completed...");
                progressDialog.show();
            }
        });

        loadNotes();

    }

    private void loadNotes() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("assignment");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            NotesModel model = dataSnapshot1.getValue(NotesModel.class);
                            Log.d("TAG1", "onDataChange: " + model);
                            arrayList.add(model);
                        }
                    }
                    Log.d("TAG1", "arraylist size : " + arrayList.size());
                    adapter = new studentViewAssignmentAdapter(ViewAssignment.this, arrayList, childName);
                    rv_viewnotes.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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