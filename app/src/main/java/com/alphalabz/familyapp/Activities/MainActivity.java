package com.alphalabz.familyapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alphalabz.familyapp.Fragments.BlankFragment;
import com.alphalabz.familyapp.Fragments.CalendarFragment;
import com.alphalabz.familyapp.Fragments.EventsTableFragment;
import com.alphalabz.familyapp.Fragments.GalleryFragment;
import com.alphalabz.familyapp.Fragments.SearchListFragment;
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

    private static final String RESULTS_FETCH_MEMBERS_URL = "http://alpha95.net63.net/get_members_2.php";
    private static final String RESULTS_FETCH_EVENTS_URL = "http://alpha95.net63.net/get_events.php";
    private static Context mContext;
    private Drawer result = null;
    private FloatingActionButton fab;
    private String membersListJsonString, eventsListJsonString;
    public SharedPreferences sharedPreferences;
    public ProgressDialog progressDialog;
    private float defaultElevation;


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
        defaultElevation = ((AppBarLayout)findViewById(R.id.app_bar_layout)).getTargetElevation();;

        mContext = getApplicationContext();

        //Create the drawer
        result = new DrawerBuilder(this)
                //this layout have to contain child layouts
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName("Tree View").withIcon(FontAwesome.Icon.faw_tree),
                        new PrimaryDrawerItem().withName("All Members List").withIcon(FontAwesome.Icon.faw_list_ul),
                        new PrimaryDrawerItem().withName("Calendar").withIcon(FontAwesome.Icon.faw_table),
                        new PrimaryDrawerItem().withName("All Events").withIcon(FontAwesome.Icon.faw_birthday_cake),
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
                                    fragment = new BlankFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 1:
                                    progressDialog.show();
                                    fragment = new TreeViewFragment();
                                    new Handler().postDelayed(new Runnable(){
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                        }
                                    }, 1000);

                                    break;
                                case 2:
                                    fragment = new SearchListFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 3:
                                    fragment = new CalendarFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 4:
                                    fragment = new EventsTableFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 5:
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.search).setVisible(false);
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
            getMembersData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMembersData() {
        class GetMembersData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                URL obj = null;
                String result = null;
                InputStream inputStream = null;
                try {
                    obj = new URL(RESULTS_FETCH_MEMBERS_URL);
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
                progressDialog.setTitle("Loading...");
                progressDialog.show();
                super.onPreExecute();
            }
        }
        GetMembersData g = new GetMembersData();
        g.execute();
    }


    public void getEventsData() {
        class GetEventsData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                URL obj = null;
                String result = null;
                InputStream inputStream = null;
                try {
                    obj = new URL(RESULTS_FETCH_EVENTS_URL);
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
                eventsListJsonString = result;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("EVENTS_STRING", membersListJsonString);
                editor.apply();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventsTableFragment) {
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EventsTableFragment()).commit();
                        }
                    }, 1000);

                }

            }

            @Override
            protected void onPreExecute() {
                progressDialog.setTitle("Loading...");
                progressDialog.show();
                super.onPreExecute();
            }
        }
        GetEventsData g = new GetEventsData();
        g.execute();
    }




}
