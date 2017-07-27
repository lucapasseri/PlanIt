package com.example.luca.planit;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class EventManagementActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;

    private static final int GUESTS_FRAGMENT = 0;
    private static final int INFO_FRAGMENT = 1;
    private static final int PROPOSALS_FRAGMENT = 2;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private GuestsFragment guestsFragment = new GuestsFragment();
    private EventInfoFragment infoFragment = new EventInfoFragment();
    private ProposalsFragment proposalsFragment = new ProposalsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_management);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new EventManagementActivity.ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(INFO_FRAGMENT);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position==GUESTS_FRAGMENT) {
                return guestsFragment;
            } else if (position==INFO_FRAGMENT) {
                return infoFragment;
            } else {
                return proposalsFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SelectedEvent.removeSelectedEvent();
    }
}
