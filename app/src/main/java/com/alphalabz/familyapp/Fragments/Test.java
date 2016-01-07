package com.alphalabz.familyapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


public class Test extends Fragment {
    private Fragment myContext;

    public Test() {
        // Required empty public constructor
    }

    public static Test newInstance(String param1, String param2) {
        Test fragment = new Test();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add("All", BlankFragment.class)
                .add("Bday", BlankFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        return v;
    }


}
