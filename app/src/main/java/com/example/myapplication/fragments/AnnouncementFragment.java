package com.example.myapplication.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private CommonDialog mLookUpAnnounceDialog;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载的视图
        mView = inflater.inflate(R.layout.fragment_announcement, container, false);
        //配置滑动列表以及它的适配器
        setUpView();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent event) {
        if (getUserVisibleHint()) {
            refresh();
        }
    }

    @Override
    public void initTitle() {
        super.initTitle();
        //打开添加奖学金公告时下拉列表中的数据
        getAnnonYears();
        //title
        TextView titleTv = mView.findViewById(R.id.toolbar_title);
        titleTv.setText(R.string.announce);
        //添加
        Button btnTrigger = mView.findViewById(R.id.btn_trigger);
        //查询
        Button btnLookup = mView.findViewById(R.id.btn_lookup);
        btnLookup.setVisibility(View.VISIBLE);
        if (LibConfig.LOGIN_TYPE_OFFICE.equals(LoginUtils.getLoginType())) {
            btnTrigger.setVisibility(View.VISIBLE);
        }
        //添加的点击事件
        btnTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加
                CommonDialog commonDialog = new CommonDialog.Builder(getContext())
                        .setTitle("选择公告类型")
                        .setNegativeButton("普通公告", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                commonDialog("添加公告", "common", null);
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("奖学金公告", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                commonDialog("添加公告", "add", null);
                            }
                        }).create();
                commonDialog.show();
                commonDialog.setCanceledOnTouchOutside(true);
            }
        });
        //查询的点击事件
        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLookUpAnnounceDialog = new CommonDialog.Builder(getContext())
                        .setTitle("查询公告")
                        .setContentView(View.inflate(getContext(), R.layout.dialog_announc_look, null))
                        .setPositiveButton("查询", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lookUpAnnounce();
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

    private void lookUpAnnounce() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
        addLoading();
        EditText contentKey = mLookUpAnnounceDialog.findViewById(R.id.et_announce_look_up);
        new SystemApi(getContext()).lookUpAnnounce(contentKey.getText().toString()).enqueue(new Callback<ResponseBody>() {
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

    /**
     * 添加 普通公告 或者奖学金公告的弹框
     * @param title  标题
     * @param type   公告的类型  普通 ｜ 奖学金   -> 编辑 ｜ 添加
     * @param bean   公告的数据，  当你点击的是编辑， 携带的数据， 显示在弹框上
     */
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
                            //添加奖学金公告
                            addAnnounce();
                        } else if ("edit".equals(type)) {
                            //编辑奖学金公告
                            editAnnounce(bean.getId());
                        } else if ("common".equals(type)) {
                            //添加一个普通公告
                            mSelectType = "";
                            addAnnounce();
                        } else if ("commonedit".equals(type)) {
                            //编辑一个普通公告
                            mSelectType = "";
                            editAnnounce(bean.getId());
                        }
                        dialogInterface.dismiss();
                    }
                })
                .create();
        mCommonDialog.show();
        //配置奖学金弹框中  选择事件的下拉列表
        changeSpinner();
        //隐藏下拉列表
        if ("common".equals(type) || "commonedit".equals(type)) {
            mSpinner.setVisibility(View.GONE);
        } else {
            mSpinner.setVisibility(View.VISIBLE);
        }
        //如果是编辑操作，将携带来的数据显示在弹框中
        if (bean != null) {
            EditText contentET = mCommonDialog.findViewById(R.id.dialog_edit_content);
            contentET.setText(bean.getContent());
            mSpinner.setSelection(mSelects.indexOf(bean.getYears()));
        }
    }

    private void editAnnounce(String id) {
        //编辑
        EditText contentET = mCommonDialog.findViewById(R.id.dialog_edit_content);
        new SystemApi(getContext()).editScholarAnnounce(id, mSelectType, contentET.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == LibConfig.SUCCESS_CODE) {
                            UIutils.instance().toast("编辑成功");
                            refresh();
                        } else {
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

    /*
     * 添加公告
     */
    private void addAnnounce() {
        EditText contentET = mCommonDialog.findViewById(R.id.dialog_edit_content);
        //new SystemApi(getContext())  配置网络请求的引擎
        //.addAnnounce(contentET.getText().toString(), mSelectType) 进行网络请求
        //.enqueue  真正的将网络请求发送给服务器   并且这是一个异步请求
        new SystemApi(getContext()).addAnnounce(contentET.getText().toString(), mSelectType).enqueue(
                //new Callback<ResponseBody>() 相当于匿名方法， 也就是回调
                new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //请求成功
                //判断请求成功后，服务器是否返回了数据
                if (null != response.body()) {
                    try {
                        //将你的请求体 转成一个json对象
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        //拿到你的请求码
                        int code = jsonObject.optInt("code");
                        //拿到你的请求信息  添加成功，  添加失败
                        String msg = jsonObject.optString("msg");
                        if (code == LibConfig.SUCCESS_CODE) {  //请求码正确

                            UIutils.instance().toast("添加成功");
                            //刷新当前的 页面，，  添加  新添加的那一个放到了数据库的最后一行
                            //但是我们拿的是数据库列表的前十五行，， 所有就看不见
                            //你数据库有  15条  -> 16 -> 15  -> 1
                            refresh();
                        } else {
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
               //请求失败
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
                    if (!TextUtils.isEmpty(bean.getYears())) {
                        commonDialog("修改公告", "edit", bean);
                    } else {
                        commonDialog("修改公告", "commonedit", bean);
                    }
                } else {
                    //点击进入公告详情页
                    if (!TextUtils.isEmpty(bean.getYears())) {
                        Intent intent = new Intent(getContext(), ScoreDetailActivity.class);
                        intent.putExtra("years", bean.getYears());
                        startActivity(intent);
                    }
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
        //重新获取数据
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
