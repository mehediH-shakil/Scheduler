package com.im.scheduler.TeacherFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;

public class TeacherEmptyClassFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static String m;
    private SearchView mSearch;
    RecyclerView recviewForEmpty ;
    teacherEmptyAdapter myAdapterForEmpty ;
    private static String mailPath,emptydayPath,emptytimePath,s3;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TeacherEmptyClassFragment teacherEmptyClassFragment;

    public TeacherEmptyClassFragment() {
        // Required empty public constructor
    }

    public TeacherEmptyClassFragment(String mailpath, String emptyDayPath, String emptyTimePath, String deptPath) {
        mailPath = mailpath;
        emptydayPath = emptyDayPath;
        emptytimePath = emptyTimePath;
        s3 = deptPath;
    }

    public static TeacherEmptyClassFragment newInstance(String param1, String param2) {
        TeacherEmptyClassFragment fragment = new TeacherEmptyClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static void putArguments(Bundle bundle) {
        if (bundle != null) {
            String myInt = bundle.getString("key");
            m = myInt;
        }
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
        final View view = inflater.inflate(R.layout.fragment_teacher_empty_class, container, false);
        recviewForEmpty  = (RecyclerView)view.findViewById(R.id.teacherEmptyRecycleView);
        recviewForEmpty.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<emptyModel> TeacherOptionsForEmpty  =
                new FirebaseRecyclerOptions.Builder<emptyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(s3+"/Empty Room/"+emptydayPath), emptyModel.class)
                        .build();
        myAdapterForEmpty = new teacherEmptyAdapter(TeacherOptionsForEmpty);
        recviewForEmpty.setAdapter(myAdapterForEmpty);

        mSearch = (SearchView)view.findViewById(R.id.search_barForTeacherEmptyRoom);
        mSearch.setOnQueryTextListener(this);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        myAdapterForEmpty.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdapterForEmpty.stopListening();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        search(s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        search(s);
        return false;
    }

    private void search(String s) {
        FirebaseRecyclerOptions<emptyModel> optionsForSearch=
                new FirebaseRecyclerOptions.Builder<emptyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(s3+"/Empty Room/"+emptydayPath).orderByChild("room").startAt(s).endAt(s+"\uf8ff"),emptyModel.class)
                        .build();

        myAdapterForEmpty = new teacherEmptyAdapter(optionsForSearch);
        myAdapterForEmpty.startListening();
        recviewForEmpty.setAdapter(myAdapterForEmpty);
    }
}

class teacherEmptyAdapter extends FirebaseRecyclerAdapter<emptyModel,teacherEmptyAdapter.myviewholder>{

    public teacherEmptyAdapter(@NonNull FirebaseRecyclerOptions<emptyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull teacherEmptyAdapter.myviewholder holder, int position, @NonNull emptyModel model) {
        holder.emptyRoomText.setText("Room: "+model.getRoom());
        holder.emptyTimeText.setText("Time: "+model.getTime());
    }

    @NonNull
    @Override
    public teacherEmptyAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_empty_class,parent,false);
        return new teacherEmptyAdapter.myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView emptyRoomText,emptyTimeText;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            emptyRoomText = (TextView)itemView.findViewById(R.id.roomNo);
            emptyTimeText = (TextView)itemView.findViewById(R.id.timeFild);
        }
    }
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
















/*
package com.im.scheduler.TeacherFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class TeacherEmptyClassFragment extends Fragment {
    private static String m;
    RecyclerView recviewForEmpty ;
    teacherEmptyAdapter myAdapterForEmpty ;
    private static String mailPath,emptydayPath,emptytimePath;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TeacherEmptyClassFragment() {
        // Required empty public constructor
    }

    public TeacherEmptyClassFragment(String mailpath, String emptyDayPath, String emptyTimePath) {
        mailPath = mailpath;
        emptydayPath = emptyDayPath;
        emptytimePath = emptyTimePath;
    }

    public static TeacherEmptyClassFragment newInstance(String param1, String param2) {
        TeacherEmptyClassFragment fragment = new TeacherEmptyClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static void putArguments(Bundle bundle) {
        if (bundle != null) {
            String myInt = bundle.getString("key");
            m = myInt;
        }
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
        final View view = inflater.inflate(R.layout.fragment_teacher_empty_class, container, false);
        recviewForEmpty  = (RecyclerView)view.findViewById(R.id.teacherEmptyRecycleView);
        recviewForEmpty.setLayoutManager(new LinearLayoutManager(getContext()));

        String [] Day = {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
        Spinner daySpinner = (Spinner) view.findViewById(R.id.emptyRoomDayForTeacher);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,Day);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        String [] time = {"9.00 AM - 10.25 AM","10.30 AM -11.55 AM","12.00 PM - 1.25 PM","1.30 PM - 2.55 PM","3.00 PM - 4.25 PM","4.30 PM - 5.55 PM"};
        Spinner timeSpinner = (Spinner) view.findViewById(R.id.emptyRoomTimeForTeacher);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,time);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,int position, long arg3) {
                FirebaseDatabase.getInstance().getReference("Teacher")
                        .child(mailPath+"/empty_room_dayPath")
                        .setValue(parent.getItemAtPosition(position).toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                        }else{
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,int position, long arg3) {
                FirebaseDatabase.getInstance().getReference("Teacher")
                        .child(mailPath+"/empty_room_timePath")
                        .setValue(parent.getItemAtPosition(position).toString().replaceAll("[^a-zA-Z0-9]","")).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                        }else{
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        TextView gg = (TextView)view.findViewById(R.id.TEXT);
            gg.setText(m);

        FirebaseRecyclerOptions<teacherEmptyModel> TeacherOptionsForEmpty  =
                new FirebaseRecyclerOptions.Builder<teacherEmptyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Empty Room/"+emptydayPath+"/"+emptytimePath), teacherEmptyModel.class)
                        .build();
        myAdapterForEmpty = new teacherEmptyAdapter(TeacherOptionsForEmpty);
        recviewForEmpty.setAdapter(myAdapterForEmpty);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        myAdapterForEmpty.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdapterForEmpty.stopListening();
    }
}

class teacherEmptyAdapter extends FirebaseRecyclerAdapter<teacherEmptyModel,teacherEmptyAdapter.myviewholder>{

    public teacherEmptyAdapter(@NonNull FirebaseRecyclerOptions<teacherEmptyModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull teacherEmptyAdapter.myviewholder holder, int position, @NonNull teacherEmptyModel model) {
        if(model.getStatus().equals("ON")) {
            holder.emptyRoomText.setText(model.getRoom());
        }else{
            holder.emptyRoomText.setText("NO");
        }
    }

    @NonNull
    @Override
    public teacherEmptyAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_empty_class,parent,false);
        return new teacherEmptyAdapter.myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView emptyRoomText;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            emptyRoomText = (TextView)itemView.findViewById(R.id.roomNo);
        }
    }
}

class teacherEmptyModel{
    String status,Room;

    public teacherEmptyModel() {
    }

    public teacherEmptyModel(String status, String room) {
        this.status = status;
        Room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }
}
 */