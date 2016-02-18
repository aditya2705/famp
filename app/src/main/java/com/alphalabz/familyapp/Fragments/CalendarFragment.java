package com.alphalabz.familyapp.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alphalabz.familyapp.Custom.EventDecorator;
import com.alphalabz.familyapp.Objects.Event;
import com.alphalabz.familyapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

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

    MaterialCalendarView calendarView;
    private String eventsListJsonString;

    private SharedPreferences sharedPreferences;
    private ArrayList<Event> eventsList = new ArrayList<>();

    public CalendarFragment() {
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

        View v = inflater.inflate(R.layout.fragment_calendar_events, container, false);
        calendarView = (MaterialCalendarView) v.findViewById(R.id.calendarView);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        calendarView.setCurrentDate(new Date(System.currentTimeMillis()));
        calendarView.setSelectionColor(getResources().getColor(R.color.md_green_700));


        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());
        return v;
    }


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            ArrayList<CalendarDay> calendarDatesList = new ArrayList<>();

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

                    if(!date.equals("null")&&!date.equals("")) {
                        Event event = new Event(event_id, date, birthday, anniversary, remarks, years, city, contact, email);
                        eventsList.add(event);

                        Calendar currentCalendar = Calendar.getInstance();

                        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                        int day = Integer.parseInt(dayFormat.format(Date.parse(date)));

                        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                        int month = Integer.parseInt(monthFormat.format(Date.parse(date)));


                        Calendar calendarDate = new GregorianCalendar(currentCalendar.get(Calendar.YEAR),month-1,day);
                        CalendarDay calendar = new CalendarDay(calendarDate);
                        calendarDatesList.add(calendar);

                        eventsList.add(event);
                        calendarDate = new GregorianCalendar(currentCalendar.get(Calendar.YEAR)-1,month-1,day);
                        calendar = new CalendarDay(calendarDate);
                        calendarDatesList.add(calendar);

                        eventsList.add(event);
                        calendarDate = new GregorianCalendar(currentCalendar.get(Calendar.YEAR)+1,month-1,day);
                        calendar = new CalendarDay(calendarDate);
                        calendarDatesList.add(calendar);
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return calendarDatesList;
        }

        @Override
        protected void onPostExecute(@NonNull final List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            calendarView.addDecorator(new EventDecorator(getResources().getColor(R.color.md_green_700), calendarDays));
            calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                    final ArrayList<Integer> indexList = new ArrayList<Integer>();
                    for (int i = 0; i < calendarDays.size(); i++)
                        if(date.equals(calendarDays.get(i)))
                            indexList.add(i);

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.simple_text_view);

                    for(int i=0;i<indexList.size();i++) {

                        Event curEvent = eventsList.get(indexList.get(i));
                        String birthday = curEvent.getBirthday();
                        int eventInteger;
                        if(birthday.equals("null")||birthday.equals("")){
                            eventInteger = 1;
                        }else{
                            eventInteger = 0;
                        }
                        arrayAdapter.add(eventInteger==1?"Anniversary of "+curEvent.getAnniversary():"Birthday of "+curEvent.getBirthday());

                    }

                    new MaterialDialog.Builder(getActivity())
                            .theme(Theme.LIGHT)
                            .title("Events")
                            .adapter(arrayAdapter, new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                                    final Event curEvent = eventsList.get(indexList.get(which));

                                    String birthday = curEvent.getBirthday();
                                    String marriage = curEvent.getAnniversary();

                                    int event, titleColor;
                                    if(!birthday.equals("null")&&!birthday.equals("")){
                                        event = 0;
                                        titleColor = R.color.birthday;
                                    }else{
                                        if(!marriage.equals("null")&&!marriage.equals("")) {
                                            event = 1;
                                            titleColor = R.color.marriage;
                                        }else{
                                            event = 2;
                                            titleColor = R.color.death;
                                        }
                                    }

                                    final MaterialDialog eventDialog = new MaterialDialog.Builder(getActivity())
                                            .theme(Theme.LIGHT)
                                            .title("Event")
                                            .icon(getResources().getDrawable(event==1?R.drawable.ic_love:R.drawable.ic_cake))
                                            .titleColor(getResources().getColor(titleColor))
                                            .customView(R.layout.dialog_event_details, true)
                                            .positiveText("OK")
                                            .positiveColor(getResources().getColor(titleColor))
                                            .build();


                                    ((TextView)eventDialog.getCustomView().findViewById(R.id.event_type)).setText(event==1?"Anniversary":"Birthday");
                                    ((TextView)eventDialog.getCustomView().findViewById(R.id.members_concerned)).setText(event==1?curEvent.getAnniversary():curEvent.getBirthday());

                                    String dateString = curEvent.getDate();

                                    ((TextView)eventDialog.getCustomView().findViewById(R.id.date)).setText(dateString.substring(0,9));

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

                                    ((TextView)eventDialog.getCustomView().findViewById(R.id.years)).setText("YEARS: "+diff);

                                    String city = curEvent.getCity();
                                    ((TextView)eventDialog.getCustomView().findViewById(R.id.city)).setText(city.equals("")||city.equals("null")?"":"CITY: "+city);

                                    (eventDialog.getCustomView().findViewById(R.id.contact_click_layout)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            phoneIntent(curEvent.getContact());
                                        }
                                    });

                                    String temp = curEvent.getContact();
                                    if(!temp.equals("")&&!temp.equals("null"))
                                        ((TextView)eventDialog.getCustomView().findViewById(R.id.contact)).setText("Contact: "+temp);
                                    else
                                        (eventDialog.getCustomView().findViewById(R.id.contact_click_layout)).setVisibility(View.GONE);

                                    (eventDialog.getCustomView().findViewById(R.id.email_click_layout)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            emailIntent(curEvent.getEmail());
                                        }
                                    });

                                    temp = curEvent.getEmail();
                                    if(!temp.equals("")&&!temp.equals("null"))
                                        ((TextView)eventDialog.getCustomView().findViewById(R.id.email)).setText("Email: "+temp);
                                    else
                                        (eventDialog.getCustomView().findViewById(R.id.email_click_layout)).setVisibility(View.GONE);

                                    eventDialog.show();


                                }
                            })
                            .show();


                }
            });
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
