package com.example.enrserviceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WorkCompletedActivity extends AppCompatActivity {
    customAdapterWorkCompleted customAdapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_completed);
        recyclerView = findViewById(R.id.completedList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        FirebaseRecyclerOptions < WorkDoneClass > options = new
                FirebaseRecyclerOptions.Builder<WorkDoneClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("WorkDone"), WorkDoneClass.class)
                .build();
        customAdapter = new customAdapterWorkCompleted(options);
        recyclerView.setAdapter(customAdapter);
        AlertDialog.Builder loading = new AlertDialog.Builder(WorkCompletedActivity.this);
        loading.setTitle("Fetching Data")
                .setView(LayoutInflater.from(WorkCompletedActivity.this).inflate(R.layout.export_dialog, null, false));
        final AlertDialog loadingDialog = loading.create();
        loadingDialog.show();
        CountDownTimer count = new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                loadingDialog.dismiss();
                if(customAdapter.getItemCount() ==0){
                    ConstraintLayout nothingScreen = findViewById(R.id.nothingScreen);
                    nothingScreen.setVisibility(View.VISIBLE);
                }
            }
        };
        count.start();
    }
    public void sendDetails(View view){
        view.getId();
        int value = 0;
        for(int i=0; i<customAdapter.getItemCount(); i++){
            if(customAdapter.getItemId(i) == view.getId()){
                value = i;
                break;
            }
        }
        Intent sendDetails = new Intent(Intent.ACTION_SEND);
        String heading = "Payroll Slip for the work";
        String content =
                "Work Title: " + customAdapter.getItem(value).getTitle() + "\n" +
                "Work Description: " + customAdapter.getItem(value).getWorkType() + "\n" +
                "Work Starting details: " + customAdapter.getItem(value).getStartingTime() + " on Date: " + customAdapter.getItem(value).getStartingDate() + "\n" +
                "Work Ending details: " + customAdapter.getItem(value).getEndTime() + " on Date: " + customAdapter.getItem(value).getEndingDate() + "\n" +
                "Break Time: " + customAdapter.getItem(value) + "min" + "\n" +
                "Worker: " + customAdapter.getItem(value).getWorkerId() + "\n";
        sendDetails.putExtra(Intent.EXTRA_SUBJECT, heading)
                .setType("message/rfc822")
                .putExtra(Intent.EXTRA_TEXT, content)
                .putExtra(Intent.EXTRA_EMAIL, new String[]{customAdapter.getItem(value).getCustomerId()});
        try{
            startActivity(Intent.createChooser(sendDetails,"Send email using..."));
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private String TAG = "Final Result";
    @Override
    protected void onStart() {
        super.onStart();
        customAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        customAdapter.stopListening();
        ArrayList < WorkDoneClass > arr = new ArrayList<>();
        for(int i=0; i<customAdapter.getItemCount(); i++){
            arr.add(customAdapter.getItem(i));
        }
    }
}