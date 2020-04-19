package com.example.myapplication.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.activities.ScoreDetailActivity;
import com.example.myapplication.adapters.AnnouncementAdapter;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.base.BaseListFragment;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.dialog.CommonDialog;
import com.example.myapplication.event.RefreshEvent;
import com.example.myapplication.model.AnnounceBean;
import com.example.myapplication.utils.LoginUtils;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

    private CommonDialog mCommonDialog;
    private Spinner mSpinner;
    private String mSelectType;
    private List<String> mSelects = new ArrayList<>();

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_announcement, container, false);
        setUpView();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onEvent(RefreshEvent event) {
        if (getUserVisibleHint()) {
            refresh();
        }
    }

    @Override
    public void initTitle() {
        super.initTitle();
        getAnnonYears();
        TextView titleTv = mView.findViewById(R.id.toolbar_title);
        titleTv.setText(R.string.announce);
        Button btnTrigger = mView.findViewById(R.id.btn_trigger);
        if (LibConfig.LOGIN_TYPE_OFFICE.equals(LoginUtils.getLoginType())) {
            btnTrigger.setVisibility(View.VISIBLE);
        }
        btnTrigger.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //增加
                commonDialog("添加公告", "add", null);
            }
        });
        mIsCreateView = true;
    }

    private void commonDialog(String title, String type, AnnounceBean bean) {
        mCommonDialog = new CommonDialog.Builder(getContext())
                .setTitle(title)
                .setContentView((ViewGroup) View.inflate(getContext(), R.layout.dialog_content_announce_add, null))
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ("add".equals(type)) {
                            addAnnounce();
                        } else if ("edit".equals(type)) {
                            editAnnounce();
                        }
                        dialogInterface.dismiss();
                    }
                })
                .create();
        mCommonDialog.show();
        changeSpinner();
        if (bean != null) {
            EditText contentET = mCommonDialog.findViewById(R.id.dialog_edit_content);
            contentET.setText(bean.getContent());
            mSpinner.setSelection(mSelects.indexOf(bean.getYears()));
        }
        CommonDialog.setDialogWindowAttr(mCommonDialog, getContext());
    }

    private void editAnnounce() {
    }

    public void changeSpinner() {
        mSpinner = mCommonDialog.findViewById(R.id.spinner);
        mSpinner.setDropDownWidth(600); //下拉宽度
        mSpinner.setDropDownHorizontalOffset(100); //下拉的横向偏移
        //mSpinner.setDropDownVerticalOffset(100); //下拉的纵向偏移

        //要去获取
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.item_select, mSelects);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectType = ((TextView) view).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getAnnonYears() {
        new SystemApi(getContext()).getAnnonYears().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            List<String> beans = new Gson().fromJson(data.toString(), new TypeToken<List<String>>() {
                            }.getType());
                            mSelects.addAll(beans);
                            Log.i("llllyllly", mSelects.get(0));
                            Log.i("llllyllly", beans.get(0));
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

    private void addAnnounce() {
        EditText contentET = mCommonDialog.findViewById(R.id.dialog_edit_content);
        new SystemApi(getContext()).addAnnounce(contentET.getText().toString(), mSelectType).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == LibConfig.SUCCESS_CODE) {
                           UIutils.instance().toast("添加成功");
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

    private void setUpView() {
        mRecyclerView = mView.findViewById(R.id.recycler);
        mSRefresh = mView.findViewById(R.id.swiprefresh);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AnnouncementAdapter(mData, new AnnouncementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, AnnounceBean bean) {
                if (view.getId() == R.id.announce_btn) {
                    //点击编辑公告
                    commonDialog("修改公告", "edit", bean);
                } else {
                    //点击进入公告详情页
                    Intent intent = new Intent(getContext(), ScoreDetailActivity.class);
                    intent.putExtra("years", bean.getYears());
                    startActivity(intent);
                }
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
        if (mIsCreateView && getUserVisibleHint() && mFirstLoading) {
            refresh();
            mFirstLoading = false;
        }
    }

    private void refresh() {
        mCurrentPage = 1;
        mData.clear();
        mAdapter.notifyDataSetChanged();
        getAnnouncementList();
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
        }, 0);
    }
}
