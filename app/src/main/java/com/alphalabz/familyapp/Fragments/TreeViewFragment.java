package com.alphalabz.familyapp.Fragments;


import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphalabz.familyapp.Activities.MainActivity;
import com.alphalabz.familyapp.Activities.ProfileActivity;
import com.alphalabz.familyapp.Objects.Person;
import com.alphalabz.familyapp.Objects.PersonLayout;
import com.alphalabz.familyapp.R;

import net.i2p.android.ext.floatingactionbutton.FloatingActionButton;
import net.i2p.android.ext.floatingactionbutton.FloatingActionsMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreeViewFragment extends Fragment {

    private MainActivity mainActivity;
    private ScrollView verticalScrollView;
    private HorizontalScrollView horizontalScrollView;

    private View rootView;
    private ArrayList<PersonLayout> personList;// = new ArrayList<>();
    private LinearLayout parentLayout;
    private PersonLayout rootPerson;
    private int marginForChildLayout;
    private LinkedHashMap<String, PersonLayout> membersListMap;
    private FloatingActionsMenu menuMultipleActions;

    private String membersListJsonString;
    private Bitmap familyTreeBitmap;

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

        rootView.findViewById(R.id.shadowView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getVisibility() == View.VISIBLE){
                    menuMultipleActions.collapse();
                }
            }
        });

        menuMultipleActions = (FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);
        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                rootView.findViewById(R.id.shadowView).setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                rootView.findViewById(R.id.shadowView).setVisibility(View.INVISIBLE);
            }
        });


        FloatingActionButton actionButton2 = (FloatingActionButton)
                rootView.findViewById(R.id.action_download);
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
                menuMultipleActions.collapse();
            }
        });

        verticalScrollView = (ScrollView) rootView.findViewById(R.id.vertical_scroll_view);
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontal_scroll_view);

        parentLayout = (LinearLayout) rootView.findViewById(R.id.parent_layout);

        marginForChildLayout = (int) (getResources().getDimension(R.dimen._minus15sdp));

        personList = new ArrayList<>();

        if (membersListJsonString.equals("") || membersListJsonString == null)
            getData();
        else
            showList();

        return rootView;
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


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void openProfileActivity(PersonLayout person) {

        Intent intent = new Intent(mainActivity, ProfileActivity.class);
        Bundle bundle = new Bundle();

        Person actualPerson = new PersonLayout(person);
        bundle.putSerializable("Actual_Person", actualPerson);

        Person motherOfPerson = null, fatherOfPerson = null, spouseOfPerson = null;

        if (membersListMap.get(person.getMother_id()) != null)
            motherOfPerson = new PersonLayout(membersListMap.get(person.getMother_id()));

        if (membersListMap.get(person.getFather_id()) != null)
            fatherOfPerson = new PersonLayout(membersListMap.get(person.getFather_id()));


        if (membersListMap.get(person.getSpouse_id()) != null)
            spouseOfPerson = new PersonLayout(membersListMap.get(person.getSpouse_id()));


        bundle.putSerializable("Person_Mother", motherOfPerson);
        bundle.putSerializable("Person_Father", fatherOfPerson);
        bundle.putSerializable("Person_Spouse", spouseOfPerson);

        intent.putExtras(bundle);
        startActivity(intent);

    }

    public static Bitmap loadBitmapFromView(View v)
    {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width/500, v.getLayoutParams().height/500);
        v.draw(c);
        return b;
    }

    private void generateImage()
    {

        HorizontalScrollView z = horizontalScrollView;
        int totalHeight = z.getChildAt(0).getHeight();
        int totalWidth = z.getChildAt(0).getWidth();

       familyTreeBitmap = getBitmapFromView(parentLayout,totalHeight,totalWidth);

    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;

        int height = (int) Math.min(screenHeight/1.1, totalHeight);
        float percent = height / (float)totalHeight;

        Bitmap canvasBitmap = Bitmap.createBitmap((int)(totalWidth*percent),(int)(totalHeight*percent), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(canvasBitmap);

        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        canvas.save();
        canvas.scale(percent, percent);
        view.draw(canvas);
        canvas.restore();

        return canvasBitmap;
    }


    private void saveImage() {

        generateImage();

        class saveImageTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String result = "";
                //Save bitmap
                File imageDirectory = new File(Environment.getExternalStorageDirectory()+"/Family Tree/");
                imageDirectory.mkdirs();
                String fileName = "family_tree.jpg";
                File myPath = new File(imageDirectory, fileName);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(myPath);
                    familyTreeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();

                }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                Toast.makeText(getActivity(),"Family tree saved as image inside /sdcard/Family Tree/ as family_tree.jpg",Toast.LENGTH_SHORT).show();
                mainActivity.progressDialog.dismiss();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mainActivity.progressDialog.setTitle("Saving Image...");
                mainActivity.progressDialog.show();
            }
        }

        saveImageTask g = new saveImageTask();
        g.execute();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }


    private RelativeLayout getNodeLayout(final PersonLayout person) {

        RelativeLayout nodeLayout;

        nodeLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.node_layout, null, false);

        TextView personNameView = (TextView) nodeLayout.findViewById(R.id.person_name);
        ImageView personImageView = (ImageView) nodeLayout.findViewById(R.id.person_image_view);
        TextView spouseNameView = (TextView) nodeLayout.findViewById(R.id.spouse_name);
        ImageView spouseImageView = (ImageView) nodeLayout.findViewById(R.id.spouse_image_view);

        personNameView.setText((person.getTitle().equals("null")?"":person.getTitle()+" ")+person.getFirst_name()+" "
                +(person.getMiddle_name().equals("null")?"":person.getMiddle_name()+" ")
                +person.getLast_name());

        personNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileActivity(person);
            }
        });
        personImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileActivity(person);
            }
        });
        personNameView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openChildrenLayouts(person);
                return true;
            }
        });
        personImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openChildrenLayouts(person);
                return true;
            }
        });


        String s = null;
        if (membersListMap.get(person.getSpouse_id()) != null) {
            Person p = membersListMap.get(person.getSpouse_id());
            
            s = (p.getTitle().equals("null")?"":p.getTitle()+" ")+p.getFirst_name()+" "
                    +(p.getMiddle_name().equals("null")?"":p.getMiddle_name()+" ")
                    +p.getLast_name();
        }

        if (s != null && !s.equals("")) {
            spouseNameView.setText(s);
            spouseNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openProfileActivity(membersListMap.get(person.getSpouse_id()));

                }
            });
            spouseImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    openProfileActivity(membersListMap.get(person.getSpouse_id()));

                }
            });
            spouseNameView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    openChildrenLayouts(person);
                    return true;
                }
            });
            spouseImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    openChildrenLayouts(person);
                    return true;
                }
            });


        } else {
            nodeLayout.findViewById(R.id.spouse_layout).setVisibility(View.INVISIBLE);
        }

        return nodeLayout;

    }

    private void openChildrenLayouts(final PersonLayout person) {

        LinearLayout pChildLayout = (LinearLayout) person.getPersonLayout().findViewById(R.id.childLinearLayout);

        if(person.getChildCount()>0)
            person.getPersonLayout().findViewById(R.id.bottom_branch_connect).setVisibility(View.VISIBLE);

        if(!person.isChildrenOpened()){

            for(int i=0;i<person.getChildCount();i++){

                PersonLayout child = person.getChildren().get(i);
                RelativeLayout childLayout = getNodeLayout(child);

                if(i==0){
                    childLayout.findViewById(R.id.left_branch).setVisibility(View.INVISIBLE);
                }
                if(i==person.getChildCount()-1){
                    childLayout.findViewById(R.id.right_branch).setVisibility(View.INVISIBLE);
                }

                childLayout.findViewById(R.id.bottom_branch_connect).setVisibility(View.INVISIBLE);

                child.setPersonLayout(childLayout);

                pChildLayout.addView(childLayout);

            }

        }

        person.setChildrenOpened(true);

    }


    public void buildPersonTree() {
        parentLayout.removeAllViews();


        membersListMap = new LinkedHashMap<>();
        for (int i = 0; i < personList.size(); i++) {
            membersListMap.put(personList.get(i).getUnique_id(), personList.get(i));
        }
        for (int i = 0; i < personList.size(); i++) {
            PersonLayout p = personList.get(i);
            String parentID = null;

            if(membersListMap.get(p.getFather_id())!=null&&!membersListMap.get(p.getFather_id()).getIn_law().equals("Y"))
                parentID = p.getFather_id();
            else if(membersListMap.get(p.getMother_id())!=null&&!membersListMap.get(p.getMother_id()).getIn_law().equals("Y"))
                parentID = p.getMother_id();

            if (parentID!=null&&!p.getIn_law().equals("Y")) {
                membersListMap.get(parentID).addChild(p);
            } else {
                if (!p.getIn_law().equals("Y")&&i<=1)
                    rootPerson = p;
            }
        }

        RelativeLayout rootLayout = getNodeLayout(rootPerson);
        rootLayout.findViewById(R.id.left_branch).setVisibility(View.INVISIBLE);
        rootLayout.findViewById(R.id.right_branch).setVisibility(View.INVISIBLE);
        rootLayout.findViewById(R.id.bottom_branch_connect).setVisibility(View.INVISIBLE);
        rootLayout.findViewById(R.id.parent_branch).setVisibility(View.INVISIBLE);

        parentLayout.addView(rootLayout);
        rootPerson.setPersonLayout(rootLayout);

        rootLayout.findViewById(R.id.person_image_view).performLongClick();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.scrollTo((int) (rootPerson.getPersonLayout().getWidth()/2.4f),0);
                mainActivity.progressDialog.dismiss();
            }
        }, 300);


    }

}
