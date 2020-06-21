package com.theradcolor.kernel.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.KcalActivity;
import com.theradcolor.kernel.R;


public class KernelFragment extends Fragment implements View.OnClickListener{

    private TextView vib;
    private SeekBar seekBar;
    private LinearLayout srgb,kcal,vibration,hpg,mcg;
    int progressChangedValue = 1;
    public static final int MIN_VIBRATION = 116;
    public static final int MAX_VIBRATION = 3596;
    int  vibrationValue;
    SharedPreferences.Editor editor;
    private Switch vibsw,srgbsw;
    SharedPreferences preferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_kernel, container, false);
        seekBar = root.findViewById(R.id.vibration);
        seekBar.setPadding(16,16,16,16);
        vib = root.findViewById(R.id.pervib);
        srgb = root.findViewById(R.id.ll_srgb);
        kcal = root.findViewById(R.id.ll_kcal);
        vibration = root.findViewById(R.id.ll_vib);
        vibsw = root.findViewById(R.id.vibsw);
        vibsw.setOnCheckedChangeListener(myCheckboxListener);
        srgbsw = root.findViewById(R.id.srgbsw);
        srgbsw.setOnCheckedChangeListener(myCheckboxListener);

        hpg = root.findViewById(R.id.ll_hpg);
        hpg.setOnClickListener(this);
        mcg = root.findViewById(R.id.ll_mcg);
        mcg.setOnClickListener(this);

        preferences = getActivity().getSharedPreferences("preferences",Context.MODE_PRIVATE);
        vib.setText(preferences.getInt("vibration",1)+getString(R.string.percent));
        seekBar.setProgress(preferences.getInt("vibration",1));

        srgb.setOnClickListener(this);
        kcal.setOnClickListener(this);
        vibration.setOnClickListener(this);

        final Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                vib.setText(progress + getString(R.string.percent));
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrationValue = (int) (progress / 100.0 * (MAX_VIBRATION - MIN_VIBRATION) + MIN_VIBRATION);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                RootUtils.runCommand("echo " +vibrationValue+ " > /sys/devices/virtual/timed_output/vibrator/vtg_level");
                v.cancel();
                editor = preferences.edit();
                editor.putInt("vibration",progressChangedValue);
                editor.apply();
                v.vibrate(50);
            }
        });
        setsw();
        if(setvib()==-1){
            seekBar.setEnabled(false);
            vib.setText(" ");
            vibsw.setEnabled(false);
        }else{
            seekBar.setProgress(setvib());
        }
        return root;
    }

    private int setvib(){
        String vib = RootUtils.runCommand("cat /sys/devices/virtual/timed_output/vibrator/vtg_level");
        if(vib==null){
            return -1;
        }
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
            }
        }
    };

    private int getVibration() {
      String vib = RootUtils.runCommand("cat /sys/devices/virtual/timed_output/vibrator/vtg_level");
      int vibval = Integer.parseInt(vib);
      return vibval;
    }

    private void srgb()
    {
        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert))
                .setTitle("sRGB colors")
                .setPositiveButton("On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RootUtils.runCommand("active=1\n" +
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
                        RootUtils.runCommand("active=0\n" +
                                "\n" +
                                "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                                "\n" +
                                "if [ $active = \"1\" ]\n" +
                                "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "fi");
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
            case R.id.ll_srgb:
                srgb();
                break;
            case R.id.ll_kcal:
                startActivity(new Intent(getContext(), KcalActivity.class));
                break;
            case R.id.ll_vib:
                break;
            case R.id.ll_hpg:
                hpgDialog(getView());
                break;
            case R.id.ll_mcg:
                mcgDialog(getView());
                break;
        }
    }

    public void hpgDialog(View view) {        // create an alert builder
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert));
        builder.setTitle(R.string.title_hp_gain);        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.headgain_dialog, null);
        builder.setView(customLayout);        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
            }
        });        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void mcgDialog(View view) {        // create an alert builder
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert));
        builder.setTitle(R.string.title_mp_gain);        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.micgain_dialog, null);
        builder.setView(customLayout);        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
            }
        });        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}