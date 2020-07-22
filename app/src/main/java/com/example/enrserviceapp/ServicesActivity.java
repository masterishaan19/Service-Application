package com.example.enrserviceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServicesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList< ServicesDetails > arr;
    FirebaseAuth mAuth;
    String userType;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getApplicationContext());
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem setLocation = menu.findItem(R.id.setLocation);
        MenuItem startTimer = menu.findItem(R.id.startTimer);
        MenuItem exportExcel = menu.findItem(R.id.exportExcel);
        MenuItem showCompleted = menu.findItem(R.id.showCompleted);
        MenuItem serviceRequest = menu.findItem(R.id.serviceRequest);
        if(userType.equals("Customer")){
            setLocation.setVisible(false);
            startTimer.setVisible(false);
            exportExcel.setVisible(false);
            showCompleted.setVisible(false);
            serviceRequest.setVisible(false);
        }else if(userType.equals("Worker")){
            setLocation.setVisible(false);
            exportExcel.setVisible(false);
            showCompleted.setVisible(false);
            serviceRequest.setVisible(false);
        }else{
            setLocation.setVisible(true);
            startTimer.setVisible(true);
            exportExcel.setVisible(true);
            showCompleted.setVisible(true);
            serviceRequest.setVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.setLocation){
            Intent intent = new Intent(getApplicationContext(), AddMapLocationActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.logout){
            mAuth.signOut();
            finish();
        }else if(item.getItemId() == R.id.startTimer){
            Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.showCompleted){
            Intent intent = new Intent(getApplicationContext(), WorkCompletedActivity.class);
            intent.putExtra("from", "services");
            startActivity(intent);
        }else if(item.getItemId() == R.id.exportExcel){
            Intent intent = new Intent(getApplicationContext(), ExportToExcelActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.serviceRequest){
            Intent intent = new Intent(ServicesActivity.this, ServiceRequestActivity.class);
            startActivity(intent);
        }
        return false;
    }
    public void getSharedPreference(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sharedPreference", MODE_PRIVATE);
        userType = sharedPreferences.getString(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), "Customer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        // Code creation starts from here !!
        getSharedPreference();
        setTitle("Services Available");
        arr = new ArrayList<>();
        recyclerView = findViewById(R.id.recycelerView);
        arr.add(new ServicesDetails("Electrician", R.drawable.electrician));
        arr.add(new ServicesDetails("Plumbing", R.drawable.plumber));
        arr.add(new ServicesDetails("Gardener", R.drawable.farm));
        arr.add(new ServicesDetails("Generic Activities", R.drawable.generic));
        arr.add(new ServicesDetails("Other Services", R.drawable.other));
        arr.add(new ServicesDetails("More Coming Soon", R.drawable.plus));
        adapter = new RecyclerViewAdapter(getApplicationContext(), arr);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
    }
}