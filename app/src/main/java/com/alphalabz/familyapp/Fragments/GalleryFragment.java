package com.alphalabz.familyapp.Fragments;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.R;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;

import java.util.ArrayList;

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


        scrollGalleryView = (ScrollGalleryView)v.findViewById(R.id.scroll_gallery_view);
        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getChildFragmentManager())
                .addImage(decodeSampledBitmapFromResource(getResources(),R.drawable.gallery_2,screenWidth,screenHeight))
                .addImage(decodeSampledBitmapFromResource(getResources(),R.drawable.gallery_3,screenWidth,screenHeight))
                .addImage(decodeSampledBitmapFromResource(getResources(),R.drawable.gallery_4,screenWidth,screenHeight))
                .addImage(decodeSampledBitmapFromResource(getResources(),R.drawable.gallery_5,screenWidth,screenHeight))
                .addImage(decodeSampledBitmapFromResource(getResources(),R.drawable.gallery_6,screenWidth,screenHeight))
                .addImage(decodeSampledBitmapFromResource(getResources(),R.drawable.gallery_7,screenWidth,screenHeight))
                .addImage(decodeSampledBitmapFromResource(getResources(),R.drawable.gallery_1,screenWidth,screenHeight));

        return v;

    }

    //Load a bitmap from a resource with a target size
    static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //Given the bitmap size and View size calculate a subsampling size (powers of 2)
    static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 1;	//Default subsampling size
        // See if image raw height and width is bigger than that of required view
        if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
            //bigger
            final int halfHeight = options.outHeight / 2;
            final int halfWidth = options.outWidth / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
