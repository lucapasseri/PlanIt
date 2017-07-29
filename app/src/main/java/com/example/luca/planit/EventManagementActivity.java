package com.example.luca.planit;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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

    private GuestDownloader guestDownloader;

    private boolean bounded;
    private ServiceConnection conn = new ServiceConnection (){
        @Override
        public void onServiceConnected (ComponentName cls , IBinder bnd ){
            guestDownloader = (( GuestDownloader.GuestDownloaderBinder ) bnd).getService();
            bounded = true ;
            guestDownloader.startMonitoring(EventManagementActivity.this.guestsFragment);
        }
        @Override
        public void onServiceDisconnected ( ComponentName cls){
            bounded = false ;
        }
    };

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

        FloatingActionButton fabGuests = (FloatingActionButton) findViewById(R.id.fab_guests);
        FloatingActionButton fabProposals = (FloatingActionButton) findViewById(R.id.fab_proposals);

        fabGuests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new InviteToEventDialog(EventManagementActivity.this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(EventManagementActivity.this, EventManagementActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        String toPass = SelectedInvite.getSelectedInvite().isMailGroupWrapper()?
                                SelectedInvite.getSelectedInvite().getEmail():SelectedInvite.getSelectedInvite().getUsername();
                        intent.putExtra("TASK","Invite to sended");

                        EventManagementActivity.this.startActivity(intent);

                    }
                });
                dialog.show();

            }
        });

        fabProposals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new MakeProposalDialog(EventManagementActivity.this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent intent = new Intent(EventManagementActivity.this, EventManagementActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("TASK","Proposal sended");

                        EventManagementActivity.this.startActivity(intent);

                    }
                });
                dialog.show();

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);
        Intent intent = new Intent(this,GuestDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
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
        Intent intent = new Intent(this,GuestDownloader.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }
}
