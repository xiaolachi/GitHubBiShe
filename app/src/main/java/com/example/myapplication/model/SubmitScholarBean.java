package com.example.myapplication.model;

public class SubmitScholarBean {

    private String jiangjinType;
    private String jiangjinNUmber;
    private String stuAccount;

    public SubmitScholarBean() {
    }

    public String getJiangjinType() {
        return jiangjinType;
    }

    public void setJiangjinType(String jiangjinType) {
        this.jiangjinType = jiangjinType;
    }

    public String getJiangjinNUmber() {
        return jiangjinNUmber;
    }

    public void setJiangjinNUmber(String jiangjinNUmber) {
        this.jiangjinNUmber = jiangjinNUmber;
    }

    public String getStuAccount() {
        return stuAccount;
    }

    public void setStuAccount(String stuAccount) {
        this.stuAccount = stuAccount;
    }

    @Override
    public String toString() {
        return "SubmitScholarBean{" +
                "jiangjinType='" + jiangjinType + '\'' +
                ", jiangjinNUmber='" + jiangjinNUmber + '\'' +
                ", stuAccount='" + stuAccount + '\'' +
                '}';
    }
}
