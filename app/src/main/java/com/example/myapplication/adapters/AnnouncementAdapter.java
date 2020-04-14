package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseListAdapter;
import com.example.myapplication.model.AnnounceBean;

import java.util.List;

public class AnnouncementAdapter extends BaseListAdapter {

    private List<AnnounceBean> mData;
    private OnItemClickListener mListener;

    public AnnouncementAdapter(List<AnnounceBean> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, AnnounceBean bean);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mData != null && mData.size() > 0 && holder instanceof ItemViewHolder) {
           ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
           itemViewHolder.contentTv.setText(mData.get(position).getContent());
           itemViewHolder.timeTv.setText(mData.get(position).getPublis_time());
           itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   mListener.onItemClick(view, mData.get(position));
               }
           });
        } else if (holder instanceof FooterViewHolder) {
            loadingAnimation((FooterViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        if (mData.isEmpty()) {
            return 1;
        } else{
            return mData.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData != null && mData.size() > 0 && null == mData.get(position)) {
           return VIEW_FOOTER;
        } else {
            return super.getItemViewType(position);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView contentTv;
        private TextView timeTv;
        private View itemView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            contentTv = itemView.findViewById(R.id.tv_content);
            timeTv = itemView.findViewById(R.id.tv_time);
        }
    }
}
