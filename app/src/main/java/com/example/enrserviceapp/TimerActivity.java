package com.example.enrserviceapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class TimerActivity extends Activity implements Chronometer.OnChronometerTickListener{
    ImageButton startButton, stopButton, resetButton;
    int elapsedMillis, progress = 0;
    private Chronometer chronometer;
    private boolean running, res = false, isSent = false;
    private long pauseOffset, breakTime;
    Date startTime, endTime;
    ProgressBar timerProgressBar;
    String userEmail, title;
    public void reset(View view){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            startButton.setEnabled(true);
            res = false;
            startButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            stopButton.setImageResource(R.drawable.stop_button);
            isSent = false;
            progress = 0;
            startButton.setBackgroundResource(R.drawable.timer_button_background);
            resetButton.setBackgroundResource(R.drawable.timer_button_background);
            stopButton.setBackgroundResource(R.drawable.timer_button_background);
            timerProgressBar.setVisibility(View.INVISIBLE);
        }else{
            Toast.makeText(this, "Stop Timer First", Toast.LENGTH_SHORT).show();
        }
    }
    public void start(View view){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            stopButton.setBackgroundResource(R.drawable.timer_null_button_background);
            timerProgressBar.setVisibility(View.VISIBLE);
            if(!res){
                res = true;
                timerProgressBar.setProgress(0);
                startTime = Calendar.getInstance().getTime();
                breakTime = Calendar.getInstance().getTimeInMillis();
            }
            startButton.setImageResource(R.drawable.ic_baseline_pause_24);
            startButton.setBackgroundResource(R.drawable.timer_button_background);
            resetButton.setBackgroundResource(R.drawable.timer_null_button_background);
        }else{
            chronometer.stop();
            running = false;
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            startButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            startButton.setBackgroundResource(R.drawable.timer_button_background);
            resetButton.setBackgroundResource(R.drawable.timer_button_background);
            timerProgressBar.setVisibility(View.INVISIBLE);
        }
        stopButton.setBackgroundResource(R.drawable.timer_button_background);
    }
    public void stop(View view){
        startButton.setEnabled(false);
        if(running){
            stopButton.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
            elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
            endTime = Calendar.getInstance().getTime();
            start(view);
            sendTime(view);
            timerProgressBar.setVisibility(View.INVISIBLE);
            startButton.setBackgroundResource(R.drawable.timer_null_button_background);
            resetButton.setBackgroundResource(R.drawable.timer_button_background);
            stopButton.setBackgroundResource(R.drawable.timer_button_background);
            if(isSent){
                stopButton.setEnabled(false);
                startButton.setBackgroundResource(R.drawable.timer_null_button_background);
                resetButton.setBackgroundResource(R.drawable.timer_button_background);
                stopButton.setBackgroundResource(R.drawable.timer_null_button_background);
            }
        }else if(isSent){
            stopButton.setEnabled(false);
            stopButton.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
            startButton.setBackgroundResource(R.drawable.timer_null_button_background);
            resetButton.setBackgroundResource(R.drawable.timer_button_background);
            stopButton.setBackgroundResource(R.drawable.timer_null_button_background);
            Toast.makeText(this, "Data has already been sent", Toast.LENGTH_SHORT).show();
        }else{
            stopButton.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
            elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
            endTime = Calendar.getInstance().getTime();
            sendTime(view);
            timerProgressBar.setVisibility(View.INVISIBLE);
            startButton.setBackgroundResource(R.drawable.timer_null_button_background);
            resetButton.setBackgroundResource(R.drawable.timer_button_background);
            stopButton.setBackgroundResource(R.drawable.timer_button_background);
            if(isSent){
                stopButton.setEnabled(false);
                startButton.setBackgroundResource(R.drawable.timer_null_button_background);
                resetButton.setBackgroundResource(R.drawable.timer_button_background);
                stopButton.setBackgroundResource(R.drawable.timer_null_button_background);
            }
        }
    }
    public void sendTime(View view){
        final int valueRandom = (int)Math.random()*100000000 + 100000000;
        AlertDialog.Builder dialog = new AlertDialog.Builder(TimerActivity.this);
        final View userEmailView = LayoutInflater.from(TimerActivity.this)
                .inflate(R.layout.timer_email_dialog, null, false);
        final EditText workType = userEmailView.findViewById(R.id.workType);
        final EditText referenceNumber = userEmailView.findViewById(R.id.referenceNumber);
        dialog.setTitle("Work Completed")
                .setMessage("Confirm to send the timing details to the owner: ")
                .setView(userEmailView)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        elapsedMillis /= 1000;
                        breakTime = Calendar.getInstance().getTimeInMillis() - breakTime;
                        breakTime /= 1000;
                        breakTime /= 60;
                        final String startTimeString = startTime.getHours() + ":" + startTime.getMinutes() + ":" + startTime.getSeconds();
                        final String endTimeString = endTime.getHours() + ":" +endTime.getMinutes() + ":" + endTime.getSeconds();
                        final String startingDate = startTime.getDate() + "/" + startTime.getMonth() + "/" + (1900 + startTime.getYear());
                        final String endingDate = endTime.getDate() + "/" + endTime.getMonth() + "/" + (1900 + endTime.getYear());
                        if(check_credentials()){
                            final boolean[] isDone = {false};
                            FirebaseDatabase.getInstance().getReference("Request")
                                    .child('-' + referenceNumber.getText().toString().trim())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(!isDone[0]){
                                                FirebaseDatabase.getInstance().getReference("Request")
                                                        .child('-' + referenceNumber.getText().toString())
                                                        .child("image").setValue(valueRandom);
                                                for(DataSnapshot data: dataSnapshot.getChildren()){
                                                    if(data.getKey().equals("email"))
                                                        userEmail = data.getValue().toString();
                                                    else if(data.getKey().equals("worktype"))
                                                        title = data.getValue().toString();
                                                }
                                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                                                WorkDoneClass object = new WorkDoneClass(email, userEmail,
                                                        startTimeString, endTimeString, breakTime, workType.getText().toString(),
                                                        startingDate, endingDate, title);
                                                FirebaseDatabase.getInstance()
                                                        .getReference("WorkDone")
                                                        .child(referenceNumber.getText().toString())
                                                        .setValue(object).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            isDone[0] = true;
                                                            Toast.makeText(TimerActivity.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                                                            FirebaseDatabase.getInstance().getReference("Request")
                                                                    .child('-' + referenceNumber.getText().toString())
                                                                    .removeValue();
                                                        }else
                                                            Toast.makeText(TimerActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            isSent = true;
                        }
                    }
                    private boolean check_credentials(){
                        if(workType.getText().toString().length() <= 10){
                            Toast.makeText(TimerActivity.this, "Proper Work Description", Toast.LENGTH_SHORT).show();
                            return false;
                        }else if(referenceNumber.getText().toString().length() <= 5){
                            Toast.makeText(TimerActivity.this, "Invalid Reference Number", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        return true;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: No clicked !!");
                    }
                })
                .create();
        dialog.show();
    }
    String TAG = "This is the result for the selection....";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        chronometer = findViewById(R.id.chronometer);
        startButton = findViewById(R.id.start_pause);
        stopButton = findViewById(R.id.stop);
        resetButton = findViewById(R.id.Reset);
        chronometer.setFormat("%s");
        timerProgressBar = findViewById(R.id.timerProgressBar);
        timerProgressBar.setVisibility(View.INVISIBLE);
        timerProgressBar.setMax(60);
        chronometer.setOnChronometerTickListener(this);
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        String text = chronometer.getText().toString();
        Log.d(TAG, "onChronometerTick: " + text.split(":")[1].toString());
        timerProgressBar.setProgress(Integer.parseInt(text.split(":")[1]));
    }
}