package com.alphalabz.familyapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "unique_id";
    private static final String TAG_GEN = "gen";
    private static final String TAG_TITLE = "title";
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_MIDDLE_NAME = "middle_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_NICK_NAME = "nick_name";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_IN_LAW = "in_law";
    private static final String TAG_MOTHER_ID = "mother_id";
    private static final String TAG_MOTHER_NAME = "mother_name";
    private static final String TAG_FATHER_ID = "father_id";
    private static final String TAG_FATHER_NAME = "father_name";
    private static final String TAG_SPOUSE_ID = "spouse_id";
    private static final String TAG_SPOUSE_NAME = "spouse_name";
    private static final String TAG_BIRTH_DATE = "birth_date";
    private static final String TAG_MARRIAGE_DATE = "marriage_date";
    private static final String TAG_DEATH_DATE = "death_date";
    private static final String TAG_MOBILE_NUMBER = "mobile_number";
    private static final String TAG_ALTERNATE_NUMBER = "alternate_number";
    private static final String TAG_RESIDENCE_NUMBER = "residence_number";
    private static final String TAG_EMAIL1 = "email1";
    private static final String TAG_EMAIL2 = "email2";
    private static final String TAG_ADDRESS_1 = "address_1";
    private static final String TAG_ADDRESS_2 = "address_2";
    private static final String TAG_CITY = "city";
    private static final String TAG_STATE_COUNTRY = "state_country";
    private static final String TAG_PINCODE = "pincode";
    private static final String TAG_DESIGNATION = "designation";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_INDUSTRY_SPECIAL = "industry_special";
    private static final String TAG_IMAGE_URL = "image_url";

    private Person actualPerson, motherOfPerson, fatherOfPerson, spouseOfPerson;
    private String temp;
    private boolean isInLaw = false;

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

        isInLaw = actualPerson.getIn_law().equals("Y") ? true : false;

        if (isInLaw) {
            ((TextView) findViewById(R.id.father_text)).setText("Father In Law's Name");
            ((TextView) findViewById(R.id.mother_text)).setText("Mother In Law's Name");
        }

        int k = 0;
        ImageView userIcon = (ImageView) findViewById(R.id.user_icon);
        userIcon.setImageResource(R.drawable.profile);

        TextView nameT = (TextView) findViewById(R.id.profile_name);
        String newName = ((actualPerson.getTitle().equals("null") || actualPerson.getTitle().equals("")) ? "" : (actualPerson.getTitle() + " ")) +
                actualPerson.getFirst_name() + (actualPerson.getMiddle_name().equals("null") ? "" : " " + actualPerson.getMiddle_name())
                + " " + actualPerson.getLast_name() + (actualPerson.getNick_name().equals("null") ? "" : " ( " + actualPerson.getNick_name() + " )");
        nameT.setText(newName);

        TextView firstNameT = (TextView) findViewById(R.id.profile_firstname);
        firstNameT.setText(actualPerson.getFirst_name());

        TextView familyNameT = (TextView) findViewById(R.id.profile_familyname);
        familyNameT.setText(actualPerson.getLast_name());

        TextView fatherNameT = (TextView) findViewById(R.id.profile_fathername);
        if (fatherOfPerson != null) {
            temp = (fatherOfPerson.getTitle().equals("null") ? "" : " " + fatherOfPerson.getTitle() + " ") + fatherOfPerson.getFirst_name() + (fatherOfPerson.getMiddle_name().equals("null") ? "" : " " + fatherOfPerson.getMiddle_name());
            if (!temp.equals(""))
                fatherNameT.setText(temp);
            else {
                if (!actualPerson.getFather_name().equals("null"))
                    fatherNameT.setText(actualPerson.getFather_name());
                else {
                    findViewById(R.id.father_text).setVisibility(View.GONE);
                    fatherNameT.setVisibility(View.GONE);
                }
            }
        } else {
            if (!actualPerson.getFather_name().equals("null"))
                fatherNameT.setText(actualPerson.getFather_name());
            else {
                findViewById(R.id.father_text).setVisibility(View.GONE);
                fatherNameT.setVisibility(View.GONE);
            }
        }

        TextView motherNameT = (TextView) findViewById(R.id.profile_mothername);
        if (motherOfPerson != null) {
            temp = (motherOfPerson.getTitle().equals("null") ? "" : " " + motherOfPerson.getTitle() + " ") + motherOfPerson.getFirst_name() + (motherOfPerson.getMiddle_name().equals("null") ? "" : " " + motherOfPerson.getMiddle_name());
            if (!temp.equals(""))
                motherNameT.setText(temp);
            else {
                if (!actualPerson.getMother_name().equals("null"))
                    motherNameT.setText(actualPerson.getMother_name());
                else {
                    findViewById(R.id.mother_text).setVisibility(View.GONE);
                    motherNameT.setVisibility(View.GONE);
                }
            }
        } else {
            if (!actualPerson.getMother_name().equals("null"))
                motherNameT.setText(actualPerson.getMother_name());
            else {
                findViewById(R.id.mother_text).setVisibility(View.GONE);
                motherNameT.setVisibility(View.GONE);
            }
        }

        TextView spouseNameT = (TextView) findViewById(R.id.profile_spousename);
        if (spouseOfPerson != null) {
            temp = (spouseOfPerson.getTitle().equals("null") ? "" : " " + spouseOfPerson.getTitle() + " ") + spouseOfPerson.getFirst_name() + (spouseOfPerson.getMiddle_name().equals("null") ? "" : " " + spouseOfPerson.getMiddle_name());
            ;
            if (!temp.equals(""))
                spouseNameT.setText(temp);
            else {
                if (!actualPerson.getSpouse_name().equals("null"))
                    spouseNameT.setText(actualPerson.getSpouse_name());
                else {
                    findViewById(R.id.spouse_text).setVisibility(View.GONE);
                    spouseNameT.setVisibility(View.GONE);
                }
            }
        } else {
            if (!actualPerson.getSpouse_name().equals("null"))
                spouseNameT.setText(actualPerson.getSpouse_name());
            else {
                findViewById(R.id.spouse_text).setVisibility(View.GONE);
                spouseNameT.setVisibility(View.GONE);
            }
        }

        TextView genderT = (TextView) findViewById(R.id.profile_gender);
        temp = actualPerson.getGender();

        if (temp.equals("M"))
            genderT.setText("Gender: Male");
        else
            genderT.setText("Gender: Female");

        TextView birthdateT = (TextView) findViewById(R.id.profile_birthdate);
        temp = actualPerson.getBirth_date();

        if (temp.length() >= 10)
            birthdateT.setText("Birth Date: " + temp.substring(0, 10));
        else
            birthdateT.setVisibility(View.GONE);

        TextView marriagedateT = (TextView) findViewById(R.id.profile_marriagedate);
        temp = actualPerson.getMarriage_date();

        if (temp.length() >= 10)
            marriagedateT.setText("Marriage Date: " + temp.substring(0, 10));
        else
            marriagedateT.setVisibility(View.GONE);

        TextView deathdateT = (TextView) findViewById(R.id.profile_deathdate);
        temp = actualPerson.getDeath_date();
        if (temp.length() >= 10) {

            String t1 = actualPerson.getBirth_date();
            if (t1.length() >= 10)
                deathdateT.setText(t1.substring(0, 9) + " - " + temp.substring(0, 9));
            else
                deathdateT.setText("Demise: " + temp.substring(0, 9));
            birthdateT.setVisibility(View.GONE);
            marriagedateT.setVisibility(View.GONE);
        } else
            deathdateT.setVisibility(View.GONE);

        k = 0;


        TextView email1T = (TextView) findViewById(R.id.profile_email1);
        temp = !actualPerson.getEmail1().equals("null") ? actualPerson.getEmail1() : "-";
        if (!temp.equals("-")) {
            email1T.setText(temp);
            findViewById(R.id.email1_click_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailIntent(actualPerson.getEmail1());
                }
            });
        } else {
            findViewById(R.id.email_layout1).setVisibility(View.GONE);
            ++k;
        }

        TextView email2T = (TextView) findViewById(R.id.profile_email2);
        temp = !actualPerson.getEmail2().equals("null") ? actualPerson.getEmail2() : "-";
        if (!temp.equals("-")) {
            email2T.setText(temp);
            findViewById(R.id.email2_click_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailIntent(actualPerson.getEmail2());
                }
            });
        } else {
            findViewById(R.id.email_layout2).setVisibility(View.GONE);
            ++k;
        }

        TextView mobile1T = (TextView) findViewById(R.id.profile_mobile1);
        temp = !actualPerson.getMobile_number().equals("null") ? actualPerson.getMobile_number() : "-";
        if (!temp.equals("-")) {
            mobile1T.setText(temp);
            findViewById(R.id.mobile1_click_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneIntent(actualPerson.getMobile_number());
                }
            });
        } else {
            findViewById(R.id.mobile1_layout).setVisibility(View.GONE);
            ++k;
        }

        TextView mobile2T = (TextView) findViewById(R.id.profile_mobile2);
        temp = !actualPerson.getAlternate_number().equals("null") ? actualPerson.getAlternate_number() : "-";
        if (!temp.equals("-")) {
            mobile2T.setText(temp);
            findViewById(R.id.mobile2_click_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneIntent(actualPerson.getAlternate_number());
                }
            });
        } else {
            findViewById(R.id.mobile2_layout).setVisibility(View.GONE);
            ++k;
        }

        TextView residenceT = (TextView) findViewById(R.id.profile_residence_number);
        temp = !actualPerson.getResidence_number().equals("null") ? actualPerson.getResidence_number() : "-";
        if (!temp.equals("-")) {
            residenceT.setText(temp);
            findViewById(R.id.residence_number_click_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneIntent(actualPerson.getResidence_number());
                }
            });
        } else {
            findViewById(R.id.residence_layout).setVisibility(View.GONE);
            ++k;
        }

        TextView addressT = (TextView) findViewById(R.id.profile_address);
        temp = !actualPerson.getAddress_1().equals("null") ? actualPerson.getAddress_1() : "";
        temp += !actualPerson.getAddress_2().equals("null") ? " " + actualPerson.getAddress_2() : "";
        temp += !actualPerson.getCity().equals("null") ? "\n " + actualPerson.getCity() : "";
        temp += !actualPerson.getState_country().equals("null") ? "\n " + actualPerson.getState_country() : "";
        if (!temp.equals(""))
            addressT.setText(temp);
        else {
            findViewById(R.id.address_layout).setVisibility(View.GONE);
            ++k;
        }


        if (k == 6)
            findViewById(R.id.contact_details_card).setVisibility(View.GONE);

        k = 0;

        TextView designationT = (TextView) findViewById(R.id.profile_designation);
        temp = !actualPerson.getDesignation().equals("null") ? actualPerson.getDesignation() : "-";
        if (!temp.equals("-"))
            designationT.setText(temp);
        else {
            findViewById(R.id.designation_layout).setVisibility(View.GONE);
            ++k;
        }

        TextView companyT = (TextView) findViewById(R.id.profile_company);
        temp = !actualPerson.getCompany().equals("null") ? actualPerson.getCompany() : "-";
        if (!temp.equals("-"))
            companyT.setText(temp);
        else {
            findViewById(R.id.company_layout).setVisibility(View.GONE);
            ++k;
        }


        TextView ind_specialT = (TextView) findViewById(R.id.profile_ind_special);
        temp = !actualPerson.getIndustry_special().equals("null") ? actualPerson.getIndustry_special() : "-";
        if (!temp.equals("-"))
            ind_specialT.setText(temp);
        else {
            findViewById(R.id.ind_special_layout).setVisibility(View.GONE);
            ++k;
        }

        if (k == 3)
            findViewById(R.id.professional_card_layout).setVisibility(View.GONE);


        checkAndAddChildren();


    }

    private void checkAndAddChildren() {

        ArrayList<Person> childrenList = new ArrayList<>();
        JSONArray membersJsonArray = null;

        String membersString = getSharedPreferences("FAMP", 0).getString("MEMBERS_STRING", "");
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(membersString);

            membersJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < membersJsonArray.length(); i++) {
                JSONObject c = membersJsonArray.getJSONObject(i);

                String unique_id, generation, title, first_name, middle_name, last_name, nick_name, gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id, spouse_name, birth_date, marriage_date, death_date,
                        mobile_number, alternate_number, residence_number, email1, email2, address_1, address_2, city, state_country, pincode, designation, company, industry_special, image_url;


                unique_id = c.getString(TAG_ID);
                generation = c.getString(TAG_GEN);
                title = c.getString(TAG_TITLE);
                first_name = c.getString(TAG_FIRST_NAME);
                middle_name = c.getString(TAG_MIDDLE_NAME);
                last_name = c.getString(TAG_LAST_NAME);
                nick_name = c.getString(TAG_NICK_NAME);
                gender = c.getString(TAG_GENDER);
                in_law = c.getString(TAG_IN_LAW);
                mother_id = c.getString(TAG_MOTHER_ID);
                mother_name = c.getString(TAG_MOTHER_NAME);
                father_id = c.getString(TAG_FATHER_ID);
                father_name = c.getString(TAG_FATHER_NAME);
                spouse_id = c.getString(TAG_SPOUSE_ID);
                spouse_name = c.getString(TAG_SPOUSE_NAME);
                birth_date = c.getString(TAG_BIRTH_DATE);
                marriage_date = c.getString(TAG_MARRIAGE_DATE);
                death_date = c.getString(TAG_DEATH_DATE);
                mobile_number = c.getString(TAG_MOBILE_NUMBER);
                alternate_number = c.getString(TAG_ALTERNATE_NUMBER);
                residence_number = c.getString(TAG_RESIDENCE_NUMBER);
                email1 = c.getString(TAG_EMAIL1);
                email2 = c.getString(TAG_EMAIL2);
                address_1 = c.getString(TAG_ADDRESS_1);
                address_2 = c.getString(TAG_ADDRESS_2);
                city = c.getString(TAG_CITY);
                state_country = c.getString(TAG_STATE_COUNTRY);
                pincode = c.getString(TAG_PINCODE);
                designation = c.getString(TAG_DESIGNATION);
                company = c.getString(TAG_COMPANY);
                industry_special = c.getString(TAG_INDUSTRY_SPECIAL);
                image_url = c.getString(TAG_IMAGE_URL);

                if (actualPerson.getGender().equals("M")) {

                    if (actualPerson.getUnique_id().equals(father_id) && !in_law.equals("Y")) {
                        Person person = new Person(unique_id, generation, title, first_name, middle_name, last_name, nick_name,
                                gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id,
                                spouse_name, birth_date, marriage_date, death_date, mobile_number, alternate_number,
                                residence_number, email1, email2, address_1, address_2, city, state_country, pincode,
                                designation, company, industry_special, image_url, -1);

                        childrenList.add(person);
                    }

                } else {

                    if (actualPerson.getUnique_id().equals(mother_id) && !in_law.equals("Y")) {
                        Person person = new Person(unique_id, generation, title, first_name, middle_name, last_name, nick_name,
                                gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id,
                                spouse_name, birth_date, marriage_date, death_date, mobile_number, alternate_number,
                                residence_number, email1, email2, address_1, address_2, city, state_country, pincode,
                                designation, company, industry_special, image_url, -1);

                        childrenList.add(person);
                    }

                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < childrenList.size(); i++) {
            View childView = View.inflate(this, R.layout.profile_child_layout, null);
            ((TextView) childView.findViewById(R.id.child_name)).setText(childrenList.get(i).getFirst_name() + (childrenList.get(i).getMiddle_name().equals("null") ? "" : " " + childrenList.get(i).getMiddle_name()) + " " + childrenList.get(i).getLast_name());
            if (i % 2 == 0) {
                ((LinearLayout) findViewById(R.id.vertical_layout_even)).addView(childView);
            } else {
                ((LinearLayout) findViewById(R.id.vertical_layout_odd)).addView(childView);
            }
        }

        if (childrenList.size() <= 1)
            findViewById(R.id.vertical_layout_odd).setVisibility(View.GONE);

        if (childrenList.size() == 0) {
            findViewById(R.id.children_details_card).setVisibility(View.GONE);
        }


    }

    private void emailIntent(final String emailString) {

        new MaterialDialog.Builder(this)
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

        new MaterialDialog.Builder(this)
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
                        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
