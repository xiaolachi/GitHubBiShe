package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.adapters.TabFragmentAdapter;
import com.example.myapplication.constant.LibConfig;
import com.example.myapplication.fragments.AnnouncementFragment;
import com.example.myapplication.fragments.AwardsListFragment;
import com.example.myapplication.fragments.ExtralScoreFragment;
import com.example.myapplication.fragments.MessageFragment;
import com.example.myapplication.fragments.ScoreFragment;
import com.example.myapplication.fragments.MeFragment;
import com.example.myapplication.utils.LoginUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Fragment> mFragmentList;
    private TabFragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private AnnouncementFragment mAnnouncementFragment;
    private MessageFragment mMessageFragment;
    private ExtralScoreFragment mExtralScoreFragment;
    private ScoreFragment mScoreFragment;
    private AwardsListFragment mAwardsListFragment;
    private MeFragment mMeFragment;
    private int[] mIconResID;
    private String[] mIconText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        String type = LoginUtils.getLoginType();
        if (TextUtils.isEmpty(type)) {
            LoginUtils.toLoginActivity(this);
            return;
        }
        if (type.equals(LibConfig.LOGIN_TYPE_STUDENT)) {
            mIconResID = new int[2];
            mIconText = new String[2];
            mIconResID[0] = R.drawable.icon_tab_announce;
            mIconResID[1] = R.drawable.icon_tab_mine;

            mIconText[0] = getString(R.string.announce);
            mIconText[1] = getString(R.string.mine);
            mAnnouncementFragment = new AnnouncementFragment();
            mMeFragment = new MeFragment();

            mFragmentList = new ArrayList<Fragment>() {
                {
                    add(mAnnouncementFragment);
                    add(mMeFragment);
                }
            };
        } else if (type.equals(LibConfig.LOGIN_TYPE_OFFICE) || type.equals(LibConfig.LOGIN_TYPE_TEACHER)) {
            mIconResID = new int[6];
            mIconText = new String[6];
            mIconResID[0] = R.drawable.icon_tab_announce;
            mIconResID[1] = R.drawable.icon_tab_message;
            mIconResID[2] = R.drawable.icon_tab_extral;
            mIconResID[3] = R.drawable.icon_tab_score;
            mIconResID[4] = R.drawable.icon_tab_awards;
            mIconResID[5] = R.drawable.icon_tab_mine;

            mIconText[0] = getString(R.string.announce);
            mIconText[1] = getString(R.string.message);
            mIconText[2] = getString(R.string.extralscore);
            mIconText[3] = getString(R.string.score);
            mIconText[4] = getString(R.string.award);
            mIconText[5] = getString(R.string.mine);
            mAnnouncementFragment = new AnnouncementFragment();
            mMessageFragment = new MessageFragment();
            mExtralScoreFragment = new ExtralScoreFragment();
            mScoreFragment = new ScoreFragment();
            mAwardsListFragment = new AwardsListFragment();
            mMeFragment = new MeFragment();

            mFragmentList = new ArrayList<Fragment>() {
                {
                    add(mAnnouncementFragment);
                    add(mMessageFragment);
                    add(mExtralScoreFragment);
                    add(mScoreFragment);
                    add(mAwardsListFragment);
                    add(mMeFragment);
                }
            };
        }

        mTabLayout = findViewById(R.id.tablayout_main);
        mViewPager = findViewById(R.id.viewpager_main);
        mFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setTabRippleColor(null);
        mTabLayout.setupWithViewPager(mViewPager, true);
        for (int i = 0; i < mIconResID.length; i++) {
            mTabLayout.getTabAt(i).setCustomView(R.layout.item_home_tab);
            mTabLayout.getTabAt(i).setIcon(mIconResID[i]);
            mTabLayout.getTabAt(i).setText(mIconText[i]);
        }
        final View tabDivider = findViewById(R.id.view_tab_divider);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mFragmentAdapter.getItem(i).setUserVisibleHint(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}
