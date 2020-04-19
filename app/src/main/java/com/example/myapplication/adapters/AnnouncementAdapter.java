package com.example.myapplication.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.base.BaseListAdapter;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.event.RefreshEvent;
import com.example.myapplication.model.AnnounceBean;
import com.example.myapplication.model.LoginStuBean;
import com.example.myapplication.utils.LoginUtils;
import com.example.myapplication.utils.UIutils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

            if (LibConfig.LOGIN_TYPE_OFFICE.equals(LoginUtils.getLoginType())) {
                itemViewHolder.announceBtn.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.announceBtn.setVisibility(View.GONE);
            }
            itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mData.size() <= 0 || LibConfig.LOGIN_TYPE_STUDENT.equals(LoginUtils.getLoginType()) || LibConfig.LOGIN_TYPE_TEACHER.equals(LoginUtils.getLoginType())) {
                        return false;
                    }
                    delAnnounce(mData.get(position).getYears());
                    return true;
                }
            });
            itemViewHolder.announceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(view, mData.get(position));
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            loadingAnimation((FooterViewHolder) holder);
        }
    }

    private void delAnnounce(String years) {
        new SystemApi(MyApplication.getApp().getApplicationContext()).delAnnounce(years).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            UIutils.instance().toast("删除成功");
                            EventBus.getDefault().post(new RefreshEvent());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    UIutils.instance().toast("没有任何数据");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


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
        private TextView contentTv;
        private TextView timeTv;
        private View itemView;
        private Button announceBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            contentTv = itemView.findViewById(R.id.tv_content);
            timeTv = itemView.findViewById(R.id.tv_time);
            announceBtn = itemView.findViewById(R.id.announce_btn);
        }
    }
}
