package com.alphalabz.familyapp.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alphalabz.familyapp.R;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.List;

/**
 * Created by Rushabh on 14-01-2016.
 */
public class SwipeDeckAdapter extends BaseAdapter {

    private List<Integer> data;
    private Context context;

    public SwipeDeckAdapter(List<Integer> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // normally use a viewholder
            v = inflater.inflate(R.layout.material_image_card, parent, false);
            ImageView it = (ImageView) v.findViewById(R.id.image);
            it.setImageResource((Integer) getItem(position));
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = (Integer) getItem(position);
                showCustomDialog();
                Log.i("MainActivity", item + "");
            }
        });


        return v;
    }

    public void showCustomDialog() {


        Dialog d = new Dialog(context);

        d.setContentView(R.layout.image_zoom_view);
        d.setTitle("Image");
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) d.findViewById(R.id.imageView);
        imageView.setImage(ImageSource.resource(R.drawable.background));


        d.show();

    }

}
