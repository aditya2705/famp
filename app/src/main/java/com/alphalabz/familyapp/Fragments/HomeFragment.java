package com.alphalabz.familyapp.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.activities.MainActivity;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.LinkedHashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener {


    @Bind(R.id.btn_family_tree) FButton treeViewButton;
    @Bind(R.id.btn_all_events) FButton allEventsButton;
    @Bind(R.id.btn_calendar) FButton calendarButton;
    @Bind(R.id.btn_members_list) FButton membersListButton;
    @Bind(R.id.btn_gallery) FButton galleryButton;
    @Bind(R.id.btn_announcements) FButton announcementsButton;
    @Bind(R.id.btn_settings) FButton settingsButton;
    @Bind(R.id.btn_contact) FButton contactButton;
    @Bind(R.id.slider) SliderLayout mSlider;

    private View rootView;
    private MainActivity mainActivity;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,rootView);

        //setup slider
        LinkedHashMap<String,Integer> gallery_images = new LinkedHashMap<String, Integer>();
        gallery_images.put("1", R.drawable.gallery_1);
        gallery_images.put("2", R.drawable.gallery_2);
        gallery_images.put("3", R.drawable.gallery_3);
        gallery_images.put("4", R.drawable.gallery_4);
        gallery_images.put("5", R.drawable.gallery_5);
        gallery_images.put("6", R.drawable.gallery_6);
        gallery_images.put("7", R.drawable.gallery_7);

        for(String name : gallery_images.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .image(gallery_images.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            mSlider.addSlider(textSliderView);
        }


        mSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(3000);


        treeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(1);

            }
        });

        allEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(4);

            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(3);

            }
        });

        membersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(2);

            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(5);

            }
        });

        announcementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(6);

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(7);

            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.drawer.setSelectionAtPosition(8);

            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity)activity;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }

}
