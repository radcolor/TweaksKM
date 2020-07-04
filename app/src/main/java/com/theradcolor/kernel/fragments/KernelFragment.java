package com.theradcolor.kernel.fragments;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.grarak.kerneladiutor.utils.Prefs;
import com.grarak.kerneladiutor.utils.Utils;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.activities.KcalActivity;
import com.theradcolor.kernel.activities.SpectrumActivity;
import com.theradcolor.kernel.utils.kernel.Battery;
import com.theradcolor.kernel.utils.kernel.Network;
import com.theradcolor.kernel.utils.kernel.Sound;
import com.theradcolor.kernel.utils.kernel.Vibration;
import com.theradcolor.kernel.utils.kernel.sRGB;

@SuppressLint("SetTextI18n")
public class KernelFragment extends Fragment implements View.OnClickListener{

    Network mNetwork; Battery mBattery; Sound mSound; Vibration mVibration;
    View root;
    TextView vib,ffc,tcp;
    SeekBar seekBar;
    LinearLayout sRGB, kCAL, spectrum, vibration, ll_ffc, ll_tcp, hpg, mcg;
    int progressChangedValue = 1;
    SharedPreferences.Editor editor;
    SwitchMaterial vibSW, srgbSW, hpgSW, mpgSW, ffcSW, tcpSW;
    SharedPreferences preferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_kernel, container, false);

        InitView();
        InitUI();

        final Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                vib.setText(progress + getResources().getString(R.string.percent));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                mVibration.setVibrationValue(progressChangedValue, getContext());
                Prefs.saveInt("vibration_value", progressChangedValue, getContext());
                v.vibrate(50);
            }
        });
        setSW();
        return root;
    }

    public void InitView(){
        seekBar = root.findViewById(R.id.vibration);
        vib = root.findViewById(R.id.pervib);
        ffc = root.findViewById(R.id.txt_ffc);
        tcp = root.findViewById(R.id.txt_tcp);
        sRGB = root.findViewById(R.id.ll_srgb);
        kCAL = root.findViewById(R.id.ll_kcal);
        spectrum = root.findViewById(R.id.ll_spec);
        vibration = root.findViewById(R.id.ll_vib);
        ll_ffc = root.findViewById(R.id.ll_ffc);
        ll_tcp = root.findViewById(R.id.ll_tcp);
        vibSW = root.findViewById(R.id.vibsw);
        srgbSW = root.findViewById(R.id.srgbsw);
        hpgSW = root.findViewById(R.id.hgsw);
        mpgSW = root.findViewById(R.id.mgsw);
        ffcSW = root.findViewById(R.id.ffcsw);
        tcpSW = root.findViewById(R.id.tcpsw);

        mBattery = new Battery();
        mNetwork = new Network();
        mSound = new Sound();
        mVibration = new Vibration();

        hpg = root.findViewById(R.id.ll_hpg);
        mcg = root.findViewById(R.id.ll_mcg);
        hpg.setOnClickListener(this);
        mcg.setOnClickListener(this);
    }

    public void InitUI(){
        seekBar.setPadding(16,16,16,16);
        spectrum.setOnClickListener(this);
        vibSW.setOnCheckedChangeListener(myCheckboxListener);
        srgbSW.setOnCheckedChangeListener(myCheckboxListener);
        hpgSW.setOnCheckedChangeListener(myCheckboxListener);
        mpgSW.setOnCheckedChangeListener(myCheckboxListener);
        ffcSW.setOnCheckedChangeListener(myCheckboxListener);
        tcpSW.setOnCheckedChangeListener(myCheckboxListener);
        vib.setText(mVibration.getVibrationValue()+getResources().getString(R.string.percent));
        seekBar.setProgress(mVibration.getVibrationValue());
        sRGB.setOnClickListener(this);
        kCAL.setOnClickListener(this);
        vibration.setOnClickListener(this);
        ll_ffc.setOnClickListener(this);
        ll_tcp.setOnClickListener(this);
        ffc.setText(Battery.FastChargeStatus());
        tcp.setText(Network.getTcpCongestion());
    }

    public void refreshUI(){
        vib.setText(mVibration.getVibrationValue()+getResources().getString(R.string.percent));
        seekBar.setProgress(mVibration.getVibrationValue());
        ffc.setText(Battery.FastChargeStatus());
        tcp.setText(Network.getTcpCongestion());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
    }

    private void setSW(){
        if(Prefs.getBoolean("vibration", false, getContext())){
            vibSW.setChecked(true);
        }
        if(Prefs.getBoolean("sRGB", false, getContext())){
            srgbSW.setChecked(true);
        }
        if(Prefs.getBoolean("hpGain", false, getContext())){
            hpgSW.setChecked(true);
        }
        if(Prefs.getBoolean("mpGain", false, getContext())){
            mpgSW.setChecked(true);
        }
        if(Prefs.getBoolean("ffc", false, getContext())){
            ffcSW.setChecked(true);
        }
        if(Prefs.getBoolean("tcp", false, getContext())){
            tcpSW.setChecked(true);
        }
    }

    private CompoundButton.OnCheckedChangeListener myCheckboxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.vibsw:
                    if(vibSW.isChecked()){ Prefs.saveBoolean("vibration", true, getContext()); }
                    else{ Prefs.saveBoolean("vibration", false, getContext()); }
                    break;
                case R.id.srgbsw:
                    if(srgbSW.isChecked()){ Prefs.saveBoolean("sRGB", true, getContext()); }
                    else{ Prefs.saveBoolean("sRGB", false, getContext()); }
                    break;
                case R.id.hgsw:
                    if(hpgSW.isChecked()){ Prefs.saveBoolean("hpGain", true, getContext()); }
                    else{ Prefs.saveBoolean("hpGain", false, getContext()); }
                    break;
                case R.id.mgsw:
                    if(mpgSW.isChecked()){ Prefs.saveBoolean("mpGain", true, getContext()); }
                    else{ Prefs.saveBoolean("mpGain", false, getContext()); }
                    break;
                case R.id.ffcsw:
                    if(ffcSW.isChecked()){ Prefs.saveBoolean("ffc", true, getContext());}
                    else{ Prefs.saveBoolean("ffc", false, getContext()); }
                    break;
                case R.id.tcpsw:
                    if(tcpSW.isChecked()){ Prefs.saveBoolean("tcp", true, getContext());}
                    else{ Prefs.saveBoolean("tcp", false, getContext()); }
                    break;
            }
        }
    };

    private void srgb()
    {
        com.theradcolor.kernel.utils.kernel.sRGB sRGB = new sRGB();
        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomDialogTheme))
                .setTitle("sRGB colors")
                .setPositiveButton("On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       sRGB.srgbON();
                       Prefs.saveBoolean("srgb_value", true, getContext());
                    }
                })
                .setNegativeButton("Off", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sRGB.srgbOFF();
                        Prefs.saveBoolean("srgb_value", false, getContext());
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
            case R.id.ll_spec:
                startActivity(new Intent(getContext(), SpectrumActivity.class));
                break;
            case R.id.ll_vib:
                break;
            case R.id.ll_ffc:
                if(Battery.FastChargeStatus().equals("Enabled")){
                    mBattery.ForceFastChargeEnable(false, getContext());
                    Prefs.saveBoolean("ffc_value", false, getContext());
                }else if(Battery.FastChargeStatus().equals("Disabled")){
                    mBattery.ForceFastChargeEnable(true, getContext());
                    Prefs.saveBoolean("ffc_value", true, getContext());
                }
                refreshUI();
                break;
            case R.id.ll_tcp:
                tcpDialog(getView());
                break;
            case R.id.ll_hpg:
                hpgDialog(getView());
                break;
            case R.id.ll_mcg:
                mcgDialog(getView());
                break;
        }
    }

    SeekBar hp_lft_sb, hp_rgt_sb, mp_sb;
    TextView hp_lft_txt, hp_rgt_txt, mp_txt;

    public void hpgDialog(View view) {        // create an alert builder
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomDialogTheme));
        builder.setTitle(R.string.title_hp_gain);        // set the custom layout
        final View hpLayout = getLayoutInflater().inflate(R.layout.headgain_dialog, null);

        hp_lft_sb = hpLayout.findViewById(R.id.hp_left);
        hp_rgt_sb = hpLayout.findViewById(R.id.hp_right);
        hp_lft_sb.setProgress(Utils.strToInt(mSound.getHeadphoneFlar("left")));
        hp_rgt_sb.setProgress(Utils.strToInt(mSound.getHeadphoneFlar("right")));
        hp_lft_txt = hpLayout.findViewById(R.id.hp_left_txt);
        hp_rgt_txt = hpLayout.findViewById(R.id.hp_right_txt);
        hp_lft_txt.setText(""+mSound.getHeadphoneFlar("left"));
        hp_rgt_txt.setText(""+mSound.getHeadphoneFlar("right"));

        hp_lft_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hp_lft_txt.setText(""+progress);
                mSound.setHeadphoneFlar("left", ""+progress, getContext());
                Prefs.saveInt("hp_left_value", progress, getContext());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        hp_rgt_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hp_rgt_txt.setText(""+progress);
                mSound.setHeadphoneFlar("right", ""+progress, getContext());
                Prefs.saveInt("hp_right_value", progress, getContext());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setView(hpLayout);        // add a button
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void mcgDialog(View view) {        // create an alert builder
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomDialogTheme));
        builder.setTitle(R.string.title_mp_gain);        // set the custom layout
        final View mpLayout = getLayoutInflater().inflate(R.layout.micgain_dialog, null);
        mp_sb = mpLayout.findViewById(R.id.mp_gain);
        mp_sb.setProgress(Utils.strToInt(mSound.getMicrophoneFlar()));

        mp_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mp_txt.setText(""+progress);
                mSound.setMicrophoneFlar(""+progress,getContext());
                Prefs.saveInt("mp_value", progress, getContext());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mp_txt = mpLayout.findViewById(R.id.mp_txt);
        mp_txt.setText(""+mSound.getMicrophoneFlar());
        builder.setView(mpLayout);        // add a button
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void tcpDialog(View view) {        // create an alert builder
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.CustomDialogTheme));
        builder.setTitle("TCP congestion algorithm");
        int selected_tcp=0;
        String[] tcps = Network.getTcpAvailableCongestions().toArray(new String[0]);
        builder.setSingleChoiceItems(tcps, selected_tcp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNetwork.setTcpCongestion(tcps[which], getContext());
                Prefs.saveString("tcp_congestion_algorithm", tcps[which], getContext());
                refreshUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}