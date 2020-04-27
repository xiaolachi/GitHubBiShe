package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ExtralScoreBean implements Parcelable {


    /**
     * awards_grade : 一等奖
     * awards_name : 校园歌手大赛
     * extral_score : 5
     * id : 1
     * match_time : 2020-30-40
     * name : 刘文强
     * stu_account : 16407301
     */

    private String awards_grade;
    private String awards_name;
    private String extral_score;
    private String id;
    private String match_time;
    private String name;
    private String stu_account;

    protected ExtralScoreBean(Parcel in) {
        awards_grade = in.readString();
        awards_name = in.readString();
        extral_score = in.readString();
        id = in.readString();
        match_time = in.readString();
        name = in.readString();
        stu_account = in.readString();
    }

    public static final Creator<ExtralScoreBean> CREATOR = new Creator<ExtralScoreBean>() {
        @Override
        public ExtralScoreBean createFromParcel(Parcel in) {
            return new ExtralScoreBean(in);
        }

        @Override
        public ExtralScoreBean[] newArray(int size) {
            return new ExtralScoreBean[size];
        }
    };

    public String getAwards_grade() {
        return awards_grade;
    }

    public void setAwards_grade(String awards_grade) {
        this.awards_grade = awards_grade;
    }

    public String getAwards_name() {
        return awards_name;
    }

    public void setAwards_name(String awards_name) {
        this.awards_name = awards_name;
    }

    public String getExtral_score() {
        return extral_score;
    }

    public void setExtral_score(String extral_score) {
        this.extral_score = extral_score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(awards_grade);
        parcel.writeString(awards_name);
        parcel.writeString(extral_score);
        parcel.writeString(id);
        parcel.writeString(match_time);
        parcel.writeString(name);
        parcel.writeString(stu_account);
    }
}
