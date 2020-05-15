package com.example.myapplication.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.MimeDetalInfoActivity;
import com.example.myapplication.activities.ScoreDetailActivity;
import com.example.myapplication.activities.StuSubmitActivity;
import com.example.myapplication.api.SystemApi;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.dialog.CommonDialog;
import com.example.myapplication.model.LoginBean;
import com.example.myapplication.model.LoginStuBean;
import com.example.myapplication.model.MineInfoBean;
import com.example.myapplication.model.StudentInfoBean;
import com.example.myapplication.model.StudentScoreBean;
import com.example.myapplication.utils.LoginUtils;
import com.example.myapplication.utils.SPUtils;
import com.example.myapplication.utils.UIutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeFragment extends Fragment {

    private final String TAG = MeFragment.class.getSimpleName();
    private View mView;

    // Content View Elements

    private ImageView mH_back;
    private ImageView mH_head;
    private ImageView mUser_line;
    private TextView mUser_name;
    private TextView mUser_val;
    private TextView mBtnSignOut;
    private TextView mUpdatePassTv;
    private TextView mStuSubmitTv;
    private TextView mBtnSelfInfo;
    private TextView mBtnSelfScore;

    private CommonDialog mCommonDialog;

    private LoginBean loginBean = null;
    private LoginStuBean studentInfoBean = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();
        String type = LoginUtils.getLoginType();
        if (LibConfig.LOGIN_TYPE_STUDENT.equals(LoginUtils.getLoginType())) {
            studentInfoBean = LoginUtils.getLoginStuData();
        } else {
            loginBean = LoginUtils.getLoginOTData();
        }
        mUser_name.setText(loginBean == null ? Objects.requireNonNull(studentInfoBean).getName() : loginBean.getName());
        mUser_val.setText(loginBean == null ? studentInfoBean.getStu_accout() : loginBean.getAccount());

        mBtnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUtils.toLoginActivity((MainActivity) getActivity());
            }
        });

        mBtnSelfInfo = mView.findViewById(R.id.btn_self_info);
        mBtnSelfInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MimeDetalInfoActivity.class);
                Bundle bundle = new Bundle();
                MineInfoBean mineInfoBean = new MineInfoBean(loginBean, studentInfoBean);
                bundle.putParcelable("bean_info", mineInfoBean);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mUpdatePassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommonDialog = new CommonDialog.Builder(getContext())
                        .setTitle("修改密码")
                        .setContentView(View.inflate(getContext(), R.layout.dialog_update_pass, null))
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText oldPwd = mCommonDialog.findViewById(R.id.dialog_edit_old_pwd);
                                EditText newPwd = mCommonDialog.findViewById(R.id.dialog_edit_new_pwd);
                                new SystemApi(getContext()).updatePassWord(type, oldPwd.getText().toString(), mUser_val.getText().toString(), newPwd.getText().toString()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (null != response.body()) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response.body().string());
                                                int code = jsonObject.optInt("code");
                                                String msg = jsonObject.optString("msg");
                                                if (code == LibConfig.SUCCESS_CODE) {
                                                    UIutils.instance().toast("修改成功");
                                                } else {
                                                    UIutils.instance().toast(msg);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Log.i(TAG + "-----", "onResponse");
                                            }
                                        } else {
                                            UIutils.instance().toast("没有任何数据");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                mCommonDialog.show();
            }
        });

        //submit
        mStuSubmitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳到申报详情页
                Intent intent = new Intent(getContext(), StuSubmitActivity.class);
                startActivity(intent);
            }
        });

        //学生查看个人成绩
        mBtnSelfScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SystemApi(getContext()).lookUpScore(studentInfoBean.getStu_accout()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (null != response.body()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                int code = jsonObject.optInt("code");
                                if (code == LibConfig.SUCCESS_CODE) {
                                    JSONArray data = jsonObject.optJSONArray("data");
                                    ArrayList<StudentScoreBean> beans = new Gson().fromJson(data.toString(), new TypeToken<List<StudentScoreBean>>() {
                                    }.getType());
                                    if (beans != null && beans.size() > 0) {
                                        Intent intent = new Intent(getContext(), ScoreDetailActivity.class);
                                        intent.putParcelableArrayListExtra("scorelist", beans.get(0).getScorelist());
                                        startActivity(intent);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i(TAG + "-----", "onResponse");
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
        });
    }

    private void bindViews() {

        mH_back = (ImageView) mView.findViewById(R.id.h_back);
        mH_head = (ImageView) mView.findViewById(R.id.h_head);
        mUser_line = (ImageView) mView.findViewById(R.id.user_line);
        mUser_name = (TextView) mView.findViewById(R.id.user_name);
        mUser_val = (TextView) mView.findViewById(R.id.user_val);
        mBtnSignOut = mView.findViewById(R.id.btn_sign_out);
        mUpdatePassTv = mView.findViewById(R.id.update_pass_tv);
        mStuSubmitTv = mView.findViewById(R.id.stu_submit_tv);
        mBtnSelfScore = mView.findViewById(R.id.btn_self_score);
        if (LibConfig.LOGIN_TYPE_STUDENT.equals(LoginUtils.getLoginType())) {
            mStuSubmitTv.setVisibility(View.VISIBLE);
            mBtnSelfScore.setVisibility(View.VISIBLE);
        } else {
            mStuSubmitTv.setVisibility(View.GONE);
            mBtnSelfScore.setVisibility(View.GONE);
        }
    }
}
