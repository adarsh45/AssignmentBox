package com.example.assignmentbox.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignmentbox.R;
import com.example.assignmentbox.Utils.UploadDialog;

public class TestActivity extends AppCompatActivity {

    //Content View for Specific Subjects ........ *****
    // Assignments to be displayed here ........ *****


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    public void uploadFile(View view) {
        UploadDialog uploadDialog = new UploadDialog(TestActivity.this);
        uploadDialog.show(getSupportFragmentManager(), "Upload File");
    }
}
