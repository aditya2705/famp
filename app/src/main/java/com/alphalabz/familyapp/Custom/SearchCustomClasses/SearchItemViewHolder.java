package com.alphalabz.familyapp.custom.SearchCustomClasses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphalabz.familyapp.R;


public class SearchItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView profileNameText;
    private final ImageView profileImage;

    public SearchItemViewHolder(View itemView) {
        super(itemView);

        profileNameText = (TextView) itemView.findViewById(R.id.text);
        profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
    }

    public void bind(SearchMemberModel model) {
        profileNameText.setText(model.getNameString());
        profileImage.setImageResource(R.drawable.profile);
    }
}