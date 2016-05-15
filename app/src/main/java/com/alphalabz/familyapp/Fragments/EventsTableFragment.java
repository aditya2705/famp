package com.alphalabz.familyapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.activities.MainActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsTableFragment extends Fragment {


    private MainActivity mainActivity;


    private View rootView;
    private ViewPager monthPager;
    private MonthPagerAdapter adapter;

    public EventsTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_events_table, container, false);

        showViews();

        return rootView;

    }
    public void showViews() {

        monthPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        //monthPager.setOffscreenPageLimit(20);
        SmartTabLayout viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.month_tabs);

        adapter = new MonthPagerAdapter(getChildFragmentManager());
        monthPager.setAdapter(adapter);

        viewPagerTab.setViewPager(monthPager);

        Calendar currentDate = Calendar.getInstance();
        monthPager.setCurrentItem(currentDate.get(Calendar.MONTH));

    }

    public class MonthPagerAdapter extends FragmentPagerAdapter {
        private final String[] tab_names = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

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
        mainActivity = (MainActivity) activity;
    }
}
