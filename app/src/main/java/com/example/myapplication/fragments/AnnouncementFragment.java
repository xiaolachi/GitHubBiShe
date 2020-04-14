package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.ScoreDetailActivity;
import com.example.myapplication.adapters.AnnouncementAdapter;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.base.BaseListAdapter;
import com.example.myapplication.base.BaseListFragment;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.model.AnnounceBean;
import com.example.myapplication.model.StudentInfoBean;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnnouncementFragment extends BaseListFragment {

    private final String TAG = AnnouncementFragment.class.getSimpleName();
    private View mView;

    private AnnouncementAdapter mAdapter = null;
    private List<AnnounceBean> mData = new ArrayList();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSRefresh;

    protected int mCurrentPage = 1;
    protected int mPageSize = 15;
    protected Boolean mIsCreateView = false;
    protected Boolean mLoading = false;
    protected Boolean mFirstLoading = true;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_announcement, container, false);
        setUpView();
        return mView;
    }

    @Override
    public void initTitle() {
        super.initTitle();
        TextView titleTv = mView.findViewById(R.id.toolbar_title);
        titleTv.setText(R.string.announce);
        mIsCreateView = true;
    }

    private void setUpView() {
        mRecyclerView = mView.findViewById(R.id.recycler);
        mSRefresh = mView.findViewById(R.id.swiprefresh);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AnnouncementAdapter(mData, new AnnouncementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, AnnounceBean bean) {
                //点击进入公告详情页
                Intent intent = new Intent(getContext(), ScoreDetailActivity.class);
                intent.putExtra("years", bean.getYears());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!mLoading && mData.size() >= mPageSize && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    int itemCount = linearLayoutManager.getItemCount();
                    if (lastItemPosition == itemCount - 1) {
                        getAnnouncementList();
                    }
                }

            }
        });
        mSRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mLoading) {
                    refresh();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getAnnouncementList();
    }

    private void refresh() {
        mCurrentPage = 1;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        getAnnouncementList();
        mSRefresh.setRefreshing(false);
    }

    private void addLoading() {
        mLoading = true;
        mData.add(mData.size(), null);
        mAdapter.notifyItemInserted(mData.size() - 1);
    }

    private void removeLoading() {
        mLoading = false;
        int index = mData.indexOf(null);
        mData.remove(index);
        mAdapter.notifyItemRemoved(index);
    }

    private void getAnnouncementList() {
        addLoading();
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = mCurrentPage - 1; i < mPageSize; i++) {
//                    mData.add("卫含晨 " + (i+1));
//                }
//                mCurrentPage++;
//                removeLoading();
//            }
//        }, 2000);

        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                new SystemApi(getContext()).getAnnounceList(String.valueOf(mCurrentPage), String.valueOf(mPageSize)).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (null != response.body()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                int code = jsonObject.optInt("code");
                                if (code == LibConfig.SUCCESS_CODE) {
                                    JSONArray data = jsonObject.optJSONArray("data");
                                    List<AnnounceBean> beans = new Gson().fromJson(data.toString(), new TypeToken<List<AnnounceBean>>() {
                                    }.getType());
                                    if (beans != null && beans.size() > 0) {
                                        mData.addAll(beans);
                                        mCurrentPage++;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i(TAG +"-----", "onResponse");
                            }
                        } else {
                            UIutils.instance().toast("没有任何数据");
                        }
                        removeLoading();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i(TAG +"-----", "onFailure：" + t);
                        removeLoading();
                    }
                });
            }
        }, 100);
    }
}
