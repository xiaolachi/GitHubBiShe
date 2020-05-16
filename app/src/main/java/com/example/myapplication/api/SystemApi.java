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

    public final static String DOMAIN = "http://192.168.0.105:8080/";
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    private LoginApi mLoginApi;
    private MessageApi mMessageApi;
    private ScoreApi mScoreApi;
    private AnnounceApi mAnnounceApi;
    private ScholarApi mScholarApi;
    private ExtralScoreApi mExtralScoreApi;
    private AwardsListApi mAwardsListApi;

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
        mScholarApi = mRetrofit.create(ScholarApi.class);
        mAnnounceApi = mRetrofit.create(AnnounceApi.class);
        mExtralScoreApi = mRetrofit.create(ExtralScoreApi.class);
        mAwardsListApi = mRetrofit.create(AwardsListApi.class);
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

    public Call<ResponseBody> delAnnounce(String id) {
        return mAnnounceApi.delAnnounce(id);
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

    public Call<ResponseBody> editAnnounce(String stuAccount, String gender, String name, String mSystem, String mClass, String mClassNum, String stuCard, String credit) {
        return mMessageApi.editAnnounce(stuAccount, gender, name, mSystem, mClass, mClassNum, stuCard, credit);
    }

    public Call<ResponseBody> addStuScore(String json) {
        return mScoreApi.addStuScore(json);
    }

    public Call<ResponseBody> getExtralScoreList(String page, String pageSize) {
        return mExtralScoreApi.getExtralScoreList(page, pageSize);
    }

    public Call<ResponseBody> postExtalScore(String id, String extralscore) {
        return mExtralScoreApi.postExtalScore(id, extralscore, "1");
    }

    public Call<ResponseBody> editScholarAnnounce(String id, String selectType, String content) {
        return mAnnounceApi.editScholarAnnounce(id, selectType, content);
    }

    public Call<ResponseBody> lookUpAnnounce(String contentKey) {
        return mAnnounceApi.lookUpAnnounce(contentKey);
    }

    public Call<ResponseBody> lookUpMessage(String uaccount) {
        return mMessageApi.lookUpMessage(uaccount);
    }

    public Call<ResponseBody> lookUpScore(String uaccount) {
        return mScoreApi.lookUpScore(uaccount);
    }

    public Call<ResponseBody> getAwardsList() {
        return mAwardsListApi.getAwardsList();
    }

    public Call<ResponseBody> addScholar(String json) {
        return mScholarApi.addScholar(json);
    }

    public Call<ResponseBody> getScoreGradeList() {
        return mScoreApi.getScoreGradeList();
    }
}
