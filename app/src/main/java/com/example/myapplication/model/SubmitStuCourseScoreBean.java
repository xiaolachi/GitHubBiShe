package com.example.myapplication.model;

public class SubmitStuCourseScoreBean {

    private String Stu_account;
    private String course;
    private String course_score;

    public SubmitStuCourseScoreBean() {
    }

    public SubmitStuCourseScoreBean(String stu_account, String course, String course_score) {
        Stu_account = stu_account;
        this.course = course;
        this.course_score = course_score;
    }

    public String getStu_account() {
        return Stu_account;
    }

    public void setStu_account(String stu_account) {
        Stu_account = stu_account;
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

    @Override
    public String toString() {
        return "SubmitStuCourseScoreBean{" +
                "Stu_account='" + Stu_account + '\'' +
                ", course='" + course + '\'' +
                ", course_score='" + course_score + '\'' +
                '}';
    }
}
