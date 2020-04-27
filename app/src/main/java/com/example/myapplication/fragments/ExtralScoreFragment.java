package com.example.myapplication.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.activities.StuInfoEditActivity;
import com.example.myapplication.activities.StudentDetailActivity;
import com.example.myapplication.adapters.ExtralScoreAdapter;
import com.example.myapplication.adapters.MessageAdapter;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.base.BaseListFragment;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.dialog.CommonDialog;
import com.example.myapplication.model.ExtralScoreBean;
import com.example.myapplication.model.StudentInfoBean;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtralScoreFragment extends BaseListFragment {

    protected int mCurrentPage = 1;
    protected int mPageSize = 15;
    protected Boolean mIsCreateView = false;
    protected Boolean mLoading = false;
    protected Boolean mFirstLoading = true;

    private final String TAG = ExtralScoreFragment.class.getSimpleName();
    private View mView;

    private ExtralScoreAdapter mAdapter = null;
    private ArrayList<ExtralScoreBean> mData = new ArrayList();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSRefresh;
    private CommonDialog mCommonDialog;

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
        titleTv.setText(R.string.extralscore);
        mIsCreateView = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            refresh();
        }
    }

    private void setUpView() {
        mRecyclerView = mView.findViewById(R.id.recycler);
        mSRefresh = mView.findViewById(R.id.swiprefresh);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ExtralScoreAdapter(mData, new ExtralScoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ExtralScoreBean bean) {
                mCommonDialog = new CommonDialog.Builder(getContext())
                        .setTitle("审查附加项")
                        .setContentView(View.inflate(getContext(), R.layout.dialog_look_extral, null))
                        .setPositiveButton("通过", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //进行提交
                                postExtalScore(bean.getId() );
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                mCommonDialog.show();
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
                        getMessageList();
                    }
                }

            }
        });
        mSRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSRefresh.setRefreshing(false);
                if (!mLoading) {
                    mSRefresh.setRefreshing(true);
                    refresh();
                    mSRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void postExtalScore(String id) {
        EditText extralscore = mCommonDialog.findViewById(R.id.dialog_edit_extral_score);
        new SystemApi(getContext()).postExtalScore(id, extralscore.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == LibConfig.SUCCESS_CODE) {
                            UIutils.instance().toast("审阅成功");
                            refresh();
                        }else {
                            UIutils.instance().toast(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG + "-----", "onResponse");
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mIsCreateView && isVisibleToUser && mFirstLoading) {
            refresh();
            mFirstLoading = false;
        }
    }

    private void refresh() {
        mCurrentPage = 1;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        getMessageList();
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

    private void getMessageList() {
        addLoading();
        new SystemApi(getContext()).getExtralScoreList(String.valueOf(mCurrentPage), String.valueOf(mPageSize)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            ArrayList<ExtralScoreBean> beans = new Gson().fromJson(data.toString(), new TypeToken<List<ExtralScoreBean>>() {
                            }.getType());
                            if (beans != null && beans.size() > 0) {
                                Collections.reverse(beans);
                                mData.addAll(beans);
                                mCurrentPage++;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i(TAG + "-----", "onResponse");
                    }
                } else {
                    UIutils.instance().toast("没有任何数据");
                }
                removeLoading();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG + "-----", "onFailure：" + t);
                removeLoading();
            }
        });
    }
}
