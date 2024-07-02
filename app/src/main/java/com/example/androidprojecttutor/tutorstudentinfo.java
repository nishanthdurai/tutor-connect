package com.example.androidprojecttutor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tutorstudentinfo extends AppCompatActivity {


    Intent intent;

    String tutorEmail = "";
    String tutorPassword = "";
    String tutorId = "";

    DatabaseReference dbReference;

    List<studenttutor> mList = new ArrayList<>();
    RecyclerView recyclerView;
    StudentAdapter mAdapter;


    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorstudentinfo);


        setTitle("Tutor Dashboard");

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(tutorstudentinfo.this));


        intent = getIntent();
        tutorEmail = intent.getStringExtra("email");
        tutorPassword = intent.getStringExtra("password");
        tutorId = intent.getStringExtra("tutorId");

        dbReference = FirebaseDatabase.getInstance().getReference();
    }


    private void onClickAddNewStudent() {
        Intent intent = new Intent(tutorstudentinfo.this, addstudent.class);
        intent.putExtra("tutorId", tutorId);
        intent.putExtra("email", tutorEmail);
        intent.putExtra("password", tutorPassword);
        startActivity(intent);
    }


}