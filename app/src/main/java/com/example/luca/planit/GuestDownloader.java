package com.example.luca.planit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * Created by diego on 05/12/2016.
 */

public class GuestDownloader extends Service {

    private final IBinder binder = new GuestDownloaderBinder();
    private MonitoringAgent agent;
    private boolean isSet = false;
    private GuestsFragment guestsFragment;
    public void startMonitoring(GuestsFragment guestsFragment)  {
        if(!isSet){
            isSet = true;
            this.guestsFragment = guestsFragment;
            this.agent = new MonitoringAgent();
            this.agent.start();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class GuestDownloaderBinder extends Binder {
        public GuestDownloader getService() {
            return GuestDownloader.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        this.agent.setStop();
        return super.onUnbind(intent);
    }

    public class MonitoringAgent extends Thread {
        private boolean stop = false;

        @Override
        public void run() {

            while (!this.stop) {
                GuestDownloader.this.guestsFragment.startTask();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public void setStop() {
            this.stop = true;
        }

        public boolean isStop() {
            return stop;
        }
    }
}
