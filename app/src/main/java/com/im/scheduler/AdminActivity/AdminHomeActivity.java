package com.im.scheduler.AdminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.im.scheduler.LoginActivity;
import com.im.scheduler.R;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button adminClassBtn,adminExamBtn,adminLogOutBtn55,adminTeacherInfoBtn,adminAddRoomBth;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        adminClassBtn = (Button) findViewById(R.id.adminClassScheduleBtn);
        adminExamBtn = (Button) findViewById(R.id.adminAssignmentBtn);
        adminLogOutBtn55 = (Button) findViewById(R.id.adminLogOutBtn);
        adminTeacherInfoBtn = (Button) findViewById(R.id.adminTeacherInformationBtn);
        adminAddRoomBth = (Button) findViewById(R.id.adminStudentInformationBtn);

        adminClassBtn.setOnClickListener(this);
        adminExamBtn.setOnClickListener(this);
        adminLogOutBtn55.setOnClickListener(this);
        adminTeacherInfoBtn.setOnClickListener(this);
        adminAddRoomBth.setOnClickListener(this);

        mAdView = (AdView)findViewById(R.id.adView11);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.adminClassScheduleBtn){
            Intent classIntent = new Intent(AdminHomeActivity.this,AdminDeshboard.class);
            startActivity(classIntent);
        }
        if(v.getId()==R.id.adminAssignmentBtn){
            Intent examIntent = new Intent(AdminHomeActivity.this,AdminAssignmentOrExam.class);
            startActivity(examIntent);
        }
        if(v.getId()==R.id.adminLogOutBtn){
            Intent logIntent = new Intent(AdminHomeActivity.this, LoginActivity.class);
            startActivity(logIntent);
        }
        if(v.getId()==R.id.adminTeacherInformationBtn){
            Intent teacherInfoIntent = new Intent(AdminHomeActivity.this, teacherInformationForAdminPanel.class);
            startActivity(teacherInfoIntent);
        }
        if(v.getId()==R.id.adminStudentInformationBtn){
            Intent roomAddIntent = new Intent(AdminHomeActivity.this, AdminRoomAddActivity.class);
            startActivity(roomAddIntent);
        }
    }
}