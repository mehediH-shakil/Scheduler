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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.im.scheduler.R;

public class AdminDeshboard extends AppCompatActivity implements View.OnClickListener {
    private Button saveBtn;
    private EditText aa,cc,dd,ee,ff,ff2,gg,gg2;
    private Spinner bb;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_deshboard);

        saveBtn = (Button)findViewById(R.id.AdimBtn);
        saveBtn.setOnClickListener(this);

        mAdView = (AdView)findViewById(R.id.adView13);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        aa = (EditText)findViewById(R.id.a);
        bb = (Spinner)findViewById(R.id.b);
        cc = (EditText)findViewById(R.id.c);
        dd = (EditText)findViewById(R.id.d);
        ee = (EditText)findViewById(R.id.e);
        ff = (EditText)findViewById(R.id.f);
        ff2 = (EditText)findViewById(R.id.f2);
        gg = (EditText)findViewById(R.id.g);
        gg2 = (EditText)findViewById(R.id.g2);

        String [] Day = {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,Day);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bb.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.AdimBtn) {
            final String course = aa.getText().toString().trim();
            final String day = bb.getSelectedItem().toString().trim();
            final String end = dd.getText().toString().trim();
            final String room = ee.getText().toString().trim();
            final String sec = ff2.getText().toString().trim();
            final String sem = ff.getText().toString().trim();
            final String start = cc.getText().toString().trim();
            final String teacher = gg.getText().toString().trim();
            final String teacherEmail = gg2.getText().toString().trim();

            final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admin Panel")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String dept = snapshot.child("dept").getValue().toString();

                    //Path
                    String time = start+end;
                    String timePath1 = time.replaceAll("[^a-zA-Z0-9]","");
                    final String timePath = timePath1;
                    String Path2 = teacherEmail.replaceAll("[^a-zA-Z0-9]","_");
                    final String myPath2 = Path2;

                    //Student Class Schedule
                    String myPath = dept+"/Student Class Schedule/"+sem+"_"+sec+"_"+day;
                    String teacherNamePath = myPath2;
                    UserHelperClass helperClass = new UserHelperClass(course, day, end, room, start, sec, sem, teacher, myPath2,dept);
                    FirebaseDatabase.getInstance().getReference(myPath)
                            .child(teacherNamePath+"_"+course)
                            .setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            }else{
                            }
                        }
                    });

                    //Teacher Class Schedule
                    UserHelperClass helperClassForTeacher = new UserHelperClass(course, day, end, room, start, sec, sem, teacher, myPath2,dept);
                    FirebaseDatabase.getInstance().getReference(dept+"/Teacher Class Schedule")
                            .child(myPath2+"_"+day+"/"+sem+"_"+sec+"_"+course)
                            .setValue(helperClassForTeacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText( AdminDeshboard.this,"Class Schedule has Updated..",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText( AdminDeshboard.this,"Failed to update! Try again!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    FirebaseDatabase.getInstance().getReference(dept+"/Empty Room")
                            .child(day.replaceAll("[^a-zA-Z0-9]","")).child(timePath.replaceAll("[^a-zA-Z0-9]","")+"_"+room.replaceAll("[^a-zA-Z0-9]",""))
                            .removeValue();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
class UserHelperClass {
    String course,day,end,room,start,sec,sem,teacher,teacherEmailPath,deptPath;

    public UserHelperClass() {
    }

    public UserHelperClass(String course, String day, String end, String room, String start, String sec, String sem, String teacher, String teacherEmailPath,String deptPath) {
        this.course = course;
        this.day = day;
        this.end = end;
        this.room = room;
        this.start = start;
        this.sec = sec;
        this.sem = sem;
        this.teacher = teacher;
        this.teacherEmailPath = teacherEmailPath;
        this.deptPath = deptPath;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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

    public String getTeacherEmailPath() {
        return teacherEmailPath;
    }

    public void setTeacherEmailPath(String teacherEmail) {
        this.teacherEmailPath = teacherEmail;
    }

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }
}

class emptyModel{
    String status,Room;

    public emptyModel() {
    }

    public emptyModel(String status, String room) {
        this.status = status;
        Room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }
}