package com.example.myapplication.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.myapplication.activities.ScoreAddActivity;
import com.example.myapplication.activities.ScoreDetailActivity;
import com.example.myapplication.adapters.AnnouncementAdapter;
import com.example.myapplication.adapters.ScoreAdapter;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.base.BaseListFragment;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.dialog.CommonDialog;
import com.example.myapplication.model.StudentInfoBean;
import com.example.myapplication.model.StudentScoreBean;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreFragment extends BaseListFragment {

    private final String TAG = ScoreFragment.class.getSimpleName();
    protected int mCurrentPage = 1;
    protected int mPageSize = 15;
    protected Boolean mIsCreateView = false;
    protected Boolean mLoading = false;
    protected Boolean mFirstLoading = true;

    private View mView;

    private ScoreAdapter mAdapter = null;
    private List<StudentScoreBean> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSRefresh;
    private CommonDialog mLookUpAnnounceDialog;

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
        titleTv.setText(R.string.score);
        Button btnTrigger = mView.findViewById(R.id.btn_trigger);
        btnTrigger.setVisibility(View.VISIBLE);
        btnTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加成绩
                Intent intent = new Intent(getContext(), ScoreAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button btnLookup = mView.findViewById(R.id.btn_lookup);
        btnLookup.setVisibility(View.VISIBLE);
        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查询
                mLookUpAnnounceDialog = new CommonDialog.Builder(getContext())
                        .setTitle("查询公告")
                        .setContentView(View.inflate(getContext(), R.layout.dialog_message_look, null))
                        .setPositiveButton("查询", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lookUpScore();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                mLookUpAnnounceDialog.show();
            }
        });
        mIsCreateView = true;
    }

    private void lookUpScore() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
        addLoading();
        EditText uaccount = mLookUpAnnounceDialog.findViewById(R.id.et_message_look_up);
        new SystemApi(getContext()).lookUpScore(uaccount.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            ArrayList<StudentScoreBean> beans = new Gson().fromJson(data.toString(), new TypeToken<List<StudentScoreBean>>() {
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

    private void setUpView() {
        mRecyclerView = mView.findViewById(R.id.recycler);
        mSRefresh = mView.findViewById(R.id.swiprefresh);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ScoreAdapter(mData, new ScoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ArrayList<StudentScoreBean.ScorelistBean> scorelist) {
                //跳转到成绩详情页
                Log.i("lllllltt", scorelist.toString());
                Intent intent = new Intent(getContext(), ScoreDetailActivity.class);
                intent.putParcelableArrayListExtra("scorelist", scorelist);
                //Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("scorelist", scorelist);
                //intent.putExtras(bundle);
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
                        getScoreList();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            refresh();
        }
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
        getScoreList();
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

    private void getScoreList() {
        addLoading();
        new SystemApi(getContext()).getScoreList(String.valueOf(mCurrentPage), String.valueOf(mPageSize)).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            List<StudentScoreBean> beans = new Gson().fromJson(data.toString(), new TypeToken<List<StudentScoreBean>>() {
                            }.getType());
                            if (beans != null && beans.size() > 0) {
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
