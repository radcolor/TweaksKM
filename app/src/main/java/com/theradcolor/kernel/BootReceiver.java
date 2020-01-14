package com.theradcolor.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("myPrefs",
                Context.MODE_PRIVATE);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            if(preferences.getBoolean("srgbsw",false)){
                RootUtils.runCommand("active=1\n" +
                        "\n" +
                        "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                        "\n" +
                        "if [ $active = \"1\" ]\n" +
                        "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                        "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                        "fi");
            }
            if(preferences.getBoolean("ebmsw",false)){
                RootUtils.runCommand("echo 30 > /proc/sys/vm/swappiness \n" +
                        "echo \" + \"\\\"noop\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                        "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                        "echo 128 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                        "echo powersave > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                        "echo 1401600 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                        "echo 1401600 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");
            }
            if(preferences.getBoolean("bbmsw",false)){
                RootUtils.runCommand("echo 30 > /proc/sys/vm/swappiness \n" +
                        "echo \" + \"\\\"cfq\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                        "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                        "echo 128 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                        "echo msm-adreno-tz > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                        "echo 1536000 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                        "echo 1747200 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");
            }
            if(preferences.getBoolean("pmsw",false)){
                RootUtils.runCommand("echo 30 > /proc/sys/vm/swappiness \n" +
                        "echo \" + \"\\\"anxiety\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                        "echo 2 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                        "echo 128 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                        "echo msm-adreno-tz > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                        "echo 1612800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                        "echo 1804800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");
            }
            if(preferences.getBoolean("vibsw",false)){
                int vibval = preferences.getInt("vibval",1500);
                RootUtils.runCommand("echo " +vibval+ " > /sys/devices/virtual/timed_output/vibrator/vtg_level");
                Toast.makeText(context,"Tweaks: applying settings", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
