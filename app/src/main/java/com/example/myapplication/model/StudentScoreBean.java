package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class StudentScoreBean implements Parcelable {


    private ArrayList<ScorelistBean> scorelist;

    protected StudentScoreBean(Parcel in) {
    }

    public static final Creator<StudentScoreBean> CREATOR = new Creator<StudentScoreBean>() {
        @Override
        public StudentScoreBean createFromParcel(Parcel in) {
            return new StudentScoreBean(in);
        }

        @Override
        public StudentScoreBean[] newArray(int size) {
            return new StudentScoreBean[size];
        }
    };

    public ArrayList<ScorelistBean> getScorelist() {
        return scorelist;
    }

    public void setScorelist(ArrayList<ScorelistBean> scorelist) {
        this.scorelist = scorelist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static class ScorelistBean implements Parcelable{
        /**
         * average_credit : 2.51
         * average_score : 76.34
         * class_number : 1601
         * course : TCP/IP协议分析与应用编程
         * course_score : 100.0
         * credit : 40.10
         * name : lyf
         * stu_accout : 16407302
         * stu_class : 网络工程
         * stu_system : 计算机系
         * total_score : 382.0
         */

        private String average_credit;
        private String average_score;
        private String class_number;
        private String course;
        private String course_score;
        private String credit;
        private String name;
        private String stu_accout;
        private String stu_class;
        private String stu_system;
        private String total_score;

        protected ScorelistBean(Parcel in) {
            average_credit = in.readString();
            average_score = in.readString();
            class_number = in.readString();
            course = in.readString();
            course_score = in.readString();
            credit = in.readString();
            name = in.readString();
            stu_accout = in.readString();
            stu_class = in.readString();
            stu_system = in.readString();
            total_score = in.readString();
        }

        public static final Creator<ScorelistBean> CREATOR = new Creator<ScorelistBean>() {
            @Override
            public ScorelistBean createFromParcel(Parcel in) {
                return new ScorelistBean(in);
            }

            @Override
            public ScorelistBean[] newArray(int size) {
                return new ScorelistBean[size];
            }
        };

        public String getAverage_credit() {
            return average_credit;
        }

        public void setAverage_credit(String average_credit) {
            this.average_credit = average_credit;
        }

        public String getAverage_score() {
            return average_score;
        }

        public void setAverage_score(String average_score) {
            this.average_score = average_score;
        }

        public String getClass_number() {
            return class_number;
        }

        public void setClass_number(String class_number) {
            this.class_number = class_number;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getCourse_score() {
            return course_score;
        }

        public void setCourse_score(String course_score) {
            this.course_score = course_score;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStu_accout() {
            return stu_accout;
        }

        public void setStu_accout(String stu_accout) {
            this.stu_accout = stu_accout;
        }

        public String getStu_class() {
            return stu_class;
        }

        public void setStu_class(String stu_class) {
            this.stu_class = stu_class;
        }

        public String getStu_system() {
            return stu_system;
        }

        public void setStu_system(String stu_system) {
            this.stu_system = stu_system;
        }

        public String getTotal_score() {
            return total_score;
        }

        public void setTotal_score(String total_score) {
            this.total_score = total_score;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(average_credit);
            parcel.writeString(average_score);
            parcel.writeString(class_number);
            parcel.writeString(course);
            parcel.writeString(course_score);
            parcel.writeString(credit);
            parcel.writeString(name);
            parcel.writeString(stu_accout);
            parcel.writeString(stu_class);
            parcel.writeString(stu_system);
            parcel.writeString(total_score);
        }
    }
}
