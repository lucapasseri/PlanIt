package com.example.luca.planit;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


/**
 * Created by diego on 05/12/2016.
 */

public class EventDownloader extends Service {

    private final IBinder binder = new EventDonwloaderBinder();
    private MonitoringAgent agent;
    private boolean isSet = false;
    private EventOrganizedFragment eventOrganizedFragment;
    private EventTakePartFragment eventTakePartFragment;
    public void startMonitoring(EventTakePartFragment eventTakePartFragment,EventOrganizedFragment eventOrganizedFragment)  {
        if(!isSet){
            isSet = true;
            this.eventOrganizedFragment = eventOrganizedFragment;
            this.eventTakePartFragment = eventTakePartFragment;
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
                Log.d("Monitor","Started");
                EventDownloader.this.eventOrganizedFragment.startTask();
                EventDownloader.this.eventTakePartFragment.startTask();
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
