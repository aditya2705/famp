package com.alphalabz.familyapp.Fragments;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

                    int position = calendarDays.indexOf(date);

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
            });
        }
    }
}
