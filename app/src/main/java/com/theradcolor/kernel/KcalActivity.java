package com.theradcolor.kernel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.RootUtils;

public class KcalActivity extends AppCompatActivity implements View.OnClickListener{

    private SeekBar red,green,blue,sat,val,con,hue;
    private LinearLayout pre_kcal,reset_kcal;

    String KCAL_ENABLE = "/sys/devices/platform/kcal_ctrl.0/kcal_enable";
    String KCAL_CONT = "/sys/devices/platform/kcal_ctrl.0/kcal_cont";
    String KCAL_HUE = "/sys/devices/platform/kcal_ctrl.0/kcal_hue";
    String KCAL_MIN = "/sys/devices/platform/kcal_ctrl.0/kcal_min";
    String KCAL_RGB = "/sys/devices/platform/kcal_ctrl.0/kcal";
    String KCAL_SAT = "/sys/devices/platform/kcal_ctrl.0/kcal_sat";
    String KCAL_VAL = "/sys/devices/platform/kcal_ctrl.0/kcal_val";

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
        red = findViewById(R.id.kcal_r);
        green = findViewById(R.id.kcal_g);
        blue = findViewById(R.id.kcal_b);
        sat = findViewById(R.id.kcal_sat);
        val = findViewById(R.id.kcal_val);
        con = findViewById(R.id.kcal_cont);
        hue = findViewById(R.id.kcal_hue);

        pre_kcal = findViewById(R.id.ll_color);
        pre_kcal.setOnClickListener(this);
        reset_kcal = findViewById(R.id.ll_color_reset);
        reset_kcal.setOnClickListener(this);
        set_kcal();
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
                break;
        }
    }

    public void set_kcal(){

    }

    public int getScreenContrast() {
        return Utils.strToInt(Utils.readFile(KCAL_CONT));
    }
    public int getScreenValue() {
        return Utils.strToInt(Utils.readFile(KCAL_VAL));
    }
    public int getScreenHue() {
        return Utils.strToInt(Utils.readFile(KCAL_HUE));
    }
    public int getSaturationIntensity() {
        return Utils.strToInt(Utils.readFile(KCAL_SAT));
    }

}
