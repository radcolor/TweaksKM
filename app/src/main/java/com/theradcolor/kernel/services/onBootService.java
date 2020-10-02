package com.theradcolor.kernel.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import com.grarak.kerneladiutor.utils.Prefs;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.utils.kernel.Battery;
import com.theradcolor.kernel.utils.kernel.KCAL;
import com.theradcolor.kernel.utils.kernel.Network;
import com.theradcolor.kernel.utils.kernel.Sound;
import com.theradcolor.kernel.utils.kernel.Vibration;
import com.theradcolor.kernel.utils.kernel.sRGB;

public class onBootService extends Service{

    static final String CHANNEL_ID = "TweaksKM onBoot Service";
    static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "Apply on boot", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(null, null);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            Notification.Builder builder = new Notification.Builder(
                    this, CHANNEL_ID);
            builder.setContentTitle("Applying onBoot Parameters")
                    .setContentText("Just hold on tight, it can be all right!")
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp);
            startForeground(NOTIFICATION_ID, builder.build());
        }
        executeParam();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void executeParam(){
        Toast.makeText(getApplicationContext(), "Applying settings", Toast.LENGTH_LONG).show();
        if (Prefs.getBoolean("srgb", true, getApplicationContext())
                && Prefs.getBoolean("srgb_value", false, getApplicationContext())){
            sRGB.srgbON();
        }
        int vibration = Prefs.getInt("vibration_value", 83, getApplicationContext());
        if (Prefs.getBoolean("vibration", true, getApplicationContext()) || !(vibration==83)){
            Vibration.getInstance().setVibrationValue(vibration, getApplicationContext());
        }
        int hp_left = Prefs.getInt("hp_left_value", 0, getApplicationContext());
        if (Prefs.getBoolean("hpGain", true, getApplicationContext()) || !(hp_left == 0)) {
            Sound.setHeadphoneFlar("left", ""+hp_left , getApplicationContext());
        }
        int hp_right = Prefs.getInt("hp_right_value", 0, getApplicationContext());
        if (Prefs.getBoolean("hpGain", true, getApplicationContext()) || !(hp_right == 0)) {
            Sound.setHeadphoneFlar("right", ""+hp_right, getApplicationContext());
        }
        int mp_gain = Prefs.getInt("mp_value", 2, getApplicationContext());
        if (Prefs.getBoolean("mpGain", true, getApplicationContext()) || mp_gain != 2){
            Sound.setMicrophoneFlar(""+mp_gain, getApplicationContext());
        }
        boolean ffc = Prefs.getBoolean("ffc_value", true, getApplicationContext());
        if (Prefs.getBoolean("ffc", true, getApplicationContext()) && !ffc){
            Battery.ForceFastChargeEnable(false, getApplicationContext());
        }
        String tcp = Prefs.getString("tcp_congestion_algorithm", "westwood", getApplicationContext());
        if (Prefs.getBoolean("tcp", true, getApplicationContext()) || !tcp.equals("westwood")){
            Network.setTcpCongestion(tcp, getApplicationContext());
        }
        if (Prefs.getBoolean("sob_sw", false, getApplicationContext())){
            KCAL.getInstance().setColors(Prefs.getInt("kcal_r", 256, getApplicationContext()) + " " +
                            Prefs.getInt("kcal_g", 256, getApplicationContext()) + " " +
                            Prefs.getInt("kcal_b", 256, getApplicationContext()), getApplicationContext());
            KCAL.getInstance().setSaturationIntensity(
                    Prefs.getInt("kcal_sat", 30+225, getApplicationContext()),
                    getApplicationContext());
            KCAL.getInstance().setScreenValue(
                    Prefs.getInt("kcal_val", 127+128, getApplicationContext()),
                    getApplicationContext());
            KCAL.getInstance().setScreenContrast(
                    Prefs.getInt("kcal_con", 127+128, getApplicationContext()),
                    getApplicationContext());
            KCAL.getInstance().setScreenHue(
                    Prefs.getInt("kcal_hue", 0, getApplicationContext()),
                    getApplicationContext());
        }
        stopForeground(true);
    }

}
