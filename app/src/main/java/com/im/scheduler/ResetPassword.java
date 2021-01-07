package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    private Button forgetBtn;
    private EditText emailText;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        forgetBtn =(Button)findViewById(R.id.sendLinkBtn);
        forgetBtn.setOnClickListener(this);
        emailText = (EditText)findViewById(R.id.editForgetEmailBar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        final String Email = emailText.getText().toString().trim();
        if(Email.isEmpty()){
            emailText.setError("Email is required!");
            emailText.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            emailText.setError("Please provide valid email!");
            emailText.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                        Intent intent = new Intent(ResetPassword.this,successEmailActivity.class);
                        startActivity(intent);
                }else{
                    Toast.makeText(ResetPassword.this,"Sorry, Please complete you registration!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}