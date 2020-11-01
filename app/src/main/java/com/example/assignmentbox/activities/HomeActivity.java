package com.example.assignmentbox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.assignmentbox.R;
import com.example.assignmentbox.Utils.SubjectDialog;
import com.example.assignmentbox.adapters.SubjectsAdapter;
import com.example.assignmentbox.pojo.SubjectPOJO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    
    private static final String TAG = "HomeActivity";
    
    private FirebaseDatabase myDB;
    private DatabaseReference rootRef;

    private RecyclerView rvSubjects;
    private FloatingActionButton fabAddSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initFirebase();
        addNewSubject();
        setupRV();

    }

    private void setupRV() {
        final ArrayList<SubjectPOJO> subjectPOJOArrayList = new ArrayList<>();

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    subjectPOJOArrayList.clear();
                    for (DataSnapshot data: snapshot.getChildren()){
                        SubjectPOJO subjectPOJO = data.getValue(SubjectPOJO.class);
                        if (subjectPOJO != null){
                            subjectPOJOArrayList.add(subjectPOJO);
                            Log.d(TAG, "onDataChange: Name: "+ subjectPOJO.getSubjectName());
                        } else {
                            Log.d(TAG, "onDataChange: ERROR getting subject data in arrayList");
                        }
                    }
                    rvSubjects.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    rvSubjects.setAdapter(new SubjectsAdapter(subjectPOJOArrayList));
                } else {
                    Log.d(TAG, "onDataChange: Got Empty Subjects Data from DB!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: DB ERROR: "+ error.getMessage());
            }
        });

    }

    private void addNewSubject() {
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubjectDialog subjectDialog = new SubjectDialog(HomeActivity.this);
                subjectDialog.show(getSupportFragmentManager(), "Subject Dialog Box");
            }
        });
    }

    private void initViews() {
        rvSubjects = findViewById(R.id.rv_subjects);
        fabAddSubject = findViewById(R.id.fab_add_subject);
    }

    private void initFirebase() {
        myDB = FirebaseDatabase.getInstance();
        rootRef = myDB.getReference("Subjects");
    }
}