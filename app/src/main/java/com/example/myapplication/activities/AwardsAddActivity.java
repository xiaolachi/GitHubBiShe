package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.extensionview.ItemSingleCourseView;
import com.example.myapplication.model.SubmitScholarBean;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AwardsAddActivity extends Activity {

    private ItemSingleCourseView mItem_viewgroup;
    private TextView mTv_add_scholar_item;
    private TextView mSubmit_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards_add);
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
