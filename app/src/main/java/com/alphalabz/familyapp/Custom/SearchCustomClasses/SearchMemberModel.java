package com.alphalabz.familyapp.custom.SearchCustomClasses;

public class SearchMemberModel {

    private String mNameString, mUniqueIDString, imageUrl;

    public SearchMemberModel(String mUniqueIDString, String mNameString, String imageUrl) {
        this.mNameString = mNameString;
        this.mUniqueIDString = mUniqueIDString;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}