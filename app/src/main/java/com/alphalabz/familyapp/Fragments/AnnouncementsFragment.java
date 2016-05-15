package com.alphalabz.familyapp.fragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.activities.MainActivity;
import com.alphalabz.familyapp.adapters.NewsListAdapter;
import com.alphalabz.familyapp.objects.NewsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementsFragment extends Fragment {

    private static final String RESULTS_FETCH_NEWS = "http://alpha95.net63.net/get_news.php";

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_DESC = "description";

    private String newsJsonString = "";
    private ArrayList<NewsObject> newsObjectArrayList = new ArrayList<>();


    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private View rootView;
    private MainActivity mainActivity;
    private NewsListAdapter adapter;

    public AnnouncementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this,rootView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        adapter = new NewsListAdapter(getActivity(),newsObjectArrayList);
        recyclerView.setAdapter(adapter);

        getNews();

        return rootView;
    }


    public void getNews() {
        class GetNewsData extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                URL obj = null;
                String result = null;
                InputStream inputStream = null;
                try {
                    obj = new URL(RESULTS_FETCH_NEWS);
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
                mainActivity.progressDialog.dismiss();
                if(result!=null&&!result.equals("")) {
                    newsJsonString = result;
                    generateNewsList();
                }
            }

            @Override
            protected void onPreExecute() {
                mainActivity.progressDialog.setTitle("Loading...");
                mainActivity.progressDialog.show();
                super.onPreExecute();
            }
        }
        GetNewsData g = new GetNewsData();
        g.execute();
    }

    protected void generateNewsList(){

        newsObjectArrayList = new ArrayList<>();

        JSONArray newsJsonArray;
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(newsJsonString);

            newsJsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < newsJsonArray.length(); i++) {
                JSONObject c = newsJsonArray.getJSONObject(i);

                String id, title, description;


                id = c.optString(TAG_ID);
                title = c.optString(TAG_TITLE);
                description = c.optString(TAG_DESC);

                NewsObject newsObject = new NewsObject(id, title, description);
                newsObjectArrayList.add(newsObject);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new NewsListAdapter(getActivity(),newsObjectArrayList);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }
}
