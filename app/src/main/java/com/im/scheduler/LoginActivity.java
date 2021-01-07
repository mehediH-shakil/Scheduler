package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.im.scheduler.AdminActivity.AdminDeshboard;
import com.im.scheduler.AdminActivity.AdminHomeActivity;
import com.im.scheduler.AdminActivity.AdminLogIn;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Sign Up
    private Button TeacherBtn, StudentBtn;
    private TextView display;
    private TextView SignUp;
    private TextView admin;
    int counter = 0, counter1 = 0;

    //Log in
    private Button LogInBtn;
    private EditText editEmailID, editPassword;
    private FirebaseAuth mAuth;

    //Forget Password
    private TextView forgetPassBtn;

    //Data Passing
    private static String out;
    private Bundle results;

    //shearedPreferences
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Boolean saveLogin;

    //Progressbar
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Sign Up
        SignUp = (TextView) findViewById(R.id.signUpbtn2);
        TeacherBtn = (Button) findViewById(R.id.teacherBtn);
        StudentBtn = (Button) findViewById(R.id.studentBtn);
        display = (TextView) findViewById(R.id.editText);

        SignUp.setOnClickListener(this);
        TeacherBtn.setOnClickListener(this);
        StudentBtn.setOnClickListener(this);

        //Log in
        editEmailID = (EditText) findViewById(R.id.EmailLogbtn);
        editPassword = (EditText) findViewById(R.id.PasswordLog);
        LogInBtn = (Button) findViewById(R.id.Loginbtn);
        LogInBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        //Forget Password
        forgetPassBtn = (TextView)findViewById(R.id.forgotbtn);
        forgetPassBtn.setOnClickListener(this);

        //admin
        admin = (TextView)findViewById(R.id.AdminLogBtn);
        admin.setOnClickListener(this);

        //ProgressBar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

    }
    @Override
    public void onClick(View v) {

        //Teacher
        if (v.getId() == R.id.teacherBtn) {
            counter++;
            if (counter == 1) counter1 = 0;
            if (counter % 2 == 0) {
                display.setText("");
            }
            if (counter % 2 != 0) {
                display.setText("Hello Teacher!");
            }
        }
        //Student
        if (v.getId() == R.id.studentBtn) {
            counter1++;
            if (counter1 == 1) counter = 0;
            if (counter1 % 2 == 0) {
                display.setText("");
            }
            if (counter1 % 2 != 0) {
                display.setText("Hello Student!");
            }
        }
        //SignUp
        if (v.getId() == R.id.signUpbtn2) {
            String cheakUser = display.getText().toString();
            if (cheakUser == "Hello Teacher!") {
                Intent intent = new Intent(getApplicationContext(), TeacherRegistration.class);
                startActivity(intent);
            }
            if (cheakUser == "Hello Student!") {
                Intent intent = new Intent(getApplicationContext(), studentRegistration.class);
                startActivity(intent);
            }
            if (cheakUser == "") {
                Toast.makeText(getApplicationContext(), "Please select user type", Toast.LENGTH_SHORT).show();
            }

        }
        //LogIn
        if (v.getId() == R.id.Loginbtn) {
            userLogin();
        }
        //ForgetPassword
        if(v.getId() == R.id.forgotbtn){
            Intent forgetIntent = new Intent(LoginActivity.this,ResetPassword.class);
            startActivity(forgetIntent);
        }
        //admin
        if(v.getId() == R.id.AdminLogBtn){
            Intent adminIntent = new Intent(LoginActivity.this, AdminLogIn.class);
            startActivity(adminIntent);
        }
    }

    private void userLogin(){
        final String email = editEmailID.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        String Path = email.replaceAll("[^a-zA-Z0-9]","_");
        final String myPath = Path;

        if (email.isEmpty()) {
            editEmailID.setError("Enter an email address");
            editEmailID.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailID.setError("Email is required!");
            editEmailID.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editPassword.setError("Enter a password");
            editPassword.requestFocus();
            return;
        }
        if (password.length() <= 5) {
            editPassword.setError("Min password length should be 6 characters!");
            editPassword.requestFocus();
            return;
        }

        final String android_id = Settings.Secure.getString(display.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            String myString = display.getText().toString();
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admin Panel")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String user = snapshot.child("user").getValue().toString();
                            if(mAuth.getCurrentUser().isEmailVerified()){

                                //Device ID store
                                FirebaseDatabase.getInstance().getReference("Login Panel")
                                        .child(android_id)
                                        .child("user")
                                        .setValue(user+"ON").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                        }else{
                                        }
                                    }
                                });

                                if(user.equals("student")){
                                    if(myString.equals("Hello Student!") || myString.equals("")){
                                        Intent StudentIntent = new Intent(getApplicationContext(),studentDeshboard.class);
                                        startActivity(StudentIntent);
                                    }else if(myString.equals("Hello Teacher!")){
                                        display.setText(R.string.wrong_selection);
                                        Toast.makeText(getApplicationContext(), "Sorry, you are not a Teacher!", Toast.LENGTH_SHORT).show();
                                    }

                                }else if(user.equals("teacher")){
                                    if(myString.equals("Hello Teacher!") || myString.equals("")) {
                                        Intent TeacherIntent = new Intent(getApplicationContext(), teacherDeshboardActivity.class);
                                        TeacherIntent.putExtra("myPath", myPath);
                                        FirebaseDatabase.getInstance().getReference("Email Path")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("Path")
                                                .setValue(myPath);
                                        startActivity(TeacherIntent);
                                    }else if(myString.equals("Hello Student!")){
                                        display.setText(R.string.wrong_selection);
                                        Toast.makeText(getApplicationContext(), "Sorry, you are not a Student!", Toast.LENGTH_SHORT).show();
                                    }
                                }else if(user.equals("none")&&!myString.equals("Hello Student!")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(display.getContext());
                                    builder.setTitle("Log in Permission");
                                    builder.setMessage("Sorry, please contact authority for permission.");
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.show();
                                }else if(user.equals("none")&&myString.equals("Hello Student!")){
                                    Toast.makeText(getApplicationContext(), "Sorry, you are not a Teacher!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Sorry, Please complete you registration!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(display.getContext());
                                builder.setTitle("E-mail Verification");
                                builder.setMessage("Sorry, Please verify your Email Address.");
                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private static final int TIME_INTERVAL = 2000;
    private long backPressed;

    @Override
    public void onBackPressed() {
        if(backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            finish();
            moveTaskToBack(true);
        }
        backPressed = System.currentTimeMillis();
    }
}
