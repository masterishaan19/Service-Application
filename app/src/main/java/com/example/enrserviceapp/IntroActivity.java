package com.example.enrserviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class IntroActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    ArrayList < ScreenItemClass > arr;
    TabLayout introTabLayout;
    Button introNextButton;
    Button getStarted;
    OnBoardingScreenAdapter onBoardingScreenAdapter;
    int position = 0;
    TabLayoutMediator mediator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        // Coding part starts from here !!
        introNextButton = findViewById(R.id.introNextButton);
        getStarted = findViewById(R.id.getStarted);
        arr = new ArrayList<>();
        introTabLayout = findViewById(R.id.introTabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        getSupportActionBar().hide();
        //Checking whether we should show this activity or not ?
        if (restorePrefsData()) {
            Intent intent = new Intent(IntroActivity.this, InitialActivity.class);
            startActivity(intent);
            finish();
        }
        // Adding those items that are to be displayed in the intro page for the application
        arr.add(new ScreenItemClass("The Handyman are well skilled and well trained, to resolve customers problem in effective way", "Highly Skilled Handyman", R.drawable.skill));
        arr.add(new ScreenItemClass("The company provides highly efficient and fast services to the customer", "Fast Services", R.drawable.delivery));
        arr.add(new ScreenItemClass("Quick service registration and quick solution. Get things done quickly", "Quick response", R.drawable.quick));
        arr.add(new ScreenItemClass("User Friendly mobile app interface for easy operation", "Friendly UI ", R.drawable.ui));
        onBoardingScreenAdapter = new OnBoardingScreenAdapter(arr, IntroActivity.this);
        viewPager2.setAdapter(onBoardingScreenAdapter);
        getStarted.setVisibility(View.INVISIBLE);
        introNextButton.setVisibility(View.VISIBLE);

        mediator = new TabLayoutMediator(introTabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setIcon(R.drawable.indicator_default);
                if (tab.getPosition() == arr.size()) {
                    getStarted.setVisibility(View.VISIBLE);
                    introNextButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        mediator.attach();
        introNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager2.getCurrentItem();
                if (position >= arr.size() - 1) {
                    getStarted.setVisibility(View.VISIBLE);
                    introNextButton.setVisibility(View.INVISIBLE);
                } else {
                    viewPager2.setCurrentItem(position + 1);
                }
            }
        });
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, InitialActivity.class);
                startActivity(intent);
                saveSharedPrefs();
                finish();
            }
        });
    }
    private void saveSharedPrefs(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }
    public boolean restorePrefsData(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("myprefs", MODE_PRIVATE);
        return prefs.getBoolean("isIntroOpened", false);
    }
}