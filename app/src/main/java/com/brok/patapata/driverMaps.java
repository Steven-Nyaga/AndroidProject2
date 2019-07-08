package com.brok.patapata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.brok.patapata.directionhelpers.FetchURL;
import com.brok.patapata.directionhelpers.TaskLoadedCallback;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class driverMaps extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback, RoutingListener {
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

    Location locationResult;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    LatLng dlocation, ulocation;
    MarkerOptions driverlocation, userlocation;
    Polyline currentPolyline;
    LocationManager locationManager;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polylines = new ArrayList<>();
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
                erasePolylines();
                mReq = FirebaseDatabase.getInstance().getReference().child("requests");
                mReq.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            userid = snapshot.child("userid").getValue(String.class);
                            if (FirebaseAuth.getInstance().getCurrentUser().getUid() == userid) {
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
        mMap = googleMap;

        dlat = -1.3122795;
        dlng = 36.812145;
        ulat = -1.3087;
        ulng = 36.8001;

         dlocation = new LatLng(dlat, dlng);
         ulocation = new LatLng(ulat, ulng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dlocation, 15));
        driverlocation = new MarkerOptions().position(dlocation).title("You");
        userlocation = new MarkerOptions().position(ulocation).title("Customer");

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest= new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Double latitude = locationResult.getLastLocation().getLatitude();
                Double longitude = locationResult.getLastLocation().getLongitude();
                getRouteToMarker(ulocation, locationResult);



            }
        }, getMainLooper());


        mMap.addMarker(driverlocation);
        mMap.addMarker(userlocation);


    }

    private void getRouteToMarker(LatLng ulocation, LocationResult locationResult) {
//b58fb6f70c665ad7b049a4760c82fa0e082d54e9

        Routing routing = new Routing.Builder()
                .key("AIzaSyBn1UV2_yiWFII0lhZmP-urMQr9E-yYMas")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()), ulocation)
                .build();
        routing.execute();
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

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
    public void erasePolylines(){
        for (Polyline line : polylines ){
            line.remove();
        }
        polylines.clear();
    }
}
