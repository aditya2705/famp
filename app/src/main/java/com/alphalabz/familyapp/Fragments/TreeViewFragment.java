package com.alphalabz.familyapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreeViewFragment extends Fragment {

    private View rootView;
    private ArrayList<Person> personList;// = new ArrayList<>();
    private Person rootPerson;


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

    public void buildPersonTree()
    {
        HashMap<String,Integer> map = new HashMap<>();
        for(int i = 0; i < personList.size(); i++)
        {
            Person p = personList.get(i);
            map.put(p.getName(),i);
        }
        for(int i = 0; i < personList.size(); i++){
            Person p = personList.get(i);
            String fatherName = p.getFatherName();
            if(map.containsKey(fatherName)){
                int pos = map.get(fatherName);
                personList.get(pos).addChild(p);
            }
            else
            {
                rootPerson = p;
            }
        }

    }

    public void bfs(Person root)
    {
        ArrayList<Person> Q = new ArrayList<>();
        Q.add(root);
        while(!Q.isEmpty())
        {
            Person p = Q.get(0);
            Q.remove(0);
            for(int i = 0; i < p.getChildCount(); i++)
            {
                Person c = p.getChildAt(i);
                c.setTreeLevel(p.getTreeLevel() + 1);
                Q.add(c);
                //Add views here
            }
        }
    }

}
