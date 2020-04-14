package com.example.myapplication.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.model.StudentScoreBean;

import java.util.ArrayList;

public class ScoreDetailActivity extends Activity {

    private WebView mWvContent;
    private ImageView mLoading;
    private ArrayList<StudentScoreBean.ScorelistBean> beans;
    private String mYears;
    private String mUrl = "http://192.168.0.107:8080/BiSheServer/jsp/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);
        mWvContent = findViewById(R.id.mwv_content);
        mLoading = findViewById(R.id.loading_img);
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("成绩详情页");
        beans = getIntent().getParcelableArrayListExtra("scorelist");
        mYears = getIntent().getStringExtra("years");
        if (beans != null) {
            mUrl += "score.jsp?uaccount=" + beans.get(0).getStu_accout();
        }
        if (mYears != null) {
            mUrl += "scholar.jsp?years=" + mYears;
        }
        initListener();
    }

    public void loadingAnimation() {
        mLoading.setVisibility(View.VISIBLE);
        AnimationDrawable loadAnimation = (AnimationDrawable) mLoading.getDrawable();
        loadAnimation.start();
    }

    public void initListener() {


        WebSettings settings = mWvContent.getSettings();
        //设置true,才能让Webivew支持<meta>标签的viewport属性
        settings.setUseWideViewPort(true);//设置可以支持缩放
        settings.setSupportZoom(true);//设置出现缩放工具
        settings.setBuiltInZoomControls(true);//设定缩放控件隐藏
        settings.setDisplayZoomControls(false);//最小缩放等级
        //settings.setInitialScale(25);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWvContent.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                loadingAnimation();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mLoading.setVisibility(View.GONE);
                //String json = new Gson().toJson(beans);
                //mWvContent.loadUrl("javascript:score(" + json + ", " + beans.size() + ")");
            }
        });

        mWvContent.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //加载进度
            }
        });

        mWvContent.loadUrl(mUrl);
        //mWvContent.loadUrl("file:///android_asset/score/index.html");

        mWvContent.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {  //手机菜单的返回键
                if (keyCode == KeyEvent.KEYCODE_BACK && mWvContent.canGoBack()) {  //表示按返回键
                    mWvContent.goBack();   //后退
                    return true;    //已处理
                }
            }
            return false;
        });
    }

}
