package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.base.BaseListAdapter;
import com.example.myapplication.model.StudentInfoBean;

import java.util.List;

public class MessageAdapter extends BaseListAdapter {

    private List<StudentInfoBean> mData;
    private OnItemClickListener mListener;

    public MessageAdapter(List<StudentInfoBean> data, OnItemClickListener listener) {
        this.mData = data;
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, StudentInfoBean bean);
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (mData != null && mData.size() > 0 && holder instanceof ItemViewHolder) {
           ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
           itemViewHolder.idTv.setText("" + (position+1));
           itemViewHolder.nameTv.setText(mData.get(position).getName());
           itemViewHolder.systemTv.setText(mData.get(position).getStu_system());
           itemViewHolder.profressionTv.setText(mData.get(position).getStu_class());
           itemViewHolder.classNumTv.setText(mData.get(position).getClass_number());

           itemViewHolder.lookDetailTv.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (mListener != null) {
                       mListener.onItemClick(view, mData.get(position));
                   }
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
        private TextView nameTv;
        private TextView systemTv;
        private TextView profressionTv;
        private TextView classNumTv;
        private TextView lookDetailTv;
        private TextView idTv;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            systemTv = itemView.findViewById(R.id.system_tv);
            profressionTv = itemView.findViewById(R.id.class_tv);
            classNumTv = itemView.findViewById(R.id.account_tv);
            lookDetailTv = itemView.findViewById(R.id.look_detail_tv);
            idTv = itemView.findViewById(R.id.id_tv);
        }
    }
}
