package com.example.enrserviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FinalOrderConfirmationActivity extends AppCompatActivity {
    String key;
    String name, from, address, mobileNumber;
    TextView finalUserName, finalPageUserName, finalAddress, finalPhoneNumber, finalReferenceNumber, finalServiceType;
    ConstraintLayout loadingScreen, finalReceipt;
    boolean res = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order_confirmation);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        mobileNumber = intent.getStringExtra("mobileNumber");
        finalAddress = findViewById(R.id.finalUserAddress);
        finalPageUserName = findViewById(R.id.finalPageUserName);
        finalUserName = findViewById(R.id.finalUserName);
        finalPhoneNumber = findViewById(R.id.finalPhoneNumber);
        finalServiceType = findViewById(R.id.finalServiceType);
        finalReferenceNumber = findViewById(R.id.finalReferenceNumber);
        loadingScreen = findViewById(R.id.loadingScreen);
        finalReceipt = findViewById(R.id.receiptScreeen);
        finalPhoneNumber.setText(mobileNumber);
        finalUserName.setText(name);
        finalPageUserName.setText(name);
        finalServiceType.setText(from);
        finalAddress.setText(address);
        registerServiceRequest();
    }
    private void registerServiceRequest(){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Request");
        key = reference.push().getKey();
        Log.d("TAG", "registerServiceRequest: "+ key);
        reference.child(key).setValue(new ServiceRequestClass(name, mobileNumber, address, from, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(FinalOrderConfirmationActivity.this, "Service request registered", Toast.LENGTH_SHORT).show();
                            StringBuilder referenceNumber = new StringBuilder();
                            for(int i=1; i<key.length(); i++)
                                referenceNumber.append(key.charAt(i));
                            Log.d("TAG", "onComplete: " + referenceNumber);
                            finalReferenceNumber.setText(referenceNumber.toString());
                            finalReceipt.setAlpha(1.0f);
                            loadingScreen.setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(FinalOrderConfirmationActivity.this, "Failed to register service", Toast.LENGTH_SHORT).show();
                            finalReferenceNumber.setText("None");
                            res = false;
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(res){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(FinalOrderConfirmationActivity.this);
            alertDialog.setTitle("Service Details")
                    .setCancelable(false)
                    .setMessage("Save the reference number for the later use. Please take a screenshot or save this number")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("TAG", "onClick: OK clicked for saving the details..");
                        }
                    })
                    .setNegativeButton("Quit Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }else{
            super.onBackPressed();
        }
    }
}