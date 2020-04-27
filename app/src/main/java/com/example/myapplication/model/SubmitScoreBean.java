package com.example.myapplication.model;

import java.util.List;

public class SubmitScoreBean {

    private String stuAccount;
    private String averageCredit;
    private String averageScore;
    private String totalScore;

    private List<SubmitStuCourseScoreBean> listCourse;


    public String getStuAccount() {
        return stuAccount;
    }

    public void setStuAccount(String stuAccount) {
        this.stuAccount = stuAccount;
    }

    public String getAverageCredit() {
        return averageCredit;
    }

    public void setAverageCredit(String averageCredit) {
        this.averageCredit = averageCredit;
    }

    public String getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public List<SubmitStuCourseScoreBean> getListCourse() {
        return listCourse;
    }

    public void setListCourse(List<SubmitStuCourseScoreBean> listCourse) {
        this.listCourse = listCourse;
    }

    @Override
    public String toString() {
        return "SubmitScoreBean{" +
                "stuAccount='" + stuAccount + '\'' +
                ", averageCredit='" + averageCredit + '\'' +
                ", averageScore='" + averageScore + '\'' +
                ", totalScore='" + totalScore + '\'' +
                ", listCourse=" + listCourse +
                '}';
    }
}
