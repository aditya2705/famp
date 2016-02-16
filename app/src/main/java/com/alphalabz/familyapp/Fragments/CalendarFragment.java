package com.alphalabz.familyapp.Fragments;


import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private String[] monthStrings={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

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

                        String s1 = date.substring(7, 9);
                        int year = Integer.parseInt(s1);
                        if (year >= 0 && year <= 20)
                            year = Integer.parseInt("20" + s1);
                        else
                            year = Integer.parseInt("19" + s1);

                        String s2 = date.substring(3, 6);
                        String s3 = date.substring(0, 2);

                        Calendar calendarDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                                Arrays.asList(monthStrings).indexOf(s2), Integer.parseInt(s3));
                        CalendarDay calendar = new CalendarDay(calendarDate);
                        calendarDatesList.add(calendar);

                        eventsList.add(event);
                        calendarDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR)-1,
                                Arrays.asList(monthStrings).indexOf(s2), Integer.parseInt(s3));
                        calendar = new CalendarDay(calendarDate);
                        calendarDatesList.add(calendar);

                        eventsList.add(event);
                        calendarDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR)+1,
                                Arrays.asList(monthStrings).indexOf(s2), Integer.parseInt(s3));
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

                    final int position = calendarDays.indexOf(date);

                    String birthday = eventsList.get(position).getBirthday();

                    int event;
                    if(birthday.equals("null")||birthday.equals("")){
                        event = 1;
                    }else{
                        event = 0;
                    }

                    MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
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
