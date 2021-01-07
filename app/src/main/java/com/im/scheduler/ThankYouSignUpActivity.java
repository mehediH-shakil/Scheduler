package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThankYouSignUpActivity extends AppCompatActivity{
    private Button ThanksBtn;
    CircleImageView circleImage;
    public static final int IMAGE_CODE=1;
    public static int c = 0;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAtuh;
    private Uri imageUri;
    private Bitmap bitmap;
    private String myUri = "";
    private StorageTask uplodeTask;
    private StorageReference storageProfilePicsRef;
    private FirebaseUser UProfile;
    private String UUserID;
    private String lastImage;
    private Button imageChangerBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_sign_up);
        ThanksBtn = (Button)findViewById(R.id.getStartedBtn);
       // Toast.makeText(ThankYouSignUpActivity.this, "Please check Email for verification.", Toast.LENGTH_SHORT).show();


        mAtuh = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Picture");

        circleImage = (CircleImageView)findViewById(R.id.UserImage);
        UProfile = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        UUserID = UProfile.getUid();

        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c++;
                Dexter.withActivity(ThankYouSignUpActivity.this)
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
        imageChangerBtn = (Button)findViewById(R.id.changeImageBtn);
        imageChangerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c++;
                Dexter.withActivity(ThankYouSignUpActivity.this)
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
        ThanksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastImage != null && circleImage.toString().trim() != null){
                    uploadtofirebase();
                    FirebaseDatabase.getInstance().getReference("DefaultImageLoad")
                            .child(UUserID+"/defaultImageLoad")
                            .setValue("OFF");
                }else {
                    FirebaseDatabase.getInstance().getReference("DefaultImageLoad")
                            .child(UUserID+"/defaultImageLoad")
                            .setValue("ON");
                }
                Intent LoginIntent = new Intent(ThankYouSignUpActivity.this, LoginActivity.class);
                startActivity(LoginIntent);

            }
        });
        if(c==0 || lastImage==null){
            String studentGen = getIntent().getStringExtra("mag4S");
            if(studentGen.equals("Male")){
                circleImage.setImageResource(R.drawable.man);
            }
            else if(studentGen.equals("Female")){
                circleImage.setImageResource(R.drawable.female1);
            }
        }
    }

    private void uploadtofirebase() {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploading...");
        dialog.show();


        FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference upoader = storage.getReference("Image"+new Random().nextInt(999999999));

        upoader.putFile(imageUri)
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
                imageUri=data.getData();
                lastImage = imageUri.toString();
                try {
                    InputStream inputStream=getContentResolver().openInputStream(imageUri);
                    bitmap= BitmapFactory.decodeStream(inputStream);
                    circleImage.setImageBitmap(bitmap);
                }
                catch (Exception ex) {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
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
