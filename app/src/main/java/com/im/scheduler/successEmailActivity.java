package com.im.scheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class successEmailActivity extends AppCompatActivity implements View.OnClickListener {
    private Button successLogBtn;
    private TextView ResendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_email);
        successLogBtn = (Button)findViewById(R.id.successBtn);
        ResendBtn = (TextView)findViewById(R.id.resendBtn);
        successLogBtn.setOnClickListener(this);
        ResendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.successBtn){
            Intent LogIntent = new Intent(successEmailActivity.this,LoginActivity.class);
            startActivity(LogIntent);
        }
        if(v.getId()==R.id.resendBtn){
            Intent resendIntent = new Intent(successEmailActivity.this,ResetPassword.class);
            startActivity(resendIntent);
        }
    }
}