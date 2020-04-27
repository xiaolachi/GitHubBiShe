package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MineInfoBean implements Parcelable {

    private LoginBean loginBean = null;
    private LoginStuBean studentInfoBean = null;

    public MineInfoBean(LoginBean loginBean, LoginStuBean studentInfoBean) {
        this.loginBean = loginBean;
        this.studentInfoBean = studentInfoBean;
    }

    protected MineInfoBean(Parcel in) {
        loginBean = in.readParcelable(LoginBean.class.getClassLoader());
        studentInfoBean = in.readParcelable(LoginStuBean.class.getClassLoader());
    }

    public static final Creator<MineInfoBean> CREATOR = new Creator<MineInfoBean>() {
        @Override
        public MineInfoBean createFromParcel(Parcel in) {
            return new MineInfoBean(in);
        }

        @Override
        public MineInfoBean[] newArray(int size) {
            return new MineInfoBean[size];
        }
    };

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public LoginStuBean getStudentInfoBean() {
        return studentInfoBean;
    }

    public void setStudentInfoBean(LoginStuBean studentInfoBean) {
        this.studentInfoBean = studentInfoBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(loginBean, i);
        parcel.writeParcelable(studentInfoBean, i);
    }

    @Override
    public String toString() {
        return "MineInfoBean{" +
                "loginBean=" + loginBean +
                ", studentInfoBean=" + studentInfoBean +
                '}';
    }
}
