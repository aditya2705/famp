package com.alphalabz.familyapp.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alphalabz.familyapp.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private Dialog d;
    private SubsamplingScaleImageView imageView;

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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        MaterialListView mListView = (MaterialListView) v.findViewById(R.id.material_listview);
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("CARD_TYPE", "" + card.getTag());
                showCustomDialog();
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            cards.add(getCard());
        }

        mListView.getAdapter().addAll(cards);
        return v;
    }

    public Card getCard() {
        Card card = new Card.Builder(getContext())
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_image_card)
                .setTitle("Image Name")
                .setDescription("Description of the image")
                .setDrawable(R.drawable.background_profile)
                .endConfig()
                .build();

        return card;
    }

    public void showDialog(String title, String content) {
        boolean wrapInScrollView = true;

        MaterialDialog md = new MaterialDialog.Builder(getContext())
                .title(title)
                .customView(R.layout.image_zoom_view, wrapInScrollView)
                .positiveText("Back")
                .backgroundColorRes(R.color.md_white_1000)
                .positiveColorRes(R.color.md_green_500)
                .build();
        View par = md.getView();
        android.view.ViewGroup.LayoutParams params = par.getLayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        par.setLayoutParams(params);
        View v = md.getCustomView();
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) v.findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.drawable.background_profile));
        md.show();


    }

    public void showCustomDialog() {


        d = new Dialog(getActivity());

        d.setContentView(R.layout.image_zoom_view);
        d.setTitle("Image");
        imageView = (SubsamplingScaleImageView) d.findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.drawable.background));


        d.show();

    }

}
