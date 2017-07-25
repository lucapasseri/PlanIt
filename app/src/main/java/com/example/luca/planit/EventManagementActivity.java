package com.example.luca.planit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EventManagementActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_management);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new EventManagementActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0) {
                //eventOrganizedFragment = new EventOrganizedFragment();
                return eventOrganizedFragment;
            } else {
                //eventTakePartFragment = new EventTakePartFragment();
                return  eventTakePartFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
