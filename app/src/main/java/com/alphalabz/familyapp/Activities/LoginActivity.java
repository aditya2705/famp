package com.alphalabz.familyapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alphalabz.familyapp.MainApplication;
import com.alphalabz.familyapp.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class LoginActivity extends AppCompatActivity {

    private static final String DEFAULT_URL = "http://www.alphalabz.com/family_app_url.php";
    private static final int RC_SIGN_IN = 9001;
    private static String AUTHENTICATE_PASSWORD_URL = "authenticate.php";
    private static String VERIFY_EMAIL_URL = "get_emails.php";
    @Bind(R.id.password_edit_text)
    AppCompatEditText passwordEditText;
    @Bind(R.id.google_sign_in_button)
    SignInButton signInButton;
    @Bind(R.id.google_sign_out_button)
    FButton signOutButton;
    private SharedPreferences sharedPreferences;
    private boolean authenticated = false;
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences("FAMP", 0);
        authenticated = sharedPreferences.getBoolean("Auth", false);

        if (authenticated) {
            String url = sharedPreferences.getString("URL_TO_FETCH_FROM","");
            ((MainApplication)getApplicationContext()).setUrlToFetchFrom(url);
            startMainActivity();
        }else{
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            getURLToFetchFrom();
            defaultSettingUp();
        }

    }

    private void defaultSettingUp() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // [END configure_si
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
                        // be available.
                        Log.d("LoginActivity", "onConnectionFailed:" + connectionResult);

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Attempting to Sign In...");
                progressDialog.show();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("LoginActivity", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {

            updateUI(true);
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personEmail = acct.getEmail();
            Log.d("PERSON EMAIL", personEmail + " signed in");
            progressDialog.dismiss();
            verifyEmail(personEmail);

        } else {
            Toast.makeText(LoginActivity.this, "Sign in failed! Try again", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    private void verifyEmail(final String email_id) {

        class VerifyEmailTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String result = null;

                try {
                    URL obj = new URL(VERIFY_EMAIL_URL + "?email=" + email_id);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setConnectTimeout(600000);

                    // optional default is GET
                    con.setRequestMethod("GET");

                    //add request header
                    con.setRequestProperty("User-Agent", "Mozilla/5.0");
                    // writing response to log
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    inputLine = in.readLine();
                    response.append(inputLine);

                    in.close();

                    result = response.toString();

                    Log.d("VERIFY EMAIL LENGTH", "" + result.length());
                    return result;

                } catch (Exception e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.length() > 0) {
                    authenticated = true;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Auth", true);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Verification successful!", Toast.LENGTH_SHORT).show();
                    startMainActivity();
                } else {
                    authenticated = false;
                    Toast.makeText(LoginActivity.this, "Verification failed!\nEmail ID not present in database.", Toast.LENGTH_SHORT).show();
                    signOut();
                }

                progressDialog.dismiss();

            }

            @Override
            protected void onPreExecute() {
                progressDialog.setTitle("Verifying Email...");
                progressDialog.show();
                if (!isNetworkAvailable()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Check Internet connection and try again. Exiting app...", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                }
                super.onPreExecute();
            }
        }
        VerifyEmailTask g = new VerifyEmailTask();
        g.execute();


    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            signInButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
        } else {

            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick(R.id.google_sign_out_button)
    public void signOut() {
        progressDialog.show();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
        progressDialog.dismiss();
    }
    // [END signOut]


    @OnClick(R.id.btn_authenticate)
    public void onAuthenticate() {


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

                    String line = reader.readLine();
                    sb.append(line);

                    result = sb.toString();
                    Log.d("RESULT", result);

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
                progressDialog.dismiss();
                if (result!=null && result.equals(passwordEditText.getText().toString())) {
                    authenticated = true;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("Auth", true);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Authentication successful!", Toast.LENGTH_SHORT).show();
                    startMainActivity();

                } else {
                    authenticated = false;
                    Toast.makeText(LoginActivity.this, "Authentication failed!\n", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected void onPreExecute() {
                progressDialog.setTitle("Authenticating Password...");
                progressDialog.show();
                if (!isNetworkAvailable()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Check Internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
                super.onPreExecute();
            }
        }
        AuthenticatePasswordTask g = new AuthenticatePasswordTask();
        g.execute();

    }

    private void startMainActivity() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getURLToFetchFrom() {

        class getURLToFetchFromTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                URL obj = null;
                String result = null;
                InputStream inputStream = null;
                try {
                    obj = new URL(DEFAULT_URL);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder sb = new StringBuilder();

                    String line = reader.readLine();
                    sb.append(line);

                    result = sb.toString();
                    Log.d("RESULT", result);

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
                progressDialog.dismiss();
                if (result!=null&&result.length()>0) {
                    ((MainApplication)getApplicationContext()).setUrlToFetchFrom("http://"+result);
                    MainApplication application = (MainApplication) getApplicationContext();
                    AUTHENTICATE_PASSWORD_URL = application.getUrlToFetchFrom()+"/"+AUTHENTICATE_PASSWORD_URL;
                    VERIFY_EMAIL_URL = application.getUrlToFetchFrom()+"/"+VERIFY_EMAIL_URL;
                    sharedPreferences.edit().putString("URL_TO_FETCH_FROM","http://"+result).apply();
                } else {
                    Toast.makeText(LoginActivity.this, "Check internet connection and try again. Exiting app...", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                }




            }

            @Override
            protected void onPreExecute() {
                progressDialog.setTitle("Loading...");
                progressDialog.show();
                if (!isNetworkAvailable()) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Check internet connection and try again. Exiting app...", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.finish();
                }
                super.onPreExecute();
            }
        }
        getURLToFetchFromTask g = new getURLToFetchFromTask();
        g.execute();


    }


}
