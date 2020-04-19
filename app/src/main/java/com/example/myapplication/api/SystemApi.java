package com.example.myapplication.api;

import android.content.Context;
import android.widget.EditText;

import com.example.myapplication.api.intercepter.UrlInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SystemApi {

    public final static String DOMAIN = "http://192.168.43.130:8080/";
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private LoginApi mLoginApi;
    private MessageApi mMessageApi;
    private ScoreApi mScoreApi;
    private AnnounceApi mAnnounceApi;
    private ScholarApi mScholarApi;
    private ExtralScoreApi mExtralScoreApi;

    public SystemApi(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new UrlInterceptor(context))
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(DOMAIN)
                .build();

        mLoginApi = mRetrofit.create(LoginApi.class);
        mMessageApi = mRetrofit.create(MessageApi.class);
        mScoreApi = mRetrofit.create(ScoreApi.class);
        mAnnounceApi = mRetrofit.create(AnnounceApi.class);
        mExtralScoreApi = mRetrofit.create(ExtralScoreApi.class);
    }

    public Call<ResponseBody> login(RequestBody body) {
        return mLoginApi.login(body);
    }

    public Call<ResponseBody> getMessageList(String page, String pageSize) {
        return mMessageApi.getMessageList(page, pageSize);
    }

    public Call<ResponseBody> getScoreList(String page, String pagesize) {
        return mScoreApi.getScoreList(page, pagesize);
    }

    public Call<ResponseBody> getAnnounceList(String page, String pagesize) {
        return mAnnounceApi.getAnnounceList(page, pagesize);
    }

    public Call<ResponseBody> getScholarList(String page, String pagesize) {
        return mScoreApi.getScoreList(page, pagesize);
    }

    public Call<ResponseBody> getAnnonYears() {
        return mAnnounceApi.getAnnonYears();
    }

    public Call<ResponseBody> addAnnounce(String content, String years) {
        return mAnnounceApi.addAnnounce(content, years);
    }

    public Call<ResponseBody> delAnnounce(String years) {
        return mAnnounceApi.delAnnounce(years);
    }

    public Call<ResponseBody> addStudent(String utype, String pwd, String stuAccount, String gender, String name, String system, String stuClass, String classNum, String stuCard, String credit) {
        return mMessageApi.addStudent(utype, pwd, stuAccount, gender, name, system, stuClass, classNum, stuCard, credit);
    }

    public Call<ResponseBody> getSystemClass() {
        return mMessageApi.getSystemClass();
    }

    public Call<ResponseBody> delStudent(String utype, int id) {
        return mMessageApi.delStudent(utype, id);
    }

    public Call<ResponseBody> updatePassWord(String type, String oldPwd, String account, String newPwd) {
        return mLoginApi.updatePassWord(type, oldPwd, account, newPwd);
    }

    public Call<ResponseBody> submitExtral(String stu_account, String stuAward, String matchDate, String etGrade, String extralScore) {
        return mExtralScoreApi.submitExtral(stu_account, stuAward, matchDate, etGrade, extralScore);
    }

    public Call<ResponseBody> editAnnounce() {
        return mMessageApi.editAnnounce();
    }

    public Call<ResponseBody> addStuScore() {
        return mScoreApi.addStuScore();
    }
}
