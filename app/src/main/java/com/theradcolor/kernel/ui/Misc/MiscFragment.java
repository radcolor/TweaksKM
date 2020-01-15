package com.theradcolor.kernel.ui.Misc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.theradcolor.kernel.KcalActivity;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.RootUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MiscFragment extends Fragment implements View.OnClickListener{

    private MiscViewModel dashboardViewModel;
    private TextView vib;
    private SeekBar seekBar;
    private LinearLayout srgb,kcal,vibration;
    int progressChangedValue = 1;
    public static final int MIN_VIBRATION = 116;
    public static final int MAX_VIBRATION = 3596;
    int  vibrationValue;
    SharedPreferences.Editor editor;
    private Switch vibsw,srgbsw,kcalsw;
    SharedPreferences preferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(MiscViewModel.class);
        View root = inflater.inflate(R.layout.fragment_misc, container, false);
        seekBar = root.findViewById(R.id.vibration);
        seekBar.setPadding(16,16,16,16);
        vib = root.findViewById(R.id.pervib);
        srgb = root.findViewById(R.id.ll_srgb);
        //kcal = root.findViewById(R.id.ll_kcal);
        vibration = root.findViewById(R.id.ll_vib);
        vibsw = root.findViewById(R.id.vibsw);
        vibsw.setOnCheckedChangeListener(myCheckboxListener);
        srgbsw = root.findViewById(R.id.srgbsw);
        srgbsw.setOnCheckedChangeListener(myCheckboxListener);
        //kcalsw = root.findViewById(R.id.kcalsw);
        //kcalsw.setOnCheckedChangeListener(myCheckboxListener);

        preferences = getActivity().getSharedPreferences("preferences",Context.MODE_PRIVATE);
        vib.setText(""+preferences.getInt("vibration",1));
        seekBar.setProgress(preferences.getInt("vibration",1));

        srgb.setOnClickListener(this);
        //kcal.setOnClickListener(this);
        vibration.setOnClickListener(this);

        final Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                vib.setText(progress + "%");
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrationValue = (int) (progress / 100.0 * (MAX_VIBRATION - MIN_VIBRATION) + MIN_VIBRATION);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                /*// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    long[] pattern = {1, 1000, 0};
                    v.vibrate(VibrationEffect.createWaveform(pattern,1));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }*/

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                execCommandLine("echo " +vibrationValue+ " > /sys/devices/virtual/timed_output/vibrator/vtg_level");
                v.cancel();
                editor = preferences.edit();
                editor.putInt("vibration",progressChangedValue);
                editor.apply();
            }
        });
        setsw();
        seekBar.setProgress(setvib());
        return root;
    }

    private int setvib(){
        String vib = RootUtils.runCommand("cat /sys/devices/virtual/timed_output/vibrator/vtg_level");
        int hapticval = Integer.parseInt(vib);
        int x = (int) ((hapticval * 100.0 - 100.0 * MIN_VIBRATION) /  (MAX_VIBRATION - MIN_VIBRATION));
        return x;
    }

    private void setsw(){
        if(preferences.getBoolean("vibsw",false)){
            vibsw.setChecked(true);
        }
        if(preferences.getBoolean("srgbsw",false)){
            srgbsw.setChecked(true);
        }
    }

    private CompoundButton.OnCheckedChangeListener myCheckboxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.vibsw:
                    if(vibsw.isChecked()){
                        editor = preferences.edit();
                        editor.putBoolean("vibsw",true);
                        editor.putInt("vibval",getVibration());
                        editor.apply();
                    }else{
                        editor = preferences.edit();
                        editor.putBoolean("vibsw",false);
                        editor.apply();
                    }
                    break;
                case R.id.srgbsw:
                    if(srgbsw.isChecked()){
                        editor = preferences.edit();
                        editor.putBoolean("srgbsw",true);
                        editor.apply();
                    }else{
                        editor = preferences.edit();
                        editor.putBoolean("srgbsw",false);
                        editor.apply();
                    }
                    break;
                /*case R.id.kcalsw:
                    Toast.makeText(getContext(),"kcal: set on boot true",Toast.LENGTH_SHORT).show();
                    break;*/
            }
        }
    };

    private void execCommandLine(String command)
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

    private int getVibration() {
      String vib = RootUtils.runCommand("cat /sys/devices/virtual/timed_output/vibrator/vtg_level");
      int vibval = Integer.parseInt(vib);
      return vibval;
    }

    private void srgb()
    {
        new AlertDialog.Builder(getContext())
                .setTitle("sRGB colors")
                .setPositiveButton("On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        execCommandLine("active=1\n" +
                                "\n" +
                                "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                                "\n" +
                                "if [ $active = \"1\" ]\n" +
                                "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "fi");
                    }
                })
                .setNegativeButton("Off", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        execCommandLine("active=0\n" +
                                "\n" +
                                "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                                "\n" +
                                "if [ $active = \"1\" ]\n" +
                                "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "fi");
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
            case R.id.ll_srgb:
                srgb();
                break;
            /*case R.id.ll_kcal:
                startActivity(new Intent(getContext(), KcalActivity.class));
                break;*/
            case R.id.ll_vib:

                break;
        }
    }
}