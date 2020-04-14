package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.model.StudentInfoBean;

public class StudentDetailActivity extends Activity {

    // Content View Elements
    private TextView mPage_index;
    private TextView mTv_stu_account;
    private TextView mTv_sex;
    private TextView mTv_stu_name;
    private TextView mTv_stu_system;
    private TextView mTv_stu_class;
    private TextView mTv_class_num;
    private TextView mTv_stu_card;
    private TextView mTv_credit;

    private StudentInfoBean bean;

    // End Of Content View Elements



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_detail);
        bindViews();
        TextView titleTv = findViewById(R.id.toolbar_title);
        titleTv.setText("成绩详情页");
        bean = getIntent().getParcelableExtra("studetail");

        mTv_stu_account.setText(bean.getStu_account());
        mTv_sex.setText(bean.getStu_sex());
        mTv_stu_name.setText(bean.getName());
        mTv_stu_system.setText(bean.getStu_system());
        mTv_stu_class.setText(bean.getStu_class());
        mTv_class_num.setText(bean.getClass_number());
        mTv_stu_card.setText(bean.getStu_card());
        mTv_credit.setText(bean.getStu_score());
    }

    private void bindViews() {
        mTv_stu_account = (TextView) findViewById(R.id.tv_stu_account);
        mTv_sex = (TextView) findViewById(R.id.tv_sex);
        mTv_stu_name = (TextView) findViewById(R.id.tv_stu_name);
        mTv_stu_system = (TextView) findViewById(R.id.tv_stu_system);
        mTv_stu_class = (TextView) findViewById(R.id.tv_stu_class);
        mTv_class_num = (TextView) findViewById(R.id.tv_class_num);
        mTv_stu_card = (TextView) findViewById(R.id.tv_stu_card);
        mTv_credit = (TextView) findViewById(R.id.tv_credit);
    }


}
