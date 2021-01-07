package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThankYouActivityTeacher extends AppCompatActivity implements View.OnClickListener {

    private Button ThanksBtn;
    CircleImageView circleImage;
    public static final int IMAGE_CODE=1;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAtuh;
    private Uri imageUri;
    private Bitmap bitmap;
    private String myUri = "";
    private StorageTask uplodeTask;
    private StorageReference storageProfilePicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_teacher);
        ThanksBtn = (Button) findViewById(R.id.getStartedBtn1);
        ThanksBtn.setOnClickListener(this);
        Toast.makeText(ThankYouActivityTeacher.this, "Please check Email for verification.", Toast.LENGTH_SHORT).show();


        mAtuh = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Picture");

        circleImage = (CircleImageView) findViewById(R.id.UserImage1);
        String teacher = null;
        String teacherGen = null;
        teacher = getIntent().getStringExtra("mag1");
        teacherGen = getIntent().getStringExtra("mag2");

        String imagePath = null;
        if (teacherGen.equals("Male")) {
            imagePath = "male_teacher.png";
        } else if (teacherGen.equals("Female")) {
            imagePath = "female.jfif";
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl("gs://scheduler-627c4.appspot.com/profile pic").child(imagePath);

        File file = null;
        try {
            file = File.createTempFile("image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalFile = file;
        final String finalTeacherGen = teacherGen;
        storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                circleImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(finalTeacherGen.equals("Male")){
                    circleImage.setImageResource(R.drawable.male_teacher);
                }
                else if(finalTeacherGen.equals("Female")){
                    circleImage.setImageResource(R.drawable.female1);
                }
                //Toast.makeText(ThankYouActivityTeacher.this, "Image Failed to Load", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.getStartedBtn1) {
            Intent LoginIntent = new Intent(ThankYouActivityTeacher.this, LoginActivity.class);
            startActivity(LoginIntent);
        }
    }
}