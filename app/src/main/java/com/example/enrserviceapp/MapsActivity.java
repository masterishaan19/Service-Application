package com.example.enrserviceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    int Result_Code = 5;
    ArrayList < LocationDetails > arr;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        arr = new ArrayList<>();
        new AlertDialog.Builder(MapsActivity.this)
                .setTitle("Location Instruction")
                .setMessage("Tap the location which you want to select")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("TAG", "onClick: Yes Clicked");
                    }
                })
                .show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("Locations");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                double radius = 0 , longitude = 0, latitude = 0;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getKey().equals("radius"))
                        radius = Double.parseDouble(data.getValue().toString());
                    else if(data.getKey().equals("longitude"))
                        longitude = (double) data.getValue();
                    else if(data.getKey().equals("latitude"))
                        latitude = (double) data.getValue();
                }
                arr.add(new LocationDetails(new LatLng(latitude, longitude), radius));
                drawMarkerWithCircle(new LatLng(latitude, longitude), radius);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void drawMarkerWithCircle(LatLng position, double radiuss){
        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMap.addMarker(markerOptions);
        mMap.addCircle(new CircleOptions()
                .center(position)
                .radius(radiuss)
                .strokeWidth(5f)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(70,239,167,167)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 8));
    }
    public void LocateGeoCode(LatLng latLng){
        OrderPlacingActivity.location = latLng;
        setResult(Result_Code);
        finish();
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
                float[] distance = new float[2];
                int res = 1;
                for(int i=0; i<arr.size(); i++){
                    drawMarkerWithCircle(arr.get(i).getLatLng(), arr.get(i).getRadius());
                    Location.distanceBetween(latLng.latitude, latLng.longitude, arr.get(i).getLatLng().latitude, arr.get(i).getLatLng().longitude,  distance);
                    mMap.addMarker(new MarkerOptions().position(arr.get(i).getLatLng()).title("Center Location"));
                    if(distance[0] <= arr.get(i).getRadius()){
                        Toast.makeText(MapsActivity.this, "We got you covered !!", Toast.LENGTH_SHORT).show();
                        res = 0;
                        LocateGeoCode(latLng);
                    }
                }
                if(res==1)
                    Toast.makeText(MapsActivity.this, "Area Unavailable", Toast.LENGTH_SHORT).show();
            }
        });
        if(Build.VERSION.SDK_INT < 26){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mMap.clear();
                if(lastKnowLocation != null){
                    LatLng userLocation = new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(userLocation)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
            }
        }
        for(int i=0; i<arr.size(); i++){
            drawMarkerWithCircle(arr.get(i).getLatLng(), arr.get(i).getRadius());
        }
    }

}