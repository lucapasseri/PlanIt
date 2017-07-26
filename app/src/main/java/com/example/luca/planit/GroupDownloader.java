package com.example.luca.planit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * Created by diego on 05/12/2016.
 */

public class GroupDownloader extends Service {

    private final IBinder binder = new GroupDownloaderBinder();
    private MonitoringAgent agent;
    private boolean isSet = false;
    private GroupActivity groupActivity;
    public void startMonitoring(GroupActivity groupActivity)  {
        if(!isSet){
            isSet = true;
            this.groupActivity = groupActivity;
            this.agent = new MonitoringAgent();
            this.agent.start();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class GroupDownloaderBinder extends Binder {
        public GroupDownloader getService() {
            return GroupDownloader.this;
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
                GroupDownloader.this.groupActivity.startTask();
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
