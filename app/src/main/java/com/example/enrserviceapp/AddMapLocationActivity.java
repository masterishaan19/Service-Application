package com.example.enrserviceapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMapLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    ArrayList < MarkerLocationClass > arr;
    FirebaseDatabase database;
    DatabaseReference reference;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_map_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        String locationInstruction = "Instruction:\n" +
                "1. Add new location by long press at the location and then adding the radius for the location\n" +
                "2. Edit the existing location by tapping the red color marker";
        new AlertDialog.Builder(AddMapLocationActivity.this)
                .setTitle("Add & Edit Location Instruction")
                .setMessage(locationInstruction)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: Ok clicked by admin");
                    }
                })
                .show();
    }

    private void drawMarkerWithCircle(LatLng position, double radiuss){
        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMap.addMarker(markerOptions);
        mMap.addCircle(new CircleOptions()
                .center(position)
                .radius(radiuss)
                .strokeWidth(5f)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(70,150,50,50)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
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

    public void addtheLocations(DataSnapshot dataSnapshot){
        double radius = 0 , longitude = 0, latitude = 0;
        Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey().toString());
        for(DataSnapshot data: dataSnapshot.getChildren()){
            if(data.getKey().equals("radius")) {
                radius = Double.parseDouble(data.getValue().toString());
            }
            else if(data.getKey().equals("longitude")) {
                longitude = (double) data.getValue();
            }
            else if(data.getKey().equals("latitude")) {
                latitude = (double) data.getValue();
            }
        }
        Log.d(TAG, "onChildAdded: - " + radius + " - " + longitude + " - " + latitude);
        drawMarkerWithCircle(new LatLng(latitude, longitude), radius);
        arr.add(new MarkerLocationClass(dataSnapshot.getKey(), latitude, longitude, radius));
    }
    String TAG = "Result";
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                final EditText radius = new EditText(AddMapLocationActivity.this);
                AlertDialog.Builder addLocation = new AlertDialog.Builder(AddMapLocationActivity.this);
                addLocation.setTitle("Add Location")
                        .setMessage("Enter your radius: (in Km) ")
                        .setView(radius)
                        .setPositiveButton("Add Location", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Locations");
                                long value = Long.parseLong(radius.getText().toString())*1000;
                                reference.push().setValue(new MarkerObject(latLng.latitude, latLng.longitude,  value));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "onClick: no clicked!!");
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Locations");
        arr = new ArrayList<>();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addtheLocations(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                addtheLocations(dataSnapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                addtheLocations(dataSnapshot);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                String key = "";
                for(int i=0; i<arr.size(); i++){
                    if(arr.get(i).getLatitude() == marker.getPosition().latitude && arr.get(i).getLongitude() == marker.getPosition().longitude){
                        key = arr.get(i).getKey();
                        break;
                    }
                }
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddMapLocationActivity.this);
                View alertDialogView = LayoutInflater.from(AddMapLocationActivity.this).inflate(R.layout.custom_map_alert_dialog, null);
                alertDialog.setView(alertDialogView);
                final AlertDialog customDialog = alertDialog.create();
                Button editMarker = alertDialogView.findViewById(R.id.editMaker);
                Button deleteMarker = alertDialogView.findViewById(R.id.deleteMarker);
                Button cancelEdit = alertDialogView.findViewById(R.id.cancelEdit);
                final String finalKey = key;
                editMarker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText getRadius = new EditText(AddMapLocationActivity.this);
                        getRadius.setText("0");
                        AlertDialog.Builder getRadiusAlertDialog = new AlertDialog.Builder(AddMapLocationActivity.this);
                        customDialog.dismiss();
                        getRadiusAlertDialog.setView(getRadius)
                                .setTitle("Update Radius")
                                .setMessage("Enter your radius: (in Km): ")
                                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        double value = Double.parseDouble(getRadius.getText().toString())*1000;
                                        Map < String, Object > mp = new HashMap<>();
                                        mp.put("latitude", marker.getPosition().latitude);
                                        mp.put("longitude", marker.getPosition().longitude);
                                        mp.put("radius", value);
                                        reference.child(finalKey)
                                                .updateChildren(mp)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task == null)
                                                            Toast.makeText(AddMapLocationActivity.this, "Error !!", Toast.LENGTH_SHORT).show();
                                                        else{
                                                            arr.clear();
                                                            mMap.clear();
                                                            Toast.makeText(AddMapLocationActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton("Cancel Changes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "onClick: Cancel button clicked for updating");
                                    }
                                })
                                .show();
                    }
                });
                deleteMarker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                        reference.child(finalKey)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task== null)
                                            Toast.makeText(AddMapLocationActivity.this, "Error while deleting marker", Toast.LENGTH_SHORT).show();
                                        else{
                                            arr.clear();
                                            mMap.clear();
                                            Toast.makeText(AddMapLocationActivity.this, "Deletion Done!!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
                    }
                });
                cancelEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
                customDialog.show();
                return false;
            }
        });
    }
}