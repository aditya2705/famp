package com.alphalabz.familyapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphalabz.familyapp.R;
import com.alphalabz.familyapp.custom.SearchCustomClasses.SearchItemViewHolder;
import com.alphalabz.familyapp.objects.NewsObject;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CustomViewHolder> {

    private ArrayList<NewsObject> itemList;
    private Context context;

    public NewsListAdapter(Context context, ArrayList<NewsObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View itemView = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.titleTextView.setText(itemList.get(position).getTitle());
        holder.descriptionTextView.setText(itemList.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.description);
        }
    }


}

