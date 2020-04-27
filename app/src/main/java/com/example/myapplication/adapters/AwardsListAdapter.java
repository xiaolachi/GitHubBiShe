package com.example.myapplication.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseListAdapter;
import com.example.myapplication.model.ExtralScoreBean;

import java.util.ArrayList;
import java.util.List;

public class AwardsListAdapter extends BaseListAdapter {

    private List<String> mData;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public AwardsListAdapter(ArrayList<String> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String years);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, String years);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_awards_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mData != null && mData.size() > 0 && holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mTvContent.setText(mData.get(position) + " 年度奖学金列表");


            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(view, mData.get(position));
                    }
                }
            });

            itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mLongListener != null) {
                        mLongListener.onItemLongClick(view, mData.get(position));
                    }
                    return true;
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            loadingAnimation((FooterViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
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

        private TextView mTvContent;
        private View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            mTvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
