package com.alphalabz.familyapp.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.Activities.MainActivity;
import com.alphalabz.familyapp.Activities.ProfileActivity;
import com.alphalabz.familyapp.Custom.RecyclerItemClickListener;
import com.alphalabz.familyapp.Custom.SearchCustomClasses.SearchListAdapter;
import com.alphalabz.familyapp.Custom.SearchCustomClasses.SearchMemberModel;
import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.Objects.PersonLayout;
import com.alphalabz.familyapp.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private MainActivity mainActivity;
    private String membersListJsonString;
    private JSONArray membersJsonArray = null;
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

    private ArrayList<Person> personList = new ArrayList<>();
    private LinkedHashMap<String, String> searchableMembersStringMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Person> membersListMap = new LinkedHashMap<>();

    private View rootView;

    private FastScrollRecyclerView mRecyclerView;
    private SearchListAdapter mAdapter;
    private List<SearchMemberModel> mModels;


    public SearchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_list_view, container, false);

        mRecyclerView = (FastScrollRecyclerView) rootView.findViewById(R.id.recycler);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mModels = new ArrayList<>();

        membersListJsonString = mainActivity.sharedPreferences.getString("MEMBERS_STRING", "");

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(membersListJsonString);

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


                Person person = new Person(unique_id, generation, title, first_name, middle_name, last_name, nick_name,
                        gender, in_law, mother_id, mother_name, father_id, father_name, spouse_id,
                        spouse_name, birth_date, marriage_date, death_date, mobile_number, alternate_number,
                        residence_number, email1, email2, address_1, address_2, city, state_country, pincode,
                        designation, company, industry_special, image_url, -1);

                personList.add(person);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (Person person : personList) {
            String s = "";
            s += person.getFirst_name().equals("null") || person.getFirst_name().equals("") ? "" : person.getFirst_name() + " ";
            s += person.getMiddle_name().equals("null") || person.getMiddle_name().equals("") ? "" : person.getMiddle_name() + " ";
            s += person.getLast_name().equals("null") || person.getLast_name().equals("") ? "" : person.getLast_name() + " ";
            searchableMembersStringMap.put(person.getUnique_id(), s);
            membersListMap.put(person.getUnique_id(), person);
        }

        searchableMembersStringMap = sortHashMapByValuesD(searchableMembersStringMap);
        final ArrayList<String> mapKeys = new ArrayList(searchableMembersStringMap.keySet());
        int k = 0;
        for (String s : searchableMembersStringMap.values())
            mModels.add(new SearchMemberModel(mapKeys.get(k++), s));

        mAdapter = new SearchListAdapter(getActivity(), mModels);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(),"Unique_id"+mAdapter.getItem(position).getUniqueIDString(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mainActivity, ProfileActivity.class);
                Bundle bundle = new Bundle();

                Person actualPerson = membersListMap.get(mAdapter.getItem(position).getUniqueIDString());
                bundle.putSerializable("Actual_Person", actualPerson);

                Person motherOfPerson = null, fatherOfPerson = null, spouseOfPerson = null;

                if (membersListMap.get(actualPerson.getMother_id()) != null)
                    motherOfPerson = new PersonLayout(membersListMap.get(actualPerson.getMother_id()));

                if (membersListMap.get(actualPerson.getFather_id()) != null)
                    fatherOfPerson = new PersonLayout(membersListMap.get(actualPerson.getFather_id()));


                if (membersListMap.get(actualPerson.getSpouse_id()) != null)
                    spouseOfPerson = new PersonLayout(membersListMap.get(actualPerson.getSpouse_id()));


                bundle.putSerializable("Person_Mother", motherOfPerson);
                bundle.putSerializable("Person_Father", fatherOfPerson);
                bundle.putSerializable("Person_Spouse", spouseOfPerson);

                intent.putExtras(bundle);
                startActivity(intent);


            }
        }));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchMenuItem.setVisible(true);
        MenuItem refreshMenuItem = menu.findItem(R.id.refresh);
        refreshMenuItem.setVisible(false);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<SearchMemberModel> filteredModelList = filter(mModels, query);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<SearchMemberModel> filter(List<SearchMemberModel> models, String query) {
        query = query.toLowerCase();

        final List<SearchMemberModel> filteredModelList = new ArrayList<>();
        for (SearchMemberModel model : models) {
            final String text = model.getNameString().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    public LinkedHashMap sortHashMapByValuesD(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put(key, val);
                    break;
                }

            }

        }
        return sortedMap;
    }
}
