package com.im.scheduler.StudentFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.im.scheduler.R;

public class ExamAdepter extends FirebaseRecyclerAdapter<ExamModel,ExamAdepter.myviewholder> {

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
