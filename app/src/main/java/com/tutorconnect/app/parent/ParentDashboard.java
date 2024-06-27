package com.tutorconnect.app.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
import com.tutorconnect.app.Model.parent.StudentModel;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.parent.StudentAdapter;
import com.tutorconnect.app.tutor.TutorSignUp;

import java.util.ArrayList;
import java.util.List;

public class ParentDashboard extends AppCompatActivity {

    List<StudentModel> mList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter mAdapter;
    Intent intent;
    String childName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.rv_showAllAttendance);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        intent = getIntent();
        childName = intent.getStringExtra("childName");

        getAllAttendance();

    }

    private void getAllAttendance() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(TutorSignUp.ATTENDANCE).child(childName);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StudentModel model = dataSnapshot.getValue(StudentModel.class);
                        mList.add(model);
                    }
                    mAdapter = new StudentAdapter(ParentDashboard.this, mList);
                    recyclerView.setAdapter(mAdapter);
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