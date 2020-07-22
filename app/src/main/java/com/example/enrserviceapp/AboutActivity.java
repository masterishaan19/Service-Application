package com.example.enrserviceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_about);
        simulateDayNight(0);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
//                .setImage(R.drawable.custom_about_us)
                .setDescription("You engage and we reap for you. Your problem and our solution. Get help in any sort of matter")
                .addItem(new Element().setTitle("Version 1.0.0"))
                .addGroup("Connect With Us")
                .addEmail("info@engagenreap.com")
                .addWebsite("https://engagenreap.com/")
                .addFacebook("EnRConsultancyServices")
                .addTwitter("services_enr")
                .addPlayStore("com.example.enrserviceapp")
                .addInstagram("enrconsultancyservices")
                .addItem(getLinkedinSupport())
                .addItem(getCopyRightElement())
                .create();
        setContentView(aboutPage);
    }

    Element getCopyRightElement(){
        Element copyRightElement = new Element();
        @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
        final String copyRights = String.format(getString(R.string.app_name), Calendar.getInstance().get(Calendar.YEAR));
        copyRightElement.setTitle(copyRights);
        copyRightElement.setIconDrawable(R.drawable.logo);
//        copyRightElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightElement.setIconNightTint(android.R.color.white);
        copyRightElement.setGravity(Gravity.CENTER);
        copyRightElement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, copyRights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightElement;
    }
    public Element getLinkedinSupport(){
        Element linkedin = new Element();
        linkedin.setTitle("Follow us on Linkedin");
        linkedin.setIconDrawable(R.drawable.linkedin);
        linkedin.setIconNightTint(android.R.color.white);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.linkedin.com/company/engage-reap-consultancy-services/";
                Intent linkedInAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                linkedInAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(linkedInAppIntent);
            }
        });
        return linkedin;
    }
    public void simulateDayNight(int currentSettings){
        int Day = 0;
        int Night = 1;
        int Follow_System = 3;
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(currentSettings == Day && currentNightMode != Configuration.UI_MODE_NIGHT_NO){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else if(currentSettings ==Night && currentNightMode != Configuration.UI_MODE_NIGHT_YES){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if(currentSettings == Follow_System){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}