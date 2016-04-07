package com.alphalabz.familyapp.custom.SearchCustomClasses;

public class SearchMemberModel {

    private String mNameString, mUniqueIDString;

    public SearchMemberModel(String mUniqueIDString, String mNameString) {
        this.mNameString = mNameString;
        this.mUniqueIDString = mUniqueIDString;
    }

    public String getNameString() {
        return mNameString;
    }

    public void setNameString(String mNameString) {
        this.mNameString = mNameString;
    }

    public String getUniqueIDString() {
        return mUniqueIDString;
    }

    public void setUniqueIDString(String mUniqueIDString) {
        this.mUniqueIDString = mUniqueIDString;
    }
}