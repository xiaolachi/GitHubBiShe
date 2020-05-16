package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.extensionview.ItemSingleCourseView;
import com.example.myapplication.model.AnnounceBean;
import com.example.myapplication.model.SubmitScholarBean;
import com.example.myapplication.model.ThirdStuBean;
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

public class AwardsAddActivity extends Activity {

    private ItemSingleCourseView mItem_viewgroup;
    private TextView mTv_add_scholar_item;
    private TextView mSubmit_tv;
    private LinearLayout mScoreGradeLy;

    private List<ThirdStuBean> mLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards_add);
        getScoreGradeList();
        mItem_viewgroup = findViewById(R.id.item_viewgroup);
        mItem_viewgroup.addSingleView(this, R.layout.item_award_add);
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("添加奖学金列表");
        mTv_add_scholar_item = findViewById(R.id.tv_add_scholar_item);
        mTv_add_scholar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItem_viewgroup.addSingleView(AwardsAddActivity.this, R.layout.item_award_add);
            }
        });

        mSubmit_tv = findViewById(R.id.submit_tv);
        mSubmit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SubmitScholarBean> list = mItem_viewgroup.getList();
                if (list == null) {
                    return;
                }
                addScholar(list);
            }
        });

        mScoreGradeLy = findViewById(R.id.score_list_ly);
    }

    private void getScoreGradeList() {
        new SystemApi(this).getScoreGradeList().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            List<ThirdStuBean> beans = new Gson().fromJson(data.toString(), new TypeToken<List<ThirdStuBean>>() {
                            }.getType());
                            if (beans != null && beans.size() > 0) {
                                mLists.addAll(beans);
                                mScoreGradeLy.removeAllViews();
                                for (ThirdStuBean bean : mLists) {
                                    LinearLayout ly = (LinearLayout) View.inflate(AwardsAddActivity.this, R.layout.score_grade_list_item, null);
                                    TextView tv0 = (TextView) ly.getChildAt(0);
                                    tv0.setText(bean.getOrder());
                                    TextView tv1 = (TextView) ly.getChildAt(1);
                                    tv1.setText(bean.getStu_account());
                                    TextView tv2 = (TextView) ly.getChildAt(2);
                                    tv2.setText(bean.getYears());
                                    mScoreGradeLy.addView(ly);
                                }
                            }
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

    private void addScholar(List<SubmitScholarBean> list) {
        String json = new Gson().toJson(list);
        new SystemApi(this).addScholar(json).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == LibConfig.SUCCESS_CODE) {
                            setResult(2);
                            finish();
                        } else {
                            UIutils.instance().toast(msg);
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
}
