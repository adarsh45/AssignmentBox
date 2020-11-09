package com.example.assignmentbox.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.assignmentbox.R;
import com.example.assignmentbox.pojo.UploadPOJO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class UploadDialog extends DialogFragment {


    final static int USER_CHOOSE_PDF_CODE = 2342;

    TextView uploadStatusTV;
    TextInputLayout pdfNameET;
    EditText pdfComments;
    Button selectUploadBtn;
    ProgressBar uploadProgressBar;

    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;

    private Context context;

    public UploadDialog(Context context){
        super();
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View uploadView = inflater.inflate(R.layout.dialog_upload, null);

        initViews(uploadView);

        initFirebase();

        selectUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });

        return uploadView;
    }

    private void initFirebase() {
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");//.child(SubjectName)
    }

    private void initViews(View view) {
        uploadStatusTV = view.findViewById(R.id.tv_upload_status);
        pdfNameET = view.findViewById(R.id.et_set_pdf_name);
        pdfComments = view.findViewById(R.id.et_comments);
        uploadProgressBar = view.findViewById(R.id.pb_uploading);
        selectUploadBtn = view.findViewById(R.id.btn_select_upload);
    }

    public void getPDF() {

        if (pdfNameET.getEditText().getText().toString().isEmpty() || pdfComments.getText().toString().isEmpty()){

            pdfNameET.setError("Empty Field");
            pdfComments.setError("Empty Field");

        } else {

            //Storage Access Permission from User ......

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Please Give Storage Access Permission", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
                startActivity(intent);
                return;
            }


            // Intent for choosing File..... only PDF can be selected

            Intent fileChooserIntent = new Intent();
            fileChooserIntent.setType("application/pdf");
            fileChooserIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(fileChooserIntent, "Select PDF"), USER_CHOOSE_PDF_CODE);

        }

    }

    //User Selected file data to be captured for uploading ....
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == USER_CHOOSE_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){

            //Check file Size :
            int pdfDataSize = 0;
            try {
                InputStream fileInputStream = context.getContentResolver().openInputStream(data.getData());
                pdfDataSize = fileInputStream.available();


                // .......... 10485760 Bytes = 10MB .......
                if (pdfDataSize > 10485760){
                    Log.d("File Size", "File Size is exceeding");
                    Log.d("File Size", String.valueOf(pdfDataSize));
                    Toast.makeText(context, "File Size Exceeds 10MB ... upload smaller file", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("File Size", "File Size is perfect");

                    // Again Cross checking if FILE is selected .....
                    if (data.getData() != null) {
                        //uploading the file
                        uploadFile(data.getData());
                    }else{
                        Toast.makeText(context, "No file chosen", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Uploading File to Firebase Now.....
    private void uploadFile(Uri pdfData){

        /* TODO :
            1) Hardcode File Name ....
            2) See for Assignment Name .....
            3)
            4) */

        uploadProgressBar.setVisibility(View.VISIBLE);
        StorageReference storageRef = mStorageReference.child(pdfNameET.getEditText().getText().toString() + ".pdf");
        storageRef.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploadProgressBar.setVisibility(View.GONE);
                uploadStatusTV.setText("File Uploaded Successfully");

                getUploadFileUrl();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                uploadProgressBar.setProgress((int)progress);
                uploadStatusTV.setText((int) progress + "% Uploading...");
            }
        });
    }

    //Getting Download Url for the File ...... for download
    private void getUploadFileUrl(){

        mStorageReference.child(pdfNameET.getEditText().getText().toString() + ".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("URL Capture", "URL captured Success");
                updateDBAfterFileUpload(uri.toString());
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("URL Capture error", "URL not Captured Error");
            }
        });
    }

    //Updating Database .....
    private void updateDBAfterFileUpload(String url){

        Log.d("Check URL : ", url);
        UploadPOJO upload = new UploadPOJO(pdfNameET.getEditText().getText().toString(), url, pdfComments.getText().toString());
        mDatabaseReference.child(mDatabaseReference.push().getKey()).setValue(upload);
        Log.d("UpdateDBForFile", "UPDATE SUCCESS !!!!");

    }

}

