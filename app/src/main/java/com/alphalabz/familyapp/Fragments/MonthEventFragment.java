package com.alphalabz.familyapp.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alphalabz.familyapp.Custom.RecyclerItemClickListener;
import com.alphalabz.familyapp.Objects.Event;
import com.alphalabz.familyapp.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

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



        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        showList();

        adapter = new RecyclerAdapter(getActivity(),eventsList);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                showEventDialog(position);

            }
        }));
    }

    private void showEventDialog(final int position) {

        String birthday = eventsList.get(position).getBirthday();

        int event;
        if(birthday.equals("null")||birthday.equals("")){
            event = 1;
        }else{
            event = 0;
        }

        final MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title("Event")
                .icon(getResources().getDrawable(event==1?R.drawable.ic_love:R.drawable.ic_cake))
                .titleColor(getResources().getColor(R.color.md_green_700))
                .customView(R.layout.dialog_event_details, true)
                .positiveText("OK")
                .positiveColor(getResources().getColor(R.color.md_green_700))
                .build();


        ((TextView)dialog.getCustomView().findViewById(R.id.event_type)).setText(event==1?"Anniversary":"Birthday");
        ((TextView)dialog.getCustomView().findViewById(R.id.members_concerned)).setText(event==1?eventsList.get(position).getAnniversary():eventsList.get(position).getBirthday());

        String dateString = eventsList.get(position).getDate();

        ((TextView)dialog.getCustomView().findViewById(R.id.date)).setText(dateString.substring(0,9));

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(yearFormat.format(Date.parse(dateString)));

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        int day = Integer.parseInt(dayFormat.format(Date.parse(dateString)));

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        int month = Integer.parseInt(monthFormat.format(Date.parse(dateString)));


        Calendar a = new GregorianCalendar(year,month-1,day);
        Calendar b = Calendar.getInstance();
        int y1 = b.get(Calendar.YEAR);
        int y2 = a.get(Calendar.YEAR);
        int diff = y1-y2;
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) > b.get(Calendar.DAY_OF_MONTH))) {
            diff--;
        }

        ((TextView)dialog.getCustomView().findViewById(R.id.years)).setText("YEARS: "+diff);

        String city = eventsList.get(position).getCity();
        ((TextView)dialog.getCustomView().findViewById(R.id.city)).setText(city.equals("")||city.equals("null")?"":"CITY: "+city);

        (dialog.getCustomView().findViewById(R.id.contact_click_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneIntent(eventsList.get(position).getContact());
            }
        });

        String temp = eventsList.get(position).getContact();
        if(!temp.equals("")&&!temp.equals("null"))
            ((TextView)dialog.getCustomView().findViewById(R.id.contact)).setText("Contact: "+temp);
        else
            (dialog.getCustomView().findViewById(R.id.contact_click_layout)).setVisibility(View.GONE);

        (dialog.getCustomView().findViewById(R.id.email_click_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailIntent(eventsList.get(position).getEmail());
            }
        });

        temp = eventsList.get(position).getEmail();
        if(!temp.equals("")&&!temp.equals("null"))
            ((TextView)dialog.getCustomView().findViewById(R.id.email)).setText("Email: "+temp);
        else
            (dialog.getCustomView().findViewById(R.id.email_click_layout)).setVisibility(View.GONE);

        dialog.show();
    }

    protected void showList() {

        try {
            JSONObject jsonObj = new JSONObject(eventsListJsonString);
            eventsJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            LinkedHashMap<Event, String> dateStringMap = new LinkedHashMap<>();

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
                    dateStringMap.put(event,date.substring(0,6));
                }
            }

            dateStringMap = sortHashMapByValuesD(dateStringMap);
            eventsList = new ArrayList<>(dateStringMap.keySet());


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());

        Collections.sort(mapValues, new Comparator<String>() {
            DateFormat f = new SimpleDateFormat("dd-MMM");
            @Override
            public int compare(String o1, String o2) {
                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });


        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put(key, val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

        private ArrayList<Event> eventArrayList;
        private Context context;

        public RecyclerAdapter(Context context,ArrayList<Event> eventArrayList){
            this.context = context;
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
                holder.occasionView.setImageDrawable(new IconicsDrawable(context)
                        .icon(FontAwesome.Icon.faw_heart)
                        .color(context.getResources().getColor(R.color.md_grey_700))
                        .sizeDp(15));
                holder.detailsView.setText(anniversary);
            }else{
                holder.occasionView.setImageDrawable(new IconicsDrawable(context)
                        .icon(FontAwesome.Icon.faw_birthday_cake)
                        .color(context.getResources().getColor(R.color.md_grey_700))
                        .sizeDp(15));
                holder.detailsView.setText(birthday);
            }


        }

        @Override
        public int getItemCount() {
            return eventArrayList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView dateView, detailsView;
            public ImageView occasionView;

            public ViewHolder(View itemView) {
                super(itemView);
                dateView = (TextView) itemView.findViewById(R.id.date);
                occasionView = (ImageView) itemView.findViewById(R.id.occasion);
                detailsView = (TextView) itemView.findViewById(R.id.details);
            }
        }
    }

    private void emailIntent(final String emailString) {

        new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title("EMAIL")
                .icon(getResources().getDrawable(R.drawable.ic_email))
                .content("Draft an email to "+emailString+" ?")
                .negativeText("NO")
                .positiveText("YES")
                .positiveColor(getResources().getColor(R.color.md_green_700))
                .titleColor(getResources().getColor(R.color.md_green_700))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+emailString));
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

        new MaterialDialog.Builder(getActivity())
                .theme(Theme.LIGHT)
                .title("CALL")
                .icon(getResources().getDrawable(R.drawable.ic_contact_phone))
                .content("Call on "+phone+" ?")
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
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

}
