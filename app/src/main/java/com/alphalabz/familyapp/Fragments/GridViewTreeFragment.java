package com.alphalabz.familyapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridViewTreeFragment extends Fragment {

    private View rootView;
    private ArrayList<Person> personList;// = new ArrayList<>();
    private GridLayout parentLayout;
    private Person rootPerson;
    private int marginForChildLayout;


    public GridViewTreeFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tree_grid_view, container, false);
        parentLayout = (GridLayout) rootView.findViewById(R.id.parent_layout);

        marginForChildLayout = (int) (getResources().getDimension(R.dimen._minus15sdp));

        personList = new ArrayList<>();
        personList.add(new Person("101","atrhrth","123","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("123","atrhrth","","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null,null));
        personList.add(new Person("102","atrhrth","123","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("103","atrhrth","123","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("105","atrhrth","103","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("107","atrhrth","101","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));

        //buildPersonTree();
        //bfs();



        return rootView;
    }

    public LinearLayout getHorizontalLL(){
        LinearLayout linearLayoutHorizontal = (LinearLayout)View.inflate(getActivity(),R.layout.children_layout,null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(2000
                , LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0,marginForChildLayout,0,0);
        linearLayoutHorizontal.setLayoutParams(layoutParams);
        return linearLayoutHorizontal;
    }

    public void buildPersonTree()
    {
        parentLayout.removeAllViews();

        HashMap<String,Integer> map = new HashMap<>();
        for(int i = 0; i < personList.size(); i++)
        {
            Person p = personList.get(i);
            map.put(p.getUniqueID(),i);
        }
        for(int i = 0; i < personList.size(); i++){
            Person p = personList.get(i);
            String fatherID = p.getFatherID();
            if(map.containsKey(fatherID)){
                int pos = map.get(fatherID);
                personList.get(pos).addChild(p);
            }
            else
            {
                rootPerson = p;
            }
        }

    }

    public void bfs()
    {
        ArrayList<Person> Q = new ArrayList<>();
        Q.add(rootPerson);
        LinearLayout rootPersonViewLayout = (LinearLayout) View.inflate(getActivity(),R.layout.node_layout,null);
        parentLayout.addView(rootPersonViewLayout);
        LinearLayout childrenRootLayout = getHorizontalLL();
        rootPerson.setChildrenLayout((LinearLayout) childrenRootLayout.findViewById(R.id.child_adding_layout));
        rootPerson.setSelfViewLayout(rootPersonViewLayout);
        rootPersonViewLayout.addView(childrenRootLayout);

        while(!Q.isEmpty())
        {
            Person p = Q.get(0);
            Q.remove(0);
            for(int i = 0; i < p.getChildCount(); i++)
            {
                Person c = p.getChildAt(i);
                LinearLayout newChildLayout = (LinearLayout) View.inflate(getActivity(),R.layout.node_layout,null);

                if(p.getChildrenLayout()==null) {
                    childrenRootLayout = getHorizontalLL();
                    p.setChildrenLayout((LinearLayout) childrenRootLayout.findViewById(R.id.child_adding_layout));
                    p.getSelfViewLayout().addView(childrenRootLayout);
                }

                p.getChildrenLayout().addView(newChildLayout);
                c.setSelfViewLayout(newChildLayout);

                Q.add(c);

            }
        }
    }

}
