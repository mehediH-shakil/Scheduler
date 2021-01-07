package com.im.scheduler.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.im.scheduler.R;
import com.im.scheduler.studentDeshboard;
import com.im.scheduler.teacherDeshboardActivity;

public class AdminAssignmentOrExam extends AppCompatActivity implements View.OnClickListener {
    private Button backBtn,saveBtn;
    EditText titleText,timeText,semesterText,sectionText,teacherText,teacherEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assignment_or_exam);

        backBtn = (Button)findViewById(R.id.adminAssigBackBtn);
        saveBtn = (Button)findViewById(R.id.adminExamSaveBtn);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        titleText = (EditText)findViewById(R.id.adminExamTitleFild);
        timeText = (EditText)findViewById(R.id.adminExamTimeFild);
        semesterText = (EditText)findViewById(R.id.adminExamSemesterFild);
        sectionText = (EditText)findViewById(R.id.AdminExamSectionFild);
        teacherText = (EditText)findViewById(R.id.adminExamTeacherNameFild2);
        teacherEmailText = (EditText)findViewById(R.id.adminExamTeacherNameFild);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.adminAssigBackBtn){
            Intent intent = new Intent(AdminAssignmentOrExam.this,AdminHomeActivity.class);
            startActivity(intent);
        }
        if(v.getId()==R.id.adminExamSaveBtn){
            final String sec = sectionText.getText().toString().trim();
            final String sem = semesterText.getText().toString().trim();
            final String teacher = teacherText.getText().toString().trim();
            final String time = timeText.getText().toString().trim();
            final String title = titleText.getText().toString().trim();
            final String teacherEmail = teacherEmailText.getText().toString().trim();
            String Path3 = teacherEmail.replaceAll("[^a-zA-Z0-9]","_");
            final String myPath3 = Path3;

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admin Panel")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String dept = snapshot.child("dept").getValue().toString();

                    AdminAssigmentOrExamModel modelClass = new AdminAssigmentOrExamModel(sec,sem,teacher,time,title,dept);
                    FirebaseDatabase.getInstance().getReference(dept+"/Student Assignment_Exam")
                            .child(sem+"_"+sec+"/"+myPath3+"_"+title)
                            .setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            }else{
                            }
                        }
                    });

                    AdminAssigmentOrExamModel modelClassForTeacher = new AdminAssigmentOrExamModel(sec,sem,teacher,time,title,dept);
                    FirebaseDatabase.getInstance().getReference(dept+"/Teacher Assignment_Exam")
                            .child(myPath3+"/"+sem+"_"+sec+"_"+title)
                            .setValue(modelClassForTeacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText( AdminAssignmentOrExam.this,"Assignment has Updated..",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText( AdminAssignmentOrExam.this,"Failed to update! Try again!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
class AdminAssigmentOrExamModel {
    String sec,sem,teacher,time,title,deptPath;

    public AdminAssigmentOrExamModel() {
    }

    public AdminAssigmentOrExamModel(String sec, String sem, String teacher, String time, String title, String deptPath) {
        this.sec = sec;
        this.sem = sem;
        this.teacher = teacher;
        this.time = time;
        this.title = title;
        this.deptPath = deptPath;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }
}