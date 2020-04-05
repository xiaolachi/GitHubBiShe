package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.adapters.TabFragmentAdapter;
import com.example.myapplication.fragments.Fragment1;
import com.example.myapplication.fragments.Fragment2;
import com.example.myapplication.fragments.Fragment3;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<Fragment> mFragmentList;
    private TabFragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        int[] iconResID = {
                R.drawable.icon_tab_homepage,
                R.drawable.icon_tab_works,
                R.drawable.icon_tab_personal,
        };
        String[] iconText = {
                getString(R.string.homepage),
                getString(R.string.work),
                getString(R.string.mine),
        };
        mFragment1 = new Fragment1();
        mFragment2 = new Fragment2();
        mFragment3 = new Fragment3();

        mFragmentList = new ArrayList<Fragment>() {{
            add(mFragment1);
            add(mFragment2);
            add(mFragment3);
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
