package com.brok.patapata;

import android.Manifest;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class activity_user extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth auth;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    DatabaseReference Users;
    private String user_email;
    private TextView  cUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragmentUser()).commit();

        View header = navigationView.getHeaderView(0);
        cUser = (TextView) header.findViewById(R.id.currentUser);
        //cUser = (TextView) findViewById(R.id.currentUser);
        Users = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
                getInstance().getCurrentUser().getUid());
        Users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_email = dataSnapshot.child("inputEmail").getValue().toString();
                cUser.setText(user_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


//        if (auth.getCurrentUser() != null) {
//
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                return;
//            }
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//            locationRequest = new LocationRequest();
//            locationRequest.setInterval(5000);
//            locationRequest.setFastestInterval(2000);
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
//                @Override
//                public void onLocationResult(LocationResult locationResult) {
//                    super.onLocationResult(locationResult);
//                    Double latitude = locationResult.getLastLocation().getLatitude();
//                    Double longitude = locationResult.getLastLocation().getLongitude();
//                    if (latitude != null&&longitude!=null&&auth.getCurrentUser() != null){
//
//                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
//                                getInstance().getCurrentUser().getUid()).child("latitude").setValue(latitude);
//                        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.
//                                getInstance().getCurrentUser().getUid()).child("longitude").setValue(longitude);
//
//
//                    }
//                }
//            }, getMainLooper());
//
//        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragmentUser()).commit();
                break;
            case R.id.nav_edit:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new activity_edit()).commit();
                break;
            case R.id.nav_option:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new activity_sugg()).commit();
                break;

            case R.id.nav_report:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportFragment()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
