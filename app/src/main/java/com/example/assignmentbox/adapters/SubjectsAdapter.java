package com.example.assignmentbox.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmentbox.R;
import com.example.assignmentbox.pojo.SubjectPOJO;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.SubjectsViewHolder> {

    private static final String TAG = "SubjectAdapter";

    private ArrayList<SubjectPOJO> subjectPOJOS;

    public SubjectsAdapter(ArrayList<SubjectPOJO> subjectPOJOArrayList){
        this.subjectPOJOS = subjectPOJOArrayList;
    }

    @NonNull
    @Override
    public SubjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_subject, parent, false);
        return new SubjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsViewHolder holder, int position) {
        SubjectPOJO currentSubject = subjectPOJOS.get(position);
        if (currentSubject == null){
            Log.d(TAG, "onBindViewHolder: got subjectPOJO as Null");
            return;
        }
        holder.rvItemTVSubjectCount.setText(String.valueOf(position + 1));
        holder.rvItemTVSubjectName.setText(currentSubject.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return subjectPOJOS.size();
    }

    public class SubjectsViewHolder extends RecyclerView.ViewHolder{

        public TextView rvItemTVSubjectCount, rvItemTVSubjectName;

        public SubjectsViewHolder(@NonNull View itemView) {
            super(itemView);
            rvItemTVSubjectCount = itemView.findViewById(R.id.rv_item_tv_subject_count);
            rvItemTVSubjectName = itemView.findViewById(R.id.rv_item_tv_subject_name);
        }
    }
}
