package com.im.scheduler.TeacherFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class TeacherUpdateFragment extends Fragment {

    GridLayout teacherGridLayout;
    private String emailPath,dayPath,namePath,s3;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeacherUpdateFragment() {
        // Required empty public constructor
    }
    public TeacherUpdateFragment(String mailPath, String namepath, String daypath, String deptPath) {
        emailPath = mailPath;
        namePath = namepath;
        dayPath = daypath;
        s3 = deptPath;

    }
    
    public static TeacherUpdateFragment newInstance(String param1, String param2) {
        TeacherUpdateFragment fragment = new TeacherUpdateFragment();
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
        View view = inflater.inflate(R.layout.fragment_teacher_update, container, false);


        teacherGridLayout =(GridLayout)view.findViewById(R.id.mainGridLayout);
        setSingleEvent(teacherGridLayout);
        return view;
    }

    private void setSingleEvent(final GridLayout teacherGridLayout) {

        for(int i=0;i<teacherGridLayout.getChildCount();i++)
        {
            final String [] Day = {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};

            CardView cardView = (CardView)teacherGridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI==0){
                        final DialogPlus dialogPlus = DialogPlus.newDialog(teacherGridLayout.getContext())
                                .setContentHolder(new ViewHolder(R.layout.class_schedule_dialog))
                                .setExpanded(false)
                                .setGravity(Gravity.CENTER)
                                .setCancelable(true)
                                .create();
                        View myviwe = dialogPlus.getHolderView();
                        final EditText course = (EditText)myviwe.findViewById(R.id.ateacherUpdate);
                        final Spinner day = (Spinner) myviwe.findViewById(R.id.bteacherUpdate);
                        final EditText room = (EditText)myviwe.findViewById(R.id.eteacherUpdate);
                        final EditText sec = (EditText)myviwe.findViewById(R.id.f2teacherUpdate);
                        final EditText sem = (EditText)myviwe.findViewById(R.id.fteacherUpdate);
                        final EditText end = (EditText)myviwe.findViewById(R.id.dteacherUpdate);
                        final EditText start = (EditText)myviwe.findViewById(R.id.cteacherUpdate);
                        Button updateBtnForTeacher = (Button)myviwe.findViewById(R.id.saveBtnteacherUpdate);
                        dialogPlus.show();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,Day);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        day.setAdapter(adapter);


                        updateBtnForTeacher.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("course", course.getText().toString());
                                map.put("day", day.getSelectedItem().toString());
                                map.put("room", room.getText().toString());
                                map.put("end", end.getText().toString());
                                map.put("sec", sec.getText().toString());
                                map.put("sem", sem.getText().toString());
                                map.put("start", start.getText().toString());
                                map.put("teacher", namePath);
                                map.put("teacherEmailPath", emailPath);
                                map.put("deptPath", s3);
                                final String semPath = sem.getText().toString().trim();
                                final String secPath = sec.getText().toString().trim();
                                final String coursePath = course.getText().toString().trim();
                                final String myDay = day.getSelectedItem().toString().trim();
                                final String timePath = start.getText().toString()+" - " + end.getText().toString();
                                final String roomPath = room.getText().toString();


                                if(semPath.isEmpty()||secPath.isEmpty()||coursePath.isEmpty()){
                                    dialogPlus.dismiss();
                                } else {
                                FirebaseDatabase.getInstance().getReference(s3+"/Teacher Class Schedule")
                                        .child(emailPath + "_" + myDay).child(semPath + "_" + secPath + "_" + coursePath)
                                        .updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialogPlus.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference(s3+"/Student Class Schedule")
                                        .child(semPath + "_" + secPath + "_" + myDay).child(emailPath + "_" + coursePath)
                                        .updateChildren(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialogPlus.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference(s3+"/Empty Room")
                                            .child(myDay.replaceAll("[^a-zA-Z0-9]","")).child(timePath.replaceAll("[^a-zA-Z0-9]","")+"_"+roomPath.replaceAll("[^a-zA-Z0-9]",""))
                                            .removeValue();
                                }
                            }
                        });
                    }
                    else if(finalI==1){
                        final DialogPlus dialogPlus = DialogPlus.newDialog(teacherGridLayout.getContext())
                                .setContentHolder(new ViewHolder(R.layout.assignment_dialog))
                                .setExpanded(false)
                                .setGravity(Gravity.CENTER).setCancelable(true)
                                .create();
                        View myviwe1 = dialogPlus.getHolderView();
                        final EditText sec = (EditText)myviwe1.findViewById(R.id.updateExamSectionFild);
                        final EditText sem = (EditText)myviwe1.findViewById(R.id.updateExamSemesterFild);
                        final EditText time = (EditText)myviwe1.findViewById(R.id.updateExamTimeFild);
                        final EditText title = (EditText)myviwe1.findViewById(R.id.updateExamTitleFild);
                        Button updateBtnForTeacher1 = (Button)myviwe1.findViewById(R.id.updateExamSaveBtn);
                        dialogPlus.show();

                        updateBtnForTeacher1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Map<String, Object> map = new HashMap<>();
                                map.put("sec", sec.getText().toString());
                                map.put("sem", sem.getText().toString());
                                map.put("teacher", namePath);
                                map.put("time", time.getText().toString());
                                map.put("title", title.getText().toString());
                                map.put("teacherEmailPath", emailPath);
                                map.put("deptPath", s3);
                                final String semPath = sem.getText().toString().trim();
                                final String secPath = sec.getText().toString().trim();
                                final String titlePath = title.getText().toString().trim();


                                if(semPath.isEmpty()||secPath.isEmpty()||titlePath.isEmpty()){
                                    dialogPlus.dismiss();
                                }else{
                                    FirebaseDatabase.getInstance().getReference(s3+"/Teacher Assignment_Exam")
                                            .child(emailPath).child(semPath + "_" + secPath + "_" + titlePath)
                                            .updateChildren(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialogPlus.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogPlus.dismiss();
                                        }
                                    });
                                    FirebaseDatabase.getInstance().getReference(s3+"/Student Assignment_Exam")
                                            .child(semPath + "_" + secPath).child(emailPath + "_" + titlePath)
                                            .updateChildren(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialogPlus.dismiss();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogPlus.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else if(finalI==2){
                        AlertDialog.Builder builder = new AlertDialog.Builder(teacherGridLayout.getContext());
                        builder.setTitle("Exam Update Panel");
                        builder.setMessage("not available");

                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    }
                }
            });
        }
    }
}