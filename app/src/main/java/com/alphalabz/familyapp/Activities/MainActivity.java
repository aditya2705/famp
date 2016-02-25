package com.alphalabz.familyapp.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alphalabz.familyapp.Fragments.CalendarFragment;
import com.alphalabz.familyapp.Fragments.ContactFragment;
import com.alphalabz.familyapp.Fragments.EventsTableFragment;
import com.alphalabz.familyapp.Fragments.GalleryFragment;
import com.alphalabz.familyapp.Fragments.SearchListFragment;
import com.alphalabz.familyapp.Fragments.SettingsFragment;
import com.alphalabz.familyapp.Fragments.SplashImageFragment;
import com.alphalabz.familyapp.Fragments.TreeViewFragment;
import com.alphalabz.familyapp.NotificationPublisher;
import com.alphalabz.familyapp.Objects.Event;
import com.alphalabz.familyapp.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private static final String RESULTS_FETCH_MEMBERS_URL = "http://alpha95.net63.net/get_members_2.php";
    private static final String RESULTS_FETCH_EVENTS_URL = "http://alpha95.net63.net/get_events.php";
    private static Context mContext;
    private Drawer drawer = null;
    private String membersListJsonString, eventsListJsonString;
    public SharedPreferences sharedPreferences;
    public ProgressDialog progressDialog;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "events_id";
    private static final String TAG_DATE = "date";
    private static final String TAG_BIRTHDAY = "birthday";
    private static final String TAG_ANNIVERSARY = "anniversary";
    private static final String TAG_REMARKS = "remarks";
    private static final String TAG_YEARS = "years";
    private static final String TAG_CITY = "city";
    private static final String TAG_CONTACT = "contact";
    private static final String TAG_EMAIL = "email";

    JSONArray eventsJsonArray = null;
    public LinkedHashMap<String, Event> eventIDMap = new LinkedHashMap<>();

    private int newDay = -1;


    public static Context getContext() {
        return mContext;
    }


    private boolean notificationCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationCall = getIntent().getBooleanExtra("Notification", false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("FAMP", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();

        //Create the drawer
        drawer = new DrawerBuilder(this)
                //this layout have to contain child layouts
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName("Tree View").withIcon(FontAwesome.Icon.faw_tree),
                        new PrimaryDrawerItem().withName("Members List").withIcon(FontAwesome.Icon.faw_list_ul),
                        new PrimaryDrawerItem().withName("Calendar").withIcon(FontAwesome.Icon.faw_table),
                        new PrimaryDrawerItem().withName("Events").withIcon(FontAwesome.Icon.faw_birthday_cake),
                        new PrimaryDrawerItem().withName("Gallery").withIcon(FontAwesome.Icon.faw_image),
                        new PrimaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog),
                        new PrimaryDrawerItem().withName("Contact").withIcon(FontAwesome.Icon.faw_user_md)

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
                                    fragment = new SplashImageFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 1:
                                    progressDialog.show();
                                    fragment = new TreeViewFragment();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                        }
                                    }, 300);

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
                                case 6:
                                    fragment = new SettingsFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 7:
                                    fragment = new ContactFragment();
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

        drawer.setSelectionAtPosition(0, true);

        membersListJsonString = sharedPreferences.getString("MEMBERS_STRING", "");
        eventsListJsonString = sharedPreferences.getString("EVENTS_STRING", "");
        newDay = sharedPreferences.getInt("NEW_DAY", -1);

        if (!notificationCall) {
            if (membersListJsonString.equals("") || eventsListJsonString.equals("")) {
                getMembersData();
            } else {
                if (newDay != Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                    progressDialog.show();
                    generateEventList();
                    scheduleNotifications();
                    progressDialog.dismiss();
                }
            }
        } else {

            showEventDialog((Event) getIntent().getSerializableExtra("Event"));

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.grid_view).setVisible(false);
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TreeViewFragment()).commit();
                        }
                    }, 1000);

                }

                getEventsData();

            }

            @Override
            protected void onPreExecute() {
                progressDialog.setTitle("Loading...");
                progressDialog.show();
                if (!isNetworkAvailable()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Check Internet connection and try again", Toast.LENGTH_SHORT).show();
                }
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
                editor.putString("EVENTS_STRING", eventsListJsonString);
                editor.apply();

                generateEventList();
                scheduleNotifications();

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof EventsTableFragment) {
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
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
                if (!isNetworkAvailable()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Check Internet connection and try again", Toast.LENGTH_SHORT).show();
                }
                super.onPreExecute();
            }
        }
        GetEventsData g = new GetEventsData();
        g.execute();
    }

    protected void generateEventList() {

        try {
            JSONObject jsonObj = new JSONObject(eventsListJsonString);
            eventsJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            LinkedHashMap<Event, String> dateStringMap = new LinkedHashMap<>();

            for (int i = 0; i < eventsJsonArray.length(); i++) {
                JSONObject c = eventsJsonArray.getJSONObject(i);

                String event_id, date, birthday, anniversary, remarks, years, city, contact, email;

                event_id = c.getString(TAG_ID);
                date = c.getString(TAG_DATE);
                birthday = c.getString(TAG_BIRTHDAY);
                anniversary = c.getString(TAG_ANNIVERSARY);
                remarks = c.getString(TAG_REMARKS);
                years = c.getString(TAG_YEARS);
                city = c.getString(TAG_CITY);
                contact = c.getString(TAG_CONTACT);
                email = c.getString(TAG_EMAIL);

                Event event = new Event(event_id, date, birthday, anniversary, remarks, years, city, contact, email);
                eventIDMap.put(event_id, event);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void scheduleNotifications() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("NEW_DAY", Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
        editor.apply();

        ArrayList<Event> eventArrayList = new ArrayList<>(eventIDMap.values());

        for (int i = 0; i < eventArrayList.size(); i++) {

            Event currentEvent = eventArrayList.get(i);

            String birthday = currentEvent.getBirthday();

            int event;
            if (birthday.equals("null") || birthday.equals("")) {
                event = 1;
            } else {
                event = 0;
            }

            String dateString = currentEvent.getDate();
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            int year = Integer.parseInt(yearFormat.format(Date.parse(dateString)));

            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            int day = Integer.parseInt(dayFormat.format(Date.parse(dateString)));

            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            int month = Integer.parseInt(monthFormat.format(Date.parse(dateString)));


            Calendar a = new GregorianCalendar(year, month - 1, day);
            Calendar b = Calendar.getInstance();
            int y1 = b.get(Calendar.YEAR);
            int y2 = a.get(Calendar.YEAR);
            int diff = y1 - y2;
            if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                    (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
                diff--;
            }


            Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.putExtra("Event", currentEvent);
            myIntent.putExtra("Notification", true);
            PendingIntent goToDifferentPendingIntent = PendingIntent.getActivity(
                    this,
                    i,
                    myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(event == 1 ? "Anniversary of " + currentEvent.getAnniversary() : "Birthday of " + currentEvent.getBirthday())
                    .setContentText("Date: " + dateString.substring(0, 9) + " Years: " + diff)
                    .setContentIntent(goToDifferentPendingIntent)
                    .setSmallIcon(event == 1 ? R.drawable.ic_love : R.drawable.ic_cake)
                    .setSound(alarmSound);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setPriority(Notification.PRIORITY_HIGH);
            }

            if (Build.VERSION.SDK_INT >= 21)
                builder.setVibrate(new long[0]);

            Notification notification = builder.build();
            notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            Intent notificationIntent = new Intent(this, NotificationPublisher.class);
            notificationIntent.setAction("com.alphalabz.familyapp" + i);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, i + 1);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
            PendingIntent broadcastIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (a.get(Calendar.MONTH) >= b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) >= b.get(Calendar.DAY_OF_MONTH)) {

                Calendar eventDataCalendar = new GregorianCalendar(b.get(Calendar.YEAR), a.get(Calendar.MONTH), a.get(Calendar.DAY_OF_MONTH));

                long futureInMillis = eventDataCalendar.getTimeInMillis();
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, broadcastIntent);

            }

        }
    }

    private void showEventDialog(final Event eventObject) {

        String birthday = eventObject.getBirthday();
        String marriage = eventObject.getAnniversary();

        int event, titleColor;
        if (!birthday.equals("null") && !birthday.equals("")) {
            event = 0;
            titleColor = R.color.birthday;
        } else {
            if (!marriage.equals("null") && !marriage.equals("")) {
                event = 1;
                titleColor = R.color.marriage;
            } else {
                event = 2;
                titleColor = R.color.death;
            }
        }

        final MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .theme(Theme.LIGHT)
                .title("Event")
                .icon(getResources().getDrawable(event == 1 ? R.drawable.ic_love : R.drawable.ic_cake))
                .titleColor(getResources().getColor(titleColor))
                .customView(R.layout.dialog_event_details, true)
                .positiveText("OK")
                .positiveColor(getResources().getColor(titleColor))
                .build();

        ((TextView) dialog.getCustomView().findViewById(R.id.event_type)).setText(event == 1 ? "Anniversary" : "Birthday");
        ((TextView) dialog.getCustomView().findViewById(R.id.members_concerned))
                .setText(event == 1 ? eventObject.getAnniversary() : eventObject.getBirthday());

        String dateString = eventObject.getDate();

        ((TextView) dialog.getCustomView().findViewById(R.id.date)).setText(dateString.substring(0, 9));

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(yearFormat.format(Date.parse(dateString)));

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        int day = Integer.parseInt(dayFormat.format(Date.parse(dateString)));

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        int month = Integer.parseInt(monthFormat.format(Date.parse(dateString)));


        Calendar a = new GregorianCalendar(year, month - 1, day);
        Calendar b = Calendar.getInstance();
        int y1 = b.get(Calendar.YEAR);
        int y2 = a.get(Calendar.YEAR);
        int diff = y1 - y2;
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
            diff--;
        }

        ((TextView) dialog.getCustomView().findViewById(R.id.years)).setText("YEARS: " + diff);

        String city = eventObject.getCity();
        ((TextView) dialog.getCustomView().findViewById(R.id.city)).setText(city.equals("") || city.equals("null") ? "" : "CITY: " + city);

        (dialog.getCustomView().findViewById(R.id.contact_click_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneIntent(eventObject.getContact());
            }
        });

        String temp = eventObject.getContact();
        if (!temp.equals("") && !temp.equals("null"))
            ((TextView) dialog.getCustomView().findViewById(R.id.contact)).setText("Contact: " + temp);
        else
            (dialog.getCustomView().findViewById(R.id.contact_click_layout)).setVisibility(View.GONE);

        (dialog.getCustomView().findViewById(R.id.email_click_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailIntent(eventObject.getEmail());
            }
        });

        temp = eventObject.getEmail();
        if (!temp.equals("") && !temp.equals("null"))
            ((TextView) dialog.getCustomView().findViewById(R.id.email)).setText("Email: " + temp);
        else
            (dialog.getCustomView().findViewById(R.id.email_click_layout)).setVisibility(View.GONE);

        dialog.show();
    }

    private void emailIntent(final String emailString) {

        new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title("EMAIL")
                .icon(getResources().getDrawable(R.drawable.ic_email))
                .content("Draft an email to " + emailString + " ?")
                .negativeText("NO")
                .positiveText("YES")
                .positiveColor(getResources().getColor(R.color.md_green_700))
                .titleColor(getResources().getColor(R.color.md_green_700))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailString));
                        email.putExtra(Intent.EXTRA_SUBJECT, "Your subject here");
                        email.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(email);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();


    }

    private void phoneIntent(final String phone) {

        new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .title("CALL")
                .icon(getResources().getDrawable(R.drawable.ic_contact_phone))
                .content("Call on " + phone + " ?")
                .positiveText("YES")
                .negativeText("NO")
                .positiveColor(getResources().getColor(R.color.md_green_700))
                .titleColor(getResources().getColor(R.color.md_green_700))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent phoneCallIntent;
                        phoneCallIntent = new Intent(Intent.ACTION_CALL);
                        phoneCallIntent.setData(Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(phoneCallIntent);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else
            super.onBackPressed();

    }
}
