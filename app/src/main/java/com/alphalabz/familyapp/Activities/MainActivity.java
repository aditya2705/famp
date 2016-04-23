package com.alphalabz.familyapp.activities;

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
import com.alphalabz.familyapp.fragments.CalendarFragment;
import com.alphalabz.familyapp.fragments.ContactFragment;
import com.alphalabz.familyapp.fragments.EventsTableFragment;
import com.alphalabz.familyapp.fragments.GalleryFragment;
import com.alphalabz.familyapp.fragments.SearchListFragment;
import com.alphalabz.familyapp.fragments.SettingsFragment;
import com.alphalabz.familyapp.fragments.SplashImageFragment;
import com.alphalabz.familyapp.fragments.TreeViewFragment;
import com.alphalabz.familyapp.NotificationPublisher;
import com.alphalabz.familyapp.objects.Event;
import com.alphalabz.familyapp.objects.Person;
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

    private static final String RESULTS_FETCH_MEMBERS_URL = "http://alpha95.net63.net/get_members_3.php";

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "unique_id";
    private static final String TAG_GEN = "gen";
    private static final String TAG_TITLE = "title";
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_MIDDLE_NAME = "middle_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_NICK_NAME = "nick_name";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_IN_LAW = "in_law";
    private static final String TAG_MOTHER_ID = "mother_id";
    private static final String TAG_MOTHER_NAME = "mother_name";
    private static final String TAG_FATHER_ID = "father_id";
    private static final String TAG_FATHER_NAME = "father_name";
    private static final String TAG_SPOUSE_ID = "spouse_id";
    private static final String TAG_SPOUSE_NAME = "spouse_name";
    private static final String TAG_BIRTH_DATE = "dob";
    private static final String TAG_MARRIAGE_DATE = "dom";
    private static final String TAG_DEATH_DATE = "dod";
    private static final String TAG_MOBILE_NUMBER = "mobile_number";
    private static final String TAG_ALTERNATE_NUMBER = "alternate_number";
    private static final String TAG_RESIDENCE_NUMBER = "residence_number";
    private static final String TAG_EMAIL1 = "email1";
    private static final String TAG_EMAIL2 = "email2";
    private static final String TAG_ADDRESS_1 = "address_1";
    private static final String TAG_ADDRESS_2 = "address_2";
    private static final String TAG_CITY = "city";
    private static final String TAG_STATE_COUNTRY = "state_country";
    private static final String TAG_PINCODE = "pincode";
    private static final String TAG_DESIGNATION = "designation";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_INDUSTRY_SPECIAL = "industry_special";
    private static final String TAG_IMAGE_URL = "image_url";

    public LinkedHashMap<String, Person> membersListMap = new LinkedHashMap<>();
    private JSONArray membersJsonArray = null;

    public ArrayList<Event> eventArrayList = new ArrayList<>();
    public ArrayList<Person> personArrayList = new ArrayList<>();


    private static Context mContext;
    private Drawer drawer = null;
    private String membersListJsonString;
    public SharedPreferences sharedPreferences;
    public ProgressDialog progressDialog;


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
        newDay = sharedPreferences.getInt("NEW_DAY", -1);

        if (!notificationCall) {
            if (membersListJsonString.equals("")) {
                getMembersData();
            } else {

                generateMembersList();
                generateEventsList();

                if (newDay != Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                    progressDialog.show();
                    scheduleNotifications();
                    progressDialog.dismiss();
                }
            }
        } else {

            //showEventDialog((Event) getIntent().getSerializableExtra("Event"));

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

                if(!membersListJsonString.equals("")) {
                    generateMembersList();
                    generateEventsList();
                }

                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof TreeViewFragment) {
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
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
                if (!isNetworkAvailable()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Check Internet connection and try again. Exiting app...", Toast.LENGTH_SHORT).show();
                    MainActivity.this.finish();
                }
                super.onPreExecute();
            }
        }
        GetMembersData g = new GetMembersData();
        g.execute();
    }


    protected void generateMembersList(){

        membersListMap = new LinkedHashMap<>();

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(membersListJsonString);

            membersJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < membersJsonArray.length(); i++) {
                JSONObject c = membersJsonArray.getJSONObject(i);

                String unique_id, generation, title, first_name, middle_name, last_name, nick_name, gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id, spouse_name, birth_date, marriage_date, death_date,
                        mobile_number, alternate_number, residence_number, email1, email2, address_1, address_2, city, state_country, pincode, designation, company, industry_special, image_url;


                unique_id = c.optString(TAG_ID);
                generation = c.optString(TAG_GEN);
                title = c.optString(TAG_TITLE);
                first_name = c.optString(TAG_FIRST_NAME);
                middle_name = c.optString(TAG_MIDDLE_NAME);
                last_name = c.optString(TAG_LAST_NAME);
                nick_name = c.optString(TAG_NICK_NAME);
                gender = c.optString(TAG_GENDER);
                in_law = c.optString(TAG_IN_LAW);
                mother_id = c.optString(TAG_MOTHER_ID);
                mother_name = c.optString(TAG_MOTHER_NAME);
                father_id = c.optString(TAG_FATHER_ID);
                father_name = c.optString(TAG_FATHER_NAME);
                spouse_id = c.optString(TAG_SPOUSE_ID);
                spouse_name = c.optString(TAG_SPOUSE_NAME);
                birth_date = c.optString(TAG_BIRTH_DATE);
                marriage_date = c.optString(TAG_MARRIAGE_DATE);
                death_date = c.optString(TAG_DEATH_DATE);
                mobile_number = c.optString(TAG_MOBILE_NUMBER);
                alternate_number = c.optString(TAG_ALTERNATE_NUMBER);
                residence_number = c.optString(TAG_RESIDENCE_NUMBER);
                email1 = c.optString(TAG_EMAIL1);
                email2 = c.optString(TAG_EMAIL2);
                address_1 = c.optString(TAG_ADDRESS_1);
                address_2 = c.optString(TAG_ADDRESS_2);
                city = c.optString(TAG_CITY);
                state_country = c.optString(TAG_STATE_COUNTRY);
                pincode = c.optString(TAG_PINCODE);
                designation = c.optString(TAG_DESIGNATION);
                company = c.optString(TAG_COMPANY);
                industry_special = c.optString(TAG_INDUSTRY_SPECIAL);
                image_url = c.optString(TAG_IMAGE_URL);


                Person person = new Person(unique_id, generation, title, first_name, middle_name, last_name, nick_name,
                        gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id,
                        spouse_name, birth_date, marriage_date, death_date, mobile_number, alternate_number,
                        residence_number, email1, email2, address_1, address_2, city, state_country, pincode,
                        designation, company, industry_special, image_url, -1);

                personArrayList.add(person);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Person person : personArrayList) {
            membersListMap.put(person.getUnique_id(), person);
        }


    }

    protected void generateEventsList() {

        eventArrayList = new ArrayList<>();
        int id=0;

        for(Person person : membersListMap.values()){

            Event tempEvent;

            if(person.getBirth_date()!=null&&!person.getBirth_date().equals("null")&&!person.getBirth_date().equals("")){
                tempEvent = new Event(id++,person.getUnique_id(),person.getBirth_date(),person.getCity(),person.getMobile_number(),person.getEmail1(),0);
                eventArrayList.add(tempEvent);
            }

            if(person.getMarriage_date()!=null&&!person.getMarriage_date().equals("null")&&!person.getMarriage_date().equals("")){
                boolean addEvent = true;
                for(int k = 0; k < eventArrayList.size(); k++){
                    Event event = eventArrayList.get(k);
                    if(event.getMember_id().equals(person.getSpouse_id())&&event.getSpouse_id().equals(person.getUnique_id())&&event.getEventType()==1){
                        addEvent = false;
                        break;
                    }
                }
                if(addEvent) {
                    tempEvent = new Event(id++,person.getUnique_id(),person.getMarriage_date(),person.getCity(),person.getMobile_number(),person.getEmail1(),1);
                    tempEvent.setSpouse_id(person.getSpouse_id());
                    eventArrayList.add(tempEvent);
                }
            }

            if(person.getDeath_date()!=null&&!person.getDeath_date().equals("null")&&!person.getDeath_date().equals("")){
                tempEvent = new Event(id++,person.getUnique_id(),person.getDeath_date(),person.getCity(),person.getMobile_number(),person.getEmail1(),2);
                eventArrayList.add(tempEvent);
            }

        }

        Log.d("Event List",eventArrayList.toString());
    }

    private void scheduleNotifications() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("NEW_DAY", Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
        editor.apply();

        for (int i = 0; i < eventArrayList.size(); i++) {

            Event currentEvent = eventArrayList.get(i);

            int typeOfEvent = currentEvent.getEventType();

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

            String contentTitle = "";
            int contentIcon = -1;

            switch (typeOfEvent){

                case 0:
                    contentTitle = "Birthday of "+ membersListMap.get(currentEvent.getMember_id()).getFirst_name();
                    contentIcon = R.drawable.ic_cake;
                    break;
                case 1:
                    contentTitle = "Marriage Anniversary of "+ membersListMap.get(currentEvent.getMember_id()).getFirst_name();
                    contentIcon = R.drawable.ic_love;
                    break;
                case 2:
                    contentTitle = "Death Anniversary of "+ membersListMap.get(currentEvent.getMember_id()).getFirst_name();
                    contentIcon = R.drawable.ic_star;
                    break;

            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(contentTitle)
                    .setContentText("Date: " + dateString.substring(0, 9) + " Years: " + diff)
                    .setContentIntent(goToDifferentPendingIntent)
                    .setSmallIcon(contentIcon)
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

    public void showEventDialog(final Event eventObject) {

        int typeOfEvent = eventObject.getEventType();

        String content = "", contentType ="";
        int contentIcon = -1;
        int titleColor = -1;

        Person actualMember = membersListMap.get(eventObject.getMember_id());

        String memberName = ((actualMember.getTitle().equals("null") || actualMember.getTitle().equals("")) ? "" : (actualMember.getTitle() + " ")) +
                actualMember.getFirst_name() + (actualMember.getMiddle_name().equals("null") ? "" : " " + actualMember.getMiddle_name())
                + " " + actualMember.getLast_name();

        switch (typeOfEvent){

            case 0:
                content = memberName;
                contentType = "Birthday";
                contentIcon = R.drawable.ic_cake;
                titleColor = R.color.birthday;
                break;
            case 1:
                Person spouseOfMember = membersListMap.get(actualMember.getSpouse_id());
                String spouseName = ((spouseOfMember.getTitle().equals("null") || spouseOfMember.getTitle().equals("")) ? "" : (spouseOfMember.getTitle() + " ")) +
                        spouseOfMember.getFirst_name() + (spouseOfMember.getMiddle_name().equals("null") ? "" : " " + spouseOfMember.getMiddle_name())
                        + " " + spouseOfMember.getLast_name();
                content = memberName+" & "+spouseName;
                contentType = "Marriage Anniversary";
                contentIcon = R.drawable.ic_love;
                titleColor = R.color.marriage;
                break;
            case 2:
                content = memberName;
                contentType = "Death Anniversary";
                contentIcon = R.drawable.ic_star;
                titleColor = R.color.death;
                break;

        }

        final MaterialDialog eventDialog = new MaterialDialog.Builder(MainActivity.this)
                .theme(Theme.LIGHT)
                .title("Event")
                .icon(getResources().getDrawable(contentIcon))
                .titleColor(getResources().getColor(titleColor))
                .customView(R.layout.dialog_event_details, true)
                .positiveText("OK")
                .positiveColor(getResources().getColor(titleColor))
                .build();

        ((TextView) eventDialog.getCustomView().findViewById(R.id.event_type)).setText(contentType);
        ((TextView) eventDialog.getCustomView().findViewById(R.id.members_concerned))
                .setText(content);

        String dateString = eventObject.getDate();

        ((TextView) eventDialog.getCustomView().findViewById(R.id.date)).setText(dateString.substring(0, 9));

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

        ((TextView) eventDialog.getCustomView().findViewById(R.id.years)).setText("YEARS: " + diff);

        String city = eventObject.getCity();
        ((TextView) eventDialog.getCustomView().findViewById(R.id.city)).setText(city.equals("") || city.equals("null") ? "" : "CITY: " + city);

        (eventDialog.getCustomView().findViewById(R.id.contact_click_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneIntent(eventObject.getContact());
            }
        });

        String temp = eventObject.getContact();
        if (!temp.equals("") && !temp.equals("null"))
            ((TextView) eventDialog.getCustomView().findViewById(R.id.contact)).setText("Contact: " + temp);
        else
            (eventDialog.getCustomView().findViewById(R.id.contact_click_layout)).setVisibility(View.GONE);

        (eventDialog.getCustomView().findViewById(R.id.email_click_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailIntent(eventObject.getEmail());
            }
        });

        temp = eventObject.getEmail();
        if (!temp.equals("") && !temp.equals("null"))
            ((TextView) eventDialog.getCustomView().findViewById(R.id.email)).setText("Email: " + temp);
        else
            (eventDialog.getCustomView().findViewById(R.id.email_click_layout)).setVisibility(View.GONE);

        eventDialog.show();

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
