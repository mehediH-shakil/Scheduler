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
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;

public class studentAssignmentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String semPathForexam, secPathForexam,s3;

    private String mParam1;
    private String mParam2;

    RecyclerView recviewForExam ;
    ExamAdepter myAdapterForExam ;

    public studentAssignmentFragment() {
        // Required empty public constructor
    }
    public studentAssignmentFragment(String semPath, String secPath, String deptPath) {
        semPathForexam =semPath;
        secPathForexam = secPath;
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
        View view =  inflater.inflate(R.layout.fragment_student_assignment, container, false);

        recviewForExam  = (RecyclerView)view.findViewById(R.id.examRecyclerView);
        recviewForExam .setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<ExamModel> optionsForExam  =
                new FirebaseRecyclerOptions.Builder<ExamModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(s3+"/Student Assignment_Exam").child(semPathForexam+"_"+secPathForexam), ExamModel.class)
                        .build();
        myAdapterForExam = new ExamAdepter(optionsForExam );
        recviewForExam .setAdapter(myAdapterForExam );

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        myAdapterForExam.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdapterForExam.stopListening();
    }
}
class ExamAdepter extends FirebaseRecyclerAdapter<ExamModel,ExamAdepter.myviewholder> {

    public ExamAdepter(@NonNull FirebaseRecyclerOptions<ExamModel> optionsForExam ) {
        super(optionsForExam );
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ExamModel model) {
        holder.titleText.setText(model.getTitle());
        holder.timeText.setText(model.getTime());
        holder.sec_e.setText(model.getSec());
        holder.sem_e.setText(model.getSem());
        holder.teacher_e.setText(model.getTeacher());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_exam,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder  extends RecyclerView.ViewHolder{
        TextView titleText,timeText,sec_e,sem_e,teacher_e;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.examCourseFild);
            timeText = itemView.findViewById(R.id.examTimeFild2);
            sec_e = itemView.findViewById(R.id.examSectionFild);
            sem_e = itemView.findViewById(R.id.examSemesterFild);
            teacher_e =itemView.findViewById(R.id.examTeacherFild);
        }
    }
}
class ExamModel {
    String sec,sem,teacher,time,title;

    public ExamModel() {

    }

    public ExamModel(String sec, String sem, String teacher, String time, String title) {
        this.sec = sec;
        this.sem = sem;
        this.teacher = teacher;
        this.time = time;
        this.title = title;
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
}
