package com.brok.patapata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class user_location extends AppCompatActivity {
    private TextView label, phoneno, location, label2;
    private DatabaseReference mReq;
    private Button report, cancel, finish;
    private   LatLng ulocation;
    public  String user_id, userlocation, phone_no, driver_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        label = (TextView)findViewById(R.id.label);
     //   phoneno = (TextView)findViewById(R.id.user_no);
       // label2 = (TextView)findViewById(R.id.label2);
        location = (TextView)findViewById(R.id.user_location);

        Intent intent = getIntent();
user_id= intent.getStringExtra("User ID");;
        Log.d("userid", user_id);


    FirebaseDatabase.getInstance().getReference().child("users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                //String no = snapshot.child("digit").getValue(String.class);
                //phoneno.setText(no);
                Double lat = snapshot.child("latitude").getValue(Double.class);
                Double lng = snapshot.child("longitude").getValue(Double.class);
                //if(lat!=null&&lng!=null) {
                    ulocation = new LatLng(lat, lng);
                  //  String City = getCity(ulocation);
                Geocoder geocoder = new Geocoder(user_location.this, Locale.getDefault());
                try {
                    String mycity;
                    List<Address> addresses = geocoder.getFromLocation(ulocation.latitude, ulocation.longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    mycity = addresses.get(0).getLocality();
                    location.setText(mycity);

                } catch (IOException e) {
                    e.printStackTrace();
                }

              //  }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });



        driver_id =FirebaseAuth.getInstance().getCurrentUser().getUid();

        cancel = (Button) findViewById(R.id.cancel_transaction);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //FirebaseDatabase.getInstance().getReference().child("requests").child(ident).removeValue();
                Intent intent = new Intent(user_location.this, driver.class);
                startActivity(intent);
            }
        });

        report = (Button) findViewById(R.id.report_transaction);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(user_location.this, Activity_Send_Report.class);
                intent.putExtra("Reportee", user_id);
                startActivity(intent);
            }
        });

        finish = (Button) findViewById(R.id.finish_transaction);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(user_location.this, driver.class);
                startActivity(intent);
            }
        });
    }

    private String getCity(LatLng ulocation) {
        String mycity = "";
        Geocoder geocoder = new Geocoder(user_location.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(ulocation.latitude, ulocation.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
             mycity = addresses.get(0).getLocality();
            return mycity;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTheIntent(){
        if(getIntent().hasExtra("User ID")){
            String userid = getIntent().getStringExtra("User ID");
            return userid;
        }
        return null;
}
}
