package com.alphalabz.familyapp.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alphalabz.familyapp.Custom.RecyclerItemClickListener;
import com.alphalabz.familyapp.Objects.Event;
import com.alphalabz.familyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
* Use the {@link MonthEventFragment#newInstance} factory method to
        * create an instance of this fragment.
        */
public class MonthEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MONTH = "month";

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
    private String eventsListJsonString;

    private SharedPreferences sharedPreferences;

    // TODO: Rename and change types of parameters
    private int month;
    private String[] monthStrings={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    private ArrayList<Event> eventsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;

    private View rootView;


    public MonthEventFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MonthEventFragment newInstance(int param1) {
        MonthEventFragment fragment = new MonthEventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MONTH, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            month = getArguments().getInt(ARG_MONTH);
        }
        sharedPreferences = getActivity().getSharedPreferences("FAMP", 0);
        eventsListJsonString = sharedPreferences.getString("EVENTS_STRING", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_month_event, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.events_list_view);

        showList();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(eventsList);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .theme(Theme.LIGHT)
                        .title("Event")
                        .titleColor(getResources().getColor(R.color.md_green_700))
                        .customView(R.layout.dialog_event_details, true)
                        .positiveText("OK")
                        .positiveColor(getResources().getColor(R.color.md_green_700))
                        .build();

                String birthday = eventsList.get(position).getBirthday();
                String s = eventsList.get(position).getYears();

                float years = Float.parseFloat(s.equals("")||s.equals("null")?"0":s);

                int event;
                if(birthday.equals("null")||birthday.equals("")){
                    event = 1;
                }else{
                    event = 0;
                }

                ((TextView)dialog.getCustomView().findViewById(R.id.event_type)).setText(event==1?"Anniversary":"Birthday");
                ((TextView)dialog.getCustomView().findViewById(R.id.members_concerned)).setText(event==1?eventsList.get(position).getAnniversary():eventsList.get(position).getBirthday());
                ((TextView)dialog.getCustomView().findViewById(R.id.date)).setText(eventsList.get(position).getDate().substring(0,9));
                ((TextView)dialog.getCustomView().findViewById(R.id.years)).setText("YEARS:\n"+Math.round(years)+"");
                ((TextView)dialog.getCustomView().findViewById(R.id.remarks)).setText("REMARKS:\n"+eventsList.get(position).getRemarks());

                String city = eventsList.get(position).getCity();
                ((TextView)dialog.getCustomView().findViewById(R.id.city)).setText(city.equals("")||city.equals("null")?"":"CITY: "+city);

                String temp = eventsList.get(position).getContact();
                if(!temp.equals("")&&!temp.equals("null"))
                    ((TextView)dialog.getCustomView().findViewById(R.id.contact)).setText("Contact: "+temp);
                else
                    ((TextView)dialog.getCustomView().findViewById(R.id.contact)).setVisibility(View.GONE);

                temp = eventsList.get(position).getEmail();
                if(!temp.equals("")&&!temp.equals("null"))
                    ((TextView)dialog.getCustomView().findViewById(R.id.email)).setText("Email: "+temp);
                else
                    ((TextView)dialog.getCustomView().findViewById(R.id.email)).setVisibility(View.GONE);

                dialog.show();

            }
        }));
    }

    protected void showList() {

        try {
            JSONObject jsonObj = new JSONObject(eventsListJsonString);
            eventsJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < eventsJsonArray.length(); i++) {
                JSONObject c = eventsJsonArray.getJSONObject(i);

                String event_id,date,birthday,anniversary,remarks,years,city,contact,email;

                event_id = c.getString(TAG_ID);
                date = c.getString(TAG_DATE);
                birthday = c.getString(TAG_BIRTHDAY);
                anniversary = c.getString(TAG_ANNIVERSARY);
                remarks = c.getString(TAG_REMARKS);
                years = c.getString(TAG_YEARS);
                city = c.getString(TAG_CITY);
                contact = c.getString(TAG_CONTACT);
                email = c.getString(TAG_EMAIL);

                String s = "";

                if(date.length()>=10)
                    s = date.substring(3,6);

                if(monthStrings[month].equals(s)){
                    Event event = new Event(event_id,date,birthday,anniversary,remarks,years,city,contact,email);
                    eventsList.add(event);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        private ArrayList<Event> eventArrayList;

        public RecyclerAdapter(ArrayList<Event> eventArrayList){
            this.eventArrayList = eventArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.dateView.setText(eventArrayList.get(position).getDate().substring(0,10));
            String birthday = eventArrayList.get(position).getBirthday();
            String anniversary = eventArrayList.get(position).getAnniversary();

            if(birthday.equals("null")||birthday.equals("")){
                holder.occasionView.setText("Anniversary:\n"+anniversary);
            }else{
                holder.occasionView.setText("Birthday:\n"+birthday);
            }


        }

        @Override
        public int getItemCount() {
            return eventArrayList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView dateView, occasionView;

            public ViewHolder(View itemView) {
                super(itemView);
                dateView = (TextView) itemView.findViewById(R.id.date);
                occasionView = (TextView) itemView.findViewById(R.id.occasion);
            }
        }
    }

}
