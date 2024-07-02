//package com.example.androidprojecttutor;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class tutorstudentinfo extends AppCompatActivity {
//
//    Intent intent;
//
//    String tutorEmail = "";
//    String tutorPassword = "";
//    String tutorId = "";
//
//    DatabaseReference dbReference;
//
//    List<studenttutor> mList = new ArrayList<>();
//    RecyclerView recyclerView;
//    StudentAdapter mAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tutorstudentinfo);
//
//        setTitle("Tutor Dashboard");
//
//        // Enable the Up button
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//
//        recyclerView = findViewById(R.id.rv_showAllStudents);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(tutorstudentinfo.this));
//
//        intent = getIntent();
//        tutorEmail = intent.getStringExtra("email");
//        tutorPassword = intent.getStringExtra("password");
//        tutorId = intent.getStringExtra("tutorId");
//
//        dbReference = FirebaseDatabase.getInstance().getReference();
//
//        getAllStudent();
//    }
//
//    private void onClickAddNewStudent() {
//        Intent intent = new Intent(tutorstudentinfo.this, addstudent.class);
//        intent.putExtra("tutorId", tutorId);
//        intent.putExtra("email", tutorEmail);
//        intent.putExtra("password", tutorPassword);
//        startActivity(intent);
//    }
//
//    private void getAllStudent() {
//        Query emailQuery = dbReference.child("students")
//                .orderByChild("tutorId").equalTo(tutorId);
//
//        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    studenttutor model = dataSnapshot.getValue(studenttutor.class);
//                    Log.d("TAG", "onDataChange: " + model.getName());
//                    mList.add(model);
//                }
//                mAdapter = new StudentAdapter(tutorstudentinfo.this, mList, tutorEmail, tutorId);
//                recyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            // Handle the back button action
//            onBackPressed();
//            return true;
//        } else if (id == R.id.add_new_student) {
//            onClickAddNewStudent();
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.adduser, menu);
//        return true;
//    }
//}