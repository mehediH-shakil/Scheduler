package com.im.scheduler.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;

public class teacherInformationForAdminPanel extends AppCompatActivity implements View.OnClickListener {

    private Button saveBtnforTeacherInfo;
    private EditText nameTeax,emailText,phnText;
    private Spinner desText,deptText,genText;
    String Path;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_information_for_admin_panel);

        nameTeax = (EditText)findViewById(R.id.editTextTextPersonName);
        emailText = (EditText)findViewById(R.id.editTextTextPersonName2);
        phnText = (EditText)findViewById(R.id.editTextTextPersonName3);
        desText = (Spinner)findViewById(R.id.editTextTextPersonName4);
        deptText = (Spinner)findViewById(R.id.editTextTextPersonName5);
        genText = (Spinner)findViewById(R.id.editTextTextPersonName6);
        saveBtnforTeacherInfo = (Button)findViewById(R.id.teacherInformationSaveButton);
        saveBtnforTeacherInfo.setOnClickListener(this);
        mAdView = (AdView)findViewById(R.id.adView10);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final String [] desSpinner = {"Professor","Associate Professor","Assistant Professor","Lecturer"};
        final String [] deptSpinner = {"CSE","EEE","Pharmacy","Public Health","BBA","LLB","Economics","English","Journalism","Sociology","Political Science"};
        final String [] genSpinner = {"Male","Female","Other"};

        final ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,deptSpinner);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptText.setAdapter(deptAdapter);

        final ArrayAdapter<String> desAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,desSpinner);
        desAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desText.setAdapter(desAdapter);

        final ArrayAdapter<String> genAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,genSpinner);
        desAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genText.setAdapter(genAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.teacherInformationSaveButton) {
            final String Email = emailText.getText().toString().trim();
            final String Name = nameTeax.getText().toString().trim();
            final String Phone = phnText.getText().toString().trim();
            final String dept = deptText.getSelectedItem().toString().trim();
            final String des = desText.getSelectedItem().toString().trim();
            final String gen = genText.getSelectedItem().toString().trim();

            Path = Email.replaceAll("[^a-zA-Z0-9]","_");
            final String myPath = Path;

            UserHelperClassForTeacherInfoAdime helperClass = new UserHelperClassForTeacherInfoAdime(Email,Name,Phone,dept,des,gen," "," "," "," "," ");
            FirebaseDatabase.getInstance().getReference(dept+"/Teacher")
                    .child(myPath)
                    .setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText( teacherInformationForAdminPanel.this,"Updated..",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText( teacherInformationForAdminPanel.this,"Failed to update! Try again!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
class UserHelperClassForTeacherInfoAdime {
    String Email,Name,Phone,dept,des,gen,schedule_change,assignment_change,exam_change,empty_room_timePath,empty_room_dayPath;

    public UserHelperClassForTeacherInfoAdime() {
    }

    public UserHelperClassForTeacherInfoAdime(String email, String name, String phone, String dept, String des, String gen, String schedule_change, String assignment_change, String exam_change, String empty_room_timePath, String empty_room_dayPath) {
        Email = email;
        Name = name;
        Phone = phone;
        this.dept = dept;
        this.des = des;
        this.gen = gen;
        this.schedule_change = schedule_change;
        this.assignment_change = assignment_change;
        this.exam_change = exam_change;
        this.empty_room_timePath = empty_room_timePath;
        this.empty_room_dayPath = empty_room_dayPath;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getSchedule_change() {
        return schedule_change;
    }

    public void setSchedule_change(String schedule_change) {
        this.schedule_change = schedule_change;
    }

    public String getAssignment_change() {
        return assignment_change;
    }

    public void setAssignment_change(String assignment_change) {
        this.assignment_change = assignment_change;
    }

    public String getExam_change() {
        return exam_change;
    }

    public void setExam_change(String exam_change) {
        this.exam_change = exam_change;
    }

    public String getEmpty_room_timePath() {
        return empty_room_timePath;
    }

    public void setEmpty_room_timePath(String empty_room_timePath) {
        this.empty_room_timePath = empty_room_timePath;
    }

    public String getEmpty_room_dayPath() {
        return empty_room_dayPath;
    }

    public void setEmpty_room_dayPath(String empty_room_dayPath) {
        this.empty_room_dayPath = empty_room_dayPath;
    }
}