package com.tutorconnect.app.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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

public class StudentViewNotes extends AppCompatActivity {

    ArrayList<Notes> arrayList = new ArrayList<>();
    StudentViewAssignmentAdapter adapter;
    RecyclerView rvViewNotes;

    Intent intent;
    String studentName = "";
    String studentId = "";
    String tutorId = "";

    TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_notes);

        setTitle("Student - View Notes");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rvViewNotes = findViewById(R.id.rv_viewNotes);
        tvNoData = findViewById(R.id.tv_no_data);

        rvViewNotes.setHasFixedSize(true);
        rvViewNotes.setLayoutManager(new LinearLayoutManager(StudentViewNotes.this));

        intent = getIntent();

        studentName = intent.getStringExtra("studentName");
        studentId = intent.getStringExtra("studentId");
        tutorId = intent.getStringExtra("tutorId");

        Toast.makeText(StudentViewNotes.this, "Student name: " + studentName,
                Toast.LENGTH_SHORT).show();

        loadNotes();
    }

    private void loadNotes() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        Log.d("TAG1", "inside load notes");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("notes").child(tutorId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG1", "loading notes");
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notes model = dataSnapshot.getValue(Notes.class);
                    Log.d("TAG1", "onDataChange: " + dataSnapshot);
                    arrayList.add(model);
                }
                Log.d("TAG1", "arraylist size : " + arrayList.size());

                adapter = new StudentViewAssignmentAdapter(StudentViewNotes.this,
                        arrayList, studentName, studentId, false);
                rvViewNotes.setAdapter(adapter);

                if (arrayList.isEmpty()) {
                    tvNoData.setVisibility(View.VISIBLE);
                    rvViewNotes.setVisibility(View.GONE);
                } else {
                    tvNoData.setVisibility(View.GONE);
                    rvViewNotes.setVisibility(View.VISIBLE);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading notes", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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