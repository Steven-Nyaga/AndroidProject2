package com.brok.patapata;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportFragment extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<user_reports> list;
    report_adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

        recyclerView = (RecyclerView) getView().findViewById(R.id.reportrecyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<user_reports>();
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
                getInstance().getCurrentUser().getUid()).child("reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
    String r = dataSnapshot1.getValue(String.class);
    user_reports rep = new user_reports(r);
    // user_reports r = dataSnapshot1.getValue(user_reports.class);
    list.add(rep);
}
adapter = new report_adapter(getActivity(),list );
recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();

            }
        });

    }

}