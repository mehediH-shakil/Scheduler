package com.im.scheduler.TeacherFragment;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.im.scheduler.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class TeacherClassScheduleFragment extends Fragment {

    RecyclerView recview;
    TeacherClassScheduleAdapter myAdapter;
    public String myPathForTeacher,day,timePath,s3;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TeacherClassScheduleFragment() {
        // Required empty public constructor
    }

    public TeacherClassScheduleFragment(String Path, String s, String deptPath) {
        myPathForTeacher = Path;
        day = s;
        s3 = deptPath;
    }


    public static TeacherClassScheduleFragment newInstance(String param1, String param2) {
        TeacherClassScheduleFragment fragment = new TeacherClassScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_class_schedule, container, false);

        recview = view.findViewById(R.id.TeacherClassScheduleRecyclerView);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<TeacherClassScheduleModel> options =
                new FirebaseRecyclerOptions.Builder<TeacherClassScheduleModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(s3+"/Teacher Class Schedule").child(myPathForTeacher+"_"+day), TeacherClassScheduleModel.class)
                        .build();
        myAdapter = new TeacherClassScheduleAdapter(options);
        recview.setAdapter(myAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }
}

class TeacherClassScheduleAdapter extends FirebaseRecyclerAdapter<TeacherClassScheduleModel, TeacherClassScheduleAdapter.myviewholder> {
    public TeacherClassScheduleAdapter(@NonNull FirebaseRecyclerOptions<TeacherClassScheduleModel> options) {
        super(options);
    }
    String teacherText,teacherEmailText;
    private String semPath,secPath,dayPath,coursePath,OldCoursePath,deptP;


    @Override
    protected void onBindViewHolder(@NonNull final TeacherClassScheduleAdapter.myviewholder holder,final int position, @NonNull final TeacherClassScheduleModel model) {
        holder.courseTitle.setText(model.getCourse());
        holder.dayText.setText(model.getDay());
        holder.roomText.setText(model.getRoom());
        holder.endTime.setText(model.getEnd());
        holder.startTime.setText(model.getStart());
        holder.sec.setText(model.getSec());
        holder.sem.setText(model.getSem());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.courseTitle.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(false)
                        .setMargin(0,0,0,0)
                        .setPadding(0,0,0,0)
                        .setGravity(Gravity.CENTER).setCancelable(true)
                        .create();
                View myviwe = dialogPlus.getHolderView();
                final EditText course = (EditText)myviwe.findViewById(R.id.editCourseUpdate);
                final EditText day = (EditText)myviwe.findViewById(R.id.editDayUpdate);
                final EditText room = (EditText)myviwe.findViewById(R.id.editRoomUpdate);
                final EditText end = (EditText)myviwe.findViewById(R.id.editEndTimeUpdate);
                final EditText start = (EditText)myviwe.findViewById(R.id.editStartTimeUpdate);
                Button updateBtn = (Button)myviwe.findViewById(R.id.updateButton);

                course.setText(model.getCourse());
                day.setText(model.getDay());
                room.setText(model.getRoom());
                end.setText(model.getEnd());
                start.setText(model.getStart());

                dialogPlus.show();
                semPath = model.getSem();
                secPath = model.getSec();
                dayPath = day.getText().toString().trim();
                teacherText = model.getTeacher();
                teacherEmailText = model.getTeacherEmailPath();
                OldCoursePath = model.getCourse();
                deptP = model.getDeptPath();

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("course",course.getText().toString());
                        map.put("day",day.getText().toString());
                        map.put("room",room.getText().toString());
                        map.put("end",end.getText().toString());
                        map.put("sec",secPath);
                        map.put("sem",semPath);
                        map.put("start",start.getText().toString());
                        map.put("teacher",teacherText);
                        map.put("teacherEmailPath",teacherEmailText);
                        map.put("deptPath",deptP);

                        coursePath = course.getText().toString();
                        FirebaseDatabase.getInstance().getReference(deptP+"/Teacher Class Schedule")
                                .child(teacherEmailText+"_"+dayPath).child(semPath+"_"+secPath+"_"+coursePath)
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(!coursePath.equals(OldCoursePath)){
                                            FirebaseDatabase.getInstance().getReference(deptP+"/Teacher Class Schedule")
                                                    .child(teacherEmailText + "_" + dayPath).child(semPath + "_" + secPath + "_" + OldCoursePath)
                                                    .removeValue();
                                        }
                                        dialogPlus.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });

                        FirebaseDatabase.getInstance().getReference(deptP+"/Student Class Schedule")
                                .child(semPath+"_"+secPath+"_"+dayPath).child(teacherEmailText+"_"+coursePath)
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if(!coursePath.equals(OldCoursePath)){
                                            FirebaseDatabase.getInstance().getReference(deptP+"/Student Class Schedule")
                                                    .child(semPath + "_" + secPath + "_" + dayPath).child(teacherEmailText + "_" + OldCoursePath)
                                                    .removeValue();
                                        }
                                        dialogPlus.dismiss();
                                        coursePath = OldCoursePath;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                semPath = model.getSem();
                secPath = model.getSec();
                dayPath = model.getDay();
                teacherText = model.getTeacher();
                teacherEmailText = model.getTeacherEmailPath();
                OldCoursePath = model.getCourse();

                Map<String,Object> map = new HashMap<>();
                map.put("course",model.getCourse());
                map.put("day",model.getDay());
                map.put("room",model.getRoom());
                map.put("end",model.getEnd());
                map.put("sec",secPath);
                map.put("sem",semPath);
                map.put("start",model.getStart());
                map.put("teacher",teacherText);
                map.put("teacherEmailPath",teacherEmailText);
                map.put("deptPath",deptP);

                coursePath = model.getCourse();

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.courseTitle.getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure want to delete this class? You can't undo this action.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference(deptP+"/Teacher Class Schedule")
                                .child(teacherEmailText+"_"+dayPath).child(getRef(position).getKey())
                                .removeValue();

                        FirebaseDatabase.getInstance().getReference(deptP+"/Student Class Schedule")
                                .child(semPath+"_"+secPath+"_"+dayPath).child(teacherEmailText+"_"+coursePath)
                                .removeValue();
                        FirebaseDatabase.getInstance().getReference(deptP+"/Student Class Schedule")
                                .child(semPath+"_"+secPath+"_"+dayPath).child(teacherEmailText+"_"+OldCoursePath)
                                .removeValue();

                        String time = model.getStart()+" - " + model.getEnd();
                        Map<String,Object> emptyMap = new HashMap<>();
                        emptyMap.put("day",dayPath);
                        emptyMap.put("room",model.getRoom());
                        emptyMap.put("time",time);
                        FirebaseDatabase.getInstance().getReference(deptP+"/Empty Room")
                                .child(dayPath.replaceAll("[^a-zA-Z0-9]","")).child(time.replaceAll("[^a-zA-Z0-9]","")+"_"+model.getRoom().replaceAll("[^a-zA-Z0-9]",""))
                                .setValue(emptyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                }else{
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public TeacherClassScheduleAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_teacher_class_schedule,parent,false);
        return new TeacherClassScheduleAdapter.myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView courseTitle,dayText,endTime,startTime,roomText,sem,sec;
        Button edit,delete;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.courseTitleForTeacherClassSchedule);
            dayText = itemView.findViewById(R.id.dayTextForTeacherClassSchedule);
            endTime = itemView.findViewById(R.id.endTimeFildForTeacherClassSchedule);
            roomText = itemView.findViewById(R.id.roomFildForTeacherClassSchedule);
            startTime = itemView.findViewById(R.id.startTimeFildForTeacherClassSchedule);
            sec = itemView.findViewById(R.id.roomFildForTeacherClassSchedule3);
            sem = itemView.findViewById(R.id.roomFildForTeacherClassSchedule2);
            edit = (Button)itemView.findViewById(R.id.editButton);
            delete = (Button)itemView.findViewById(R.id.deleteButton);
        }
    }
}
class TeacherClassScheduleModel {
    String course,day,room,end,sem,sec,start,teacher,teacherEmailPath,deptPath;

    public TeacherClassScheduleModel() {
    }

    public TeacherClassScheduleModel(String course, String day, String room, String end, String sem, String sec, String start, String teacher, String teacherEmailPat, String deptPath) {
        this.course = course;
        this.day = day;
        this.room = room;
        this.end = end;
        this.sem = sem;
        this.sec = sec;
        this.start = start;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }

    public void setTeacherEmailPath(String teacherEmailPath) {
        this.teacherEmailPath = teacherEmailPath;
    }
}

