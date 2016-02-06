package com.alphalabz.familyapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alphalabz.familyapp.Fragments.EventTableFragment;
import com.alphalabz.familyapp.Fragments.EventsFragment;
import com.alphalabz.familyapp.Fragments.GalleryFragment;
import com.alphalabz.familyapp.Fragments.TreeViewFragment;
import com.alphalabz.familyapp.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String RESULTS_FETCH_URL = "http://alpha95.net63.net/get_members_2.php";
    private static Context mContext;
    private Drawer result = null;
    private FloatingActionButton fab;
    private String membersListJsonString;
    private SharedPreferences sharedPreferences;
    public ProgressDialog progressDialog;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("FAMP", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();

        //Create the drawer
        result = new DrawerBuilder(this)
                //this layout have to contain child layouts
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName("Events").withIcon(FontAwesome.Icon.faw_birthday_cake),
                        new PrimaryDrawerItem().withName("Data Table").withIcon(FontAwesome.Icon.faw_table),
                        new PrimaryDrawerItem().withName("Gallery").withIcon(FontAwesome.Icon.faw_image)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {

                        if (drawerItem != null && drawerItem instanceof Nameable) {
                            String name = ((Nameable) drawerItem).getName().getText(MainActivity.this);
                            getSupportActionBar().setTitle(name);
                            final Fragment fragment;
                            switch (i) {
                                case 0:
                                    progressDialog.show();
                                    fragment = new TreeViewFragment();
                                    new Handler().postDelayed(new Runnable(){
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                        }
                                    }, 1000);

                                    break;
                                case 1:
                                    fragment = new EventsFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 2:
                                    fragment = new EventTableFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 3:
                                    fragment = new GalleryFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                            }
                        }

                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }
                })
                .withFireOnInitialOnClick(false)
                .withSavedInstance(savedInstanceState)
                .build();

        result.setSelectionAtPosition(0,true);


    }

    public void showDialog(String title, String content, final String agree) {
        MaterialDialog dialog = new MaterialDialog.Builder(new MainActivity())
                .title(title)
                .items("Xyz Birthday")
                .positiveText(agree)
                .titleColor(Color.BLACK)
                .contentColor(Color.BLACK) // notice no 'res' postfix for literal color
                .dividerColorRes(R.color.md_red_600)
                .backgroundColorRes(R.color.md_white_1000)
                .positiveColorRes(R.color.md_green_500)
                .build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            getData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                URL obj = null;
                String result = null;
                InputStream inputStream = null;
                try {
                    obj = new URL(RESULTS_FETCH_URL);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    //add request header
                    con.setRequestProperty("Content-Type", "application/json");
                    inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
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
                membersListJsonString = result;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MEMBERS_STRING", membersListJsonString);
                editor.apply();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof TreeViewFragment) {
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TreeViewFragment()).commit();
                        }
                    }, 1000);

                }

            }

            @Override
            protected void onPreExecute() {
                progressDialog.show();
                super.onPreExecute();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


}
