package com.theradcolor.kernel.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


import com.theradcolor.kernel.R;

import java.io.IOException;
import java.io.OutputStreamWriter;


public class GamingService extends Service{

    private Context context;

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    int batt_start,batt_end;
    final Handler handler = new Handler();
    String doze,kill,mon;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        context = getApplication();
        doze = intent.getStringExtra("doze");
        kill = intent.getStringExtra("kill");
        if(doze.equals("yes"))
        {
            doze();
        }else{

        }
        if(kill.equals("yes"))
        {
            kill();
        }else
        {

        }
        execCommandLine("echo 40 > /proc/sys/vm/swappiness \n" +
                "echo " + "\\\"deadline\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                "echo 2 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                "echo 2048 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                "echo msm-adreno-tz > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                "echo 1612800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                "echo 1804800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");
        batt_start = getBatteryPercentage(this);
        return START_NOT_STICKY;
    }

    private void startMyOwnForeground(){
        final String CHANNEL_ID = "GamingService";
        String channelName = "Gaming Mode";
        NotificationChannel chan = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent mIntent = new Intent(this, BroadcastReceiver.class);
        mIntent.putExtra("action","now");
        Intent oIntent = new Intent(this,BroadcastReceiver.class);
        oIntent.putExtra("action","stop");
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this,1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent oPendingIntent = PendingIntent.getBroadcast(this,3, oIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_adb_black_24dp)
                .setContentTitle("Gaming Mode is running")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                //.addAction(R.drawable.ic_notifications_black_24dp,"opt",mPendingIntent)
                .addAction(R.drawable.ic_notifications_black_24dp,"Stop",oPendingIntent)
                .build();
        startForeground(1, notification);
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

    public void doze()
    {
        ContentResolver.setMasterSyncAutomatically(false);
        /*NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);*/
    }

    void kill()
    {
        //Warning Its Experiment only as i have found this apps continuously uses connections
        execCommandLine("adb shell"+ "\n"+
                "am force-stop am force-stop com.android.chrome"+"\n"+
                "am force-stop com.google.android.gm"+"\n"+
                "am force-stop com.UCMobile.intl"+"\n"+
                "am force-stop cn.xender"+"\n"+
                "am force-stop com.snaptube.premium"+"\n"+
                "am force-stop com.google.android.app.photos"+"\n"+
                "am force-stop com.google.android.googlequicksearchbox"+"\n");
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
        execCommandLine("echo 40 > /proc/sys/vm/swappiness\n" +
                "echo \" + \"\\\"deadline\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler\n" +
                "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost\n" +
                "echo 1024 > /sys/block/mmcblk0/queue/read_ahead_kb\n" +
                "echo msm-adreno-tz > /sys/class/kgsl/kgsl-3d0/devfreq/governor\n" +
                "echo 1612800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n" +
                "echo 1804800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");
        Toast.makeText(this,batt_start-batt_end+"% Used while gaming mode is on.",Toast.LENGTH_LONG).show();
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
