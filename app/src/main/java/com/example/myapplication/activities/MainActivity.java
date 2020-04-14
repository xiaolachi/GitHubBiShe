package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.adapters.TabFragmentAdapter;
import com.example.myapplication.fragments.AnnouncementFragment;
import com.example.myapplication.fragments.MessageFragment;
import com.example.myapplication.fragments.ScoreFragment;
import com.example.myapplication.fragments.MeFragment;
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
    private ScoreFragment mScoreFragment;
    private MeFragment mMeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        int[] iconResID = {
                R.drawable.icon_tab_announce,
                R.drawable.icon_tab_message,
                R.drawable.icon_tab_score,
                R.drawable.icon_tab_mine,
        };
        String[] iconText = {
                getString(R.string.announce),
                getString(R.string.message),
                getString(R.string.score),
                getString(R.string.mine),
        };
        mAnnouncementFragment = new AnnouncementFragment();
        mMessageFragment = new MessageFragment();
        mScoreFragment = new ScoreFragment();
        mMeFragment = new MeFragment();

        mFragmentList = new ArrayList<Fragment>() {{
            add(mAnnouncementFragment);
            add(mMessageFragment);
            add(mScoreFragment);
            add(mMeFragment);
        }};

        mTabLayout = findViewById(R.id.tablayout_main);
        mViewPager = findViewById(R.id.viewpager_main);
        mFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);
        mTabLayout.setTabRippleColor(null);
        mTabLayout.setupWithViewPager(mViewPager, true);
        for (int i = 0; i < iconResID.length; i++) {
            mTabLayout.getTabAt(i).setCustomView(R.layout.item_home_tab);
            mTabLayout.getTabAt(i).setIcon(iconResID[i]);
            mTabLayout.getTabAt(i).setText(iconText[i]);
        }

        final View tabDivider = findViewById(R.id.view_tab_divider);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
