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
    private String temp;

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
        if(fatherOfPerson!=null) {
            temp = fatherOfPerson.getFirst_name();
            if(!temp.equals(""))
                fatherNameT.setText(temp);
            else{
                if(!actualPerson.getFather_name().equals("null"))
                    fatherNameT.setText(actualPerson.getFather_name());
                else
                    fatherNameT.setText("-");
            }
        }
        else{
            if(!actualPerson.getFather_name().equals("null"))
               fatherNameT.setText(actualPerson.getFather_name());
            else
               fatherNameT.setText("-");
        }

        TextView motherNameT = (TextView) findViewById(R.id.profile_mothername);
        if(motherOfPerson!=null) {
            temp = motherOfPerson.getFirst_name();
            if(!temp.equals(""))
                motherNameT.setText(temp);
            else{
                if(!actualPerson.getMother_name().equals("null"))
                    motherNameT.setText(actualPerson.getMother_name());
                else
                    motherNameT.setText("-");
            }
        }
        else{
            if(!actualPerson.getMother_name().equals("null"))
                motherNameT.setText(actualPerson.getMother_name());
            else
                motherNameT.setText("-");
        }

        TextView spouseNameT = (TextView) findViewById(R.id.profile_spousename);
        if(spouseOfPerson!=null) {
            temp = spouseOfPerson.getFirst_name();
            if(!temp.equals(""))
                spouseNameT.setText(temp);
            else{
                if(!actualPerson.getSpouse_name().equals("null"))
                    spouseNameT.setText(actualPerson.getSpouse_name());
                else
                    spouseNameT.setText("-");
            }
        }
        else{
            if(!actualPerson.getSpouse_name().equals("null"))
                spouseNameT.setText(actualPerson.getSpouse_name());
            else
                spouseNameT.setText("-");
        }

        TextView birthdateT = (TextView) findViewById(R.id.profile_birthdate);
        temp = actualPerson.getBirth_date();
        
        if(temp.length()>=10)
            birthdateT.setText("Birth Date: "+temp.substring(0,10));
        else
            birthdateT.setVisibility(View.GONE);

        TextView marriagedateT = (TextView) findViewById(R.id.profile_marriagedate);
        temp = actualPerson.getMarriage_date();

        if(temp.length()>=10)
            marriagedateT.setText("Marriage Date: "+temp.substring(0,10));
        else
            marriagedateT.setVisibility(View.GONE);

        TextView deathdateT = (TextView) findViewById(R.id.profile_deathdate);
        temp = actualPerson.getDeath_date();

        if(temp.length()>=10)
            deathdateT.setText("Death Date: "+temp.substring(0,10));
        else
            deathdateT.setVisibility(View.GONE);
        
        TextView emailT = (TextView) findViewById(R.id.profile_email);
        temp = !actualPerson.getEmail1().equals("null")?actualPerson.getEmail1():"-";
        emailT.setText("Email: "+temp);

        TextView mobileT = (TextView) findViewById(R.id.profile_mobile);
        temp = !actualPerson.getMobile_number().equals("null")?actualPerson.getMobile_number():"-";
        temp += !actualPerson.getAlternate_number().equals("null")?" | "+actualPerson.getMobile_number():"";
        mobileT.setText(temp);

        TextView residenceT = (TextView) findViewById(R.id.profile_residence_number);
        temp = !actualPerson.getResidence_number().equals("null")?actualPerson.getResidence_number():"-";
        residenceT.setText(temp);

        TextView addressT = (TextView) findViewById(R.id.profile_address);
        temp = !actualPerson.getAddress_1().equals("null")?actualPerson.getAddress_1():"";
        temp += !actualPerson.getAddress_2().equals("null")?" "+actualPerson.getAddress_2():"";
        temp += !actualPerson.getCity().equals("null")?"\n "+actualPerson.getCity():"";
        temp += !actualPerson.getState_country().equals("null")?"\n "+actualPerson.getState_country():"";
        addressT.setText(temp);

        TextView designationT = (TextView) findViewById(R.id.profile_designation);
        temp = !actualPerson.getDesignation().equals("null")?actualPerson.getDesignation():"-";
        designationT.setText(temp);

        TextView companyT = (TextView) findViewById(R.id.profile_company);
        temp = !actualPerson.getCompany().equals("null")?actualPerson.getCompany():"-";
        companyT.setText(temp);


        TextView ind_specialT = (TextView) findViewById(R.id.profile_ind_special);
        temp = !actualPerson.getIndustry_special().equals("null")?actualPerson.getIndustry_special():"-";
        ind_specialT.setText(temp);





    }
}
