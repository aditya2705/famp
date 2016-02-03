package com.alphalabz.familyapp.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.R;
import com.rey.material.widget.ProgressView;

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

    private View rootView;
    private ArrayList<Person> personList;// = new ArrayList<>();
    private LinearLayout parentLayout;
    private Person rootPerson;
    private int marginForChildLayout;

    String myJSON;

    private static final String RESULTS_FETCH_URL = "http://alpha95.net63.net/get_members.php";

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "unique_id";
    private static final String TAG_FIRST_NAME = "first_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_MOTHER_ID ="mother_id";
    private static final String TAG_FATHER_ID ="father_id";
    private static final String TAG_PRIMARY_PARENT ="primary_parent";
    private static final String TAG_GENDER ="gender";
    private static final String TAG_SPOUSE_ID ="spouse_id";
    private static final String TAG_BIRTH_DATE="birth_date";
    private static final String TAG_MARRIAGE_DATE="marriage_date";
    private static final String TAG_DEATH_DATE="death_date";
    private static final String TAG_MOBILE ="mobile_number";
    private static final String TAG_LANDLINE ="landline";
    private static final String TAG_ADDRESS ="address";
    private static final String TAG_LATITUDE ="latitude";
    private static final String TAG_LONGITUDE ="longitude";
    private static final String TAG_EMAIL ="email";
    private static final String TAG_IMAGE_URL ="image_url";

    JSONArray membersJsonArray = null;

    private ProgressView circularProgressView;


    public TreeViewFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tree_view, container, false);

        circularProgressView = (ProgressView) rootView.findViewById(R.id.circularProgress);


        parentLayout = (LinearLayout)rootView.findViewById(R.id.parent_layout);

        marginForChildLayout = (int) (getResources().getDimension(R.dimen._minus15sdp));

        personList = new ArrayList<>();

        getData();

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
            map.put(p.getUnique_id(),i);
        }
        for(int i = 0; i < personList.size(); i++){
            Person p = personList.get(i);
            String fatherID = p.getFather_id();
            if(map.containsKey(fatherID)){
                int pos = map.get(fatherID);
                personList.get(pos).addChild(p);
            }
            else
            {
                if(!p.getPrimary_parent().equals("0"))
                    rootPerson = p;
            }
        }

    }

    private RelativeLayout getNodeLayout()
    {
        return (RelativeLayout)LayoutInflater.from(getContext()).inflate(R.layout.node_layout,null,false);
    }

    public void bfs()
    {
        ArrayList<Person> Q = new ArrayList<>();
        Q.add(rootPerson);
        rootPerson.setTreeLevel(0);
        RelativeLayout rootLayout = getNodeLayout();
        TextView personNameView = (TextView) rootLayout.findViewById(R.id.person_name);
        personNameView.setText(rootPerson.getFirst_name());
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
                personNameView.setText(c.getFirst_name());
                c.setPersonLayout(newNodeLayout);
                pChildLayout.addView(c.getPersonLayout());

                c.setTreeLevel(p.getTreeLevel() + 1);
                Q.add(c);
                Log.d("Child",c.getFirst_name());
                Log.d("Child","" + pChildLayout.getChildCount());





            }

        }
    }




    public void getData(){
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
                    con.setRequestProperty("Content-Type","application/json");
                    inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    Log.d("RESULT",result);

                } catch (Exception e) {}
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                try {
                    showList();
                    circularProgressView.stop();
                } catch (Exception e) {}
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                circularProgressView.start();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    protected void showList() throws JSONException {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            membersJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i = 0; i< membersJsonArray.length(); i++){
                JSONObject c = membersJsonArray.getJSONObject(i);

                String unique_id,first_name,last_name,mother_id,father_id,primary_parent,
                        gender,spouse_id,birth_date,marriage_date,death_date, mobile_number,
                        landline,address,latitude,longitude,email,image_url;


                unique_id = c.getString(TAG_ID);
                first_name = c.getString(TAG_FIRST_NAME);
                last_name = c.getString(TAG_LAST_NAME);
                mother_id = c.getString(TAG_MOTHER_ID);
                father_id = c.getString(TAG_FATHER_ID);
                primary_parent = c.getString(TAG_PRIMARY_PARENT);
                gender = c.getString(TAG_GENDER);
                spouse_id = c.getString(TAG_SPOUSE_ID);
                birth_date = c.getString(TAG_BIRTH_DATE);
                marriage_date = c.getString(TAG_MARRIAGE_DATE);
                death_date = c.getString(TAG_DEATH_DATE);
                mobile_number = c.getString(TAG_MOBILE);
                landline = c.getString(TAG_LANDLINE);
                address = c.getString(TAG_ADDRESS);
                latitude = c.getString(TAG_LATITUDE);
                longitude = c.getString(TAG_LONGITUDE);
                email = c.getString(TAG_EMAIL);
                image_url = c.getString(TAG_IMAGE_URL);


                Person person = new Person(unique_id,first_name,last_name,
                        mother_id,father_id,primary_parent,gender,spouse_id,birth_date,marriage_date,
                        death_date,mobile_number,landline,address,latitude,longitude,email,image_url,-1);

                personList.add(person);

            }


            buildPersonTree();
            bfs();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
