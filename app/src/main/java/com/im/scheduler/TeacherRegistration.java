package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TeacherRegistration extends AppCompatActivity implements View.OnClickListener {
    //Registration Button Variable
    private Button SignUp;
    private EditText editTeacherEmployeeID, editTeacherName, editTeacherEmail, editTeacherPhone, editTeacherPassword, editTeacherConfirmPassword;
    // private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    //Log in Button Variable
    private TextView TeacherLogInBtn;

    //Spinner Variable
    Spinner designationName, TeacherGenderName, TeacherDepartmentName;

    ArrayList<String> arrayList_designation;
    ArrayList<String> arrayList_TeacherGender;
    ArrayList<String> arrayList_Teacherdepartment;
    ArrayAdapter<String> arrayAdapter_designation;
    ArrayAdapter<String> arrayAdapter_TeacherGender;
    ArrayAdapter<String> arrayAdapter_Teacherdepartment;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_registration);

        //Student Log in Button
        TeacherLogInBtn = (TextView)findViewById(R.id.TeacherLogInBtn);
        TeacherLogInBtn.setOnClickListener(this);

        //designation spinner
        designationName = (Spinner)findViewById(R.id.designationSpinner);

        arrayList_designation = new ArrayList<>();
        arrayList_designation.add(" Professor");
        arrayList_designation.add(" Associate Professor");
        arrayList_designation.add(" Assistant Professor");
        arrayList_designation.add(" Lecturer");

        arrayAdapter_designation = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_designation);
        designationName.setAdapter(arrayAdapter_designation);


        //Teacher Gender spinner
        TeacherGenderName = (Spinner)findViewById(R.id.genSpinner);

        arrayList_TeacherGender = new ArrayList<>();
        arrayList_TeacherGender.add(" Male");
        arrayList_TeacherGender.add(" Female");
        arrayList_TeacherGender.add(" Others");

        arrayAdapter_TeacherGender = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_TeacherGender);
        TeacherGenderName.setAdapter(arrayAdapter_TeacherGender);

        //Teacher Department Spinner
        TeacherDepartmentName = (Spinner)findViewById(R.id.TeacherDepartmentSpinner);

        arrayList_Teacherdepartment = new ArrayList<>();
        arrayList_Teacherdepartment.add(" CSE");
        arrayList_Teacherdepartment.add(" EEE");
        arrayList_Teacherdepartment.add(" Pharmacy");
        arrayList_Teacherdepartment.add(" Public Health");
        arrayList_Teacherdepartment.add(" BBA");
        arrayList_Teacherdepartment.add(" LLB");
        arrayList_Teacherdepartment.add(" Economics");
        arrayList_Teacherdepartment.add(" English");
        arrayList_Teacherdepartment.add(" Journalism");
        arrayList_Teacherdepartment.add(" Sociology");
        arrayList_Teacherdepartment.add(" Political Science");

        arrayAdapter_Teacherdepartment = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_Teacherdepartment);
        TeacherDepartmentName.setAdapter(arrayAdapter_Teacherdepartment);


        //Registration Activity
        mAuth = FirebaseAuth.getInstance();

        SignUp = (Button)findViewById(R.id.signupbtnTeacher);
        SignUp.setOnClickListener(this);

        editTeacherName = (EditText)findViewById(R.id.editNameTeacher);
        editTeacherEmail = (EditText)findViewById(R.id.editEmailTeacher);
        editTeacherPhone = (EditText)findViewById(R.id.editPhoneTeacher);
        editTeacherPassword= (EditText)findViewById(R.id.editPasswordTeacher);
        editTeacherConfirmPassword = (EditText)findViewById(R.id.editPasswordTeacher2);
        designationName = (Spinner)findViewById(R.id.designationSpinner);
        TeacherGenderName = (Spinner)findViewById(R.id.genSpinner);
        TeacherDepartmentName = (Spinner)findViewById(R.id.TeacherDepartmentSpinner);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.TeacherLogInBtn) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.signupbtnTeacher){
            registerUser();
        }
    }



    private void registerUser(){
        final String Name = editTeacherName.getText().toString().trim();
        final String Email = editTeacherEmail.getText().toString().trim();
        final String Phone = editTeacherPhone.getText().toString().trim();
        final String Password = editTeacherPassword.getText().toString().trim();
        final String ConfirmPassword = editTeacherConfirmPassword.getText().toString().trim();
        final String des = designationName.getSelectedItem().toString().trim();
        final String gen = TeacherGenderName.getSelectedItem().toString().trim();
        final String dept = TeacherDepartmentName.getSelectedItem().toString().trim();
        String Path = Email.replaceAll("[^a-zA-Z0-9]","_");
        final String myPath = Path;


        if(Name.isEmpty()){
            editTeacherName.setError("Name is required!");
            editTeacherName.requestFocus();
        }
        if(Email.isEmpty()){
            editTeacherEmail.setError("Email is required!");
            editTeacherEmail.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTeacherEmail.setError("Please provide valid email!");
            editTeacherEmail.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            editTeacherPassword.setError("Password is required!");
            editTeacherPassword.requestFocus();
        }
        if(Password.length() <= 5){
            editTeacherPassword.setError("Min password length should be 6 characters!");
            editTeacherPassword.requestFocus();
            return;
        }
        if(ConfirmPassword.isEmpty()){
            editTeacherConfirmPassword.setError("Confirm Password is required!");
            editTeacherConfirmPassword.requestFocus();
            return;
        }
        if(!ConfirmPassword.equals(Password)){
            editTeacherConfirmPassword.setError("Password not matching!");
            editTeacherConfirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(Email, ConfirmPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            TeacherRegistration.User user = new TeacherRegistration.User(Name, Email, Phone, des, gen, dept," "," "," "," "," ");

                            FirebaseDatabase.getInstance().getReference("Teacher")
                                    .child(myPath)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        helperClass helper = new helperClass("none","none",dept);
                                        FirebaseDatabase.getInstance().getReference("Admin Panel")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Intent TeacherSignUp = new Intent(TeacherRegistration.this,ThankYouActivityTeacher.class);
                                                                TeacherSignUp.putExtra("mag1", "teacher");
                                                                TeacherSignUp.putExtra("mag2", gen);
                                                                startActivity(TeacherSignUp);
                                                            }else{
                                                                Toast.makeText(TeacherRegistration.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText( TeacherRegistration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText( TeacherRegistration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });

    }
    public class User {
        public String name, email, phone, des, gen, dept, schedule_change,assignment_change,exam_change,empty_room_timePath,empty_room_dayPath;

        public User() {
        }

        public User(String name, String email, String phone, String des, String gen, String dept, String schedule_change, String assignment_change, String exam_change, String empty_room_timePath, String empty_room_dayPath) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.des = des;
            this.gen = gen;
            this.dept = dept;
            this.schedule_change = schedule_change;
            this.assignment_change = assignment_change;
            this.exam_change = exam_change;
            this.empty_room_timePath = empty_room_timePath;
            this.empty_room_dayPath = empty_room_dayPath;
        }
    }
}
class helperClassForTeacher{
    String user,status,dept;

    public helperClassForTeacher() {
    }

    public helperClassForTeacher(String user, String status,String dept) {
        this.user = user;
        this.status = status;
        this.dept = dept;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
