package com.alphalabz.familyapp.Fragments;


import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alphalabz.familyapp.R;
import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment {

    private ScrollGalleryView scrollGalleryView;
    private ScrollView gridScrollView;

    private int screenWidth, screenHeight;
    private boolean gridViewActive = false;

    @Bind(R.id.img1) ImageView imageView1;
    @Bind(R.id.img2) ImageView imageView2;
    @Bind(R.id.img3) ImageView imageView3;
    @Bind(R.id.img4) ImageView imageView4;
    @Bind(R.id.img5) ImageView imageView5;

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
        ButterKnife.bind(this,v);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        gridScrollView = (ScrollView)v.findViewById(R.id.scroll_grid_view);



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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.refresh).setVisible(false);
        menu.findItem(R.id.grid_view).setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.grid_view) {

            if(gridViewActive) {
                item.setIcon(R.drawable.ic_grid_on);
                gridScrollView.setVisibility(View.INVISIBLE);
                scrollGalleryView.setVisibility(View.VISIBLE);
                gridViewActive = false;
            }
            else {
                item.setIcon(R.drawable.ic_grid_off);
                gridScrollView.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(R.drawable.gallery_1).into(imageView1);
                Picasso.with(getActivity()).load(R.drawable.gallery_2).into(imageView2);
                Picasso.with(getActivity()).load(R.drawable.gallery_3).into(imageView3);
                Picasso.with(getActivity()).load(R.drawable.gallery_4).into(imageView4);
                Picasso.with(getActivity()).load(R.drawable.gallery_5).into(imageView5);
                scrollGalleryView.setVisibility(View.INVISIBLE);
                gridViewActive = true;
            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
