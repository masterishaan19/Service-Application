package com.example.enrserviceapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{
    EditText name, email, password, confirmPassword;
    TextView loginNow;
    FirebaseAuth firebaseAuth;
    public void clearField(){
        name.setText("");
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
    }
    public void registerAccount(View view){
        if(name.getText().toString().matches("")){
            Toast.makeText(this, "Fill Name", Toast.LENGTH_SHORT).show();
            name.requestFocus();
        }else if(email.getText().toString().matches("")){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
        }else if(password.getText().toString().matches("")){
            Toast.makeText(this, "Password can't be blank", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        }else if(confirmPassword.getText().toString().matches("")){
            Toast.makeText(this, "Confirm Password Can never be blank", Toast.LENGTH_SHORT).show();
            confirmPassword.requestFocus();
        }else{
            creatingUser();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        // Code creation start here !!
        name = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.registerConfirmPassword);
        loginNow = findViewById(R.id.registerLoginNow);
        loginNow.setOnClickListener(this);
        clearField();
    }

    public void creatingUser(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth
                .createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CreateAccountActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CreateAccountActivity.this, "Email Send !! Please Verify it", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(CreateAccountActivity.this, "Unable to send email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            setUserType();
                        }else{
                            Toast.makeText(CreateAccountActivity.this, "Unable to create account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    String TAG= "CreateAccountActivity.java";
    public void setUserType(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateAccountActivity.this);
        View itemView = LayoutInflater.from(CreateAccountActivity.this).inflate(R.layout.set_user_type_dialog, null);
        alertDialog.setView(itemView);
        AlertDialog customDialog = alertDialog.create();
        customDialog.setCancelable(false);
        customDialog.setIcon(R.drawable.ic_baseline_announcement_24);
        customDialog.show();
        Button customer = itemView.findViewById(R.id.customer);
        Button worker = itemView.findViewById(R.id.worker);
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDefinitionClass user = new UserDefinitionClass(email.getText().toString(), "Customer");
                Log.d(TAG, "onClick: Customer Clicked");
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user);
                finish();
            }
        });
        worker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Worker Clickedd");
                UserDefinitionClass user = new UserDefinitionClass(email.getText().toString(), "Worker");
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(firebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user);
                finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.registerLoginNow){
            clearField();
            finish();
        }
    }
}