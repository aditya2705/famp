package com.alphalabz.familyapp;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sumeet on 21-Aug-16.
 */
public class MainApplication extends Application {

    private String urlToFetchFrom = "";

    public String getUrlToFetchFrom() {
        return urlToFetchFrom;
    }

    public void setUrlToFetchFrom(String urlToFetchFrom) {
        this.urlToFetchFrom = urlToFetchFrom;
    }
}
