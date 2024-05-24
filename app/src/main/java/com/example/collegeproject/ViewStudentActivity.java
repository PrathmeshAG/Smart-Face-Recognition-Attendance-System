package com.example.collegeproject;


import static com.example.collegeproject.Common.Common.studentNode;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeproject.Adapters.ListOfStudentAdapter;
import com.example.collegeproject.Models.StudentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewStudentActivity extends AppCompatActivity {

    RecyclerView employeesRecyclerView;
    ProgressBar progressBar;
    List<StudentModel> listOfEmployeeModels;
    ListOfStudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        employeesRecyclerView = findViewById(R.id.employeesRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        listOfEmployeeModels = new ArrayList<>();
        employeesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getListOfEmployees();
    }

    private void getListOfEmployees() {
        progressBar.setVisibility(View.VISIBLE);
        employeesRecyclerView.setVisibility(View.GONE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(studentNode);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
                    StudentModel employees = caseSnapshot.getValue(StudentModel.class);
                    assert employees != null;
                    if (caseSnapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        employeesRecyclerView.setVisibility(View.VISIBLE);
                        listOfEmployeeModels.add(employees);
                        adapter = new ListOfStudentAdapter(listOfEmployeeModels, ViewStudentActivity.this);
                        employeesRecyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(ViewStudentActivity.this, "Employees not exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                employeesRecyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(ViewStudentActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
