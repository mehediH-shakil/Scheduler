package com.im.scheduler.TeacherFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class TeacherAssignmentFragment extends Fragment {

    RecyclerView recviewForExam ;
    TeacherAssignmentAdapter myAdapterForExam ;
    private String myPathForTeacher,Path1,s3;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TeacherAssignmentFragment() {
        // Required empty public constructor
    }

    public TeacherAssignmentFragment(String Path, String namePath, String deptPath) {
        myPathForTeacher = Path;
        Path1 = namePath;
        s3 = deptPath;
    }


    public static TeacherAssignmentFragment newInstance(String param1, String param2) {
        TeacherAssignmentFragment fragment = new TeacherAssignmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_teacher_assignment, container, false);

        recviewForExam  = (RecyclerView)view.findViewById(R.id.examRecyclerViewForTeacher);
        recviewForExam .setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<TeacherAssignmentModel> TeacherOptionsForExam  =
                new FirebaseRecyclerOptions.Builder<TeacherAssignmentModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(s3+"/Teacher Assignment_Exam").child(myPathForTeacher), TeacherAssignmentModel.class)
                        .build();
        myAdapterForExam = new TeacherAssignmentAdapter(TeacherOptionsForExam);
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


class TeacherAssignmentAdapter extends FirebaseRecyclerAdapter<TeacherAssignmentModel, TeacherAssignmentAdapter.myviewholder> {
    public TeacherAssignmentAdapter(@NonNull FirebaseRecyclerOptions<TeacherAssignmentModel> options) {
        super(options);
    }
    String semPath1,secPath1,titlePath,deptP,OldTitlePath,mailPath;

    @Override
    protected void onBindViewHolder(@NonNull final TeacherAssignmentAdapter.myviewholder holder, final int position, @NonNull final TeacherAssignmentModel model) {
        holder.titleText.setText(model.getTitle());
        holder.timeText.setText(model.getTime());
        holder.sec_e.setText(model.getSec());
        holder.sem_e.setText(model.getSem());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.titleText.getContext());
                builder.setTitle("Assignment Update Panel");
                builder.setMessage("not available");

                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();


//                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.titleText.getContext())
//                        .setContentHolder(new ViewHolder(R.layout.assignment_dialog_content))
//                        .setExpanded(false)
//                        .setGravity(Gravity.CENTER).setCancelable(true)
//                        .create();
//                View myviwe = dialogPlus.getHolderView();
//                final EditText time = (EditText)myviwe.findViewById(R.id.updateExamTimeFild1);
//                final EditText sem = (EditText)myviwe.findViewById(R.id.updateExamSemesterFild1);
//                final EditText sec= (EditText)myviwe.findViewById(R.id.updateExamSectionFild1);
//                final EditText title = (EditText)myviwe.findViewById(R.id.updateExamTitleFild1);
//                Button updateBtnForAssignment1 = (Button)myviwe.findViewById(R.id.updateExamSaveBtn1);
//
//                sec.setText(model.getSec());
//                sem.setText(model.getSem());
//                time.setText(model.getTime());
//                title.setText(model.getTitle());
//
//                dialogPlus.show();
//                semPath1 = model.getSem();
//                secPath1 = model.getSec();
//                mailPath = model.getTeacherEmailPath();
//                deptP = model.getDeptPath();
//                OldTitlePath = model.getTitle();
//
//                updateBtnForAssignment1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Map<String,Object> map = new HashMap<>();
//                        map.put("deptPath",model.getDeptPath());
//                        map.put("sec",sec.getText().toString());
//                        map.put("sem",sem.getText().toString());
//                        map.put("teacher",model.getTeacher());
//                        map.put("time",time.getText().toString());
//                        map.put("title",title.getText().toString());
//
//                        titlePath = title.getText().toString();
//                        FirebaseDatabase.getInstance().getReference(deptP+"/Teacher Assignment_Exam")
//                                .child(mailPath).child(semPath1+"_"+secPath1+"_"+titlePath)
//                                .updateChildren(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        if(!titlePath.equals(OldTitlePath)) {
//                                            FirebaseDatabase.getInstance().getReference(deptP+"/Teacher Assignment_Exam")
//                                                    .child(mailPath).child(semPath1 + "_" + secPath1 +"_"+ OldTitlePath)
//                                                    .removeValue();
//                                        }
//                                        dialogPlus.dismiss();
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                dialogPlus.dismiss();
//                            }
//                        });
//
//                        FirebaseDatabase.getInstance().getReference(deptP+"/Student Assignment_Exam")
//                                .child(semPath1+"_"+secPath1).child(mailPath+"_"+titlePath)
//                                .updateChildren(map)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        if(!titlePath.equals(OldTitlePath)) {
//                                        FirebaseDatabase.getInstance().getReference(deptP+"/Student Assignment_Exam")
//                                                    .child(semPath1 + "_" + secPath1).child(mailPath+"_"+OldTitlePath)
//                                                    .removeValue();
//                                        }
//                                        dialogPlus.dismiss();
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                dialogPlus.dismiss();
//                            }
//                        });
//                    }
//                });
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semPath1 = model.getSem();
                secPath1 = model.getSec();
                titlePath = model.getTitle();
                final String mailPath = model.getTeacherEmailPath();

                final String OldTitlePath = model.getTitle();
                Map<String,Object> map = new HashMap<>();
                map.put("deptPath",model.getDeptPath());
                map.put("sec",model.getSec());
                map.put("sem",model.getSem());
                map.put("teacher",model.getTeacher());
                map.put("time",model.getTime());
                map.put("title",model.getTitle());

                titlePath = model.getTitle();
                deptP = model.getDeptPath();

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.titleText.getContext());
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure want to delete this assignment? You can't undo this action.");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference(deptP+"/Teacher Assignment_Exam")
                                .child(mailPath).child(getRef(position).getKey())
                                .removeValue();

                        FirebaseDatabase.getInstance().getReference(deptP+"/Student Assignment_Exam")
                                .child(semPath1+"_"+secPath1).child(mailPath+"_"+titlePath)
                                .removeValue();

                        FirebaseDatabase.getInstance().getReference(deptP+"/Student Assignment_Exam")
                                .child(semPath1+"_"+secPath1).child(mailPath+"_"+OldTitlePath)
                                .removeValue();
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
    public TeacherAssignmentAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_teacher_assignment,parent,false);
        return new TeacherAssignmentAdapter.myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        TextView titleText,timeText,sec_e,sem_e;
        Button editBtn,deleteBtn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.assignmentCourseFild);
            timeText = itemView.findViewById(R.id.assignmentTimeFild2);
            sec_e = itemView.findViewById(R.id.assignmentSectionFild);
            sem_e = itemView.findViewById(R.id.assignmentSemesterFild);
            editBtn = (Button)itemView.findViewById(R.id.editButtonForAssignment);
            deleteBtn = (Button)itemView.findViewById(R.id.deleteButtonForAssignment1);
        }
    }
}

class TeacherAssignmentModel{
    String sec,sem,teacher,time,title,teacherEmailPath,deptPath;

    public TeacherAssignmentModel() {
    }

    public TeacherAssignmentModel(String sec, String sem, String teacher, String time, String title, String teacherEmailPath,String deptPath) {
        this.sec = sec;
        this.sem = sem;
        this.teacher = teacher;
        this.time = time;
        this.title = title;
        this.teacherEmailPath = teacherEmailPath;
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

    public String getTeacherEmailPath() {
        return teacherEmailPath;
    }

    public void setTeacherEmailPath(String teacherEmailPath) {
        this.teacherEmailPath = teacherEmailPath;
    }

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }
}