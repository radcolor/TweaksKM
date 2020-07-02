package com.theradcolor.kernel.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.utils.kernel.KCAL;

import java.util.List;

public class KcalActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    KCAL mKCAL;
    private SeekBar red,green,blue,sat,val,con,hue;
    private TextView rTXT,gTXT,bTXT,satTXT,valTXT,conTXT,hueTXT;
    private LinearLayout pre_kcal,reset_kcal;

    int RED_DEFAULT = 256;
    int GREEN_DEFAULT = 256;
    int BLUE_DEFAULT = 256;
    int CONTRAST_DEFAULT = 255;
    int HUE_DEFAULT = 0;
    int MIN_DEFAULT = 35;
    int SATURATION_DEFAULT = 255;
    int VALUE_DEFAULT = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kcal);

        InitViews();
        InitUI();
        refreshUI();
    }

    public void InitViews(){
        mKCAL = new KCAL();
        red = findViewById(R.id.kcal_r); red.setOnSeekBarChangeListener(this);
        green = findViewById(R.id.kcal_g); green.setOnSeekBarChangeListener(this);
        blue = findViewById(R.id.kcal_b); blue.setOnSeekBarChangeListener(this);
        sat = findViewById(R.id.kcal_sat); sat.setOnSeekBarChangeListener(this);
        val = findViewById(R.id.kcal_val); val.setOnSeekBarChangeListener(this);
        con = findViewById(R.id.kcal_cont); con.setOnSeekBarChangeListener(this);
        hue = findViewById(R.id.kcal_hue); hue.setOnSeekBarChangeListener(this);
        rTXT = findViewById(R.id.r_txt);
        gTXT = findViewById(R.id.g_txt);
        bTXT = findViewById(R.id.b_txt);
        satTXT = findViewById(R.id.sat_txt);
        valTXT = findViewById(R.id.val_txt);
        conTXT = findViewById(R.id.cont_txt);
        hueTXT = findViewById(R.id.hue_txt);
        pre_kcal = findViewById(R.id.ll_color);
        pre_kcal.setOnClickListener(this);
        reset_kcal = findViewById(R.id.ll_color_reset);
        reset_kcal.setOnClickListener(this);
    }

    public void InitUI(){
        red.setMax(256);
        green.setMax(256);
        blue.setMax(256);
        sat.setMax(158);
        val.setMax(255);
        con.setMax(255);
        hue.setMax(1536);
    }

    @SuppressLint("SetTextI18n")
    public void refreshUI(){
        int saturation = mKCAL.getSaturationIntensity();
        red.setProgress(Integer.parseInt(mKCAL.getColors().get(0)));
        green.setProgress(Integer.parseInt(mKCAL.getColors().get(1)));
        blue.setProgress(Integer.parseInt(mKCAL.getColors().get(2)));
        sat.setProgress(saturation == 128 ? 30 : saturation - 225);
        val.setProgress(mKCAL.getScreenValue() - 128);
        con.setProgress(mKCAL.getScreenContrast() - 128);
        hue.setProgress(mKCAL.getScreenHue());
        rTXT.setText(""+mKCAL.getColors().get(0));
        gTXT.setText(""+mKCAL.getColors().get(1));
        bTXT.setText(""+mKCAL.getColors().get(2));
        satTXT.setText(""+(saturation == 128 ? 30 : saturation - 225));
        valTXT.setText(""+(mKCAL.getScreenValue() - 128));
        conTXT.setText(""+(mKCAL.getScreenContrast() - 128));
        hueTXT.setText(""+mKCAL.getScreenHue());
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
            case R.id.ll_color:
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert));
                builder.setTitle("Select Color Preference");

                String[] colors = {"Deep Natural Display",
                        "Triluminos Display",
                        "Cool Amoled Display",
                        "Extreme Amoled Display",
                        "Hybrid Mamba Display",
                        "Warm Amoled Display",
                        "Deep Black & White Display"};
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                RootUtils.runCommand("echo 256 250 251 > /sys/devices/platform/kcal_ctrl.0/kcal");
                                RootUtils.runCommand("echo 264 > /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                                RootUtils.runCommand("echo 0 > /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                                RootUtils.runCommand("echo 35 > /sys/devices/platform/kcal_ctrl.0/kcal_min");
                                RootUtils.runCommand("echo 285 > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                                RootUtils.runCommand("echo 245 > /sys/devices/platform/kcal_ctrl.0/kcal_val");
                                break;
                            case 1:
                                RootUtils.runCommand("echo 256 250 251 > /sys/devices/platform/kcal_ctrl.0/kcal");
                                RootUtils.runCommand("echo 260 > /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                                RootUtils.runCommand("echo 1526 > /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                                RootUtils.runCommand("echo 35 > /sys/devices/platform/kcal_ctrl.0/kcal_min");
                                RootUtils.runCommand("echo 291 > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                                RootUtils.runCommand("echo 264 > /sys/devices/platform/kcal_ctrl.0/kcal_val");
                                break;
                            case 2:
                                RootUtils.runCommand("echo 236 248 256 > /sys/devices/platform/kcal_ctrl.0/kcal");
                                RootUtils.runCommand("echo 264 > /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                                RootUtils.runCommand("echo 0 > /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                                RootUtils.runCommand("echo 35 > /sys/devices/platform/kcal_ctrl.0/kcal_min");
                                RootUtils.runCommand("echo 275 > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                                RootUtils.runCommand("echo 242 > /sys/devices/platform/kcal_ctrl.0/kcal_val");
                                break;
                            case 3:
                                RootUtils.runCommand("echo 256 256 256 > /sys/devices/platform/kcal_ctrl.0/kcal");
                                RootUtils.runCommand("echo 255 > /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                                RootUtils.runCommand("echo 0 > /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                                RootUtils.runCommand("echo 35 > /sys/devices/platform/kcal_ctrl.0/kcal_min");
                                RootUtils.runCommand("echo 290 > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                                RootUtils.runCommand("echo 255 > /sys/devices/platform/kcal_ctrl.0/kcal_val");
                                break;
                            case 4:
                                RootUtils.runCommand("echo 226 215 256 > /sys/devices/platform/kcal_ctrl.0/kcal");
                                RootUtils.runCommand("echo 260 > /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                                RootUtils.runCommand("echo 10 > /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                                RootUtils.runCommand("echo 35 > /sys/devices/platform/kcal_ctrl.0/kcal_min");
                                RootUtils.runCommand("echo 265 > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                                RootUtils.runCommand("echo 247 > /sys/devices/platform/kcal_ctrl.0/kcal_val");
                                break;
                            case 5:
                                RootUtils.runCommand("echo 253 246 243 > /sys/devices/platform/kcal_ctrl.0/kcal");
                                RootUtils.runCommand("echo 258 > /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                                RootUtils.runCommand("echo 0 > /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                                RootUtils.runCommand("echo 35 > /sys/devices/platform/kcal_ctrl.0/kcal_min");
                                RootUtils.runCommand("echo 275 > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                                RootUtils.runCommand("echo 251 > /sys/devices/platform/kcal_ctrl.0/kcal_val");
                                break;
                            case 6:
                                RootUtils.runCommand("echo 250 250 256 > /sys/devices/platform/kcal_ctrl.0/kcal");
                                RootUtils.runCommand("echo 266 > /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                                RootUtils.runCommand("echo 1526 > /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                                RootUtils.runCommand("echo 35 > /sys/devices/platform/kcal_ctrl.0/kcal_min");
                                RootUtils.runCommand("echo 279 > /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                                RootUtils.runCommand("echo 261 > /sys/devices/platform/kcal_ctrl.0/kcal_val");
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.ll_color_reset:
                RootUtils.runCommand("echo " + RED_DEFAULT+ " " + GREEN_DEFAULT + " " + BLUE_DEFAULT+ " " + ">" + " /sys/devices/platform/kcal_ctrl.0/kcal");
                RootUtils.runCommand("echo " + CONTRAST_DEFAULT + " >" +" /sys/devices/platform/kcal_ctrl.0/kcal_cont");
                RootUtils.runCommand("echo " + HUE_DEFAULT + " >" +" /sys/devices/platform/kcal_ctrl.0/kcal_hue");
                RootUtils.runCommand("echo " + MIN_DEFAULT + " >" +" /sys/devices/platform/kcal_ctrl.0/kcal_min");
                RootUtils.runCommand("echo " + SATURATION_DEFAULT + " >" +" /sys/devices/platform/kcal_ctrl.0/kcal_sat");
                RootUtils.runCommand("echo " + VALUE_DEFAULT + " >" +" /sys/devices/platform/kcal_ctrl.0/kcal_val");
                refreshUI();
                break;
        }
    }
    int r,g,b;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.kcal_r:
                rTXT.setText(""+progress);
                r = progress;
                break;
            case R.id.kcal_g:
                gTXT.setText(""+progress);
                g = progress;
                break;
            case R.id.kcal_b:
                bTXT.setText(""+progress);
                b = progress;
                break;
            case R.id.kcal_sat:
                satTXT.setText(""+progress);
                mKCAL.setSaturationIntensity(progress,this);
                break;
            case R.id.kcal_val:
                valTXT.setText(""+progress);
                mKCAL.setScreenValue(progress, this);
                break;
            case R.id.kcal_cont:
                conTXT.setText(""+progress);
                mKCAL.setScreenContrast(progress, this);
                break;
            case R.id.kcal_hue:
                hueTXT.setText(""+progress);
                mKCAL.setScreenHue(progress, this);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mKCAL.setColors(r + " " + g + " " + b, this);
    }
}
