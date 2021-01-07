package com.im.scheduler.StudentFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.im.scheduler.R;

public class studentClassSchedulFragment extends Fragment {

    private FirebaseUser StudentProfile;
    private DatabaseReference reference;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String s,s1,s2,s3;
    TextView k;

    RecyclerView recview;
    classScheduleAdapter myAdapter;

    public studentClassSchedulFragment() {
    }

    public studentClassSchedulFragment(String semPath, String secPath, String dayPath, String deptPath) {
       s = semPath;
       s1 = secPath;
       s2 = dayPath;
       s3 = deptPath;
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
        View view = inflater.inflate(R.layout.fragment_student_class_schedul, container, false);

        final String[] timePath = new String[1];

        recview = (RecyclerView)view.findViewById(R.id.classScheduleRecyclerView);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        final String[] mPath = new String[1];
        reference = FirebaseDatabase.getInstance().getReference("Schedule Path");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timePath[0] = snapshot.child("Day").getValue(String.class);
                mPath[0] = s+"_"+s1+"_"+timePath[0];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
        FirebaseRecyclerOptions<classScheduleModel> options =
                new FirebaseRecyclerOptions.Builder<classScheduleModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(s3+"/Student Class Schedule").child(s+"_"+s1+"_"+s2), classScheduleModel.class)
                        .build();
        myAdapter = new classScheduleAdapter(options);
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

class classScheduleAdapter extends FirebaseRecyclerAdapter<classScheduleModel,classScheduleAdapter.myviewholder> {
    public classScheduleAdapter(@NonNull FirebaseRecyclerOptions<classScheduleModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull classScheduleModel model) {
        holder.courseTitle.setText(model.getCourse());
        holder.dayText.setText(model.getDay());
        holder.endTime.setText(model.getEnd());
        holder.roomText.setText(model.getRoom());
        holder.startTime.setText(model.getStart());
        holder.teacherText.setText(model.getTeacher());

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_class_scledule,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView courseTitle,dayText,endTime,startTime,roomText,teacherText,teacherEmployeeID;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            dayText = itemView.findViewById(R.id.dayText);
            endTime = itemView.findViewById(R.id.endTimeFild);
            roomText = itemView.findViewById(R.id.roomFild);
            startTime = itemView.findViewById(R.id.startTimeFild);
            teacherText = itemView.findViewById(R.id.teacherName);
        }
    }
}
class classScheduleModel {
    String course,day,end,room,start,teacher;

    public classScheduleModel() {
    }

    public classScheduleModel(String course, String day, String end, String room, String start, String teacher) {
        this.course = course;
        this.day = day;
        this.end = end;
        this.room = room;
        this.start = start;
        this.teacher = teacher;
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
