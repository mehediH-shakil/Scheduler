package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.im.scheduler.StudentFragment.studentAssignmentFragment;
import com.im.scheduler.StudentFragment.studentClassSchedulFragment;
import com.im.scheduler.StudentFragment.studentEmptyClassFragment;
import com.im.scheduler.StudentFragment.studentTeacherInformationFragment;
import com.im.scheduler.TeacherFragment.TeacherAssignmentFragment;
import com.im.scheduler.TeacherFragment.TeacherClassScheduleFragment;
import com.im.scheduler.TeacherFragment.TeacherEmptyClassFragment;
import com.im.scheduler.TeacherFragment.TeacherUpdateFragment;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class teacherDeshboardActivity extends AppCompatActivity {

    BottomNavigationView TeacherBottomNavigationBtn;
    private TextView TeacherclassCounter,TeacherassginmentCounter,TeacherExamCounter;
    private TextView TeachertimeText;
    int tempTeacher,temp1Teacher;
    private String emailPath,namePath,dayPath,emptyDayPath,emptyTimePath,nextDay,deptPath;

    private FirebaseUser TeacherProfile;
    private DatabaseReference referenceTeacher;
    private String TeacherUserID;
    private String Path;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_deshboard_activity);

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Email Path")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Path = snapshot.child("Path").getValue().toString();
                Thread myThread = null;
                Runnable myRunnableThread = new teacherDeshboardActivity.CountDownRunner();
                myThread = new Thread(myRunnableThread);
                myThread.start();

                TeacherBottomNavigationBtn = (BottomNavigationView)findViewById(R.id.TeacherNavigationBtn);
                TeacherBottomNavigationBtn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatinerTeacher,new TeacherClassScheduleFragment(emailPath,dayPath,deptPath)).commit();
                        if(item.getItemId()==R.id.classScheduleForTeacher){
                            getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatinerTeacher,new TeacherClassScheduleFragment(emailPath,dayPath,deptPath)).commit();
                        }
                        if(item.getItemId()==R.id.assignmentOrExamForTeacher){
                            getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatinerTeacher,new TeacherAssignmentFragment(emailPath,namePath,deptPath)).commit();
                        }
                        if(item.getItemId()==R.id.emptyClassForTeacher){
                            getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatinerTeacher,new TeacherEmptyClassFragment(emailPath,dayPath,emptyTimePath,deptPath)).commit();
                        }
                        if(item.getItemId()==R.id.updateForTeacher){
                            getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatinerTeacher,new TeacherUpdateFragment(emailPath,namePath,dayPath,deptPath)).commit();
                        }
                        return true;
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //counter
        TeacherclassCounter = (TextView)findViewById(R.id.TeacherClassCounterBox);
        TeacherassginmentCounter = (TextView)findViewById(R.id.TeacherAssignmentCounterBox);
        TeacherExamCounter = (TextView)findViewById(R.id.TeacherExamCounterBox);

    }

    //time
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {

                    TeacherProfile = FirebaseAuth.getInstance().getCurrentUser();
                    referenceTeacher = FirebaseDatabase.getInstance().getReference("Teacher");
                    final String UserPath = Path;

                    TeachertimeText = (TextView)findViewById(R.id.timeDateFildTeacher);
                    Date currentTime = Calendar.getInstance().getTime();
                    String myTime = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
                    final String[] day = myTime.split(",");
                    Log.d("myLOG", currentTime.toString());
                    Log.d("myLOG", myTime);
                    Log.d("myLOG", day[0].trim());

                    final TextView TeacherProfileName = (TextView)findViewById(R.id.TeacherProfileNameText);
                    final TextView TeacherDepartmentName= (TextView)findViewById(R.id.TeacherdepartmentTextFilde);
                    final TextView TeacherDesFild = (TextView)findViewById(R.id.TeacherIDTextFilde2);


                    //Profile
                    referenceTeacher.child(UserPath).addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final UserForTeacher userProfileForTeacher= snapshot.getValue(UserForTeacher.class);

                            if(userProfileForTeacher != null){
                                String profileNameTeacher = userProfileForTeacher.name;
                                final String departmentNameTeacher = userProfileForTeacher.dept;
                                String TeacherDes = userProfileForTeacher.des;

                                //path
                                String Path = userProfileForTeacher.email.replaceAll("[^a-zA-Z0-9]","_");
                                final String myPath = Path;
                                emailPath = myPath;
                                namePath = userProfileForTeacher.name;
                                emptyDayPath = userProfileForTeacher.empty_room_dayPath;
                                emptyTimePath = userProfileForTeacher.empty_room_timePath;
                                dayPath = userProfileForTeacher.schedule_change;
                                deptPath = userProfileForTeacher.dept;
                                Bundle bundle = new Bundle();
                                bundle.putString("key", emptyDayPath+"/"+emptyTimePath.replaceAll("[^a-zA-Z0-9]",""));
                                TeacherEmptyClassFragment.putArguments(bundle);

                                TeacherProfileName.setText(profileNameTeacher);
                                TeacherDepartmentName.setText("Dept. of " + departmentNameTeacher + ", Varendra University");
                                TeacherDesFild.setText(TeacherDes);

                                final Map<String, Object> map = new HashMap<>();
                                map.put("dept", deptPath);

                                FirebaseDatabase.getInstance().getReference("Admin Panel")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .updateChildren(map);

                                //Profile picture
                                final CircleImageView circleImage = (CircleImageView) findViewById(R.id.profilePicTeacher);
                                String imagePath = null;
                                if(userProfileForTeacher.gen.equals("Male")){
                                    imagePath = "male_teacher.png";
                                }
                                else if(userProfileForTeacher.gen.equals("Female")){
                                    imagePath = "female.jfif";
                                }
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                final StorageReference storageReference = storage.getReferenceFromUrl("gs://scheduler-627c4.appspot.com/profile pic").child(imagePath);

                                File file = null;
                                try {
                                        file = File.createTempFile("image","jpg");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    final File finalFile = file;
                                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                                            circleImage.setImageBitmap(bitmap);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if(userProfileForTeacher.gen.equals("Male")){
                                                circleImage.setImageResource(R.drawable.male_teacher);
                                            }
                                            else if(userProfileForTeacher.gen.equals("Female")){
                                                circleImage.setImageResource(R.drawable.female1);
                                            }
                                        }
                                    });


                                //menu
                                Button moreBtn = (Button)findViewById(R.id.moreBtnT);
                                final File finalFile1 = file;
                                moreBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        final DialogPlus dialogPlus = DialogPlus.newDialog(TeacherProfileName.getContext())
                                                .setContentHolder(new ViewHolder(R.layout.profile_update_dialog_for_teacher))
                                                .setExpanded(false)
                                                .setGravity(Gravity.CENTER)
                                                .setCancelable(true)
                                                .create();
                                        final String [] desSpinner = {"Professor","Associate Professor","Assistant Professor","Lecturer"};
                                        final String [] deptSpinner = {"CSE","EEE","Pharmacy","Public Health","BBA","LLB","Economics","English","Journalism","Sociology","Political Science"};

                                        View myviwe = dialogPlus.getHolderView();
                                        final EditText name = (EditText)myviwe.findViewById(R.id.nameIDForTeacher);
                                        final Spinner department = (Spinner) myviwe.findViewById(R.id.deptIDForTeacher);
                                        final Spinner designation = (Spinner) myviwe.findViewById(R.id.desID);
                                        final ImageView imageView = (ImageView) myviwe.findViewById(R.id.imageView2ForTeacher);
                                        Button logOutBtnForTeacher = (Button)myviwe.findViewById(R.id.teacherLogOutBtn);
                                        Button closeBtnForTeacher = (Button)myviwe.findViewById(R.id.teacherCloseBtn);
                                        Button ProfileUpdateBtnForTeacher = (Button)myviwe.findViewById(R.id.updateButtonForTeacherProfile);

                                        final ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(TeacherProfileName.getContext(), android.R.layout.simple_spinner_item,deptSpinner);
                                        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        department.setAdapter(deptAdapter);

                                        final ArrayAdapter<String> desAdapter = new ArrayAdapter<String>(TeacherProfileName.getContext(), android.R.layout.simple_spinner_item,desSpinner);
                                        desAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        designation.setAdapter(desAdapter);


                                        final File finalFile = finalFile1;
                                        storageReference.getFile(finalFile1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());

                                                imageView.setImageBitmap(bitmap);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if(userProfileForTeacher.gen.equals("Male")){
                                                    imageView.setImageResource(R.drawable.male_teacher);
                                                }
                                                else if(userProfileForTeacher.gen.equals("Female")){
                                                    imageView.setImageResource(R.drawable.female1);
                                                }
                                            }
                                        });
                                        name.setText(TeacherProfileName.getText().toString());
                                        department.setSelection(deptAdapter.getPosition(departmentNameTeacher));
                                        designation.setSelection(desAdapter.getPosition(TeacherDesFild.getText().toString()));

                                        dialogPlus.show();

                                        ProfileUpdateBtnForTeacher.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final Map<String, Object> map1 = new HashMap<>();
                                                map1.put("dept",department.getSelectedItem().toString().trim());
                                                map1.put("des", designation.getSelectedItem().toString().trim());
                                                map1.put("name", name.getText().toString().trim());
                                                FirebaseDatabase.getInstance().getReference("Teacher")
                                                        .child(emailPath)
                                                        .updateChildren(map1)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(teacherDeshboardActivity.this,"Your Profile is Updated",Toast.LENGTH_LONG).show();
                                                                dialogPlus.dismiss();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(teacherDeshboardActivity.this,"Try Again!, Update your profile",Toast.LENGTH_LONG).show();
                                                        dialogPlus.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                        closeBtnForTeacher.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialogPlus.dismiss();
                                            }
                                        });

                                        //Log out
                                        final String android_id = Settings.Secure.getString(TeacherProfileName.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                        logOutBtnForTeacher.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherProfileName.getContext());
                                                builder.setTitle("Log Out");
                                                builder.setMessage("Are you sure you want to eixt!");
                                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        FirebaseDatabase.getInstance().getReference("Login Panel")
                                                                .child(android_id+"/user")
                                                                .setValue("OFF")
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                        }else{
                                                                        }
                                                                    }
                                                                });

                                                        Intent logOutIntent = new Intent(getApplicationContext(),LoginActivity.class);
                                                        startActivity(logOutIntent);
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
                                });

                                //classCounter
                                final int[] childCountTeacher = {0};
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference(deptPath+"/Teacher Class Schedule").child(myPath+"_"+dayPath);

                                database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                            childCountTeacher[0] +=snap.getChildrenCount();
                                        }
                                        tempTeacher = childCountTeacher[0]/9;
                                        String C = Integer.toString(tempTeacher);
                                        TeacherclassCounter.setText(C);
                                        tempTeacher=0;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                //examCounter
                                final int[] childCount1Teacher = {0};
                                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference(deptPath+"/Teacher Assignment_Exam").child(myPath);

                                database2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                            childCount1Teacher[0] +=snap.getChildrenCount();
                                        }
                                        temp1Teacher = childCount1Teacher[0]/5;
                                        String C1 = Integer.toString(temp1Teacher);
                                        TeacherassginmentCounter.setText(C1);
                                        temp1Teacher=0;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(teacherDeshboardActivity.this,"Something wrong happened!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
                    String myHours = simpleDateFormat.format(currentTime);
                    TeachertimeText.setText(myTime);

                    int i=Integer.parseInt(myHours.replaceAll("[\\D]", ""));

                    //Day Change
                    if(i >= 180000 && i <= 240000) {
                        if (day[0].equals("Saturday")) {
                            nextDay = "Sunday";
                        } else if (day[0].equals("Sunday")) {
                            nextDay = "Monday";
                        } else if (day[0].equals("Monday")) {
                            nextDay = "Tuesday";
                        } else if (day[0].equals("Tuesday")) {
                            nextDay = "Wednesday";
                        } else if (day[0].equals("Wednesday")) {
                            nextDay = "Thursday";
                        } else if (day[0].equals("Thursday")) {
                            nextDay = "Friday";
                        } else if (day[0].equals("Friday")) {
                            nextDay = "Saturday";
                        }
                    }else{
                        nextDay = day[0];
                    }
                    if(emailPath != null){
                        FirebaseDatabase.getInstance().getReference("Teacher")
                                .child(emailPath + "/schedule_change")
                                .setValue(nextDay).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                } else {
                                }
                            }
                        });
                    }

                } catch (Exception e) {

                }
            }
        });
    }

    class CountDownRunner implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
}