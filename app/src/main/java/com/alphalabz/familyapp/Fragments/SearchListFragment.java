package com.alphalabz.familyapp.Fragments;


import android.app.Activity;
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
import com.alphalabz.familyapp.Custom.SearchCustomClasses.SearchListAdapter;
import com.alphalabz.familyapp.Custom.SearchCustomClasses.SearchMemberModel;
import com.alphalabz.familyapp.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchListFragment extends Fragment implements SearchView.OnQueryTextListener{

    private MainActivity mainActivity;

    private static final String[] MOVIES = new String[]{
            "The Woman in Black: Angel of Death",
            "20 Once Again",
            "Taken 3",
            "Tevar",
            "I",
            "Blackhat",
            "Spare Parts",
            "The Wedding Ringer",
            "Ex Machina",
            "Mortdecai",
            "Strange Magic",
            "The Boy Next Door",
            "The SpongeBob Movie: Sponge Out of Water",
            "Kingsman: The Secret Service",
            "Boonie Bears: Mystical Winter",
            "Project Almanac",
            "Running Man",
            "Wild Card",
            "It Follows",
            "C'est si bon",
            "Yennai Arindhaal",
            "Shaun the Sheep Movie",
            "Jupiter Ascending",
            "Old Fashioned",
            "Somewhere Only We Know",
            "Fifty Shades of Grey",
            "Dragon Blade",
            "Zhong Kui: Snow Girl and the Dark Crystal",
            "Badlapur",
            "Hot Tub Time Machine 2",
            "McFarland, USA",
            "The Duff",
            "The Second Best Exotic Marigold Hotel",
            "A la mala",
            "Focus",
            "The Lazarus Effect",
            "Chappie",
            "Faults",
            "Road Hard",
            "Unfinished Business",
            "Cinderella",
            "NH10",
            "Run All Night",
            "X+Y",
            "Furious 7",
            "Danny Collins",
            "Do You Believe?",
            "Jalaibee",
            "The Divergent Series: Insurgent",
            "The Gunman",
            "Get Hard",
            "Home"
    };

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

        for (String movie : MOVIES) {
            mModels.add(new SearchMemberModel(movie));
        }

        mAdapter = new SearchListAdapter(getActivity(), mModels);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        final MenuItem item = menu.findItem(R.id.search);
        item.setVisible(true);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
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
            final String text = model.getText().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }
}
