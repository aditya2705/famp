package com.alphalabz.familyapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.activities.MainActivity;
import com.alphalabz.familyapp.custom.EventDecorator;
import com.alphalabz.familyapp.objects.Event;
import com.alphalabz.familyapp.objects.Person;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    
    private MainActivity mainActivity;

    MaterialCalendarView calendarView;

    private ArrayList<Event> calendarEventsList = new ArrayList<>();

    public CalendarFragment() {
        // Required empty public constructor
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

        calendarSetup();

        return v;
    }

    private void calendarSetup() {

        final ArrayList<CalendarDay> calendarDatesList = new ArrayList<>();

        for (int i = 0; i < mainActivity.eventArrayList.size(); i++) {

            Event event = mainActivity.eventArrayList.get(i);
            String date = event.getDate();

            Calendar currentCalendar = Calendar.getInstance();

            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            int day = Integer.parseInt(dayFormat.format(Date.parse(date)));

            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            int month = Integer.parseInt(monthFormat.format(Date.parse(date)));

            calendarEventsList.add(event);
            Calendar calendarDate = new GregorianCalendar(currentCalendar.get(Calendar.YEAR), month - 1, day);
            CalendarDay calendar = new CalendarDay(calendarDate);
            calendarDatesList.add(calendar);

            calendarEventsList.add(event);
            calendarDate = new GregorianCalendar(currentCalendar.get(Calendar.YEAR) - 1, month - 1, day);
            calendar = new CalendarDay(calendarDate);
            calendarDatesList.add(calendar);

            calendarEventsList.add(event);
            calendarDate = new GregorianCalendar(currentCalendar.get(Calendar.YEAR) + 1, month - 1, day);
            calendar = new CalendarDay(calendarDate);
            calendarDatesList.add(calendar);

        }

        calendarView.addDecorator(new EventDecorator(getResources().getColor(R.color.md_green_700), calendarDatesList));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                final ArrayList<Integer> indexList = new ArrayList<Integer>();
                for (int i = 0; i < calendarDatesList.size(); i++)
                    if (date.equals(calendarDatesList.get(i)))
                        indexList.add(i);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_text_view);

                for (int i = 0; i < indexList.size(); i++) {

                    Event event = calendarEventsList.get(indexList.get(i));

                    String eventTitle = "";
                    Person actualMember = mainActivity.membersListMap.get(event.getMember_id());

                    String memberName = ((actualMember.getTitle().equals("null") || actualMember.getTitle().equals("")) ? "" : (actualMember.getTitle() + " ")) +
                            actualMember.getFirst_name() + (actualMember.getMiddle_name().equals("null") ? "" : " " + actualMember.getMiddle_name())
                            + " " + actualMember.getLast_name();

                    switch(event.getEventType()){
                        case 0:
                            eventTitle = "Birthday of "+memberName;
                            break;
                        case 1:
                            Person spouseOfMember = mainActivity.membersListMap.get(actualMember.getSpouse_id());
                            String spouseName = ((spouseOfMember.getTitle().equals("null") || spouseOfMember.getTitle().equals("")) ? "" : (spouseOfMember.getTitle() + " ")) +
                                    spouseOfMember.getFirst_name() + (spouseOfMember.getMiddle_name().equals("null") ? "" : " " + spouseOfMember.getMiddle_name())
                                    + " " + spouseOfMember.getLast_name();
                            eventTitle = "Marriage Anniversary of "+memberName+" & "+spouseName;
                            break;
                        case 2:
                            eventTitle = "Death Anniversary of "+memberName;
                            break;
                    }
                    arrayAdapter.add(eventTitle);

                }

                new MaterialDialog.Builder(getActivity())
                        .theme(Theme.LIGHT)
                        .title("Events")
                        .adapter(arrayAdapter, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {

                                final Event eventObject = calendarEventsList.get(indexList.get(which));

                                mainActivity.showEventDialog(eventObject);
                            }
                        })
                        .show();

            }
        });



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
