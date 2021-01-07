package com.im.scheduler.StudentFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;

public class studentEmptyClassFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchView mSearch;
    RecyclerView recviewForEmpty ;
    studentEmptyAdapter myAdapterForEmpty ;
    private String DayPath,s3;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public studentEmptyClassFragment() {
    }

    public studentEmptyClassFragment(String dayPath, String deptPath) {
        DayPath = dayPath;
        s3 = deptPath;
    }

// TODO: Rename and change types and number of parameters
public static studentEmptyClassFragment newInstance(String param1, String param2) {
    studentEmptyClassFragment fragment = new studentEmptyClassFragment();
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
    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_student_empty_class, container, false);
        recviewForEmpty  = (RecyclerView)view.findViewById(R.id.recycleViweForstudentEmptyClass);
        recviewForEmpty.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<emptyModelForStudent> studentOptionsForEmpty  =
                new FirebaseRecyclerOptions.Builder<emptyModelForStudent>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(s3+"/Empty Room/"+DayPath), emptyModelForStudent.class)
                        .build();
        myAdapterForEmpty = new studentEmptyAdapter(studentOptionsForEmpty);
        recviewForEmpty.setAdapter(myAdapterForEmpty);

        mSearch = (SearchView)view.findViewById(R.id.search_barForStudentEmptyRoom);
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
        FirebaseRecyclerOptions<emptyModelForStudent> optionsForSearch=
                new FirebaseRecyclerOptions.Builder<emptyModelForStudent>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Empty Room/"+DayPath).orderByChild("room").startAt(s).endAt(s+"\uf8ff"),emptyModelForStudent.class)
                        .build();

        myAdapterForEmpty = new studentEmptyAdapter(optionsForSearch);
        myAdapterForEmpty.startListening();
        recviewForEmpty.setAdapter(myAdapterForEmpty);
    }
}

class studentEmptyAdapter extends FirebaseRecyclerAdapter<emptyModelForStudent,studentEmptyAdapter.myviewholder> {

    public studentEmptyAdapter(@NonNull FirebaseRecyclerOptions<emptyModelForStudent> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull studentEmptyAdapter.myviewholder holder, int position, @NonNull emptyModelForStudent model) {
        holder.emptyRoomText.setText("Room: "+model.getRoom());
        holder.emptyTimeText.setText("Time: "+model.getTime());
    }

    @NonNull
    @Override
    public studentEmptyAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_empty_class,parent,false);
        return new studentEmptyAdapter.myviewholder(view);
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

class emptyModelForStudent{
    String day,room,time;

    public emptyModelForStudent() {
    }

    public emptyModelForStudent(String day, String room, String time) {
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


package com.im.scheduler.StudentFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class studentEmptyClassFragment extends Fragment {


    RecyclerView emptyStudent ;
    emptyStudentAdepter emptyStudentAdepter ;
    private Button searchForEmpty;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public studentEmptyClassFragment() {
        // Required empty public constructor
    }

// TODO: Rename and change types and number of parameters
public static studentEmptyClassFragment newInstance(String param1, String param2) {
    studentEmptyClassFragment fragment = new studentEmptyClassFragment();
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
    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_student_empty_class, container, false);

        String [] Day = {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday"};
        Spinner daySpinner = (Spinner) view.findViewById(R.id.emptyRoomDay);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,Day);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        String [] time = {"9.00 AM - 10.25 AM","10.30 AM -11.55 AM","12.00 PM - 1.25 PM","1.30 PM - 2.55 PM","3.00 PM - 4.25 PM","4.30 PM - 5.55 PM"};
        Spinner timeSpinner = (Spinner) view.findViewById(R.id.emptyRoomTime);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,time);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);


        emptyStudent  = (RecyclerView)view.findViewById(R.id.recycleViweForstudentEmptyClass);
        emptyStudent.setLayoutManager(new LinearLayoutManager(getContext()));

        final String dayPath = daySpinner.getSelectedItem().toString().trim();
        final String timePath1 = timeSpinner.getSelectedItem().toString().trim();
        String timePath2 = timePath1.replaceAll("[^a-zA-Z0-9]","");
        final String timePath = timePath2;

        FirebaseRecyclerOptions<emptyStudentModel> optionsForEmpty  =
                new FirebaseRecyclerOptions.Builder<emptyStudentModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Empty Room/"+"Tuesday"+"/"+"1030AM1155AM"),emptyStudentModel.class)
                        .build();
        emptyStudentAdepter = new emptyStudentAdepter(optionsForEmpty);
        emptyStudent.setAdapter(emptyStudentAdepter);

        searchForEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(dayPath,timePath);
            }
        });

        return view;
    }

    private void search(String dayPath, String timePath) {


    }

    @Override
    public void onStart() {
        super.onStart();
        emptyStudentAdepter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        emptyStudentAdepter.stopListening();
    }
}
class emptyStudentAdepter extends FirebaseRecyclerAdapter<emptyStudentModel,emptyStudentAdepter.myviewholder>{

    public emptyStudentAdepter(@NonNull FirebaseRecyclerOptions<emptyStudentModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull emptyStudentModel model) {
        holder.roomText.setText(model.getRoom());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_class_scledule,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView roomText;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            roomText = (TextView) itemView.findViewById(R.id.roomNo);
        }
    }
}

class emptyStudentModel {
    String status,Room;

    public emptyStudentModel() {
    }

    public emptyStudentModel(String status, String room) {
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





















FirebaseRecyclerOptions<EmptyModel> optionsForEmpty  =
                new FirebaseRecyclerOptions.Builder<EmptyModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Empty Room/Monday/1030AM1155AM"), EmptyModel.class)
                        .build();
        myAdapterForEmpty = new emptyAdepter(optionsForEmpty);
        emptyRecycleView .setAdapter(myAdapterForEmpty);

class emptyAdepter extends FirebaseRecyclerAdapter<EmptyModel,emptyAdepter.myviewholder> {

    public emptyAdepter(@NonNull FirebaseRecyclerOptions<EmptyModel> optionsForEmpty ) {
        super(optionsForEmpty);

    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull EmptyModel model) {
        holder.roomText.setText(model.getStutas());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_empty_class,parent,false);
        return new myviewholder(view);
    }

    public static class myviewholder  extends RecyclerView.ViewHolder{
        TextView roomText;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            roomText = (TextView)itemView.findViewById(R.id.roomNo);
        }
    }
}
class EmptyModel {
    String stutas;

    public EmptyModel() {
    }

    public EmptyModel(String stutas) {
        this.stutas = stutas;
    }

    public String getStutas() {
        return stutas;
    }

    public void setStutas(String stutas) {
        this.stutas = stutas;
    }
}

 */
