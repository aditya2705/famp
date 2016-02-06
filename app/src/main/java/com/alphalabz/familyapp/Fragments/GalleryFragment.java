package com.alphalabz.familyapp.Fragments;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.R;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private ScrollGalleryView scrollGalleryView;


    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        scrollGalleryView = (ScrollGalleryView)v.findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getChildFragmentManager())
                .addImage(R.drawable.gallery_2)
                .addImage(R.drawable.gallery_3)
                .addImage(R.drawable.gallery_4)
                .addImage(R.drawable.gallery_5)
                .addImage(R.drawable.gallery_6)
                .addImage(R.drawable.gallery_1)
                .addImage(R.drawable.gallery_7);

        return v;

    }

}
