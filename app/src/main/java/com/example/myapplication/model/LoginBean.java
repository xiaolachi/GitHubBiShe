package com.example.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginBean implements Parcelable {


    /**
     * password : 1
     * name : 李不
     * id : 1
     * account : 18234505254
     */

    private String password;
    private String name;
    private int id;
    private String account;

    protected LoginBean(Parcel in) {
        password = in.readString();
        name = in.readString();
        id = in.readInt();
        account = in.readString();
    }

    public static final Creator<LoginBean> CREATOR = new Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel in) {
            return new LoginBean(in);
        }

        @Override
        public LoginBean[] newArray(int size) {
            return new LoginBean[size];
        }
    };

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(password);
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeString(account);
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", account='" + account + '\'' +
                '}';
    }
}
