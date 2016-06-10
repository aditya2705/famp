package com.alphalabz.familyapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.widget.Toast;

import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.fragments.TreeViewFragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String AUTHENTICATE_PASSWORD_URL = "http://alpha95.net63.net/authenticate.php";

    private SharedPreferences sharedPreferences;
    private boolean authenticated = false;
    private ProgressDialog progressDialog;

    @Bind(R.id.password_edit_text) AppCompatEditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("FAMP", 0);
        authenticated = sharedPreferences.getBoolean("Auth",false);

        if(authenticated)
            startMainActivity();

    }

    @OnClick(R.id.btn_authenticate)
    public void onAuthenticate(){

        class AuthenticatePasswordTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                URL obj = null;
                String result = null;
                InputStream inputStream = null;
                try {
                    obj = new URL(AUTHENTICATE_PASSWORD_URL);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    if((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    Log.d("RESULT", result.substring(0,8));

                } catch (Exception e) {
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.substring(0,8).equals(passwordEditText.getText().toString())){
                    authenticated = true;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Auth",true);
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Password successful!",Toast.LENGTH_SHORT).show();
                    startMainActivity();
                }else{
                    authenticated = false;
                    Toast.makeText(LoginActivity.this,"Password failed!",Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();


            }

            @Override
            protected void onPreExecute() {
                progressDialog.setTitle("Authenticating...");
                progressDialog.show();
                if (!isNetworkAvailable()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Check Internet connection and try again. Exiting app...", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                }
                super.onPreExecute();
            }
        }
        AuthenticatePasswordTask g = new AuthenticatePasswordTask();
        g.execute();

    }

    private void startMainActivity() {

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
