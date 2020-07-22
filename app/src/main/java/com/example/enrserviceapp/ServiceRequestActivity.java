package com.example.enrserviceapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ServiceRequestActivity extends AppCompatActivity {
    customAdapterServiceRequests adapter;
    RecyclerView requestServices;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);
        FirebaseRecyclerOptions< ServiceRequestClass > options = new
                FirebaseRecyclerOptions.Builder<ServiceRequestClass >()
                .setQuery(FirebaseDatabase.getInstance().getReference("Request"), ServiceRequestClass.class)
                .build();
        adapter = new customAdapterServiceRequests(options, ServiceRequestActivity.this);
        requestServices = findViewById(R.id.serviceRequestRecyclerView);
        requestServices.setLayoutManager(new LinearLayoutManager(ServiceRequestActivity.this));
        requestServices.setAdapter(adapter);
        AlertDialog.Builder loading = new AlertDialog.Builder(ServiceRequestActivity.this);
        loading.setTitle("Fetching Data")
                .setView(LayoutInflater.from(ServiceRequestActivity.this).inflate(R.layout.export_dialog, null, false));
        final AlertDialog loadingDialog = loading.create();
        loadingDialog.show();
        CountDownTimer count = new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                loadingDialog.dismiss();
                if(adapter.getItemCount() ==0){
                    ConstraintLayout nothingScreen = findViewById(R.id.nothingScreen);
                    nothingScreen.setVisibility(View.VISIBLE);
                }
            }
        };
        count.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}