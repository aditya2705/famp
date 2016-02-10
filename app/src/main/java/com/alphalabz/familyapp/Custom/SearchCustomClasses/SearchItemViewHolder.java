package com.alphalabz.familyapp.Custom.SearchCustomClasses;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alphalabz.familyapp.R;


public class SearchItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvText;

    public SearchItemViewHolder(View itemView) {
        super(itemView);

        tvText = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(SearchMemberModel model) {
        tvText.setText(model.getNameString());
    }
}