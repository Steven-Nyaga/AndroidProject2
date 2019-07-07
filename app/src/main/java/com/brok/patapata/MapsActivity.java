package com.brok.patapata;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.Map;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleMap.OnMarkerClickListener, View.OnClickListener {
    private GoogleMap mMap;
    private ChildEventListener mChildEcventListener;
    private Location currentLocation;
    private Button report;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST_CODE = 101;
    private DatabaseReference mUsers;
    private DatabaseReference mUser;
    private DatabaseReference mDriver;
    Marker marker;
    FirebaseFirestore mFirestore;
    private FirebaseAuth auth;
    private BroadcastReceiver locationReceiver;
    public   Double lat, lng;
    public String ident;
    private LatLng location;
    private String rates;
    public TextView textView;
    public String pushid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        textView = (TextView) findViewById(R.id.ident);

        ChildEventListener mChildEventListener;

        mUsers = FirebaseDatabase.getInstance().getReference().child("driverdetails");
        mUsers.push().setValue(marker);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fetchLastLocation();



    }


    /*
    private void onAddLocation(){
        CollectionReference locations = mFirestore.collection("locations");
        for (int i = 0; i < 10; i++) {
            // Get a random Restaurant POJO
            Restaurant location = RestaurantUtil.getRandom(this);

            // Add a new document to the restaurants collection
            locations.add(location);
        }
    }
*/
    private void fetchLastLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.
                PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    //Toast.makeText(MapsActivity.this,currentLocation.getLatitude()+" "+currentLocation.getLongitude(),Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().
                            findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                } else {
                    Toast.makeText(MapsActivity.this, "No Location recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        //Adding the created the marker on the map
        //googleMap.addMarker(markerOptions).setIcon(BitmapDescriptorFactory.defaultMarker(HUE_RED));
        googleMap.addMarker(new MarkerOptions().position(latLng)).setIcon(BitmapDescriptorFactory.defaultMarker(HUE_RED));
        //for the driver
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerClickListener(this);
        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ident = snapshot.child("id").getValue(String.class);
                    lat = snapshot.child("latitude").getValue(Double.class);
                    lng = snapshot.child("longitude").getValue(Double.class);
                    rates = snapshot.child("rates").getValue(String.class);
                    //textView.setText(ident);
                    //final double lat = snapshot.child("latitude").getValue(Double.class);
                    //final double lng = snapshot.child("longitude").getValue(Double.class);
                    //rates = .child("rates").getValue(String.class);
                    //final String ident = snapshot.child("id").getValue(String.class);

                    location = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(location).title(rates)).setIcon(BitmapDescriptorFactory.defaultMarker(HUE_YELLOW));
                    //snackbar
                    /*
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            // Snackbar snackbar = Snackbar.make(findViewById(R.id.map), R.string.snack, Snackbar.LENGTH_LONG);
                            Intent intent2 = getIntent();
                            final String litres = intent2.getStringExtra("value");
                            Bundle bundle = intent2.getExtras();
                            String status = bundle.getString("status");
                            Toast toast = Toast.makeText(MapsActivity.this, status, Toast.LENGTH_LONG);
                            toast.show();

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.map), rates, Snackbar.LENGTH_LONG);
                            snackbar.setAction(R.string.Buy, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("requests");
                                    mDatabase.child(ident).child("id").setValue(FirebaseAuth.
                                            getInstance().getCurrentUser().getUid());
                                    mDatabase.child(ident).child("latitude").setValue(lat);
                                    mDatabase.child(ident).child("longitude").setValue(lng);
                                    mDatabase.child(ident).child("litres").setValue(litres);
                                    Intent intent = new Intent(MapsActivity.this, Activity_User_Confirmpay.class);
                                    intent.putExtra("ident",ident);
                                    Bundle extras = new Bundle();
                                    extras.putString("status", "Data Received!");
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                    //Intent intent = getIntent();
/*
                                String value= intent.getStringExtra("VALUE");

                                if(value.equalsIgnoreCase("1000")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                }else if(value.equalsIgnoreCase("2500")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                }else if(value.equalsIgnoreCase("5000")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                } else if(value.equalsIgnoreCase("10000")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                }

                                }

                            });


                            snackbar.show();

                            return true;

                        }



                    });
                    */


                }
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        // Snackbar snackbar = Snackbar.make(findViewById(R.id.map), R.string.snack, Snackbar.LENGTH_LONG);

                        Intent intent2 = getIntent();
                        final String litres = intent2.getStringExtra("value");
                        Bundle bundle = intent2.getExtras();
                        String status = bundle.getString("status");
                        Toast toast = Toast.makeText(MapsActivity.this, status, Toast.LENGTH_LONG);
                        toast.show();

                        Snackbar snackbar = Snackbar.make(findViewById(R.id.map), rates, Snackbar.LENGTH_LONG);
                        snackbar.setAction(R.string.Buy, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("requests");
                                pushid = mDatabase.push().getKey();
                                mDatabase.child(pushid).child("driverid").setValue(ident);
                                mDatabase.child(pushid).child("userid").setValue(FirebaseAuth.
                                        getInstance().getCurrentUser().getUid());
                                mDatabase.child(pushid).child("latitude").setValue(lat);
                                mDatabase.child(pushid).child("longitude").setValue(lng);
                                mDatabase.child(pushid).child("litres").setValue(litres);
                                Intent intent = new Intent(MapsActivity.this, Activity_User_Confirmpay.class);
                                intent.putExtra("ident",ident);
                                Bundle extras = new Bundle();
                                extras.putString("status", "Data Received!");
                                intent.putExtras(extras);
                                startActivity(intent);
                                //Intent intent = getIntent();
/*
                                String value= intent.getStringExtra("VALUE");

                                if(value.equalsIgnoreCase("1000")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                }else if(value.equalsIgnoreCase("2500")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                }else if(value.equalsIgnoreCase("5000")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                } else if(value.equalsIgnoreCase("10000")){
                                    mDatabase.child("users").child("notification").push().setValue(value);
                                }
*/
                                }

                            });


                            snackbar.show();

                            return true;

                        }



                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                } else {
                    Toast.makeText(MapsActivity.this,"Location permission missing",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    @Override
    public void onClick(View v) {

    }




}
