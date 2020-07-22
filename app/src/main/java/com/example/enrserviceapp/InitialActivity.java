package com.example.enrserviceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    TextView forgotPassword, CreateAccount;
    FirebaseAuth mAuth;
    static String userType;
    public void clearField(){
        email.setText("");
        password.setText("");
    }
    public void LoginNow(View view){
        final Button loginNow = findViewById(R.id.loginButton);
        loginNow.setEnabled(false);
        if(email.getText().toString().matches("")){
            Toast.makeText(this, "Email Invalid", Toast.LENGTH_SHORT).show();
            email.requestFocus();
        }else if(password.getText().toString().matches("")){
            Toast.makeText(this, "Password Invalid", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            userType = dataSnapshot.child("type").getValue().toString();
                                            editSharedPreferences();
                                            loginNow.setEnabled(true);
                                            MoveToNextActivity();
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                                    });
                        }else{
                            loginNow.setEnabled(true);
                            Toast.makeText(InitialActivity.this, "Please Verify your email", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    }else{
                        loginNow.setEnabled(true);
                        Toast.makeText(InitialActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void MoveToNextActivity(){
        Toast.makeText(InitialActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ServicesActivity.class);
        clearField();
        finish();
        startActivity(intent);
    }
    public void editSharedPreferences(){
        SharedPreferences sharedPreferences  = getApplicationContext().getSharedPreferences("sharedPreference", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), userType);
        Log.d("TAG", "editSharedPreferences: " + userType);
        myEdit.apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        forgotPassword = findViewById(R.id.forgotPassword);
        CreateAccount = findViewById(R.id.loginCreateAccount);
        clearField();
        forgotPassword.setOnClickListener(this);
        CreateAccount.setOnClickListener(this);
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(this, ServicesActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.forgotPassword){
            if(email.getText().toString().matches("")){
                Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
            }else{
                FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(InitialActivity.this, "Reset link sent to email", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(InitialActivity.this, "Unable to send Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }else if(v.getId() == R.id.loginCreateAccount){
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        }
    }
}