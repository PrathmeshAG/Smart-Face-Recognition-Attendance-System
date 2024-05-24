package com.example.collegeproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.Helper.Helpers;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAsAdminActivity extends AppCompatActivity {
    EditText email, password;
    Button login;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    FirebaseAuth mAuth;
    String TAG = "kkkkkkkkk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_as_admin_layout);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

//        login.setOnClickListener(view -> validate());
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        login.setOnClickListener(view -> {
            loginUser();
        });
    }


//    private void validate() {
//        if (email.getText().toString().trim().isEmpty()) {
//            email.setError("Please enter email");
//            email.requestFocus();
//
//        } else if (password.getText().toString().trim().isEmpty()) {
//            password.setError("Please enter password");
//            password.requestFocus();
//        } else {
//            Toast.makeText(LoginAsAdminActivity.this, "invalid password", Toast.LENGTH_SHORT).show();
//        }
//    }
//    void signIn() {
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent,1000);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000)
//        {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                task.getResult(ApiException.class);
//                navigateToSecondActivity();
//            } catch (ApiException e) {
//                Toast.makeText(getApplicationContext(),"Somethimg went wrong",Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//    void navigateToSecondActivity(){
//        finish();
//        Intent intent =new Intent(LoginAsAdminActivity.this,AdminDashboardActivity.class);
//        startActivity(intent);
//    }


    private void loginUser(){
        String emails  = email.getText().toString();
        String pass =password.getText().toString();

        if (TextUtils.isEmpty(emails)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            password.setError("Email cannot be empty");
            password.requestFocus();
        }
        else {
            ProgressDialog dialog = Helpers.showProgressDialog(LoginAsAdminActivity.this, "Loading...", "Please wait");
            dialog.show();
            mAuth.signInWithEmailAndPassword(emails,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override

                public void onComplete( Task<AuthResult> task) {

                    if (task.isSuccessful()){


                        startActivity(new Intent(LoginAsAdminActivity.this,AdminDashboardActivity.class));
                        Toast.makeText(LoginAsAdminActivity.this,"user logged successful",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }
                    else
                    {

                        Toast.makeText(LoginAsAdminActivity.this,"incorrect password",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
            });
        }

    }
}


//    private void adminLogin() {
//        ProgressDialog dialog = Helpers.showProgressDialog(this, "Loading...", "Please wait");
//        dialog.show();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference(adminsNode);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot caseSnapshot : dataSnapshot.getChildren()) {
//                    AdminModel admins = caseSnapshot.getValue(AdminModel.class);
//                    assert admins != null;
//                    if (email.getText().toString().trim().toLowerCase(Locale.ROOT).equals(admins.getEmail())) {
//                        dialog.dismiss();
//                        if (email.getText().toString().toLowerCase(Locale.ROOT).matches(admins.getEmail()) && password.getText().toString().trim().equals(admins.getPassword())) {
//                            Toast.makeText(LoginAsAdminActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(LoginAsAdminActivity.this, AdminDashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                        } else {
//                            dialog.dismiss();
//                            Toast.makeText(LoginAsAdminActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        dialog.dismiss();
//                        Toast.makeText(LoginAsAdminActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                dialog.dismiss();
//                Toast.makeText(LoginAsAdminActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
