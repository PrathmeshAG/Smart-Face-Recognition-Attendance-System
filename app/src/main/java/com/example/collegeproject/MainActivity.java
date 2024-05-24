package com.example.collegeproject;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {

    Button loginAsStudentButton;
    Button loginAsAdminButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginAsStudentButton = findViewById(R.id.loginAsStudentButton);
        loginAsAdminButton = findViewById(R.id.loginAsAdminButton);

        loginAsAdminButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginAsAdminActivity.class)));
        loginAsStudentButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,LoginAsStudentActivity.class)));
    }
}