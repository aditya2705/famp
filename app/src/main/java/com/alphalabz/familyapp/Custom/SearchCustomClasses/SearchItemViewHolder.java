package com.alphalabz.familyapp.custom.SearchCustomClasses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphalabz.familyapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchItemViewHolder extends RecyclerView.ViewHolder {

    private TextView profileNameText;
    private CircleImageView profileImage;
    private Context context;

    public SearchItemViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        profileNameText = (TextView) itemView.findViewById(R.id.text);
        profileImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
    }

    public void bind(SearchMemberModel model) {

        profileNameText.setText(model.getNameString());
        Picasso.with(context).load(model.getImageUrl()).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(profileImage);

    }
}