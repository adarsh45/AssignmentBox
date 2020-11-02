package com.example.assignmentbox.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class SubjectPOJO implements Parcelable {

    private String subjectID, subjectName;

    public SubjectPOJO(){}

    public SubjectPOJO(String subjectID, String subjectName) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subjectID);
        dest.writeString(this.subjectName);
    }

    protected SubjectPOJO(Parcel in) {
        this.subjectID = in.readString();
        this.subjectName = in.readString();
    }

    public static final Parcelable.Creator<SubjectPOJO> CREATOR = new Parcelable.Creator<SubjectPOJO>() {
        @Override
        public SubjectPOJO createFromParcel(Parcel source) {
            return new SubjectPOJO(source);
        }

        @Override
        public SubjectPOJO[] newArray(int size) {
            return new SubjectPOJO[size];
        }
    };
}
