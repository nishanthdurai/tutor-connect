package com.tutorconnect.app.tutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tutorconnect.app.R;

public class AddUser extends AppCompatActivity {

    Button btn_addStudent;
    Button btn_addParent;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Enable the Up button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btn_addParent = findViewById(R.id.btn_addParent);
        btn_addStudent = findViewById(R.id.btn_addStudent);

        intent = getIntent();
        String userId = intent.getStringExtra("teacherId");
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        btn_addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUser.this, AddStudent.class);
                intent.putExtra("teacherId", userId);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });
        btn_addParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUser.this, AddParent.class);
                intent.putExtra("teacherId", userId);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
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