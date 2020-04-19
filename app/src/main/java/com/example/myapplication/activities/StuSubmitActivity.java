package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.model.LoginStuBean;
import com.example.myapplication.model.StudentInfoBean;
import com.example.myapplication.utils.LoginUtils;
import com.example.myapplication.utils.UIutils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StuSubmitActivity extends Activity {

    private EditText mEt_stu_award;
    private EditText mEt_match_date;
    private EditText mEt_grade;
    private EditText mEt_extral_score;
    private TextView mSubmit_tv;
    private LoginStuBean mLoginStuData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_submit);
        bindViews();
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("附加项提交页");
        mLoginStuData = LoginUtils.getLoginStuData();

        mSubmit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stuAward = mEt_stu_award.getText().toString();
                String matchDate = mEt_match_date.getText().toString();
                String etGrade = mEt_grade.getText().toString();
                String extralScore = mEt_extral_score.getText().toString();
                List<String> strs = new ArrayList<String>() {
                    {
                        if (!(TextUtils.isEmpty(stuAward) || TextUtils.isEmpty(matchDate) || TextUtils.isEmpty(etGrade) || TextUtils.isEmpty(extralScore)))
                        {
                            add(stuAward);
                            add(matchDate);
                            add(etGrade);
                            add(extralScore);
                        }
                    }
                };
                if (strs.size() != 4) {
                    UIutils.instance().toast("请输入奖项相关信息");
                    return;
                }
                submitExtral(stuAward, matchDate, etGrade, extralScore);
            }
        });
    }


    private void submitExtral(String stuAward, String matchDate, String etGrade, String extralScore) {
        new SystemApi(this).submitExtral(mLoginStuData.getStu_accout(), stuAward, matchDate, etGrade, extralScore).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        String msg = jsonObject.optString("msg");
                        if (code == LibConfig.SUCCESS_CODE) {
                            UIutils.instance().toast("提交成功");
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

    private void bindViews() {

        mEt_stu_award = (EditText) findViewById(R.id.et_stu_award);
        mEt_match_date = (EditText) findViewById(R.id.et_match_date);
        mEt_grade = (EditText) findViewById(R.id.et_grade);
        mEt_extral_score = (EditText) findViewById(R.id.et_extral_score);
        mSubmit_tv = (TextView) findViewById(R.id.submit_tv);
    }
}
