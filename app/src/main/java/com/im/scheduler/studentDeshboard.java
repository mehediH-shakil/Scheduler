package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.im.scheduler.StudentFragment.studentAssignmentFragment;
import com.im.scheduler.StudentFragment.studentClassSchedulFragment;
import com.im.scheduler.StudentFragment.studentEmptyClassFragment;
import com.im.scheduler.StudentFragment.studentTeacherInformationFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.Gravity.CENTER;

public class studentDeshboard extends AppCompatActivity {

    BottomNavigationView studentBottomNavigationBtn;
    private Button logOUT;
    private TextView classCounter, assginmentCounter, ExamCounter;
    TextView StudentProfileName, StudentDepartmentName, StudentSemesterName, StudentSectionName, StudentStudentID;
    private TextView timeText;
    int temp, temp1;
    String semPath, secPath,dayPath,nextDay,deptPath,genPath;

    private FirebaseUser StudentProfile;
    private DatabaseReference reference;
    private String studentUserID;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private long backPressedTime;
    private String lastImage,myImage;

    //Image
    private CircleImageView circleImageView;
    public static final int IMAGE_CODE = 1;
    private Uri imageURI;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private CircleImageView img;
    private FirebaseUser UProfile = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference Ureference = FirebaseDatabase.getInstance().getReference("Student");
    private String UUserID = UProfile.getUid();
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();
    private DatabaseReference first = databaseReference.child("UUU/"+UUserID+"/pimage");
    private Bitmap bitmap;
    private CircleImageView imageView;


    Spinner moreSpinner;
    ArrayList<String> arrayList_moreForStudent;
    ArrayAdapter<String> arrayAdapter_moreForStudent;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_deshboard);

        Thread myThread = null;
        Runnable myRunnableThread = new CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();

        studentBottomNavigationBtn = (BottomNavigationView) findViewById(R.id.studentNavigationBtn);
        img = (CircleImageView)findViewById(R.id.profilePicStudent);

        studentBottomNavigationBtn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner, new studentClassSchedulFragment(semPath, secPath, nextDay,deptPath)).commit();
                if (item.getItemId() == R.id.classScheduleForStudent) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner, new studentClassSchedulFragment(semPath, secPath, nextDay,deptPath)).commit();
                }
                if (item.getItemId() == R.id.assignmentOrExamForStudent) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner, new studentAssignmentFragment(semPath, secPath,deptPath)).commit();
                }
                if (item.getItemId() == R.id.emptyClassForStudent) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner, new studentEmptyClassFragment(nextDay,deptPath)).commit();
                }
                if (item.getItemId() == R.id.teacherInformationForStudent) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner, new studentTeacherInformationFragment(deptPath)).commit();
                }
                return true;
            }
        });
    }

    //time
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {

                    StudentProfile = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Student");
                    studentUserID = StudentProfile.getUid();

                    StudentProfileName = (TextView) findViewById(R.id.studentProfileNameText);
                    StudentDepartmentName = (TextView) findViewById(R.id.StudentdepartmentTextFilde);
                    StudentSemesterName = (TextView) findViewById(R.id.studentSemesterTextFilde);
                    StudentSectionName = (TextView) findViewById(R.id.studentSectionTextFilde3);
                    StudentStudentID = (TextView) findViewById(R.id.stuStudentIDTextFilde2);

                    //Profile
                    reference.child(studentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final UserForStudent userProfileForStudent = snapshot.getValue(UserForStudent.class);

                            if (userProfileForStudent != null) {
                                final String profileName = userProfileForStudent.Name;
                                final String semesterName = userProfileForStudent.sem;
                                final String sectionName = userProfileForStudent.sec;
                                final String departmentName = userProfileForStudent.dept;
                                final String studentIDNumber = userProfileForStudent.ID;
                                final Map<String, Object> mapForView = new HashMap<>();
                                mapForView.put("userName", profileName);
                                FirebaseDatabase.getInstance().getReference("View")
                                        .child(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID))
                                        .updateChildren(mapForView);

                                //path
                                semPath = userProfileForStudent.sem;
                                secPath = userProfileForStudent.sec;
                                deptPath = userProfileForStudent.dept;
                                genPath = userProfileForStudent.gen;

                                StudentProfileName.setText(profileName);
                                StudentDepartmentName.setText("Dept. of " + departmentName);
                                StudentSemesterName.setText("Semester: " + semesterName);
                                StudentSectionName.setText("Section: " + sectionName);
                                StudentStudentID.setText("Student ID: " + studentIDNumber);


                                final Map<String, Object> map = new HashMap<>();
                                map.put("dept", deptPath);

                                FirebaseDatabase.getInstance().getReference("Admin Panel")
                                            .child(studentUserID)
                                            .updateChildren(map);

                                FirebaseDatabase.getInstance().getReference("DefaultImageLoad")
                                        .child(UUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        final String defaultImageLoad = snapshot.child("defaultImageLoad").getValue().toString();
                                        if(defaultImageLoad.equals("ON")){
                                            if(genPath.equals("Male")){
                                                img.setImageResource(R.drawable.man);
                                            }
                                            else if(genPath.equals("Female")){
                                                img.setImageResource(R.drawable.female1);
                                            }
                                        }

                                //profile update Panel
                                Button updateBtn = (Button)findViewById(R.id.moreBtnS);
                                updateBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {
                                        final String [] sem = {"1st","2nd","3rd","4th","5th","6th","7th","8th","9th","10th","11th","12th"};
                                        final String [] sec = {"A","B","C","D","E"};
                                        final String [] dept = {"CSE","EEE","Pharmacy","Public Health","BBA","LLB","Economics","English","Journalism","Sociology","Political Science"};

                                        final DialogPlus dialogPlus = DialogPlus.newDialog(StudentProfileName.getContext())
                                                .setContentHolder(new ViewHolder(R.layout.profile_update_dialog))
                                                .setExpanded(false)
                                                .setMargin(0,0,0,0)
                                                .setPadding(0,0,0,0)
                                                .setGravity(CENTER)
                                                .setCancelable(false)
                                                .create();
                                        View myviwe = dialogPlus.getHolderView();
                                        final EditText name = (EditText)myviwe.findViewById(R.id.nameID);
                                        final EditText ID = (EditText)myviwe.findViewById(R.id.nameID2);
                                        final Spinner semester = (Spinner) myviwe.findViewById(R.id.semesterID);
                                        final Spinner section = (Spinner) myviwe.findViewById(R.id.sectionID);
                                        final Spinner department = (Spinner) myviwe.findViewById(R.id.deptID);
                                        imageView = (CircleImageView) myviwe.findViewById(R.id.imageView2);
                                        final Button changeImage = (Button) myviwe.findViewById(R.id.changeBtn);
                                        final Button logOutBtnForTeacher = (Button)myviwe.findViewById(R.id.studentLogOutBtn);
                                        final Button closeBtnForTeacher = (Button)myviwe.findViewById(R.id.studentCloseBtn);
                                        final Button ProfileUpdateBtnForStudent = (Button)myviwe.findViewById(R.id.updateButtonForStudentProfile);

                                        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(StudentSemesterName.getContext(), android.R.layout.simple_spinner_item,sem);
                                        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        semester.setAdapter(semAdapter);

                                        ArrayAdapter<String> secAdapter = new ArrayAdapter<String>(StudentSectionName.getContext(), android.R.layout.simple_spinner_item,sec);
                                        secAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        section.setAdapter(secAdapter);

                                        ArrayAdapter<String> deptAdapter = new ArrayAdapter<String>(StudentDepartmentName.getContext(), android.R.layout.simple_spinner_item,dept);
                                        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        department.setAdapter(deptAdapter);

                                        name.setText(profileName);
                                        ID.setText(studentIDNumber);
                                        semester.setSelection(semAdapter.getPosition(semesterName));
                                        section.setSelection(secAdapter.getPosition(sectionName));
                                        department.setSelection(deptAdapter.getPosition(departmentName));

                                        dialogPlus.show();
                                        first.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if(defaultImageLoad.equals("ON")){
                                                    if(genPath.equals("Male")){
                                                        imageView.setImageResource(R.drawable.man);
                                                    }
                                                    else if(genPath.equals("Female")){
                                                        imageView.setImageResource(R.drawable.female1);
                                                    }
                                                }else if(defaultImageLoad.equals("OFF")){
                                                    String link = dataSnapshot.getValue(String.class);
                                                    Picasso.get().load(link).into(imageView);
                                                }

                                                changeImage.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Dexter.withActivity(studentDeshboard.this)
                                                                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                                                .withListener(new PermissionListener() {
                                                                    @Override
                                                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                                                        Intent intent=new Intent(Intent.ACTION_PICK);
                                                                        intent.setType("image/*");
                                                                        startActivityForResult(Intent.createChooser(intent,"Select Image File"),1);
                                                                    }

                                                                    @Override
                                                                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                                                    }

                                                                    @Override
                                                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                                                        permissionToken.continuePermissionRequest();
                                                                    }
                                                                }).check();
                                                    }
                                                });

                                                ProfileUpdateBtnForStudent.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        final Map<String, Object> map = new HashMap<>();
                                                        map.put("ID", ID.getText().toString());
                                                        map.put("Name", name.getText().toString());
                                                        map.put("dept", department.getSelectedItem().toString());
                                                        map.put("sec", section.getSelectedItem().toString());
                                                        map.put("sem", semester.getSelectedItem().toString());
                                                        FirebaseDatabase.getInstance().getReference("Student")
                                                                .child(studentUserID)
                                                                .updateChildren(map)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        //Toast.makeText(studentDeshboard.this,"Your Profile is Updated",Toast.LENGTH_LONG).show();
                                                                        dialogPlus.dismiss();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(studentDeshboard.this,"Try Again!, Update your profile",Toast.LENGTH_LONG).show();
                                                                dialogPlus.dismiss();
                                                            }
                                                        });

                                                        if(lastImage != null){
                                                            uploadtofirebase();
                                                        }

                                                    }
                                                });
                                                closeBtnForTeacher.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialogPlus.dismiss();
                                                    }
                                                });

                                                //Log Out
                                                final String android_id = Settings.Secure.getString(StudentProfileName.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                                logOutBtnForTeacher.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfileName.getContext());
                                                        builder.setTitle("Log Out");
                                                        builder.setMessage("Are you sure you want to eixt!");
                                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                FirebaseDatabase.getInstance().getReference("Login Panel")
                                                                        .child(android_id+"/user")
                                                                        .setValue("OFF");

                                                                mAuth.signOut();
                                                                Intent logOutIntent = new Intent(getApplicationContext(), LoginActivity.class);
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

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                                //classCounter
                                final int[] childCount = {0};
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference(deptPath+"/Student Class Schedule").child(userProfileForStudent.sem + "_" + userProfileForStudent.sec + "_" + nextDay);

                                database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                            childCount[0] += snap.getChildrenCount();
                                        }
                                        temp = childCount[0] / 9;
                                        String C = Integer.toString(temp);
                                        classCounter.setText(C);
                                        temp = 0;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                //examCounter
                                final int[] childCount1 = {0};
                                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference(deptPath+"/Student Assignment_Exam").child(userProfileForStudent.sem + "_" + userProfileForStudent.sec);

                                database2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                            childCount1[0] += snap.getChildrenCount();
                                        }
                                        temp1 = childCount1[0] / 5;
                                        String C1 = Integer.toString(temp1);
                                        assginmentCounter.setText(C1);
                                        temp1 = 0;
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(studentDeshboard.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //counter
                    classCounter = (TextView) findViewById(R.id.StudentClassCounterBox);
                    assginmentCounter = (TextView) findViewById(R.id.studentAssignmentCounterBox);
                    ExamCounter = (TextView) findViewById(R.id.studentExamCounterBox);


                    timeText = (TextView)findViewById(R.id.timeDateFild);
                    Date currentTime = Calendar.getInstance().getTime();
                    String myTime = DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);
                    final String[] day = myTime.split(",");
                    Log.d("myLOG", currentTime.toString());
                    Log.d("myLOG", myTime);
                    Log.d("myLOG", day[0].trim());
                    dayPath = day[0];

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
                    String myHours = simpleDateFormat.format(currentTime);
                    timeText.setText(myTime);

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
                    if(studentUserID != null){
                        FirebaseDatabase.getInstance().getReference("Student")
                                .child(studentUserID + "/schedule_change")
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

    //ImageUpdate
    private void uploadtofirebase() {
            if(loadString()!=null){
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(loadString());
                storageReference.delete();
            }
            myImage = "Image"+new Random().nextInt(999999999);
            final ProgressDialog dialog=new ProgressDialog(this);
            dialog.setTitle("Uploading...");
            dialog.show();

            final Map<String, Object> defaultImageLoadMap = new HashMap<>();
            defaultImageLoadMap.put("defaultImageLoad", "OFF");

            FirebaseDatabase.getInstance().getReference("DefaultImageLoad")
                    .child(UUserID)
                    .updateChildren(defaultImageLoadMap);

            FirebaseStorage storage=FirebaseStorage.getInstance();
            final StorageReference upoader = storage.getReference(myImage);

            upoader.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            upoader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dialog.dismiss();
                                    FirebaseDatabase db=FirebaseDatabase.getInstance();
                                    DatabaseReference root=db.getReference("UUU");

                                    dataholder obj = new dataholder(uri.toString());
                                    root.child(UUserID).setValue(obj);
                                    save(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded : "+(int)percent+" %");
                        }
                    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                imageURI=data.getData();
                lastImage = imageURI.toString();
                try {
                    InputStream inputStream=getContentResolver().openInputStream(imageURI);
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                }
                catch (Exception ex) {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    @Override
    protected void onStart() {
        super.onStart();
        first.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    class dataholder
    {
        String pimage;

        public dataholder() {
        }

        public dataholder(String pimage) {

            this.pimage = pimage;
        }

        public String getPimage() {
            return pimage;
        }

        public void setPimage(String pimage) {
            this.pimage = pimage;
        }
    }

    private static final int TIME_INTERVAL = 2000;
    private long backPressed;

    @Override
    public void onBackPressed() {
        if(backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            super.onBackPressed();
            moveTaskToBack(true);
            return;
        }else {
            Toast.makeText(getBaseContext(), "Please click BACK again to exit!", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    private void save(String myString) {
        SharedPreferences sharedPreferences = getSharedPreferences("myString", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastString",myString);
        editor.commit();
    }
    private String loadString(){
        SharedPreferences sharedPreferences = getSharedPreferences("myString", Context.MODE_PRIVATE);
        String myString = sharedPreferences.getString("lastString",null);
        return myString;
    }
}
