package com.theradcolor.kernel;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.theradcolor.kernel.AppNotification.CHANNEL_ID;

public class GamingService extends Service{

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
    }

    int batt_start,batt_end;
    final Handler handler = new Handler();
    Handler handler1 = new Handler();
    Handler handler2 = new Handler();
    Handler handler3 = new Handler();

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        Intent mIntent = new Intent(this, BroadcastReceiver.class);
        mIntent.putExtra("action","now");
        Intent nIntent = new Intent(this,BroadcastReceiver.class);
        nIntent.putExtra("action","start");
        Intent oIntent = new Intent(this,BroadcastReceiver.class);
        oIntent.putExtra("action","stop");
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this,1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nPendingIntent = PendingIntent.getBroadcast(this,2, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent oPendingIntent = PendingIntent.getBroadcast(this,3, oIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Gaming Mode")
                .setContentText("Gaming Mode is Running")
                .setPriority(Notification.PRIORITY_MAX)
                .build();

        startForeground(1,notification);

        batt_start = getBatteryPercentage(this);

        return START_NOT_STICKY;
    }

    void execCommandLine(String command)
    {
        Runtime runtime = Runtime.getRuntime();
        Process proc = null;
        OutputStreamWriter osw = null;

        try
        {
            proc = runtime.exec("su");
            osw = new OutputStreamWriter(proc.getOutputStream());
            osw.write(command);
            osw.flush();
            osw.close();
        }
        catch (IOException ex)
        {
            Log.e("execCommandLine()", "Command resulted in an IO Exception: " + command);
            return;
        }
        finally
        {
            if (osw != null)
            {
                try
                {
                    osw.close();
                }
                catch (IOException e){}
            }
        }
        try
        {
            proc.waitFor();
        }
        catch (InterruptedException e){}
        if (proc.exitValue() != 0)
        {
            Log.e("execCommandLine()", "Command returned error: " + command + "\n  Exit code: " + proc.exitValue());
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        batt_end = getBatteryPercentage(this);
        Toast.makeText(this,batt_start-batt_end+"% Used while gaming mode is on.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

}
