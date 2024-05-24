package com.example.collegeproject;

import static com.example.collegeproject.Common.Common.studentNode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.Common.Common;
import com.example.collegeproject.Helper.Helpers;
import com.example.collegeproject.Models.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

public class LoginAsStudentActivity extends AppCompatActivity {
    EditText mobile, password;
    Button login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_as_student_layout);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);


        login.setOnClickListener(view -> validate());
    }

    private void validate() {
        if (mobile.getText().toString().trim().isEmpty()) {
            mobile.setError("Please enter mobile");
            mobile.requestFocus();
        } else if (password.getText().toString().trim().isEmpty()) {
            password.setError("Please enter password");
            password.requestFocus();
        } else {
            employeeLogin();
        }
    }
    private void  employeeLogin(){
            ProgressDialog dialog = Helpers.showProgressDialog(this, "Loading...", "Please wait");
            dialog.show();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(studentNode);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
                    StudentModel employees = caseSnapshot.getValue(StudentModel.class);

                    if (mobile.getText().toString().trim().equals(employees.getMobileNumber())) {
                        dialog.dismiss();
                        if (mobile.getText().toString().trim().matches(employees.getMobileNumber()) && password.getText().toString().trim().equals(employees.getPassword())) {
                            Toast.makeText(LoginAsStudentActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                            Common.currentEmployee = employees;

                            SharedPreferences preferences = getSharedPreferences("SharedPreferencesName", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putString("face", (employees.getFaceData().equals("null") ? "{}" : employees.getFaceData()));

                            editor.putBoolean("IsLogged", true);
                            editor.apply();

                            startActivity(new Intent(LoginAsStudentActivity.this, StudentDashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginAsStudentActivity.this, "Invalid  credentials!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("kkkkkkkkkkk", "onDataChange: else");
                        dialog.dismiss();
//                        Toast.makeText(LoginAsStudentActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(LoginAsStudentActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    }
