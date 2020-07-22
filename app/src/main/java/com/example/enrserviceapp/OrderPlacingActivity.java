package com.example.enrserviceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OrderPlacingActivity extends AppCompatActivity {
    public static LatLng location = null;
    ArrayList< LocationDetails > arr;
    EditText houseNumber, street, city, name, phoneNumber, state;
    String HouseNumber, Street, City, Name, PhoneNumber, State;
    int RequestCodeSend = 5;
    public void updateTheCriteria(){
        arr = new ArrayList<>();
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
    public boolean getUserLocation(){
        String locationString = street.getText().toString() + "," + city.getText().toString();
        Geocoder geocoder = new Geocoder(OrderPlacingActivity.this);
        List < Address > result = new ArrayList<>();
        try{
            result = geocoder.getFromLocationName(locationString, 1);
        }catch(Exception ex){
            Toast.makeText(OrderPlacingActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(result.size() >  0){
            Address address = result.get(0);
            location = new LatLng(address.getLatitude(), address.getLongitude());
            return true;
        }else{
            Toast.makeText(this, "No Such location found !!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean check(){
        if(getUserLocation()){
            LatLng latLng = location;
            float[] distance = new float[2];
            for(int i=0; i<arr.size(); i++){
                Location.distanceBetween(latLng.latitude, latLng.longitude, arr.get(i).getLatLng().latitude, arr.get(i).getLatLng().longitude,  distance);
                if(distance[0] <= arr.get(i).getRadius()){
                    Toast.makeText(OrderPlacingActivity.this, "Registering your service!! ", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            Toast.makeText(OrderPlacingActivity.this, "Select Valid Location from map", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
    public boolean checkNow(){
        if(houseNumber.getText().toString().matches("")){
            Toast.makeText(this, "Enter house address", Toast.LENGTH_SHORT).show();
            return false;
        }else if(city.getText().toString().matches("")){
            Toast.makeText(this, "Enter City", Toast.LENGTH_SHORT).show();
            return false;
        }else if(state.getText().toString().matches("")){
            Toast.makeText(this, "Enter state", Toast.LENGTH_SHORT).show();
            return false;
        }else if(name.getText().toString().matches("")){
            Toast.makeText(this, "Enter the Name for the user", Toast.LENGTH_SHORT).show();
            return false;
        }else if(phoneNumber.getText().toString().matches("")){
            Toast.makeText(this, "Enter the phone number", Toast.LENGTH_SHORT).show();
            return false;
        }else if(street.getText().toString().matches("")){
            Toast.makeText(this, "Enter the street name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean fieldCheck(){
        if(checkNow()){
            Street = street.getText().toString();
            Name = name.getText().toString();
            HouseNumber = houseNumber.getText().toString();
            City = city.getText().toString();
            State = state.getText().toString();
            PhoneNumber = phoneNumber.getText().toString();
            return true;
        }else {
            return false;
        }
    }
    private static final String TAG = "OrderPlacingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placing);
        Button locationSetter = findViewById(R.id.locationSelector);
        houseNumber = findViewById(R.id.houseaddress);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        name = findViewById(R.id.personName);
        phoneNumber = findViewById(R.id.mobileNumber);
        street = findViewById(R.id.streetAddress);
        updateTheCriteria();
        Button forward = findViewById(R.id.forward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldCheck()){
                    if(check()){
                        String houseAddress = houseNumber.getText().toString() +" ," + street.getText().toString() + " ," + city.getText().toString() + " ,"+state.getText().toString();
                        Intent intent = new Intent(OrderPlacingActivity.this, FinalOrderConfirmationActivity.class);
                        intent.putExtra("from", getIntent().getStringExtra("from"));
                        intent.putExtra("name", name.getText().toString());
                        intent.putExtra("address", houseAddress);
                        intent.putExtra("mobileNumber", phoneNumber.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    public void selectLocation(View view){
        location = null;
        Intent intent = new Intent(OrderPlacingActivity.this, MapsActivity.class);
        startActivityForResult(intent, RequestCodeSend);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RequestCodeSend == requestCode){
            if(location != null)
                updateFields();
            else
                Toast.makeText(this, "Invalid Location from Map", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateFields(){
        Geocoder geocoder = new Geocoder(OrderPlacingActivity.this);
        List < Address > result;
        try{
            result = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if(result.size() > 0){
                street.setText(result.get(0).getLocality() + "," + result.get(0).getFeatureName());
                city.setText(result.get(0).getSubAdminArea());
                state.setText(result.get(0).getAdminArea());
            }else{
                Toast.makeText(this, "No location found !", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}