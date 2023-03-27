package com.example.samsungprojectjava;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    Timer timer;
    TimerTask timerTask;
    String login;
    int h_tasks;
    int m_tasks;
    int l_tasks;
    DBScheduleClass mDBConnector;
    ArrayList<ScheduleClass> objects;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        objects = intent.getParcelableArrayListExtra("array");
        login = intent.getStringExtra("login");
        this.mDBConnector = new DBScheduleClass(getApplicationContext(), login);
        //objects = mDBConnector.selectAll();
        h_tasks = intent.getIntExtra("h_tasks", 120);
        m_tasks = intent.getIntExtra("m_tasks", 60);
        l_tasks = intent.getIntExtra("l_tasks", 30);
        if (objects != null) {
            for (int i = 0; i < objects.size(); i++)
                if (!objects.get(i).login.equals(login)) {
                    objects.remove(i);
                    i--;
                }
        }
        startTimer();
        return START_REDELIVER_INTENT;
    }
    public void onCreate(){
    }
    @Override
    public void onDestroy() {
        stoptimertask();
        super.onDestroy();
    }
    final Handler handler = new Handler();
    public void startTimer() {
        long cur_time = System.currentTimeMillis();
        for (int i = 0; i < objects.size(); i++) {
            if (cur_time > objects.get(i).time){
                objects.remove(i);
                i--;
            }
        }
        timer = new Timer();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).mentioned < 2) {
                initializeTimerTask("Время: ", objects.get(i));
                timer.schedule(timerTask, objects.get(i).time - cur_time);
            }
            if (objects.get(i).mentioned < 1) {
                initializeTimerTask("Скоро: ", objects.get(i));
                long check = 0;
                switch (objects.get(i).importance) {
                    case 1:
                        if (cur_time < objects.get(i).time - 1000L * 60 * l_tasks)
                            check = objects.get(i).time - 1000L * 60 * l_tasks - cur_time;
                        break;
                    case 2:
                        if (cur_time < objects.get(i).time - 1000L * 60 * m_tasks)
                            check = objects.get(i).time - 1000L * 60 * m_tasks - cur_time;
                        break;
                    case 3:
                        if (cur_time < objects.get(i).time - 1000L * 60 * h_tasks)
                            check = objects.get(i).time - 1000L * 60 * h_tasks - cur_time;
                        break;
                }
                timer.schedule(timerTask, check);
            }
        }
    }
    public void stoptimertask() {
        if (timer != null) {
            //timer.purge();
            timer.cancel();
            timer = null;
        }
    }
    public void initializeTimerTask(String msg, ScheduleClass sch) {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), LaaActivity.CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setContentTitle("Timetable v.0.1")
                                    .setContentText(msg + sch.name)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                            Notification notification = builder.build();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                            notificationManager.notify(0, notification);
                            sch.mentioned++;
                            mDBConnector.update(sch);
                    }
                });
            }
        };
    }
}