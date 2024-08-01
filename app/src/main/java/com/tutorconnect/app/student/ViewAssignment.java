package com.tutorconnect.app.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.StudentViewAssignmentAdapter;
import com.tutorconnect.app.model.Notes;

import java.util.ArrayList;

public class ViewAssignment extends AppCompatActivity {

    ArrayList<Notes> arrayList = new ArrayList<>();
    StudentViewAssignmentAdapter adapter;
    RecyclerView rvViewAssignment;

    Intent intent;
    String studentName = "";
    String studentId = "";
    String tutorId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment2);

        setTitle("Student - View Assignment");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        intent = getIntent();
        studentName = intent.getStringExtra("studentName");
        studentId = intent.getStringExtra("studentId");
        tutorId = intent.getStringExtra("tutorId");

        rvViewAssignment = findViewById(R.id.rvStudentViewAssignment);
        rvViewAssignment.setHasFixedSize(true);
        rvViewAssignment.setLayoutManager(new LinearLayoutManager(ViewAssignment.this));

//        btn_markAsDone = findViewById(R.id.btnStudentAssignmentMarkAsDone);
//
//        progressDialog = new ProgressDialog(ViewAssignment.this);
//        btn_markAsDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressDialog.setMessage("Marking Completed");
//                progressDialog.setTitle("Completed...");
//                progressDialog.show();
//            }
//        });

        loadNotes();

    }

    private void loadNotes() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("assignment").child(tutorId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notes model = dataSnapshot.getValue(Notes.class);
                    Log.d("TAG1", "onDataChange: " + model);
                    arrayList.add(model);
                }
                Log.d("TAG1", "arraylist size : " + arrayList.size());
                adapter = new StudentViewAssignmentAdapter(ViewAssignment.this, arrayList,
                        studentName, studentId, true);
                rvViewAssignment.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading assignments", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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