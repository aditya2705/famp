package com.alphalabz.familyapp.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.Adapters.SwipeDeckAdapter;
import com.alphalabz.familyapp.R;
import com.daprlabs.cardstack.SwipeDeck;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;

import java.util.ArrayList;

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

        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        final SwipeDeck cardStack = (SwipeDeck) v.findViewById(R.id.swipe_deck);

        final ArrayList<Integer> testData = new ArrayList<>();
        testData.add(R.drawable.gallery_1);
        testData.add(R.drawable.gallery_2);
        testData.add(R.drawable.gallery_3);
        testData.add(R.drawable.background);
        final ArrayList<Integer> testData2 = new ArrayList<>();
        testData2.add(R.drawable.gallery_1);
        testData2.add(R.drawable.gallery_2);
        testData2.add(R.drawable.gallery_3);
        testData2.add(R.drawable.background);

        final SwipeDeckAdapter adapter = new SwipeDeckAdapter(testData, getContext());
        cardStack.setAdapter(adapter);

        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
                testData.add(testData2.get(position % testData2.size()));

                adapter.notifyDataSetChanged();

            }


            @Override
            public void cardSwipedRight(int position) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
                testData.add(testData2.get(position % testData2.size()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");

            }
        });

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



}
