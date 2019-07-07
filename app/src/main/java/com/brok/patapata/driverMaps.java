package com.brok.patapata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brok.patapata.directionhelpers.FetchURL;
import com.brok.patapata.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class driverMaps extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {
//public class driverMaps extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference mReq;
    private Button report, button, finish;
    private GoogleMap mMap;
    private String userid;
    private String driverid;
    private Double dlat;
    private Double ulat;
    private Double dlng;
    private Double ulng;
    MarkerOptions driverlocation, userlocation;
    Polyline currentPolyline;

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

        report = (Button) findViewById(R.id.driver_report_transaction);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(driverMaps.this, Activity_Send_Report.class);
                intent.putExtra("User ID", userid);
                startActivity(intent);
            }
        });


        button = (Button) findViewById(R.id.driver_cancel_transaction);
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

        driverid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Delete all other requests the driver currently has.
        FirebaseDatabase.getInstance().getReference().child("requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String driver_id = snapshot.child("driverid").getValue(String.class);
                    if(driver_id==driverid) {

                        snapshot.getRef().removeValue();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//retrive user's location
        FirebaseDatabase.getInstance().getReference().child("users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ulat =dataSnapshot.child("latitude").getValue(Double.class);
                ulng =dataSnapshot.child("longitude").getValue(Double.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//retrieve driver's location
        FirebaseDatabase.getInstance().getReference().child("driverdetails").child(driverid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dlat =dataSnapshot.child("latitude").getValue(Double.class);
                dlng =dataSnapshot.child("longitude").getValue(Double.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Add a marker on Driver Location and move the camera
        LatLng dlocation = new LatLng(dlat, dlng);
        LatLng ulocation = new LatLng(ulat, ulng);
        //  mMap.addMarker(new MarkerOptions().position(dlocation).title("You"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(dlocation));

        driverlocation = new MarkerOptions().position(dlocation).title("You");
        userlocation = new MarkerOptions().position(ulocation).title("Customer");

        //connecting drver and user via a path
        String url = getUrl(driverlocation.getPosition(), userlocation.getPosition(), "driving");
        new FetchURL(driverMaps.this).execute(url, "driving");

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

        mMap = googleMap;
mMap.addMarker(driverlocation);
        mMap.addMarker(userlocation);
    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private String getIncomingIntent(){
        if(getIntent().hasExtra("User ID")){
         String userid = getIntent().getStringExtra("User ID");
         return userid;
        }

        return null;
    }

    @Override
    public void onTaskDone(Object... values) {
if(currentPolyline!=null)
    currentPolyline.remove();
currentPolyline = mMap.addPolyline((PolylineOptions)values[0]);
    }
}
