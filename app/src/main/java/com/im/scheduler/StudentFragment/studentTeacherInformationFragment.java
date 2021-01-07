package com.im.scheduler.StudentFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.im.scheduler.R;

public class studentTeacherInformationFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SearchView mSearch;
    RecyclerView recviewForTInfo ;
    private DatabaseReference mDatabase;
    teacherInformationAdepter myAdapterForTInfo ;
    private String s3;

    public studentTeacherInformationFragment() {
        // Required empty public constructor
    }
    public studentTeacherInformationFragment(String deptPath) {
        s3 = deptPath;
    }


 public static studentTeacherInformationFragment newInstance(String param1, String param2) {
    studentTeacherInformationFragment fragment = new studentTeacherInformationFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_student_teacher_information, container, false);
        recviewForTInfo  = (RecyclerView)view.findViewById(R.id.teacherInformationRecycleView);
        recviewForTInfo .setLayoutManager(new LinearLayoutManager(getContext()));


        FirebaseRecyclerOptions<teacherInformationModel> optionsForTInfo  =
                new FirebaseRecyclerOptions.Builder<teacherInformationModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child(s3+"/Teacher"), teacherInformationModel.class)
                        .build();
        myAdapterForTInfo = new teacherInformationAdepter(optionsForTInfo);
        recviewForTInfo .setAdapter(myAdapterForTInfo );

        mSearch = (SearchView)view.findViewById(R.id.search_barForTeacherInfo);
        mSearch.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myAdapterForTInfo.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdapterForTInfo.stopListening();
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
        FirebaseRecyclerOptions<teacherInformationModel> optionsForSearch=
                new FirebaseRecyclerOptions.Builder<teacherInformationModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child(s3+"/Teacher").orderByChild("name").startAt(s).endAt(s+"\uf8ff"),teacherInformationModel.class)
                .build();

        myAdapterForTInfo = new teacherInformationAdepter(optionsForSearch);
        myAdapterForTInfo.startListening();
        recviewForTInfo.setAdapter(myAdapterForTInfo);
    }

}
class teacherInformationAdepter extends FirebaseRecyclerAdapter<teacherInformationModel,teacherInformationAdepter.myviewholder> implements Filterable {

    public teacherInformationAdepter(@NonNull FirebaseRecyclerOptions<teacherInformationModel> optionsForTInfo ) {
        super(optionsForTInfo );
    }

    @Override
    protected void onBindViewHolder(@NonNull teacherInformationAdepter.myviewholder holder, int position, @NonNull teacherInformationModel model) {
        holder.aa2.setText(model.getName());
        holder.aa3.setText(model.getDes());
        holder.aa4.setText(model.getDept());
        holder.aa5.setText(model.getEmail());
        holder.aa6.setText(model.getPhone());
    }

    @NonNull
    @Override
    public teacherInformationAdepter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design_for_teacher_information,parent,false);
        return new teacherInformationAdepter.myviewholder(view);
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class myviewholder  extends RecyclerView.ViewHolder{
        TextView aa2,aa3,aa4,aa5,aa6;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            aa2 = itemView.findViewById(R.id.a2);
            aa3 = itemView.findViewById(R.id.a3);
            aa4 = itemView.findViewById(R.id.a4);
            aa5 = itemView.findViewById(R.id.a5);
            aa6 = itemView.findViewById(R.id.a6);
        }

    }
}
class teacherInformationModel {
    String Email,Name,Phone,dept,des;

    public teacherInformationModel() {
    }

    public teacherInformationModel(String email, String name, String phone, String dept, String des) {
        Email = email;
        Name = name;
        Phone = phone;
        this.dept = dept;
        this.des = des;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}


