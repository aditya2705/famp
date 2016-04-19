package com.alphalabz.familyapp.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alphalabz.familyapp.objects.Person;
import com.alphalabz.familyapp.activities.MainActivity;
import com.alphalabz.familyapp.custom.RecyclerItemClickListener;
import com.alphalabz.familyapp.objects.Event;
import com.alphalabz.familyapp.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthEventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MONTH = "month";

    // TODO: Rename and change types of parameters
    private int month;
    private String[] monthStrings = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;

    private View rootView;
    private ArrayList<Event> sortedEventsList;

    private MainActivity mainActivity;


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

        LinkedHashMap<Event, String> dateStringMap = new LinkedHashMap<>();

        for (int i = 0; i < mainActivity.eventArrayList.size(); i++) {

            Event event = mainActivity.eventArrayList.get(i);
            String date = event.getDate();
            String s = "";

            if (date.length() >= 10)
                s = date.substring(3, 6);

            if (monthStrings[month].equals(s)) {
                dateStringMap.put(event, date.substring(0, 6));
            }
        }

        dateStringMap = sortHashMapByValuesD(dateStringMap);
        sortedEventsList = new ArrayList<>(dateStringMap.keySet());


        adapter = new RecyclerAdapter(mainActivity, sortedEventsList);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                showEventDialog(position);

            }
        }));
    }

    private void showEventDialog(final int position) {

        final Event eventObject = sortedEventsList.get(position);

        int typeOfEvent = eventObject.getEventType();

        String content = "", contentType ="";
        int contentIcon = -1;
        int titleColor = -1;

        Person actualMember = mainActivity.membersListMap.get(eventObject.getMember_id());

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
                Person spouseOfMember = mainActivity.membersListMap.get(actualMember.getSpouse_id());
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

        final MaterialDialog eventDialog = new MaterialDialog.Builder(getActivity())
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

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put(key, val);
                    break;
                }

            }

        }
        return sortedMap;
    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private ArrayList<Event> eventArrayList;
        private MainActivity mainActivity;

        public RecyclerAdapter(MainActivity mainActivity, ArrayList<Event> eventArrayList) {
            this.mainActivity = mainActivity;
            this.eventArrayList = eventArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Event event = eventArrayList.get(position);

            holder.dateView.setText(event.getDate().substring(0, 10));
            FontAwesome.Icon icon = null;
            int color = -1;

            Person actualMember = mainActivity.membersListMap.get(event.getMember_id());

            String memberName = ((actualMember.getTitle().equals("null") || actualMember.getTitle().equals("")) ? "" : (actualMember.getTitle() + " ")) +
                    actualMember.getFirst_name() + (actualMember.getMiddle_name().equals("null") ? "" : " " + actualMember.getMiddle_name())
                    + " " + actualMember.getLast_name();

            String memberConcerned = "";
            switch (event.getEventType()){
                case 0:
                    memberConcerned = memberName;
                    icon = FontAwesome.Icon.faw_birthday_cake;
                    color = R.color.birthday;
                    break;
                case 1:
                    Person spouseOfMember = mainActivity.membersListMap.get(actualMember.getSpouse_id());
                    String spouseName = ((spouseOfMember.getTitle().equals("null") || spouseOfMember.getTitle().equals("")) ? "" : (spouseOfMember.getTitle() + " ")) +
                            spouseOfMember.getFirst_name() + (spouseOfMember.getMiddle_name().equals("null") ? "" : " " + spouseOfMember.getMiddle_name())
                            + " " + spouseOfMember.getLast_name();
                    memberConcerned = memberName +" & "+spouseName;
                    icon = FontAwesome.Icon.faw_heart;
                    color = R.color.marriage;
                    break;
                case 2:
                    memberConcerned = memberName;
                    icon = FontAwesome.Icon.faw_star;
                    color = R.color.death;
                    break;
            }

            holder.occasionView.setImageDrawable(new IconicsDrawable(mainActivity)
                    .icon(icon)
                    .color(mainActivity.getResources().getColor(color))
                    .sizeDp(15));
            holder.detailsView.setText(memberConcerned);

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

        new MaterialDialog.Builder(getActivity())
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }
}
