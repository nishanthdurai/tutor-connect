package com.tutorconnect.app.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tutorconnect.app.R;
import com.tutorconnect.app.adapter.tutor.ParentAdapter;
import com.tutorconnect.app.model.ParentTutor;

import java.util.ArrayList;
import java.util.List;

public class ViewParent extends AppCompatActivity implements ParentAdapter.OnParentActionListener {

    Intent intent;

    String tutorId = "";
    String studentId = "";
    String studentName = "";

    DatabaseReference dbReference;

    List<ParentTutor> parentList = new ArrayList<>();
    List<DataSnapshot> parentSnapshots = new ArrayList<>();

    RecyclerView recyclerView;
    ParentAdapter mAdapter;
    TextView tvNoData;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parent);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        recyclerView = findViewById(R.id.rv_showAllParents);
        tvNoData = findViewById(R.id.tv_no_data);

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
        progressDialog.show();

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
                    parentSnapshots.add(dataSnapshot);
                }
                mAdapter = new ParentAdapter(ViewParent.this, parentList, ViewParent.this);
                recyclerView.setAdapter(mAdapter);

                toggleEmptyView();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error loading parent", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void toggleEmptyView() {
        if (parentList.isEmpty()) {
            tvNoData.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRemoveParent(int position) {
        progressDialog.show();

        parentSnapshots.get(position).getRef().removeValue()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        mAdapter.removeItem(position);
                        Toast.makeText(getApplicationContext(), "Parent removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error removing parent", Toast.LENGTH_SHORT).show();
                    }
                    toggleEmptyView();
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
