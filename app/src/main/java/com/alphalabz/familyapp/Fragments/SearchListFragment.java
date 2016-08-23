package com.alphalabz.familyapp.fragments;


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

import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.activities.MainActivity;
import com.alphalabz.familyapp.activities.ProfileActivity;
import com.alphalabz.familyapp.custom.RecyclerItemClickListener;
import com.alphalabz.familyapp.custom.SearchCustomClasses.SearchListAdapter;
import com.alphalabz.familyapp.custom.SearchCustomClasses.SearchMemberModel;
import com.alphalabz.familyapp.objects.Person;
import com.alphalabz.familyapp.objects.PersonLayout;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

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
    
    private LinkedHashMap<String, String> searchableMembersStringMap = new LinkedHashMap<>();

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

        for (Person person : mainActivity.personArrayList) {
            String s = "";
            s += person.getFirst_name().equals("null") || person.getFirst_name().equals("") ? "" : person.getFirst_name() + " ";
            s += person.getMiddle_name().equals("null") || person.getMiddle_name().equals("") ? "" : person.getMiddle_name() + " ";
            s += person.getLast_name().equals("null") || person.getLast_name().equals("") ? "" : person.getLast_name() + " ";
            searchableMembersStringMap.put(person.getUnique_id(), s);
            mainActivity.membersListMap.put(person.getUnique_id(), person);
        }

        searchableMembersStringMap = sortHashMapByValuesD(searchableMembersStringMap);
        final ArrayList<String> mapKeys = new ArrayList(searchableMembersStringMap.keySet());
        int k = 0;
        for (String s : searchableMembersStringMap.values()) {
            mModels.add(new SearchMemberModel(mapKeys.get(k), s, mainActivity.membersListMap.get(mapKeys.get(k)).getImage_url()));
            ++k;
        }

        mAdapter = new SearchListAdapter(getActivity(), mModels);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(),"Unique_id"+mAdapter.getItem(position).getUniqueIDString(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mainActivity, ProfileActivity.class);
                Bundle bundle = new Bundle();

                Person actualPerson = mainActivity.membersListMap.get(mAdapter.getItem(position).getUniqueIDString());
                bundle.putSerializable("Actual_Person", actualPerson);

                Person motherOfPerson = null, fatherOfPerson = null, spouseOfPerson = null;

                if (mainActivity.membersListMap.get(actualPerson.getMother_id()) != null)
                    motherOfPerson = new PersonLayout(mainActivity.membersListMap.get(actualPerson.getMother_id()));

                if (mainActivity.membersListMap.get(actualPerson.getFather_id()) != null)
                    fatherOfPerson = new PersonLayout(mainActivity.membersListMap.get(actualPerson.getFather_id()));


                if (mainActivity.membersListMap.get(actualPerson.getSpouse_id()) != null)
                    spouseOfPerson = new PersonLayout(mainActivity.membersListMap.get(actualPerson.getSpouse_id()));


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
        menu.findItem(R.id.refresh).setVisible(false);
        menu.findItem(R.id.grid_view).setVisible(false);

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
