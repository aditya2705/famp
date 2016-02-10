package com.alphalabz.familyapp.Fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alphalabz.familyapp.Activities.MainActivity;
import com.alphalabz.familyapp.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsTableFragment extends Fragment {

    private static final String RESULTS_FETCH_EVENTS_URL = "http://alpha95.net63.net/get_events.php";


    private String eventsListJsonString;

    private SharedPreferences sharedPreferences;
    private MainActivity mainActivity;


    private View rootView;
    private ViewPager monthPager;
    private MonthPagerAdapter adapter;

    public EventsTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("FAMP", 0);
        eventsListJsonString = sharedPreferences.getString("EVENTS_STRING", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_events_table, container, false);

        if (eventsListJsonString.equals("") || eventsListJsonString == null)
            getEventsData();
        else
            showViews();

        return rootView;

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
                mainActivity.progressDialog.dismiss();
                eventsListJsonString = result;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("EVENTS_STRING", eventsListJsonString);
                editor.apply();
                showViews();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mainActivity.progressDialog.setTitle("Loading...");
                mainActivity.progressDialog.show();
            }
        }
        GetEventsData g = new GetEventsData();
        g.execute();
    }

    public void showViews(){

        monthPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.month_tabs);

        adapter = new MonthPagerAdapter(getChildFragmentManager());
        monthPager.setAdapter(adapter);

        viewPagerTab.setViewPager(monthPager);

    }

    public class MonthPagerAdapter extends FragmentPagerAdapter {
        private final String[] tab_names = {"January", "February", "March", "April", "May", "June", "July", "August","September", "October","November","December"};

        private MonthPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tab_names[position];
        }

        @Override
        public int getCount() {
            return tab_names.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                default:
                    return MonthEventFragment.newInstance((position));
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }
}
