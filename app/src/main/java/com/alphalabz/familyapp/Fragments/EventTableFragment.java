package com.alphalabz.familyapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.Adapters.EventTableAdapter;
import com.alphalabz.familyapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTableFragment extends Fragment {


    public EventTableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_table, container, false);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<String> myDataset = new ArrayList<String>(15);
        myDataset.add("January");
        myDataset.add("February");
        myDataset.add("March");
        myDataset.add("April");
        myDataset.add("May");
        myDataset.add("June");
        myDataset.add("July");

        // specify an adapter (see also next example)
        EventTableAdapter mAdapter = new EventTableAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

}
