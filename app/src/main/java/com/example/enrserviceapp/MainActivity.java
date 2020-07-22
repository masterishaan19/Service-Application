package com.example.enrserviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Code creation starts
        getSupportActionBar().hide();
        CountDownTimer timer = new CountDownTimer(2000, 200){
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.start();
    }
}