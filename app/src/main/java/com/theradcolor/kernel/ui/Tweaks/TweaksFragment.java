package com.theradcolor.kernel.ui.Tweaks;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ms_square.debugoverlay.DebugOverlay;
import com.ms_square.debugoverlay.Position;
import com.ms_square.debugoverlay.modules.CpuUsageModule;
import com.ms_square.debugoverlay.modules.FpsModule;
import com.ms_square.debugoverlay.modules.MemInfoModule;
import com.theradcolor.kernel.GamingService;
import com.theradcolor.kernel.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TweaksFragment extends Fragment implements View.OnClickListener{

    private TweaksViewModel homeViewModel;
    public TextView textView;
    private ProgressDialog mprogress;
    private LinearLayout eb,bb,bal,pm,gm,as,mon,kill,ads;
    private CheckBox dozesw,killsw,monsw;
    private Switch ebmsw,bbmsw,obmsw,pmsw,gmsw,adsw;
    final Handler handler = new Handler();

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(TweaksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tweaks, container, false);
        textView = root.findViewById(R.id.kernel_name);
        textView.setText("Kernel: " + getKernelVersion());

        ebmsw = root.findViewById(R.id.ebmsw);
        ebmsw.setOnCheckedChangeListener(myCheckboxListener);
        bbmsw = root.findViewById(R.id.bbmsw);
        bbmsw.setOnCheckedChangeListener(myCheckboxListener);
        obmsw = root.findViewById(R.id.obmsw);
        obmsw.setOnCheckedChangeListener(myCheckboxListener);
        pmsw = root.findViewById(R.id.pmsw);
        pmsw.setOnCheckedChangeListener(myCheckboxListener);
        adsw = root.findViewById(R.id.adsw);
        adsw.setOnClickListener(this);

        eb = root.findViewById(R.id.ebm);
        bb = root.findViewById(R.id.bbm);
        bal = root.findViewById(R.id.bm);
        pm = root.findViewById(R.id.pm);

        gm = root.findViewById(R.id.ll_gm);
        gm.setOnClickListener(this);
        as = root.findViewById(R.id.ll_as);
        as.setOnClickListener(this);
        mon = root.findViewById(R.id.ll_mon);
        mon.setOnClickListener(this);
        kill = root.findViewById(R.id.ll_kill);
        kill.setOnClickListener(this);
        ads = root.findViewById(R.id.ll_ad);
        ads.setOnClickListener(this);

        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }

        dozesw = root.findViewById(R.id.dozesw);
        dozesw.setChecked(true);
        killsw = root.findViewById(R.id.killsw);
        killsw.setChecked(false);
        monsw = root.findViewById(R.id.monsw);
        monsw.setChecked(true);

        gmsw = root.findViewById(R.id.gmsw);
        final Intent intent = new Intent(getContext(), GamingService.class);

        dozesw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    intent.putExtra("doze","yes");
                }else{
                    intent.putExtra("doze","no");
                }
            }
        });
        killsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    intent.putExtra("kill","yes");
                }else {
                    intent.putExtra("kill","no");
                }
            }
        });
        monsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }else {

                }
            }
        });

        intent.putExtra("doze","yes");
        intent.putExtra("kill","no");

        gmsw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getActivity().startService(intent);
                        if(monsw.isChecked()){
                            monitor();
                        }
                        if(killsw.isChecked()){
                            kill();
                        }
                    }
                }else
                {
                    getActivity().stopService(intent);
                    doze();
                }
            }
        });

        eb.setOnClickListener(this);
        bb.setOnClickListener(this);
        bal.setOnClickListener(this);
        pm.setOnClickListener(this);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        return root;
    }

    private CompoundButton.OnCheckedChangeListener myCheckboxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.ebmsw:
                    if(isChecked){
                        bbmsw.setChecked(false);
                        obmsw.setChecked(false);
                        pmsw.setChecked(false);
                    }
                    break;
                case R.id.bbmsw:
                    if(isChecked){
                        ebmsw.setChecked(false);
                        obmsw.setChecked(false);
                        pmsw.setChecked(false);
                    }
                    break;
                case R.id.obmsw:
                    if(isChecked){
                        ebmsw.setChecked(false);
                        bbmsw.setChecked(false);
                        pmsw.setChecked(false);
                    }
                    break;
                case R.id.pmsw:
                    if(isChecked){
                        ebmsw.setChecked(false);
                        bbmsw.setChecked(false);
                        obmsw.setChecked(false);
                    }
                    break;
            }
        }
    };

    private static String getKernelVersion() {
        try {
            Process p = Runtime.getRuntime().exec("uname -a");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            Log.i("Kernel Version", line);
            br.close();
            return line;
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
            case R.id.ebm:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

                execCommandLine("echo 40 > /proc/sys/vm/swappiness \n" +
                        "echo \" + \"\\\"noop\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                        "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                        "echo 512 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                        "echo powersave > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                        "echo 1401600 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                        "echo 1401600 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mprogress.dismiss();
                    }
                },1000);
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bbm:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

                execCommandLine("echo 40 > /proc/sys/vm/swappiness \n" +
                        "echo \" + \"\\\"cfq\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                        "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                        "echo 512 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                        "echo powersave > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                        "echo 1536000 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                        "echo 1747200 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mprogress.dismiss();
                    }
                },1000);
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bm:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

                execCommandLine("echo 40 > /proc/sys/vm/swappiness \n" +
                        "echo \" + \"\\\"anxiety\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                        "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                        "echo 2048 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                        "echo powersave > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                        "echo 1612800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                        "echo 1804800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mprogress.dismiss();
                    }
                },1000);
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pm:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

                execCommandLine("echo 40 > /proc/sys/vm/swappiness \n" +
                        "echo \" + \"\\\"anxiety\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler \n" +
                        "echo 2 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost \n" +
                        "echo 2048 > /sys/block/mmcblk0/queue/read_ahead_kb \n" +
                        "echo msm-adreno-tz > /sys/class/kgsl/kgsl-3d0/devfreq/governor \n" +
                        "echo 1612800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq \n" +
                        "echo 1804800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mprogress.dismiss();
                    }
                },1000);
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_gm:
                if(!gmsw.isChecked()){
                    gmsw.setChecked(true);
                }else{
                    gmsw.setChecked(false);
                }
                break;
            case R.id.ll_as:
                if(!dozesw.isChecked()){
                    dozesw.setChecked(true);
                }else{
                    dozesw.setChecked(false);
                }
                break;
            case R.id.ll_mon:
                if(!monsw.isChecked()){
                    monsw.setChecked(true);
                }else{
                    monsw.setChecked(false);
                }
                break;
            case R.id.ll_kill:
                if(!killsw.isChecked()){
                    killsw.setChecked(true);
                }else{
                    killsw.setChecked(false);
                }
                break;
            case R.id.ll_ad:
                if(!adsw.isChecked()){
                    adsw.setChecked(true);
                }else{
                    adsw.setChecked(false);
                }
                break;
        }
    }

    public void doze()
    {
        ContentResolver.setMasterSyncAutomatically(true);
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
    }

    void monitor(){
        new DebugOverlay.Builder(getActivity().getApplication())
                .modules(new CpuUsageModule(),
                        new MemInfoModule(getContext()),
                        new FpsModule())
                .position(Position.TOP_END)
                .bgColor(Color.parseColor("#60000000"))
                .textColor(Color.WHITE)
                .textSize(14f)
                .textAlpha(.8f)
                .allowSystemLayer(true)
                .notification(true,getActivity().getPackageName())
                .build()
                .install();
    }

    void kill()
    {
        //Warning Its Experiment only as i have found this apps continuously uses connections
        execCommandLine("adb shell"+ "\n"+
                "am force-stop com.android.chrome"+"\n"+
                "am force-stop com.google.android.gm"+"\n"+
                "am force-stop com.UCMobile.intl"+"\n"+
                "am force-stop cn.xender"+"\n"+
                "am force-stop com.snaptube.premium"+"\n"+
                "am force-stop com.google.android.app.photos"+"\n"+
                "am force-stop com.google.android.googlequicksearchbox");
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
}