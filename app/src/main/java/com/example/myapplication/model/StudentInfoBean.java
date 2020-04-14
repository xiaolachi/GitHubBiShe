package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentInfoBean implements Parcelable {


    /**
     * class_number : 1602
     * id : 16407301
     * name : 刘文强
     * stu_account : 16407301
     * stu_class : 网络工程
     * stu_score : 0.75
     * stu_sex : 男
     * stu_system : 计算机系
     */

    private String class_number;
    private int id;
    private String name;
    private String stu_account;
    private String stu_class;
    private String stu_score;
    private String stu_sex;
    private String stu_system;
    private String stu_card;

    public String getStu_card() {
        return stu_card;
    }

    public void setStu_card(String stu_card) {
        this.stu_card = stu_card;
    }

    protected StudentInfoBean(Parcel in) {
        class_number = in.readString();
        id = in.readInt();
        name = in.readString();
        stu_account = in.readString();
        stu_class = in.readString();
        stu_score = in.readString();
        stu_sex = in.readString();
        stu_system = in.readString();
        stu_card = in.readString();
    }

    public static final Creator<StudentInfoBean> CREATOR = new Creator<StudentInfoBean>() {
        @Override
        public StudentInfoBean createFromParcel(Parcel in) {
            return new StudentInfoBean(in);
        }

        @Override
        public StudentInfoBean[] newArray(int size) {
            return new StudentInfoBean[size];
        }
    };

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStu_account() {
        return stu_account;
    }

    public void setStu_account(String stu_account) {
        this.stu_account = stu_account;
    }

    public String getStu_class() {
        return stu_class;
    }

    public void setStu_class(String stu_class) {
        this.stu_class = stu_class;
    }

    public String getStu_score() {
        return stu_score;
    }

    public void setStu_score(String stu_score) {
        this.stu_score = stu_score;
    }

    public String getStu_sex() {
        return stu_sex;
    }

    public void setStu_sex(String stu_sex) {
        this.stu_sex = stu_sex;
    }

    public String getStu_system() {
        return stu_system;
    }

    public void setStu_system(String stu_system) {
        this.stu_system = stu_system;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(class_number);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(stu_account);
        parcel.writeString(stu_class);
        parcel.writeString(stu_score);
        parcel.writeString(stu_sex);
        parcel.writeString(stu_system);
        parcel.writeString(stu_card);
    }
}
