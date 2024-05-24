package com.example.collegeproject;


import static com.example.collegeproject.Common.Common.studentNode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.Common.Common;
import com.example.collegeproject.Helper.Helpers;
import com.example.collegeproject.Models.StudentModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AddStudentActivity extends AppCompatActivity {
    EditText name, address, mobile,  salaryPerMonth,
            currency, password ;
    AutoCompleteTextView designation;
    Button addEmployee;

    ImageView face;



    String candidateBase64Photo = "", candidateFaceArray = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_layout);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.mobile);
        designation = findViewById(R.id.designation);
        salaryPerMonth = findViewById(R.id.salaryPerMonth);
        currency = findViewById(R.id.currency);
        password = findViewById(R.id.password);

        addEmployee = findViewById(R.id.addEmployee);
        face = findViewById(R.id.face);


        addEmployee.setOnClickListener(view -> validate());

        face.setOnClickListener(view -> startActivityForResult(new Intent(AddStudentActivity.this, CaptureFace.class).putExtra("captureFace", true), 201));

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Information Technology");
        arrayList.add("Computer Science And Engineering");
        arrayList.add("Computer Science");
        arrayList.add("Electrical Engineering");
        arrayList.add("Electronics and Communication Engineering");
        arrayList.add("Civil Engineering");
        arrayList.add("Mechanical Engineering");
        arrayList.add("Artificial Intelligent");
        arrayList.add("Artificial Intelligent And Data Science");
        arrayList.add("Data Science");
        arrayList.add("Chemical Engineering");
        arrayList.add("Aeronautical Engineering");


        ArrayAdapter<String> arrayAdapter = new
                ArrayAdapter<String>(AddStudentActivity.this, android.R.layout.simple_list_item_1, arrayList);
        designation.setAdapter(arrayAdapter);
        designation.setThreshold(1);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == RESULT_OK && data != null ) {
            candidateFaceArray = data.getStringExtra("candidateFaceArray");
            candidateBase64Photo = "data:image/jpeg;base64," + Common.imageBase64;
//            imageUri = data.getData();
//            face.setImageURI(imageUri);
            byte[] decodedString = Base64.decode(Common.imageBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            face.setImageBitmap(decodedByte);

        }
    }



// validate to Students details
    private void validate()
    {
                if (name.getText().toString().trim().isEmpty()) {
                    name.setError("Required!");
                    name.requestFocus();
                } else if (address.getText().toString().trim().isEmpty()) {
                    address.setError("Required!");
                    address.requestFocus();
                } else if (mobile.getText().toString().trim().isEmpty()) {
                    mobile.setError("Required!");
                    mobile.requestFocus();
                } else if (address.getText().toString().trim().isEmpty()) {
                    address.setError("Required!");
                    address.requestFocus();
                } else if (designation.getText().toString().trim().isEmpty()) {
                    designation.setError("Required!");
                    designation.requestFocus();
                } else if (salaryPerMonth.getText().toString().trim().isEmpty()) {
                    salaryPerMonth.setError("Required!");
                    salaryPerMonth.requestFocus();
                } else if (currency.getText().toString().trim().isEmpty()) {
                    currency.setError("Required!");
                    currency.requestFocus();
                } else if (password.getText().toString().trim().isEmpty()) {
                    password.setError("Required");
                    password.requestFocus();
                }else if(Objects.equals(candidateFaceArray, "")) {
                    Toast.makeText(this, "Please select your photo", Toast.LENGTH_SHORT).show();

                }else {
                    addEmployee();
                }


        }



    private void addEmployee() {

        ProgressDialog dialog = Helpers.showProgressDialog(this, "Loading...", "Please wait");
        dialog.show();
        String id = UUID.randomUUID().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(studentNode);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("image/").child(id).putBytes(Base64.decode(Common.imageBase64, Base64.DEFAULT)).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {

            StudentModel employeeModel = new StudentModel(
                    name.getText().toString().trim(), address.getText().toString().trim(), mobile.getText().toString().trim(),
                    designation.getText().toString().trim(), currency.getText().toString().trim(), uri.toString(), candidateFaceArray,
                    id, password.getText().toString().trim(), Double.parseDouble(salaryPerMonth.getText().toString().trim())
            );

            myRef.child(id).
                    setValue(employeeModel).addOnSuccessListener(unused -> {
                        dialog.dismiss();
                        Toast.makeText(AddStudentActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                        finish();
                    }).addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(AddStudentActivity.this, "Some error occurred: " + e, Toast.LENGTH_SHORT).show();
                    });


        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(AddStudentActivity.this, "Some error occurred: " + e, Toast.LENGTH_SHORT).show();
        }));


    }



//    public void clearFields() {
//        name.setText("");
//        salaryPerMonth.setText("");
//        designation.setText("");
//        address.setText("");
//        password.setText("");
//        currency.setText("");
//        mobile.setText("");
//        face.setImageResource(android.R.color.transparent);
//        imageUri = null;
//    }
}

// create by Acoustic Guitarist


