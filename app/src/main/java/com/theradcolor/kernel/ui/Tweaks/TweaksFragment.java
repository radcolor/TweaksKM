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
    private CardView eb,bb,bal,pm;
    private CheckBox dozesw,killsw,monsw;
    private Switch gmsw;
    final Handler handler = new Handler();

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(TweaksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tweaks, container, false);
        textView = root.findViewById(R.id.kernel_info);
        textView.setText(" " + getKernelVersion());
        eb = root.findViewById(R.id.cv_eb);
        bb = root.findViewById(R.id.cv_bb);
        bal = root.findViewById(R.id.cv_bal);
        pm = root.findViewById(R.id.cv_pm);

        monitor();

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

    public static String getKernelVersion() {
        try {
            Process p = Runtime.getRuntime().exec("getprop | grep ro.product.system.name");
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
            case R.id.cv_eb:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

                execCommandLine("echo 40 > /proc/sys/vm/swappiness\n" +
                        "echo \" + \"\\\"noop\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler\n" +
                        "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost\n" +
                        "echo 512 > /sys/block/mmcblk0/queue/read_ahead_kb\n" +
                        "echo powersave > /sys/class/kgsl/kgsl-3d0/devfreq/governor\n" +
                        "echo 1401600 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n" +
                        "echo 1401600 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                mprogress.dismiss();
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cv_bb:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

               execCommandLine("echo 40 > /proc/sys/vm/swappiness\n" +
                       "echo \" + \"\\\"cfq\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler\n" +
                       "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost\n" +
                       "echo 512 > /sys/block/mmcblk0/queue/read_ahead_kb\n" +
                       "echo powersave > /sys/class/kgsl/kgsl-3d0/devfreq/governor\n" +
                       "echo 1536000 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n" +
                       "echo 1747200 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                mprogress.dismiss();
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_bal:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

                execCommandLine("echo 40 > /proc/sys/vm/swappiness\n" +
                        "echo \" + \"\\\"anxiety\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler\n" +
                        "echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost\n" +
                        "echo 2048 > /sys/block/mmcblk0/queue/read_ahead_kb\n" +
                        "echo powersave > /sys/class/kgsl/kgsl-3d0/devfreq/governor\n" +
                        "echo 1612800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n" +
                        "echo 1804800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                mprogress.dismiss();
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_pm:
                mprogress = new ProgressDialog(getContext());
                mprogress.setMessage("Applying Profile...");
                mprogress.show();
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.setCancelable(false);

                execCommandLine("echo 40 > /proc/sys/vm/swappiness\n" +
                        "echo \" + \"\\\"anxiety\\\"\" + \" > /sys/block/mmcblk0/queue/scheduler\n" +
                        "echo 2 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost\n" +
                        "echo 2048 > /sys/block/mmcblk0/queue/read_ahead_kb\n" +
                        "echo msm-adreno-tz > /sys/class/kgsl/kgsl-3d0/devfreq/governor\n" +
                        "echo 1612800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq\n" +
                        "echo 1804800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq");

                mprogress.dismiss();
                Toast.makeText(getContext(),"Successful", Toast.LENGTH_SHORT).show();
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