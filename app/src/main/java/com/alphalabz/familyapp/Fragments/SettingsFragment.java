package com.alphalabz.familyapp.fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.activities.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    @Bind(R.id.switch_notifications)
    SwitchCompat notificationSwitch;

    private MainActivity mainActivity;

    private View rootView;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, rootView);

        if (mainActivity.sharedPreferences.getBoolean("notif_on", true))
            notificationSwitch.setChecked(true);
        else
            notificationSwitch.setChecked(false);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences.Editor editor = mainActivity.sharedPreferences.edit();
                if (!isChecked) {
                    //disableNotifications();
                    editor.putBoolean("notif_on", false);
                } else {
                    //allowNotifications();
                    editor.putBoolean("notif_on", true);
                }
                editor.apply();
            }
        });


        return rootView;
    }
/*
    private void allowNotifications() {

        ArrayList<Event> eventArrayList = mainActivity.eventArrayList;

        for (int i = 0; i < eventArrayList.size(); i++) {

            Event currentEvent = eventArrayList.get(i);

            String birthday = currentEvent.getBirthday();

            int event;
            if (birthday.equals("null") || birthday.equals("")) {
                event = 1;
            } else {
                event = 0;
            }

            String dateString = currentEvent.getDate();
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


            Intent myIntent = new Intent(mainActivity, MainActivity.class);
            myIntent.putExtra("Event", currentEvent);
            myIntent.putExtra("Notification", true);
            PendingIntent goToDifferentPendingIntent = PendingIntent.getActivity(
                    mainActivity,
                    i,
                    myIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mainActivity)
                    .setContentTitle(event == 1 ? "Anniversary of " + currentEvent.getAnniversary() : "Birthday of " + currentEvent.getBirthday())
                    .setContentText("Date: " + dateString.substring(0, 9) + " Years: " + diff)
                    .setContentIntent(goToDifferentPendingIntent)
                    .setSmallIcon(event == 1 ? R.drawable.ic_love : R.drawable.ic_cake)
                    .setSound(alarmSound);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setPriority(Notification.PRIORITY_HIGH);
            }

            if (Build.VERSION.SDK_INT >= 21)
                builder.setVibrate(new long[0]);

            Notification notification = builder.build();
            notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
            Intent notificationIntent = new Intent(mainActivity, NotificationPublisher.class);
            notificationIntent.setAction("com.alphalabz.familyapp" + i);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, i + 1);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
            PendingIntent broadcastIntent = PendingIntent.getBroadcast(mainActivity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (a.get(Calendar.MONTH) >= b.get(Calendar.MONTH) && a.get(Calendar.DAY_OF_MONTH) >= b.get(Calendar.DAY_OF_MONTH)) {

                Calendar eventDataCalendar = new GregorianCalendar(b.get(Calendar.YEAR), a.get(Calendar.MONTH), a.get(Calendar.DAY_OF_MONTH));

                long futureInMillis = eventDataCalendar.getTimeInMillis();
                AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, broadcastIntent);

            }
        }

    }

    private void disableNotifications() {

        AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);

        Intent updateServiceIntent = new Intent(mainActivity, NotificationPublisher.class);
        PendingIntent pendingUpdateIntent = PendingIntent.getService(mainActivity, 0, updateServiceIntent, 0);

        // Cancel alarms
        try {
            alarmManager.cancel(pendingUpdateIntent);
        } catch (Exception e) {
        }

    }
*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }
}
