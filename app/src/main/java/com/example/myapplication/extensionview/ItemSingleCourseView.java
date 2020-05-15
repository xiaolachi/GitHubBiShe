package com.example.myapplication.extensionview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.model.SubmitScholarBean;
import com.example.myapplication.model.SubmitStuCourseScoreBean;
import com.example.myapplication.utils.UIutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSingleCourseView extends LinearLayout {

    private Map<Integer, View>  views = new HashMap<>();

    private int i = 0;

    public ItemSingleCourseView(Context context) {
        this(context, null);
    }

    public ItemSingleCourseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemSingleCourseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addSingleView(Context context, int redId) {
        View singleView = inflate(context, redId, null);
        addView(singleView, getChildCount());
        views.put(i++, singleView);

        singleView.findViewById(R.id.ib_delete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(singleView);
            }
        });
    }

    public List<SubmitStuCourseScoreBean> getList(String stuAccount) {
        List<SubmitStuCourseScoreBean> listScore = new ArrayList<>();
        List<SubmitScholarBean> listAwards = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout liner = (LinearLayout) getChildAt(i);
            EditText eCourse = liner.findViewById(R.id.et_course);
            EditText eCourseScore = liner.findViewById(R.id.et_course_score);
            if (TextUtils.isEmpty(eCourse.getText().toString()) || TextUtils.isEmpty(eCourseScore.getText().toString())) {
                UIutils.instance().toast("请完整输入课程或课程分数");
                return null;
            }
            SubmitStuCourseScoreBean bean = new SubmitStuCourseScoreBean();
            bean.setCourse(eCourse.getText().toString());
            bean.setCourse_score(eCourseScore.getText().toString());
            bean.setStu_account(stuAccount);
            listScore.add(bean);
        }
        return listScore;
    }

    public List<SubmitScholarBean> getList() {
        List<SubmitScholarBean> listAwards = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            LinearLayout liner = (LinearLayout) getChildAt(i);
            EditText eAccount = liner.findViewById(R.id.et_awards_stu_account);
            EditText eJiangType = liner.findViewById(R.id.et_jiang_type);
            EditText eJiangNumber = liner.findViewById(R.id.et_jiang_number);
            if (TextUtils.isEmpty(eAccount.getText().toString()) || TextUtils.isEmpty(eJiangType.getText().toString()) || TextUtils.isEmpty(eJiangNumber.getText().toString())) {
                UIutils.instance().toast("请完整输入");
                return null;
            }
            SubmitScholarBean submitScholarBean = new SubmitScholarBean();
            submitScholarBean.setStuAccount(eAccount.getText().toString());
            submitScholarBean.setJiangjinType(eJiangType.getText().toString());
            submitScholarBean.setJiangjinNUmber(eJiangNumber.getText().toString());
            listAwards.add(submitScholarBean);
        }
        return listAwards;
    }
}
