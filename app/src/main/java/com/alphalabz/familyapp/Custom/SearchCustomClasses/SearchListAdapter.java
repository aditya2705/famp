package com.alphalabz.familyapp.Custom.SearchCustomClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphalabz.familyapp.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * User: Xaver
 * Date: 24/05/15
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchItemViewHolder> implements FastScrollRecyclerView.SectionedAdapter{

    private final LayoutInflater mInflater;
    private final List<SearchMemberModel> mModels;

    public SearchListAdapter(Context context, List<SearchMemberModel> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.members_list_item, parent, false);
        return new SearchItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchItemViewHolder holder, int position) {
        final SearchMemberModel model = mModels.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<SearchMemberModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<SearchMemberModel> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final SearchMemberModel model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<SearchMemberModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SearchMemberModel model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SearchMemberModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SearchMemberModel model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SearchMemberModel removeItem(int position) {
        final SearchMemberModel model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SearchMemberModel model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SearchMemberModel model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public SearchMemberModel getItem(int position){
        return mModels.get(position);
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return mModels.get(position).getNameString().substring(0,1);
    }


}