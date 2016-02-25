package com.alphalabz.familyapp.Fragments;


import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.Adapters.RecyclerGridAdapter;
import com.alphalabz.familyapp.Objects.GalleryObject;
import com.alphalabz.familyapp.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment {


    private int screenWidth, screenHeight;
    private boolean gridViewActive = false;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.scroll_gallery_view)
    ScrollGalleryView scrollGalleryView;

    private RecyclerGridAdapter adapter;

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
        ButterKnife.bind(this, v);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
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

        ArrayList<GalleryObject> galleryObjectArrayList = new ArrayList<>();
        galleryObjectArrayList.add(new GalleryObject(R.drawable.gallery_1));
        galleryObjectArrayList.add(new GalleryObject(R.drawable.gallery_2));
        galleryObjectArrayList.add(new GalleryObject(R.drawable.gallery_3));
        galleryObjectArrayList.add(new GalleryObject(R.drawable.gallery_4));
        galleryObjectArrayList.add(new GalleryObject(R.drawable.gallery_5));
        galleryObjectArrayList.add(new GalleryObject(R.drawable.gallery_6));
        galleryObjectArrayList.add(new GalleryObject(R.drawable.gallery_7));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new RecyclerGridAdapter(getActivity(), galleryObjectArrayList);
        recyclerView.setVisibility(View.INVISIBLE);


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

            if (gridViewActive) {
                item.setIcon(R.drawable.ic_grid_on);
                scrollGalleryView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                gridViewActive = false;
            } else {
                item.setIcon(R.drawable.ic_grid_off);
                switchToGridView();
                scrollGalleryView.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                gridViewActive = true;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchToGridView() {
        recyclerView.setAdapter(adapter);
    }


}
