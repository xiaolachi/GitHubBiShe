package com.example.myapplication.activities;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.api.LoginApi;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.utils.SPUtils;
import com.example.myapplication.utils.UIutils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private Spinner mSpinner;
    private TextView mLoginTv;
    private EditText mUserNameEt;
    private EditText mPassWordEt;

    private String mSelectType = "学生";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSpinner = findViewById(R.id.spinner);
        mLoginTv = findViewById(R.id.tv_login);
        mUserNameEt = findViewById(R.id.et_mobile_number);
        mPassWordEt = findViewById(R.id.et_password);
        changeSpinner();
        mLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUserNameEt.getText().toString();
                String passWord = mPassWordEt.getText().toString();
                //UIutils.instance().toast("点击---"+userName+"---"+passWord);
                if (TextUtils.isEmpty(userName)) {
                    UIutils.instance().toast("请输入用户名");
                    return;
                } else if (TextUtils.isEmpty(passWord)) {
                    UIutils.instance().toast("请输入密码");
                    return;
                } else {
                    login(userName, passWord);
                }
            }
        });
    }

    private void login(String userName, String passWord) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uaccount", userName)
                .addFormDataPart("upass", passWord)
                .addFormDataPart("utype", mSelectType)
                .build();
        new SystemApi(this).login(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != response.body()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.optInt("code");
                        if (code == LibConfig.SUCCESS_CODE) {
                            UIutils.instance().toast("登录成功");
                            //将 utype  存起来， 后面各个界面  通过他去控制  退出时要清除
                            SPUtils.getInstance().put(LibConfig.LOGIN_U_TYPE, mSelectType);
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("LoginActivity-----", "onResponse");
                    }
                } else {
                    UIutils.instance().toast("没有任何数据");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("LoginActivity-----", "onFailure：" + t);
            }
        });
    }

    /**
     * Spinner自定义样式
     * 1、Spinner内的TextView样式：item_select
     * 2、Spinner下拉中每个item的TextView样式：item_drop
     * 3、Spinner下拉框样式，属性设置
     */
    public void changeSpinner() {
        mSpinner.setDropDownWidth(600); //下拉宽度
        mSpinner.setDropDownHorizontalOffset(100); //下拉的横向偏移
        //mSpinner.setDropDownVerticalOffset(100); //下拉的纵向偏移

        String[] spinnerItems = {"学生", "教师", "教务处"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.item_select, spinnerItems);
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
}
