package com.alphalabz.familyapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alphalabz.familyapp.Activities.MainActivity;
import com.alphalabz.familyapp.Fragments.ProfileFragment;
import com.alphalabz.familyapp.R;

import java.util.ArrayList;

/**
 * Created by Rushabh on 22-12-2015.
 */
public class EventTableAdapter extends RecyclerView.Adapter<EventTableAdapter.ViewHolder> implements View.OnClickListener {
    Context ct;
    private ArrayList<String> mDataset;
    // Provide a suitable constructor (depends on the kind of dataset)
    public EventTableAdapter(Context context, ArrayList<String> myDataset) {
        mDataset = myDataset;
        ct = context;
    }

    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        TableLayout tl = (TableLayout) v.findViewById(R.id.month_table);
        for (int i = 0; i < 9; i++) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
            tl.addView(row);
            TextView date = (TextView) row.findViewById(R.id.date_row);
            TextView bday = (TextView) row.findViewById(R.id.birthday_row);
            TextView anniv = (TextView) row.findViewById(R.id.anniversary_row);
            date.setText("10/12/2015");
            bday.setText("Mr.Sharma");
            anniv.setText("Mr and Mrs Patel");
            bday.setOnClickListener(this);


        }


        return vh;
    }

    @Override
    public void onClick(View v) {
        //Call FragmentManager and add Fragment to it.
        Fragment fragment;
        fragment = new ProfileFragment();
        Bundle b = new Bundle();
        b.putString("profile_id", "ady123");
        fragment.setArguments(b);
        ((MainActivity) ct).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        ((MainActivity) ct).getSupportActionBar().setTitle("Profile");
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = mDataset.get(position);
        holder.txtHeader.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.month_name);


        }
    }

}
