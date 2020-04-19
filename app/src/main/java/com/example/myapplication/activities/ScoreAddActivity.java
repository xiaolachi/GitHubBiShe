package com.example.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.api.SystemApi;

public class ScoreAddActivity extends Activity {


    private EditText mEt_stu_account;
    private EditText mEt_name;
    private Spinner mSpinner_system;
    private Spinner mSpinner_class;
    private View mView_class_divider;
    private Spinner mSpinner_class_num;
    private View mView_num_divider;
    private EditText mEt_average_credit;
    private EditText mEt_average_score;
    private EditText mEt_total_score;
    private EditText mEt_course;
    private EditText mEt_course_score;
    private EditText mEt_credit;
    private TextView mSubmit_tv;

    // End Of Content View Elements

    private void bindViews() {

        mEt_stu_account = (EditText) findViewById(R.id.et_stu_account);
        mEt_name = (EditText) findViewById(R.id.et_name);
        mSpinner_system = (Spinner) findViewById(R.id.spinner_system);
        mSpinner_class = (Spinner) findViewById(R.id.spinner_class);
        mView_class_divider = (View) findViewById(R.id.view_class_divider);
        mSpinner_class_num = (Spinner) findViewById(R.id.spinner_class_num);
        mView_num_divider = (View) findViewById(R.id.view_num_divider);
        mEt_average_credit = (EditText) findViewById(R.id.et_average_credit);
        mEt_average_score = (EditText) findViewById(R.id.et_average_score);
        mEt_total_score = (EditText) findViewById(R.id.et_total_score);
        mEt_course = (EditText) findViewById(R.id.et_course);
        mEt_course_score = (EditText) findViewById(R.id.et_course_score);
        mEt_credit = (EditText) findViewById(R.id.et_credit);
        mSubmit_tv = (TextView) findViewById(R.id.submit_tv);
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
                addStuScore();
            }
        });
    }

    private void addStuScore() {
        new SystemApi(this).addStuScore();
    }
}
