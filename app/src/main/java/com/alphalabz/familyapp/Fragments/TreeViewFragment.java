package com.alphalabz.familyapp.Fragments;


import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreeViewFragment extends Fragment {

    private View rootView;
    private ArrayList<Person> personList;// = new ArrayList<>();
    private LinearLayout parentLayout;
    private Person rootPerson;
    private int marginForChildLayout;


    public TreeViewFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tree_view, container, false);
        parentLayout = (LinearLayout)rootView.findViewById(R.id.parent_layout);

        marginForChildLayout = (int) (getResources().getDimension(R.dimen._minus15sdp));

        personList = new ArrayList<>();
        personList.add(new Person("101","A","123","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("123","B","","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null,null));
        personList.add(new Person("102","C","123","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("103","D","123","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("105","E","103","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("106","F","101","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));
        personList.add(new Person("107","F","101","142","14.35.3","1515",'M',"agaeg","arh","arg",null,1,new ArrayList<Person>(),null, null));

        buildPersonTree();
        bfs();



        return rootView;
    }

    public LinearLayout getHorizontalLL(){
        LinearLayout childrenLayout = (LinearLayout)View.inflate(getActivity(),R.layout.children_layout,null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, marginForChildLayout, 0, 0);
        childrenLayout.setLayoutParams(layoutParams);
        return childrenLayout;
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

    private RelativeLayout getNodeLayout()
    {
        return (RelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.new_node_layout,null,false);
    }

    public void bfs()
    {
        ArrayList<Person> Q = new ArrayList<>();
        Q.add(rootPerson);
        rootPerson.setTreeLevel(0);
        /*LinearLayout rootPersonViewLayout = (LinearLayout) View.inflate(getActivity(),R.layout.node_layout,null);
        TextView nameTextView = (TextView) rootPersonViewLayout.findViewById(R.id.person_name);
        nameTextView.setText(rootPerson.getFirstName());
        parentLayout.addView(rootPersonViewLayout);
        LinearLayout childrenRootLayout = getHorizontalLL();
        rootPerson.setChildrenLayout((LinearLayout) childrenRootLayout.findViewById(R.id.child_adding_layout));
        rootPerson.setSelfViewLayout(rootPersonViewLayout);
        rootPersonViewLayout.addView(childrenRootLayout);*/
        RelativeLayout rootLayout = getNodeLayout();
        TextView personNameView = (TextView) rootLayout.findViewById(R.id.person_name);
        personNameView.setText(rootPerson.getFirstName());
        parentLayout.addView(rootLayout);
        rootPerson.setPersonLayout(rootLayout);


        while(!Q.isEmpty())
        {
            Person p = Q.get(0);
            Q.remove(0);
            LinearLayout pChildLayout = (LinearLayout)p.getPersonLayout().findViewById(R.id.childLinearLayout);
            for(int i = 0; i < p.getChildCount(); i++)
            {
                Person c = p.getChildAt(i);
                RelativeLayout newNodeLayout = getNodeLayout();
                personNameView = (TextView) newNodeLayout.findViewById(R.id.person_name);
                personNameView.setText(c.getFirstName());
                c.setPersonLayout(newNodeLayout);
                pChildLayout.addView(c.getPersonLayout());

                c.setTreeLevel(p.getTreeLevel() + 1);
                Q.add(c);
                Log.d("Child",c.getFirstName());
                Log.d("Child","" + pChildLayout.getChildCount());





            }

        }


        //bfsBranchFix();
    }

    public void bfsBranchFix()
    {
        ArrayList<Person> Q = new ArrayList<>();
        Q.add(rootPerson);
        rootPerson.setTreeLevel(0);


        while(!Q.isEmpty())
        {
            Person p = Q.get(0);
            Q.remove(0);

            RelativeLayout layout = p.getPersonLayout();
            View branch = layout.findViewById(R.id.child_branch);
           // R pChildLayout = (LinearLayout)p.getPersonLayout().findViewById(R.id.childLinearLayout);
            int first = 0, last = p.getChildCount() - 1;
            if(first <= last){

                /*int marginLeft = p.getChildAt(first).getPersonLayout().getWidth()/2;
                int marginRight = p.getChildAt(last).getPersonLayout().getWidth()/2;

                Log.d("Margins",  + marginLeft + " " + marginRight);


                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) branch.getLayoutParams();
               // params.setMargins(0,marginForChildLayout,marginRight,0);
                params.leftMargin = marginLeft;
                params.rightMargin = marginRight;
                branch.setLayoutParams(params);*/

            }

            for(int i = 0; i < p.getChildCount(); i++)
            {
                Person c = p.getChildAt(i);
                Q.add(c);
               // Log.d("Child",c.getFirstName());
               // Log.d("Child","" + pChildLayout.getChildCount());

            }

        }
    }



}
