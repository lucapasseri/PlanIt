package com.example.luca.planit;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Luca on 21/07/2017.
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private EventDownloader eventDownloader;
    private EventOrganizedFragment eventOrganizedFragment = new EventOrganizedFragment();
    private EventTakePartFragment eventTakePartFragment = new EventTakePartFragment();
    private boolean bounded;
    private ServiceConnection conn = new ServiceConnection (){
        @Override
        public void onServiceConnected (ComponentName cls , IBinder bnd ){
            eventDownloader = (( EventDownloader.EventDonwloaderBinder ) bnd).getService();
            bounded = true ;
                eventDownloader.startMonitoring(eventTakePartFragment, eventOrganizedFragment);
        }
        @Override
        public void onServiceDisconnected ( ComponentName cls){
            bounded = false ;
        }
    };
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String name = prefs.getString(getString(R.string.name_pref), null);
        String surname = prefs.getString(getString(R.string.surname_pref), null);
        String email = prefs.getString(getString(R.string.email_pref), null);
        String username = prefs.getString(getString(R.string.username_pref), null);
        String password = prefs.getString(getString(R.string.password_pref), null);
        String bornDate = prefs.getString(getString(R.string.born_date_pref), null);
        String id = prefs.getString(getString(R.string.id_pref), null);


        Account loggedAccount = new AccountImpl.Builder()
                .setBorndate(bornDate)
                .setEmail(email)
                .setId(id)
                .setName(name)
                .setSurname(surname)
                .setPassword(password)
                .setUsername(username)
                .build();

        LoggedAccount.storeLoggedAccount(loggedAccount);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PlanEventActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);

        Intent intent = new Intent(this,EventDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getIntent().hasExtra(getString(R.string.extra_from_login)) ||
                    getIntent().hasExtra(getString(R.string.extra_from_signup))) {
                logout();
            }

            super.onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(bounded){
            unbindService(conn);
            bounded = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bounded){
            unbindService(conn);
            bounded = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,EventDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LoggedAccount.removeLoggedAccount();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_groups) {
            Intent intent = new Intent(getApplication(), GroupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(getApplication(), InfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_invites) {
            Intent intent = new Intent(getApplication(), InviteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    private void logout() {
        Intent intent = new Intent(getApplication(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(getString(R.string.name_pref));
        editor.remove(getString(R.string.surname_pref));
        editor.remove(getString(R.string.email_pref));
        editor.remove(getString(R.string.username_pref));
        editor.remove(getString(R.string.password_pref));
        editor.remove(getString(R.string.born_date_pref));

        editor.apply();
    }
}
