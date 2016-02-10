package com.alphalabz.familyapp.Custom.SearchCustomClasses;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 24/05/15
 */
public class SearchMemberModel {

    private final String mText;

    public SearchMemberModel(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }
}