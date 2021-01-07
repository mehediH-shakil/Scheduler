package com.im.scheduler.AdminActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.im.scheduler.R;

import java.util.PriorityQueue;

public class AdminRoomAddActivity extends AppCompatActivity {
    private Button addRoomBtn;
    EditText RoomText,TimeText;
    Spinner dayText;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room_add);

        RoomText = (EditText)findViewById(R.id.ROOMNO);
        TimeText =(EditText)findViewById(R.id.TimeID);
        dayText = (Spinner)findViewById(R.id.dayID);

        mAdView = (AdView)findViewById(R.id.adView14);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String [] Day = {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,Day);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayText.setAdapter(adapter);

        addRoomBtn = (Button)findViewById(R.id.AdimRoomaddBtn);
        addRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Admin Panel")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String dept = snapshot.child("dept").getValue().toString();

                        final String room = RoomText.getText().toString().trim();
                        final String time = TimeText.getText().toString().trim();
                        final String day = dayText.getSelectedItem().toString().trim();

                        emptyModel emptymodel = new emptyModel(day,room,time);
                        FirebaseDatabase.getInstance().getReference(dept+"/Empty Room")
                                .child(day+"/"+time.replaceAll("[^a-zA-Z0-9]","")+"_"+room)
                                .setValue(emptymodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                }else{
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    class emptyModel{
        String day,room,time;

        public emptyModel() {
        }

        public emptyModel(String day, String room, String time) {
            this.day = day;
            this.room = room;
            this.time = time;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}


