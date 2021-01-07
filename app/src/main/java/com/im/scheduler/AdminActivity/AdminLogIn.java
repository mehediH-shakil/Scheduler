package com.im.scheduler.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.FirebaseError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.im.scheduler.LoginActivity;
import com.im.scheduler.R;
import com.im.scheduler.studentDeshboard;
import com.im.scheduler.teacherDeshboardActivity;

public class AdminLogIn extends AppCompatActivity implements View.OnClickListener {

    private Button LogInBtn;
    private EditText editEmailID, editPassword;
    private FirebaseAuth mAuth;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        editEmailID = (EditText) findViewById(R.id.AdminEmailLogbtn);
        editPassword = (EditText) findViewById(R.id.AdminPasswordLog);
        LogInBtn = (Button) findViewById(R.id.AdminLoginbtn);
        LogInBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAdView = (AdView)findViewById(R.id.adViewAdmin);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View view) {
        final String email = editEmailID.getText().toString().trim();
        final String password = editPassword.getText().toString().trim();
        String Path = email.replaceAll("[^a-zA-Z0-9]","_");
        final String myPath = Path;

        if (email.isEmpty()) {
            editEmailID.setError("Enter an email address");
            editPassword.requestFocus();
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
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admin Panel")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String status = snapshot.child("status").getValue().toString();
                                if(status.equals("admin")){
                                    Intent TeacherIntent = new Intent(getApplicationContext(), AdminHomeActivity.class);
                                    startActivity(TeacherIntent);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(editEmailID.getContext());
                                    builder.setTitle("Log in Permission");
                                    builder.setMessage("Sorry, please contact authority for permission.");
                                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.show();
                                    Toast.makeText(getApplicationContext(), "You are not Admin!", Toast.LENGTH_SHORT).show();
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
}