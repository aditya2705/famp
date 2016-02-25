package com.alphalabz.familyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alphalabz.familyapp.Objects.GalleryObject;
import com.alphalabz.familyapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.CustomViewHolder> {

    private ArrayList<GalleryObject> itemList;
    private Context context;

    public RecyclerGridAdapter(Context context, ArrayList<GalleryObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_grid_item, null);
        CustomViewHolder rcv = new CustomViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Picasso.with(context).load(itemList.get(position).getImageResource()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);

        }
    }


}

