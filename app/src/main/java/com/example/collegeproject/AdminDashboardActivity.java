package com.example.collegeproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashbroad_layout);
        Button addEmployee = findViewById(R.id.addEmployee);
        TextView greetingmsg = findViewById(R.id.greetingText1);
        TextView currentDate = findViewById(R.id.currentDate1);
        Button viewEmployees = findViewById(R.id.viewEmployees);
        greetingmsg.setText(greetingText());
        currentDate.setText(getCurrentDateStamp());
        addEmployee.setOnClickListener(view -> startActivity(new Intent(AdminDashboardActivity.this, AddStudentActivity.class)));
        viewEmployees.setOnClickListener(view -> startActivity(new Intent(AdminDashboardActivity.this, ViewStudentActivity.class)));
    }

    private String getCurrentDateStamp() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private String greetingText() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay < 12) {
            return "Good Morning";
        } else if (timeOfDay < 16) {
            return "Good Afternoon";
        } else if (timeOfDay < 21) {
            return "Good Evening";
        } else {
            return "Good Night";
        }
    }
}
