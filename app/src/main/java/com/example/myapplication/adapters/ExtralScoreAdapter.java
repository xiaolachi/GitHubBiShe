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
import com.example.myapplication.model.StudentInfoBean;

import java.util.ArrayList;
import java.util.List;

public class ExtralScoreAdapter extends BaseListAdapter {

    private List<ExtralScoreBean> mData;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;

    public ExtralScoreAdapter(ArrayList<ExtralScoreBean> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ExtralScoreBean bean);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, ExtralScoreBean bean);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_extral_score, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mData != null && mData.size() > 0 && holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mPage_index.setText(String.valueOf(position + 1));
            itemViewHolder.mTv_stu_id.setText(mData.get(position).getId());
            itemViewHolder.mTv_stu_account.setText(mData.get(position).getStu_account());
            itemViewHolder.mTv_stu_name.setText(mData.get(position).getName());
            itemViewHolder.mTv_awards_name.setText(mData.get(position).getAwards_name());
            itemViewHolder.mTv_awards_grade.setText(mData.get(position).getAwards_grade());
            itemViewHolder.mTv_match_time.setText(mData.get(position).getMatch_time());
            if (!TextUtils.isEmpty(mData.get(position).getExtral_score())) {
                itemViewHolder.mTv_is_look.setText("已审核");
                itemViewHolder.mTv_extral_score.setVisibility(View.VISIBLE);
                itemViewHolder.mBtn_look.setVisibility(View.INVISIBLE);
                itemViewHolder.mTv_extral_score.setText(mData.get(position).getExtral_score());
            } else {
                itemViewHolder.mTv_extral_score.setVisibility(View.GONE);
                itemViewHolder.mBtn_look.setVisibility(View.VISIBLE);
                itemViewHolder.mTv_is_look.setText("未审核");
            }

            itemViewHolder.mBtn_look.setOnClickListener(new View.OnClickListener() {
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

        private Button mBtn_look;
        private TextView mPage_index;
        private TextView mTv_stu_id;
        private TextView mTv_stu_account;
        private TextView mTv_stu_name;
        private TextView mTv_awards_name;
        private TextView mTv_awards_grade;
        private TextView mTv_match_time;
        private TextView mTv_extral_score;
        private TextView mTv_is_look;
        private View itemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            mBtn_look = (Button) itemView.findViewById(R.id.btn_look);
            mPage_index = (TextView) itemView.findViewById(R.id.page_index);
            mTv_stu_id = itemView.findViewById(R.id.tv_stu_id);
            mTv_stu_account = (TextView) itemView.findViewById(R.id.tv_stu_account);
            mTv_stu_name = (TextView) itemView.findViewById(R.id.tv_stu_name);
            mTv_awards_name = (TextView) itemView.findViewById(R.id.tv_awards_name);
            mTv_awards_grade = (TextView) itemView.findViewById(R.id.tv_awards_grade);
            mTv_match_time = (TextView) itemView.findViewById(R.id.tv_match_time);
            mTv_extral_score = (TextView) itemView.findViewById(R.id.tv_extral_score);
            mTv_is_look = itemView.findViewById(R.id.tv_is_look);
        }
    }
}
