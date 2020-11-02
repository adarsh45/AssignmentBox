package com.example.assignmentbox.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.assignmentbox.R;
import com.example.assignmentbox.pojo.SubjectPOJO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SubjectDialog extends DialogFragment {

    private static final String TAG = "SubjectDialog";

    private EditText etSubjectName;
    private Button btnAddSubject, btnCancelSubject;

    private FirebaseDatabase myDB;
    private DatabaseReference subjectRef;

    private Context context;

    public SubjectDialog(Context context) {
        super();
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_subject, null);

        initViews(view);

        initFirebase();

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etSubjectName.getText())){
                    etSubjectName.setError("Subject Name cannot be empty!");
                    return;
                }
                if (etSubjectName.getText().length() > 25){
                    etSubjectName.setError("Too much long name! Servers gonna cry!");
                    return;
                }
                String subName = etSubjectName.getText().toString();
                String subjectID = subjectRef.push().getKey();

                if (subjectID == null){
                    Log.d(TAG, "onClick: subjectID is null");
                    Toast.makeText(context, "subjectID is null", Toast.LENGTH_SHORT).show();
                    return;
                }

                SubjectPOJO subjectPOJO = new SubjectPOJO(subjectID, subName);
                subjectRef.child(subjectID).setValue(subjectPOJO).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "onComplete: Subject added to DB successfully");
                            Toast.makeText(context, "Subject added to DB successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Subject adding Failed at DB", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Subject adding Failed at DB");
                        }
                    }
                });

                dismiss();
            }
        });
        btnCancelSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        setCancelable(false);
        return view;
    }

    private void initViews(View view) {
        etSubjectName = view.findViewById(R.id.et_subject_name);
        btnAddSubject = view.findViewById(R.id.btn_add_subject);
        btnCancelSubject = view.findViewById(R.id.btn_cancel_subject);
    }

    private void initFirebase() {
        myDB = FirebaseDatabase.getInstance();
        subjectRef = myDB.getReference("Subjects");
    }
}
