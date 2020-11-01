package com.example.assignmentbox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.assignmentbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    
    private static final String TAG = "HomeActivity";
    
    private FirebaseDatabase myDB;
    private DatabaseReference rootRef;

    private RecyclerView rvSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initFirebase();

    }

    private void initViews() {
        rvSubjects = findViewById(R.id.rv_subjects);
    }

    private void initFirebase() {
        myDB = FirebaseDatabase.getInstance();
        rootRef = myDB.getReference();
    }
}