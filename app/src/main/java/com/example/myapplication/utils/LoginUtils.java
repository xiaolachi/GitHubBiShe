package com.example.myapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.model.LoginBean;
import com.example.myapplication.model.LoginStuBean;
import com.example.myapplication.model.StudentInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class LoginUtils {

    //跳转到登录页面去
    public static void toLoginActivity(Activity activity) {
        //清空sp
        SPUtils.getInstance().put(LibConfig.LOGIN_U_TYPE, "");
        SPUtils.getInstance().put(LibConfig.LOGIN_U_DATA, "");
        activity.finish();
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    //获取 sp data
    public static LoginBean getLoginOTData() {
        return new Gson().fromJson(SPUtils.getInstance().getString(LibConfig.LOGIN_U_DATA), new TypeToken<LoginBean>() {
        }.getType());
    }

    public static LoginStuBean getLoginStuData() {
        return new Gson().fromJson(SPUtils.getInstance().getString(LibConfig.LOGIN_U_DATA), new TypeToken<LoginStuBean>() {
        }.getType());
    }

    //获取 sp type
    public static String getLoginType() {
        return SPUtils.getInstance().getString(LibConfig.LOGIN_U_TYPE);
    }
}
