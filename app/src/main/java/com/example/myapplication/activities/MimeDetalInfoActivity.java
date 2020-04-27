package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.model.MineInfoBean;

public class MimeDetalInfoActivity extends Activity {

    private MineInfoBean mMimeInfoBean;

    private androidx.cardview.widget.CardView mCard_stu;
    private TextView mPage_index;
    private TextView mTv_stu_account;
    private TextView mTv_sex;
    private TextView mTv_stu_name;
    private TextView mTv_stu_system;
    private TextView mTv_stu_class;
    private TextView mTv_class_num;
    private TextView mTv_stu_card;
    private androidx.cardview.widget.CardView mCard_other;
    private TextView mTv_id;
    private TextView mTv_other_name;
    private TextView mTv_phone;

    private void bindViews() {

        mCard_stu = (androidx.cardview.widget.CardView) findViewById(R.id.card_stu);
        mPage_index = (TextView) findViewById(R.id.page_index);
        mTv_stu_account = (TextView) findViewById(R.id.tv_stu_account);
        mTv_sex = (TextView) findViewById(R.id.tv_sex);
        mTv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
        mTv_stu_system = (TextView) findViewById(R.id.tv_stu_system);
        mTv_stu_class = (TextView) findViewById(R.id.tv_stu_class);
        mTv_class_num = (TextView) findViewById(R.id.tv_class_num);
        mTv_stu_card = (TextView) findViewById(R.id.tv_stu_card);

        mCard_other = (androidx.cardview.widget.CardView) findViewById(R.id.card_other);
        mTv_id = (TextView) findViewById(R.id.tv_mime_id);
        mTv_other_name = (TextView) findViewById(R.id.tv_other_name);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);
        bindViews();
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("个人详情页");

        mMimeInfoBean = getIntent().getParcelableExtra("bean_info");
        Log.i("iioo", mMimeInfoBean.toString());

        if (mMimeInfoBean != null) {
            if (mMimeInfoBean.getLoginBean() != null) {
                mCard_other.setVisibility(View.VISIBLE);
                mCard_stu.setVisibility(View.GONE);
                mTv_other_name.setText(mMimeInfoBean.getLoginBean().getName());
                mTv_phone.setText(mMimeInfoBean.getLoginBean().getAccount());
            } else if(mMimeInfoBean.getStudentInfoBean() != null){
                mCard_other.setVisibility(View.GONE);
                mCard_stu.setVisibility(View.VISIBLE);
                mPage_index.setText(String.valueOf(mMimeInfoBean.getStudentInfoBean().getId()));
                mTv_stu_account.setText(mMimeInfoBean.getStudentInfoBean().getStu_accout() == null? "" : mMimeInfoBean.getStudentInfoBean().getStu_accout());
                mTv_sex.setText(mMimeInfoBean.getStudentInfoBean().getStu_sex());
                mTv_stu_name.setText(mMimeInfoBean.getStudentInfoBean().getName());
                mTv_stu_system.setText(mMimeInfoBean.getStudentInfoBean().getStu_system());
                mTv_stu_class.setText(mMimeInfoBean.getStudentInfoBean().getStu_class());
                mTv_class_num.setText(mMimeInfoBean.getStudentInfoBean().getClass_number());
                mTv_stu_card.setText(mMimeInfoBean.getStudentInfoBean().getStu_Card());
            }
        }
    }
}
