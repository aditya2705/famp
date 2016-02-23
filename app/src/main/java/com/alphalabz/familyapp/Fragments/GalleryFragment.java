package com.alphalabz.familyapp.Fragments;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphalabz.familyapp.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

public class GalleryFragment extends Fragment {

    private ScrollGalleryView scrollGalleryView;

    private int screenWidth, screenHeight;

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
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        scrollGalleryView = (ScrollGalleryView) v.findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .hideThumbnails(true)
                .setZoom(true)
                .setFragmentManager(getChildFragmentManager())
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.gallery_2)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.gallery_3)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.gallery_4)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.gallery_5)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.gallery_6)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.gallery_1)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.gallery_7)));



        return v;

    }


}
