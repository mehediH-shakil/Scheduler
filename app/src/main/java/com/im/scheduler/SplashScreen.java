package com.im.scheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    int SPLASH_TIMER = 5000;
    private int counter = 0;
    private FirebaseAuth mAuth;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sreen);
        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd [HH:mm:ss a]");
        String myTime = simpleDateFormat.format(Calendar.getInstance().getTime());

        final View backgroundImage = findViewById(R.id.imageView);
        final int lastCounter = loadCounter();
        final Map<String, Object> map = new HashMap<>();
        map.put("view",lastCounter);
        map.put("lastViewTime",myTime);
        FirebaseDatabase.getInstance().getReference("View")
                .child(android_id)
                .setValue(map);
        if(lastCounter==10000000){
            counter = 1;
            save(1);
        }
        counter = loadCounter() + 1;
        save(counter);

        if(lastCounter == 0){
            FirebaseDatabase.getInstance().getReference("Login Panel")
                    .child(android_id)
                    .child("user")
                    .setValue("OFF");
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        //Animation
        Animation sideAnim = AnimationUtils.loadAnimation(this, R.anim.slide_anim);

        //setAnimation on elements
        backgroundImage.setAnimation(sideAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(lastCounter >= 1){

                    final DatabaseReference referenceForLoginPanel = FirebaseDatabase.getInstance().getReference("Login Panel")
                            .child(android_id);
                    referenceForLoginPanel.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String status = snapshot.child("user").getValue().toString();
                            if(status.equals("studentON")){
                                Intent StudentIntent = new Intent(getApplicationContext(),studentDeshboard.class);
                                startActivity(StudentIntent);
                            }else if(status.equals("teacherON")){
                                Intent TeacherIntent = new Intent(getApplicationContext(), teacherDeshboardActivity.class);
                                startActivity(TeacherIntent);
                            }else if(status.equals("OFF")){
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }else if (lastCounter == 0){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
            }
        },SPLASH_TIMER);

        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void save(int counter) {
        SharedPreferences sharedPreferences = getSharedPreferences("counter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastCounter",counter);
        editor.commit();
    }
    private int loadCounter(){
        SharedPreferences sharedPreferences = getSharedPreferences("counter", Context.MODE_PRIVATE);
        int counter = sharedPreferences.getInt("lastCounter",0);
        return counter;
    }
}


/*

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Email Path")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Path = snapshot.child("Path").getValue().toString();
                Intent TeacherIntent = new Intent(getApplicationContext(), teacherDeshboardActivity.class);
                TeacherIntent.putExtra("myPath", Path);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Login Panel")
                                .child(android_id);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String status = snapshot.child("user").getValue().toString();
                                if(status.equals("studentON")){
                                    Intent StudentIntent = new Intent(getApplicationContext(),studentDeshboard.class);
                                    startActivity(StudentIntent);
                                }else if(status.equals("teacherON")){
                                    Intent TeacherIntent = new Intent(getApplicationContext(), teacherDeshboardActivity.class);
                                    startActivity(TeacherIntent);
                                }else if(status.equals("OFF")){
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
 */