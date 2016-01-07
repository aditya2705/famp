package com.alphalabz.familyapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.alphalabz.familyapp.Activities.MainActivity;
import com.alphalabz.familyapp.R;


public class EventRowFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public EventRowFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        //Call FragmentManager and add Fragment to it.
        Fragment fragment;
        fragment = new ProfileFragment();
        Bundle b = new Bundle();
        b.putString("profile_id", "ady123");
        fragment.setArguments(b);
        ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        ((MainActivity) getContext()).getSupportActionBar().setTitle("Profile");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_row, container, false);
        TableLayout tl = (TableLayout) v.findViewById(R.id.month_table);
       /* for (int i = 0; i < 9; i++) {
            View row = LayoutInflater.from(getContext()).inflate(R.layout.event_data,container, false);

            tl.addView(row);

            TextView date = (TextView) row.findViewById(R.id.date_row);
            TextView bday = (TextView) row.findViewById(R.id.birthday_row);
            date.setText("10/12/2015");
            bday.setText("Mr.Sharma");
            bday.setOnClickListener(this);
        }*/

        return v;
    }


}