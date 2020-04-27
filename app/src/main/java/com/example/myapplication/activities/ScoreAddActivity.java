package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.extensionview.ItemSingleCourseView;
import com.example.myapplication.model.SubmitScoreBean;
import com.example.myapplication.model.SubmitStuCourseScoreBean;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreAddActivity extends Activity {


    private EditText mEt_stu_account;
    private EditText mEt_average_credit;
    private EditText mEt_average_score;
    private EditText mEt_total_score;
    private TextView mSubmit_tv;
    private TextView mTvAddCourseItem;
    private ItemSingleCourseView mItemViewgroup;

    // End Of Content View Elements

    private void bindViews() {

        mEt_stu_account = (EditText) findViewById(R.id.et_stu_account);
        mEt_average_credit = findViewById(R.id.et_average_credit);
        mEt_average_score = findViewById(R.id.et_average_score);
        mEt_total_score = findViewById(R.id.et_total_score);
        mSubmit_tv = (TextView) findViewById(R.id.submit_tv);
        mTvAddCourseItem = findViewById(R.id.tv_add_course_item);
        mItemViewgroup = findViewById(R.id.item_viewgroup);
        mItemViewgroup.addSingleView(this, R.layout.item_single_course);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_add);
        bindViews();
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("添加学生成绩");
        mSubmit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stuAccount = mEt_stu_account.getText().toString();
                String averageCredit = mEt_average_credit.getText().toString(); //绩点
                String averageScore = mEt_average_score.getText().toString(); //平均分
                String totalScore = mEt_total_score.getText().toString();
                if (TextUtils.isEmpty(stuAccount)) {
                    UIutils.instance().toast("请输入学生号");
                    return;
                } else if (TextUtils.isEmpty(averageCredit)) {
                    UIutils.instance().toast("请输入课程绩点");
                    return;
                } else if (TextUtils.isEmpty(averageScore)) {
                    UIutils.instance().toast("请输入课程平均分");
                    return;
                } else if (TextUtils.isEmpty(totalScore)) {
                    UIutils.instance().toast("请输入课程总分");
                    return;
                } else {
                    List<SubmitStuCourseScoreBean> listCourse = mItemViewgroup.getList(stuAccount);
                    if (listCourse == null) {
                        return;
                    }
                    SubmitScoreBean submitScoreBean = new SubmitScoreBean();
                    submitScoreBean.setStuAccount(stuAccount);
                    submitScoreBean.setAverageCredit(averageCredit);
                    submitScoreBean.setAverageScore(averageScore);
                    submitScoreBean.setTotalScore(totalScore);
                    submitScoreBean.setListCourse(listCourse);
                    addStuScore(submitScoreBean);
                }
            }
        });
        mTvAddCourseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemViewgroup.addSingleView(ScoreAddActivity.this, R.layout.item_single_course);
            }
        });
    }

    private void addStuScore(SubmitScoreBean submitScoreBean) {
        String json = new Gson().toJson(submitScoreBean);
        Log.e("json-lyf", json);
        new SystemApi(this).addStuScore(json).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == LibConfig.SUCCESS_CODE) {
                            UIutils.instance().toast("添加成功");
                        }else {
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
