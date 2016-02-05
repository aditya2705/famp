package com.alphalabz.familyapp.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alphalabz.familyapp.Activities.MainActivity;
import com.alphalabz.familyapp.Activities.ProfileActivity;
import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.Objects.PersonLayout;
import com.alphalabz.familyapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreeViewFragment extends Fragment {

    private MainActivity mainActivity;

    private View rootView;
    private ArrayList<PersonLayout> personList;// = new ArrayList<>();
    private LinearLayout parentLayout;
    private PersonLayout rootPerson;
    private int marginForChildLayout;
    private HashMap<String, Integer> membersListMap;

    private String membersListJsonString;

    private static final String RESULTS_FETCH_URL = "http://alpha95.net63.net/get_members_2.php";

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

    JSONArray membersJsonArray = null;

    private SharedPreferences sharedPreferences;

    private ProgressDialog progressDialog;

    public TreeViewFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = getActivity().getSharedPreferences("FAMP", 0);
        membersListJsonString = sharedPreferences.getString("MEMBERS_STRING", "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tree_view, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);

        parentLayout = (LinearLayout) rootView.findViewById(R.id.parent_layout);

        marginForChildLayout = (int) (getResources().getDimension(R.dimen._minus15sdp));

        personList = new ArrayList<>();

        if (membersListJsonString.equals("") || membersListJsonString == null)
            getData();
        else
            showList();

        return rootView;
    }

    public void buildPersonTree() {
        parentLayout.removeAllViews();

        membersListMap = new HashMap<>();
        for (int i = 0; i < personList.size(); i++) {
            PersonLayout p = personList.get(i);
            membersListMap.put(p.getUnique_id(), i);
        }
        for (int i = 0; i < personList.size(); i++) {
            PersonLayout p = personList.get(i);
            String fatherID = p.getFather_id();
            if (membersListMap.containsKey(fatherID)) {
                int pos = membersListMap.get(fatherID);
                personList.get(pos).addChild(p);
            } else {
                if (!p.getIn_law().equals("Y")&&i<=2)
                    rootPerson = p;
            }
        }

    }

    private RelativeLayout getNodeLayout() {
        return (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.new_node_layout, null, false);
    }

    public void bfs() {
        ArrayList<PersonLayout> Q = new ArrayList<>();
        Q.add(rootPerson);
        rootPerson.setTreeLevel(0);
        RelativeLayout rootLayout = getNodeLayout();
        TextView personNameView = (TextView) rootLayout.findViewById(R.id.person_name);
        ImageView personImageView = (ImageView) rootLayout.findViewById(R.id.person_image_view);
        personNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileFragment(rootPerson);
            }
        });
        personImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileFragment(rootPerson);
            }
        });
        TextView spouseNameView = (TextView) rootLayout.findViewById(R.id.spouse_name);
        ImageView spouseImageView = (ImageView) rootLayout.findViewById(R.id.spouse_image_view);
        spouseNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileFragment(personList.get(membersListMap.get(rootPerson.getSpouse_id())));
            }
        });
        spouseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileFragment(personList.get(membersListMap.get(rootPerson.getSpouse_id())));
            }
        });
        personNameView.setText(rootPerson.getFirst_name());
        //spouseNameView.setText(personList.get(membersListMap.get(rootPerson.getSpouse_id())).getFirst_name());

        parentLayout.addView(rootLayout);
        rootPerson.setPersonLayout(rootLayout);

        while (!Q.isEmpty()) {
            PersonLayout p = Q.get(0);
            Q.remove(0);
            LinearLayout pChildLayout = (LinearLayout) p.getPersonLayout().findViewById(R.id.childLinearLayout);
            for (int i = 0; i < p.getChildCount(); i++) {
                final PersonLayout c = p.getChildAt(i);
                p.getPersonLayout().findViewById(R.id.child_branch).setVisibility(View.VISIBLE);
                RelativeLayout newNodeLayout = getNodeLayout();
                personNameView = (TextView) newNodeLayout.findViewById(R.id.person_name);
                personImageView = (ImageView) newNodeLayout.findViewById(R.id.person_image_view);
                personNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openProfileFragment(c);
                    }
                });
                personImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openProfileFragment(c);
                    }
                });
                personNameView.setText(c.getFirst_name());
                spouseNameView = (TextView) newNodeLayout.findViewById(R.id.spouse_name);
                spouseImageView = (ImageView) newNodeLayout.findViewById(R.id.spouse_image_view);


                String s = null;
                if (membersListMap.get(c.getSpouse_id()) != null)
                    s = personList.get(membersListMap.get(c.getSpouse_id())).getFirst_name();

                if (s != null && !s.equals("")) {
                    spouseNameView.setText(s);
                    spouseNameView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openProfileFragment(personList.get(membersListMap.get(c.getSpouse_id())));
                        }
                    });
                    spouseImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openProfileFragment(personList.get(membersListMap.get(c.getSpouse_id())));
                        }
                    });
                    personNameView.setText(c.getFirst_name());
                } else {
                    newNodeLayout.findViewById(R.id.spouse_layout).setVisibility(View.INVISIBLE);
                    newNodeLayout.findViewById(R.id.spouse_branch).setVisibility(View.INVISIBLE);
                }

                newNodeLayout.findViewById(R.id.child_branch).setVisibility(View.INVISIBLE);

                c.setPersonLayout(newNodeLayout);
                pChildLayout.addView(c.getPersonLayout());

                c.setTreeLevel(p.getTreeLevel() + 1);
                Q.add(c);
                Log.d("Child", c.getFirst_name());
                Log.d("Child", "" + pChildLayout.getChildCount());


            }

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bfsBranchFix();
            }
        }, 100);
        //bfsBranchFix();
    }

    public void bfsBranchFix() {
        ArrayList<PersonLayout> Q = new ArrayList<>();
        Q.add(rootPerson);
        rootPerson.setTreeLevel(0);


        while (!Q.isEmpty()) {
            PersonLayout p = Q.get(0);
            Q.remove(0);

            RelativeLayout layout = p.getPersonLayout();
            View branch = layout.findViewById(R.id.branch_image);
            // R pChildLayout = (LinearLayout)p.getPersonLayout().findViewById(R.id.childLinearLayout);
            int first = 0, last = p.getChildCount() - 1;
            if (first <= last) {

                int marginLeft = p.getChildAt(first).getPersonLayout().getWidth() / 2;
                int marginRight = p.getChildAt(last).getPersonLayout().getWidth() / 2;

                Log.d("Margins", +marginLeft + " " + marginRight);


                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) branch.getLayoutParams();
                // params.setMargins(0,marginForChildLayout,marginRight,0);
                params.leftMargin = marginLeft;
                params.rightMargin = marginRight;
                branch.setLayoutParams(params);

            }

            for (int i = 0; i < p.getChildCount(); i++) {
                PersonLayout c = p.getChildAt(i);
                Q.add(c);
                // Log.d("Child",c.getFirstName());
                // Log.d("Child","" + pChildLayout.getChildCount());

            }

        }
    }

    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                URL obj = null;
                String result = null;
                InputStream inputStream = null;
                try {
                    obj = new URL(RESULTS_FETCH_URL);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    //add request header
                    con.setRequestProperty("Content-Type", "application/json");
                    inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    Log.d("RESULT", result);

                } catch (Exception e) {
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                membersListJsonString = result;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MEMBERS_STRING", membersListJsonString);
                editor.apply();
                showList();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    protected void showList() {

        try {
            JSONObject jsonObj = new JSONObject(membersListJsonString);
            membersJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < membersJsonArray.length(); i++) {
                JSONObject c = membersJsonArray.getJSONObject(i);

                String unique_id,generation,title,first_name,middle_name,last_name,nick_name
                        ,gender,in_law,mother_id,mother_name,father_id,father_name,spouse_id,spouse_name,birth_date,marriage_date,death_date,
                        mobile_number,alternate_number,residence_number,email1,email2,address_1,address_2,city,state_country,pincode
                        ,designation,company,industry_special,image_url;


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


                PersonLayout person = new PersonLayout(unique_id, generation, title, first_name, middle_name, last_name, nick_name,
                        gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id,
                        spouse_name, birth_date, marriage_date, death_date, mobile_number, alternate_number,
                        residence_number, email1, email2, address_1, address_2, city, state_country, pincode,
                        designation, company, industry_special, image_url, -1);

                personList.add(person);

            }


            buildPersonTree();
            bfs();

            progressDialog.dismiss();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void openProfileFragment(PersonLayout person) {

        Intent intent = new Intent(mainActivity, ProfileActivity.class);
        Bundle bundle = new Bundle();

        Person actualPerson = new PersonLayout(person);
        bundle.putSerializable("Actual_Person", actualPerson);

        Person motherOfPerson = null, fatherOfPerson = null, spouseOfPerson = null;

        if (membersListMap.get(person.getMother_id()) != null)
            motherOfPerson = new PersonLayout(personList.get(membersListMap.get(person.getMother_id())));

        if (membersListMap.get(person.getFather_id()) != null)
            fatherOfPerson = new PersonLayout(personList.get(membersListMap.get(person.getFather_id())));


        if (membersListMap.get(person.getSpouse_id()) != null)
            spouseOfPerson = new PersonLayout(personList.get(membersListMap.get(person.getSpouse_id())));


        bundle.putSerializable("Person_Mother", motherOfPerson);
        bundle.putSerializable("Person_Father", fatherOfPerson);
        bundle.putSerializable("Person_Spouse", spouseOfPerson);

        intent.putExtras(bundle);
        startActivity(intent);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }
}
