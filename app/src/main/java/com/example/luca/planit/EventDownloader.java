package com.example.luca.planit;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;



/**
 * Created by diego on 05/12/2016.
 */

public class EventDownloader extends Service {

    private final IBinder binder = new EventDonwloaderBinder();
    private MonitoringAgent agent;
    private boolean isSet = false;
    private Account account ;
    public void startMonitoring(Account account)  {
        if(!isSet){
            isSet = true;
            this.account = account;
            this.agent = new MonitoringAgent();
            this.agent.start();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class EventDonwloaderBinder extends Binder {
        public EventDownloader getService() {
            return EventDownloader.this;
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
                OrganizedEventTask organizedEventTask = new OrganizedEventTask();
                organizedEventTask.execute(EventDownloader.this.account);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public void setStop() {
            this.stop = false;
        }

        public boolean isStop() {
            return stop;
        }
    }
}
