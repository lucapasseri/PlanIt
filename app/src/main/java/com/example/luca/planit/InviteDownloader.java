package com.example.luca.planit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


/**
 * Created by diego on 05/12/2016.
 */

public class InviteDownloader extends Service {

    private final IBinder binder = new InviteDownloaderBinder();
    private MonitoringAgent agent;
    private boolean isSet = false;
    private InviteActivity inviteActivity;
    public void startMonitoring(InviteActivity inviteActivity)  {
        if(!isSet){
            isSet = true;
            this.inviteActivity = inviteActivity;
            this.agent = new MonitoringAgent();
            this.agent.start();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class InviteDownloaderBinder extends Binder {
        public InviteDownloader getService() {
            return InviteDownloader.this;
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
                InviteDownloader.this.inviteActivity.startTask();
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
