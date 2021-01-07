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

public class classScheduleAdapter extends FirebaseRecyclerAdapter<classScheduleModel,classScheduleAdapter.myviewholder> {
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

        TextView courseTitle,dayText,endTime,startTime,roomText,teacherText;
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
