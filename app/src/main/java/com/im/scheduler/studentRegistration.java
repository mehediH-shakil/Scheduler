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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class studentRegistration extends AppCompatActivity  implements View.OnClickListener {

    //Registration Button Variable
    private Button SignUp;
    private EditText editStudentID, editStudentName, editStudentEmail, editStudentPhone, editStudentPassword, editStudentConfirmPassword;
   // private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    //Log in Button Variable
    private TextView StudentLogInBtn;

    //Spinner Variable
    Spinner SemesterName, SectionName, DepartmentName, GenderName;

    ArrayList<String> arrayList_semester;
    ArrayList<String> arrayList_section;
    ArrayList<String> arrayList_department;
    ArrayList<String> arrayList_Gender;
    ArrayAdapter<String> arrayAdapter_semester;
    ArrayAdapter<String> arrayAdapter_section;
    ArrayAdapter<String> arrayAdapter_department;
    ArrayAdapter<String> arrayAdapter_gender;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        //Student Log in Button
        StudentLogInBtn = (TextView)findViewById(R.id.StudentLogInBtn);
        StudentLogInBtn.setOnClickListener(this);

        //semester spinner
        SemesterName = (Spinner)findViewById(R.id.semesterSpinner);

        arrayList_semester = new ArrayList<>();
        arrayList_semester.add(" 1st");
        arrayList_semester.add(" 2nd");
        arrayList_semester.add(" 3rd");
        arrayList_semester.add(" 4th");
        arrayList_semester.add(" 5th");
        arrayList_semester.add(" 6th");
        arrayList_semester.add(" 7th");
        arrayList_semester.add(" 8th");
        arrayList_semester.add(" 9th");
        arrayList_semester.add(" 10th");
        arrayList_semester.add(" 11th");
        arrayList_semester.add(" 12th");

        arrayAdapter_semester = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_semester);
        SemesterName.setAdapter(arrayAdapter_semester);


        //section spinner
        SectionName = (Spinner)findViewById(R.id.sectionSpinner);

        arrayList_section = new ArrayList<>();
        arrayList_section.add(" A");
        arrayList_section.add(" B");
        arrayList_section.add(" C");
        arrayList_section.add(" D");
        arrayList_section.add(" E");

        arrayAdapter_section = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_section);
        SectionName.setAdapter(arrayAdapter_section);

        //Department Spinner
        DepartmentName = (Spinner)findViewById(R.id.studentDepartmentSpinner);

        arrayList_department = new ArrayList<>();
        arrayList_department.add(" CSE");
        arrayList_department.add(" EEE");
        arrayList_department.add(" Pharmacy");
        arrayList_department.add(" Public Health");
        arrayList_department.add(" BBA");
        arrayList_department.add(" LLB");
        arrayList_department.add(" Economics");
        arrayList_department.add(" English");
        arrayList_department.add(" Journalism");
        arrayList_department.add(" Sociology");
        arrayList_department.add(" Political Science");


        arrayAdapter_department = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_department);
        DepartmentName.setAdapter(arrayAdapter_department);

        //Gender Spinner
        GenderName = (Spinner)findViewById(R.id.studentGenderSpinner);

        arrayList_Gender = new ArrayList<>();
        arrayList_Gender.add(" Male");
        arrayList_Gender.add(" Female");
        arrayList_Gender.add(" Others");

        arrayAdapter_gender = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_Gender);
        GenderName.setAdapter(arrayAdapter_gender);


        //Registration Activity
        mAuth = FirebaseAuth.getInstance();

        SignUp = (Button)findViewById(R.id.signupbtnStudent);
        SignUp.setOnClickListener(this);

        editStudentID = (EditText)findViewById(R.id.editStudentName);
        editStudentName = (EditText)findViewById(R.id.editNameStudent);
        editStudentEmail = (EditText)findViewById(R.id.editEmailStudent);
        editStudentPhone = (EditText)findViewById(R.id.editPhoneStudent);
        editStudentPassword= (EditText)findViewById(R.id.editPasswordStudent);
        editStudentConfirmPassword = (EditText)findViewById(R.id.editPasswordStudent2);
        SemesterName = (Spinner)findViewById(R.id.semesterSpinner);
        SectionName = (Spinner)findViewById(R.id.sectionSpinner);
        GenderName = (Spinner)findViewById(R.id.studentGenderSpinner);
        DepartmentName = (Spinner)findViewById(R.id.studentDepartmentSpinner);


        //Ads
//        AdView mAdView = (AdView)findViewById(R.id.adViewSR);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.StudentLogInBtn) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.signupbtnStudent){
            registerUser();
        }
    }



    private void registerUser(){
        final String ID = editStudentID.getText().toString().trim();
        final String Name = editStudentName.getText().toString().trim();
        final String Email = editStudentEmail.getText().toString().trim();
        final String Phone = editStudentPhone.getText().toString().trim();
        final String Password = editStudentPassword.getText().toString().trim();
        final String ConfirmPassword = editStudentConfirmPassword.getText().toString().trim();
        final String sem = SemesterName.getSelectedItem().toString().trim();
        final String sec = SectionName.getSelectedItem().toString().trim();
        final String gen = GenderName.getSelectedItem().toString().trim();
        final String dept = DepartmentName.getSelectedItem().toString().trim();

        if(ID.isEmpty()){
            editStudentID.setError("ID is required!");
            editStudentID.requestFocus();
        }
        if(Name.isEmpty()){
            editStudentName.setError("Name is required!");
            editStudentName.requestFocus();
        }
        if(Email.isEmpty()){
            editStudentEmail.setError("Email is required!");
            editStudentEmail.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editStudentEmail.setError("Please provide valid email!");
            editStudentEmail.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            editStudentPassword.setError("Password is required!");
            editStudentPassword.requestFocus();
        }
        if(Password.length() <= 5){
            editStudentPassword.setError("Min password length should be 6 characters!");
            editStudentPassword.requestFocus();
            return;
        }
        if(ConfirmPassword.isEmpty()){
            editStudentConfirmPassword.setError("Confirm Password is required!");
            editStudentConfirmPassword.requestFocus();
            return;
        }
        if(!ConfirmPassword.equals(Password)){
            editStudentConfirmPassword.setError("Password not matching!");
            editStudentConfirmPassword.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Email, ConfirmPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Users users = new Users(ID, Name, Email, Phone, sem, sec, gen, dept);

                    FirebaseDatabase.getInstance().getReference("Student")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                helperClass helper = new helperClass("student","none",dept);
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
                                                        Intent intent = new Intent(studentRegistration.this,ThankYouSignUpActivity.class);
                                                        intent.putExtra("mag3S", "student");
                                                        intent.putExtra("mag4S", gen);
                                                        startActivity(intent);
                                                    }else{
                                                        Toast.makeText(studentRegistration.this, "Something Wrong!" , Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText( studentRegistration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText( studentRegistration.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }
    public class Users {
        public String ID, Name, Email, Phone, sem, sec, gen, dept;
        public Users(){
        }
        public Users(String ID, String Name, String Email, String Phone, String sem, String sec, String gen, String dept){
            this.ID = ID;
            this.Name  = Name;
            this.Email = Email;
            this.Phone = Phone;
            this.sec = sec;
            this.sem = sem;
            this.dept = dept;
            this.gen = gen;
        }
    }
}
class helperClass{
    String user,status,dept;

    public helperClass() {
    }

    public helperClass(String user, String status, String dept) {
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

