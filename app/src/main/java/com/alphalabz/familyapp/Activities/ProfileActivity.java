package com.alphalabz.familyapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.R;

public class ProfileActivity extends AppCompatActivity {

    private Person actualPerson, motherOfPerson, fatherOfPerson, spouseOfPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        actualPerson = (Person) bundle.getSerializable("Actual_Person");
        motherOfPerson = (Person) bundle.getSerializable("Person_Mother");
        fatherOfPerson = (Person) bundle.getSerializable("Person_Father");
        spouseOfPerson = (Person) bundle.getSerializable("Person_Spouse");

        ImageView userIcon = (ImageView) findViewById(R.id.user_icon);
        userIcon.setImageResource(R.drawable.profile);

        TextView nameT = (TextView) findViewById(R.id.profile_name);
        String newName = actualPerson.getGender().equals("M") ? "Mr. " + actualPerson.getFirst_name()+" "+ actualPerson.getLast_name() : "Miss " + actualPerson.getFirst_name()+" "+ actualPerson.getLast_name();
        nameT.setText(newName);

        TextView firstNameT = (TextView) findViewById(R.id.profile_firstname);
        firstNameT.setText(actualPerson.getFirst_name());

        TextView familyNameT = (TextView) findViewById(R.id.profile_familyname);
        familyNameT.setText(actualPerson.getLast_name());

        TextView fatherNameT = (TextView) findViewById(R.id.profile_fathername);
        if(fatherOfPerson!=null)
            fatherNameT.setText(fatherOfPerson.getFirst_name());
        else{
            fatherNameT.setVisibility(View.GONE);
        }

        TextView motherNameT = (TextView) findViewById(R.id.profile_mothername);
        if(motherOfPerson!=null)
            motherNameT.setText(motherOfPerson.getFirst_name());
        else{
            motherNameT.setVisibility(View.GONE);
        }

        TextView spouseNameT = (TextView) findViewById(R.id.profile_spousename);
        if(spouseOfPerson!=null)
            spouseNameT.setText(spouseOfPerson.getFirst_name());
        else{
            spouseNameT.setVisibility(View.GONE);
        }

        TextView birthdateT = (TextView) findViewById(R.id.profile_birthdate);
        birthdateT.setText(actualPerson.getBirth_date());

        TextView emailT = (TextView) findViewById(R.id.profile_email);
        emailT.setText(actualPerson.getEmail1());




    }
}
