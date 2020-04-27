package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginStuBean implements Parcelable {


    /**
     * Stu_number : 16407336
     * password : 66666
     * name : 王晨阳
     * id : 36
     * Class_number : 1601
     * Stu_accout : 16407336
     * Stu_system : 计算机系
     * Stu_ID : 142727199709161234
     * Stu_Card : 6217280003901234
     * Stu_class : 网络工程
     * Stu_sex : 女
     */

    private String Stu_number;
    private String password;
    private String name;
    private int id;
    private String Class_number;
    private String Stu_accout;
    private String Stu_system;
    private String Stu_ID;
    private String Stu_Card;
    private String Stu_class;
    private String Stu_sex;

    protected LoginStuBean(Parcel in) {
        Stu_number = in.readString();
        password = in.readString();
        name = in.readString();
        id = in.readInt();
        Class_number = in.readString();
        Stu_accout = in.readString();
        Stu_system = in.readString();
        Stu_ID = in.readString();
        Stu_Card = in.readString();
        Stu_class = in.readString();
        Stu_sex = in.readString();
    }

    public static final Creator<LoginStuBean> CREATOR = new Creator<LoginStuBean>() {
        @Override
        public LoginStuBean createFromParcel(Parcel in) {
            return new LoginStuBean(in);
        }

        @Override
        public LoginStuBean[] newArray(int size) {
            return new LoginStuBean[size];
        }
    };

    public String getStu_number() {
        return Stu_number;
    }

    public void setStu_number(String Stu_number) {
        this.Stu_number = Stu_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClass_number() {
        return Class_number;
    }

    public void setClass_number(String Class_number) {
        this.Class_number = Class_number;
    }

    public String getStu_accout() {
        return Stu_accout;
    }

    public void setStu_accout(String Stu_accout) {
        this.Stu_accout = Stu_accout;
    }

    public String getStu_system() {
        return Stu_system;
    }

    public void setStu_system(String Stu_system) {
        this.Stu_system = Stu_system;
    }

    public String getStu_ID() {
        return Stu_ID;
    }

    public void setStu_ID(String Stu_ID) {
        this.Stu_ID = Stu_ID;
    }

    public String getStu_Card() {
        return Stu_Card;
    }

    public void setStu_Card(String Stu_Card) {
        this.Stu_Card = Stu_Card;
    }

    public String getStu_class() {
        return Stu_class;
    }

    public void setStu_class(String Stu_class) {
        this.Stu_class = Stu_class;
    }

    public String getStu_sex() {
        return Stu_sex;
    }

    public void setStu_sex(String Stu_sex) {
        this.Stu_sex = Stu_sex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Stu_number);
        parcel.writeString(password);
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeString(Class_number);
        parcel.writeString(Stu_accout);
        parcel.writeString(Stu_system);
        parcel.writeString(Stu_ID);
        parcel.writeString(Stu_Card);
        parcel.writeString(Stu_class);
        parcel.writeString(Stu_sex);
    }

    @Override
    public String toString() {
        return "LoginStuBean{" +
                "Stu_number='" + Stu_number + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", Class_number='" + Class_number + '\'' +
                ", Stu_accout='" + Stu_accout + '\'' +
                ", Stu_system='" + Stu_system + '\'' +
                ", Stu_ID='" + Stu_ID + '\'' +
                ", Stu_Card='" + Stu_Card + '\'' +
                ", Stu_class='" + Stu_class + '\'' +
                ", Stu_sex='" + Stu_sex + '\'' +
                '}';
    }
}
