package com.brok.patapata;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class driverMaps extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference mReq;
    private Button report, button, finish;
    private GoogleMap mMap;
    private String userid;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userid = getIncomingIntent();

        finish = (Button) findViewById(R.id.driver_finish_transaction);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(driverMaps.this, driver.class);
                startActivity(intent);
            }
        });

        report = (Button) findViewById(R.id.report_transaction);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(driverMaps.this, Activity_Send_Report.class);
                intent.putExtra("User ID", userid);
                startActivity(intent);
            }
        });


        button = (Button) findViewById(R.id.cancel_transaction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReq = FirebaseDatabase.getInstance().getReference().child("requests");
                mReq.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            userid = snapshot.child("userid").getValue(String.class);
                            if(FirebaseAuth.getInstance().getCurrentUser().getUid()==userid){
                                //push_key=snapshot.getKey();
                                //mReq.child(push_key).removeValue();
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //FirebaseDatabase.getInstance().getReference().child("requests").child(ident).removeValue();
                Intent intent = new Intent(driverMaps.this, driver.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
         userid = getIncomingIntent();

       FirebaseDatabase.getInstance().getReference().child("users").child(userid).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               Double lat =dataSnapshot.child("latitude").getValue(Double.class);
               Double lng =dataSnapshot.child("longitude").getValue(Double.class);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });





        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private String getIncomingIntent(){
        if(getIntent().hasExtra("User ID")){
         String userid = getIntent().getStringExtra("User ID");
         return userid;
        }

        return null;
    }
}
