package com.brok.patapata;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourRequests extends Fragment {
    private RecyclerView recyclerView;
    public  String driverid;
    public  String cdriverid;
    public String userid;

    private Button yes, no;
    public String push_key;
   // public String reqs;
    ArrayList<POJO_requests> list;
    request_adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_requests, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

//        yes = (Button) getView().findViewById(R.id.yes);
//        no = (Button) getView().findViewById(R.id.no);
        recyclerView = (RecyclerView) getView().findViewById(R.id.requestrecyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<POJO_requests>();
        FirebaseDatabase.getInstance().getReference("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                  //  push_key = dataSnapshot1.getKey();
                    driverid = dataSnapshot1.child("driverid").getValue(String.class);
                    cdriverid =FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Log.d("driver id", driverid);
                    Log.d("confirm driver id", cdriverid);
                    POJO_requests requests  = dataSnapshot1.getValue(POJO_requests.class);
                    //problem code
                 //   if(cdriverid!=driverid){


                        list.add(requests);
                 //  }
                    //POJO_requests requests  = new POJO_requests(dataSnapshot1.child("").getValue(POJO_requests.class));
                    //POJO_requests requests  = dataSnapshot1.getValue(POJO_requests.class);
                  //  POJO_requests requests = new POJO_requests(reqs);
                    // user_reports r = dataSnapshot1.getValue(user_reports.class);
                  // list.add(requests);
                }
                adapter = new request_adapter(getActivity(),list );
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();

            }
        });





    }


}