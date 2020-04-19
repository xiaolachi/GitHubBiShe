package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.myapplication.model.ClassBean;
import com.example.myapplication.model.StudentInfoBean;
import com.example.myapplication.utils.SpinnerUtil;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StuInfoEditActivity extends Activity {

    // Content View Elements

    private EditText mEt_stu_account;
    private EditText mEt_gender;
    private EditText mEt_name;
    private Spinner mSpinnerSystem;
    private Spinner mSpinnerClass;
    private Spinner mSpinnerClassNum;
    private EditText mEt_stu_card;
    private EditText mEt_credit;
    private TextView mSubmit_tv;
    private EditText mEt_password;

    private String mSystem;
    private String mClass;
    private String mClassNum;

    private SpinnerUtil mSpinnerUtils;
    private StudentInfoBean mBean;
    private List<String> mSystemList;
    private List<String> mClassNumList;
    private List<String> mClassList;
    // End Of Content View Elements

    private void bindViews() {

        mEt_stu_account = (EditText) findViewById(R.id.et_stu_account);
        mEt_gender = (EditText) findViewById(R.id.et_gender);
        mEt_name = (EditText) findViewById(R.id.et_name);
        mSpinnerSystem = findViewById(R.id.spinner_system);
        mSpinnerClass = findViewById(R.id.spinner_class);
        mSpinnerClassNum = findViewById(R.id.spinner_class_num);
        mEt_stu_card = (EditText) findViewById(R.id.et_stu_card);
        mEt_credit = (EditText) findViewById(R.id.et_credit);
        mSubmit_tv = (TextView) findViewById(R.id.submit_tv);
        mEt_password = findViewById(R.id.et_password);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_info_edit);
        mSpinnerUtils = new SpinnerUtil();
        bindViews();
        getSystemClass();
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("添加学生");
        mSubmit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stuAccount = mEt_stu_account.getText().toString();
                String gender = mEt_gender.getText().toString();
                String name = mEt_name.getText().toString();
                String stuCard = mEt_stu_card.getText().toString();
                String credit = mEt_credit.getText().toString();
                String password = mEt_password.getText().toString();
                if (TextUtils.isEmpty(stuAccount)) {
                    UIutils.instance().toast("请输入学生号");
                } else if (TextUtils.isEmpty(password)) {
                    UIutils.instance().toast("请输入密码");
                } else if (TextUtils.isEmpty(gender)) {
                    UIutils.instance().toast("请输入性别");
                } else if (TextUtils.isEmpty(name)) {
                    UIutils.instance().toast("请输入学生姓名");
                } else if (TextUtils.isEmpty(stuCard)) {
                    UIutils.instance().toast("请输入学生卡号");
                } else if (TextUtils.isEmpty(credit)) {
                    UIutils.instance().toast("请输入学分");
                } else {
                    if (mBean == null) {
                        addStudent(password, stuAccount, gender, name, mSystem, mClass, mClassNum, stuCard, credit);
                    } else {
                        editAnnounce();
                    }
                }
            }
        });
    }

    private void editAnnounce() {
        new SystemApi(this).editAnnounce();
    }

    private void getSystemClass() {
        new SystemApi(this).getSystemClass().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            JSONArray data = jsonObject.optJSONArray("data");
                            List<ClassBean> beans = new Gson().fromJson(data.toString(), new TypeToken<List<ClassBean>>() {
                            }.getType());
                            setSpinner(beans);
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

    private void setSpinner(List<ClassBean> beans) {
        //system
        mSystemList = remindOnly(beans, "system");
        new SpinnerUtil().changeSpinner(this, mSpinnerSystem, mSystemList, new SpinnerUtil.CallBack() {
            @Override
            public void onItemSelected(TextView view) {
                mSystem = view.getText().toString();
            }
        });
        //class
        mClassList = remindOnly(beans, "class");
        new SpinnerUtil().changeSpinner(this, mSpinnerClass, mClassList, new SpinnerUtil.CallBack() {
            @Override
            public void onItemSelected(TextView view) {
                mClass = view.getText().toString();
            }
        });
        //classnum
        mClassNumList = remindOnly(beans, "class");
        new SpinnerUtil().changeSpinner(this, mSpinnerClassNum, mClassNumList, new SpinnerUtil.CallBack() {
            @Override
            public void onItemSelected(TextView view) {
                mClassNum = view.getText().toString();
            }
        });

        mBean = getIntent().getParcelableExtra("bean");
        if (mBean != null) {
            mEt_password.setVisibility(View.GONE);
            mEt_stu_account.setText(mBean.getStu_account());
            mEt_gender.setText(mBean.getStu_sex());
            mEt_name.setText(mBean.getName());
            mEt_stu_card.setText(mBean.getStu_card());
            mEt_credit.setText(mBean.getStu_score());

            mSpinnerSystem.setSelection(mSystemList.indexOf(mBean.getStu_system()));
            mSpinnerClass.setSelection(mClassList.indexOf(mBean.getStu_class()));
            mSpinnerClassNum.setSelection(mClassNumList.indexOf(mBean.getClass_number()));
        } else {
            mEt_password.setVisibility(View.VISIBLE);
        }
    }

    private List<String> remindOnly(List<ClassBean> beans, String type) {
        Set<String> systems = new HashSet<>();
        List<String> lists = new ArrayList<>();
        for (ClassBean bean : beans) {
            if (type.equals("system")) {
                systems.add(bean.getSystem());
            } else if (type.equals("class")) {
                systems.add(bean.getClassName());
            } else if (type.equals("classnum")) {
                systems.add(bean.getClassNUmber());
            }
        }
        lists.addAll(systems);
        return lists;
    }

    private void addStudent(String password, String stuAccount, String gender, String name, String system, String stuClass, String classNum, String stuCard, String credit) {
        new SystemApi(this).addStudent(LibConfig.LOGIN_TYPE_STUDENT, password, stuAccount, gender, name, system, stuClass, classNum, stuCard, credit).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            UIutils.instance().toast("添加成功");
                            setResult(2);
                            finish();
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
