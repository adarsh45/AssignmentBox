package com.example.assignmentbox.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.assignmentbox.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        temporary code for directly starting homeactivity by default
//        TODO: comment out this code on your end
//        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//        finish();
    }
}