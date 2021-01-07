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

public class teacherInformationAdepter extends FirebaseRecyclerAdapter<teacherInformationModel,teacherInformationAdepter.myviewholder> {

    public teacherInformationAdepter(@NonNull FirebaseRecyclerOptions<teacherInformationModel> optionsForTInfo ) {
        super(optionsForTInfo );
    }

    @Override
    protected void onBindViewHolder(@NonNull teacherInformationAdepter.myviewholder holder, int position, @NonNull teacherInformationModel model) {
        //holder.aa1.setText(model.getName());
        holder.aa2.setText(model.getName());
        holder.aa3.setText(model.getDes());
        holder.aa4.setText(model.getDept());
        holder.aa5.setText(model.getEmail());
        holder.aa6.setText(model.getPhone());
    }

    @NonNull
    @Override
    public teacherInformationAdepter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_teacher_information,parent,false);
        return new teacherInformationAdepter.myviewholder(view);
    }

    public class myviewholder  extends RecyclerView.ViewHolder{
        TextView aa1,aa2,aa3,aa4,aa5,aa6;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            //aa1 = itemView.findViewById(R.id.a1);
            aa2 = itemView.findViewById(R.id.a2);
            aa3 = itemView.findViewById(R.id.a3);
            aa4 = itemView.findViewById(R.id.a4);
            aa5 = itemView.findViewById(R.id.a5);
            aa6 = itemView.findViewById(R.id.a6);
        }
    }
}
