package com.alphalabz.familyapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alphalabz.familyapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreeViewFragment extends Fragment {

    private View rootView;


    public TreeViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tree_view, container, false);





        return rootView;
    }

    public LinearLayout getHorizontalLL(){
        LinearLayout linearLayoutHorizontal = new LinearLayout(getActivity());
        linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayoutHorizontal;
    }

}
