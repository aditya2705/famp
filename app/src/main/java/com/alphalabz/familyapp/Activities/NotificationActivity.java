package com.alphalabz.familyapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alphalabz.familyapp.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        int i = getIntent().getIntExtra("Number",0);


        ((TextView)findViewById(R.id.notification_text)).setText("Notification "+i);

    }
}
