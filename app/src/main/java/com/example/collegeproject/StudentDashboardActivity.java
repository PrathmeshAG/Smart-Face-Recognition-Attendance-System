package com.example.collegeproject;

import static com.example.collegeproject.Common.Common.attendanceNode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.Common.Common;
import com.example.collegeproject.Models.AttendanceModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentDashboardActivity extends AppCompatActivity {

    private static final String TAG = "StudentDashboardActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final double TARGET_LATITUDE = 21.120185173354965; // college latitude
    private static final double TARGET_LONGITUDE = 79.12580857448197; // college longitude
    private static final float RADIUS = 50; // 50 meters

    Button checkIn, checkOut;
    boolean isIn = false;
    AttendanceModel attendanceModel;
    FusedLocationProviderClient fusedLocationClient;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (attendanceModel == null) {
                            attendanceModel = new AttendanceModel(getCurrentDateStamp(), getCurrentTimeStamp(), "", Common.currentEmployee.getId());
                        }
                        markAttendance(isIn, attendanceModel);
                        Toast.makeText(StudentDashboardActivity.this, "Face matched successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashbroad_activity);

        checkIn = findViewById(R.id.checkIn);
        checkOut = findViewById(R.id.checkOut);
        Button viewStudentDetail = findViewById(R.id.viewStudentDetails);
        TextView greeting = findViewById(R.id.greetingText), username = findViewById(R.id.username), currentDate = findViewById(R.id.currentDate);

        greeting.setText(getGreetingText());
        username.setText(Common.currentEmployee.getName());
        currentDate.setText(getCurrentDateStamp());

        viewStudentDetail.setOnClickListener(view -> startActivity(new Intent(this, ViewStudentDetails.class)));

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocationAndMarkAttendance();
        }

        getCurrentEmployeeAttendance();
    }

    private void getLocationAndMarkAttendance() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationResult = fusedLocationClient.getLastLocation();
        locationResult.addOnSuccessListener(this, location -> {
            if (location != null) {
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();
                if (isWithinLocation(currentLatitude, currentLongitude)) {
                    matchFace();
                } else {
                    Toast.makeText(StudentDashboardActivity.this, "Your location is not valid!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(StudentDashboardActivity.this, "Unable to get location!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(StudentDashboardActivity.this, "Failed to get location!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error getting location", e);
        });
    }

    private boolean isWithinLocation(double latitude, double longitude) {
        float[] results = new float[1];
        Location.distanceBetween(TARGET_LATITUDE, TARGET_LONGITUDE, latitude, longitude, results);
        return results[0] < RADIUS;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndMarkAttendance();
            } else {
                Toast.makeText(this, "Location permission is required!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentEmployeeAttendance() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(attendanceNode + "/" + getCurrentDateStamp());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.currentEmployee.getId()).exists()) {
                    attendanceModel = dataSnapshot.child(Common.currentEmployee.getId()).getValue(AttendanceModel.class);
                    if (attendanceModel != null) {
                        updateUIWithAttendance(attendanceModel);
                    }
                } else {
                    setupInitialAttendance();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentDashboardActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error fetching attendance data", error.toException());
            }
        });
    }

    private void updateUIWithAttendance(AttendanceModel attendance) {
        if (attendance.getInTime().isEmpty()) {
            checkOut.setEnabled(false);
        } else {
            checkIn.setEnabled(false);
            checkIn.setText("Last check-in: " + attendance.getInTime());
        }

        if (attendance.getOutTime().isEmpty()) {
            checkOut.setEnabled(true);
//            checkOut.setText("Last check-out: " + attendance.getOutTime());
        }
        if(!attendance.getOutTime().isEmpty()){
            checkOut.setEnabled(false);
            checkOut.setText("Last check-out: " + attendance.getOutTime());
        }

        checkIn.setOnClickListener(view -> {
            isIn = true;
            getLocationAndMarkAttendance();
        });

        checkOut.setOnClickListener(view -> {
            isIn = false;
            getLocationAndMarkAttendance();
        });
    }

    private void setupInitialAttendance() {
        AttendanceModel attendance = new AttendanceModel(getCurrentDateStamp(), getCurrentTimeStamp(), "", Common.currentEmployee.getId());
        checkIn.setOnClickListener(view -> {
            isIn = true;
            getLocationAndMarkAttendance();
        });
        checkOut.setEnabled(false);
    }

    private void matchFace() {
        Intent intent = new Intent(StudentDashboardActivity.this, MatchFace.class);
        someActivityResultLauncher.launch(intent);
    }

    private void markAttendance(boolean isIn, AttendanceModel attendance) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(attendanceNode);

        // Create a new AttendanceModel object with correct times
        String currentTime = getCurrentTimeStamp();
        String checkInTime = isIn ? currentTime : attendance.getInTime();
        String checkOutTime = isIn ? attendance.getOutTime() : currentTime;

        AttendanceModel updatedAttendance = new AttendanceModel(
                getCurrentDateStamp(),
                checkInTime,
                checkOutTime,
                Common.currentEmployee.getId()
        );

        // Save the updated attendance to the database
        myRef.child(getCurrentDateStamp()).child(Common.currentEmployee.getId())
                .setValue(updatedAttendance)
                .addOnSuccessListener(unused -> Toast.makeText(StudentDashboardActivity.this, "Attendance marked successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentDashboardActivity.this, "Failed to mark attendance!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error marking attendance", e);
                });
    }

    private String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).format(new Date());
    }

    private String getCurrentDateStamp() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(new Date());
    }

    private String getGreetingText() {
        int timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (timeOfDay < 12) {
            return "Good Morning Student";
        } else if (timeOfDay < 16) {
            return "Good Afternoon Student";
        } else {
            return "Good Evening Student";
        }
    }
}
