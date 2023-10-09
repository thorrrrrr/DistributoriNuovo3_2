package com.gestione.distributori.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.gestione.distributori.persistence.local.DbAdapter;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

public class ScaricoDatiService extends Service {
    public static final long NOTIFY_INTERVAL = 60000;
    /* access modifiers changed from: private */
    public DbAdapter dbHelper;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private Timer mTimer = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        } else {
            this.mTimer = new Timer();
        }
        this.mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {
        TimeDisplayTimerTask() {
        }

        public void run() {
            ScaricoDatiService.this.mHandler.post(new Runnable() {
                public void run() {
                    try {
                        DbAdapter unused = ScaricoDatiService.this.dbHelper = new DbAdapter(ScaricoDatiService.this.getApplicationContext());
                        ScaricoDatiService.this.dbHelper.TrasmettiDati();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ParseException e2) {
                        e2.printStackTrace();
                    }
                }
            });
        }
    }
}
