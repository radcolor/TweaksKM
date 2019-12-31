package com.theradcolor.kernel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;

public class KcalActivity extends AppCompatActivity {

    private SeekBar red,green,blue,sat,val,con,hue;
    String KCAL_ENABLE = "/sys/devices/platform/kcal_ctrl.0/kcal_enable";
    String KCAL_CONT = "/sys/devices/platform/kcal_ctrl.0/kcal_cont";
    String KCAL_HUE = "/sys/devices/platform/kcal_ctrl.0/kcal_hue";
    String KCAL_MIN = "/sys/devices/platform/kcal_ctrl.0/kcal_min";
    String KCAL_RGB = "/sys/devices/platform/kcal_ctrl.0/kcal";
    String KCAL_SAT = "/sys/devices/platform/kcal_ctrl.0/kcal_sat";
    String KCAL_VAL = "/sys/devices/platform/kcal_ctrl.0/kcal_val";

    int RED_DEFAULT = 255;
    int GREEN_DEFAULT = 255;
    int BLUE_DEFAULT = 255;
    int SATURATION_DEFAULT = 35;
    int SATURATION_OFFSET = 225;
    int VALUE_DEFAULT = 127;
    int VALUE_OFFSET = 128;
    int CONTRAST_DEFAULT = 127;
    int CONTRAST_OFFSET = 128;
    int HUE_DEFAULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kcal);
        red = findViewById(R.id.kcal_r);
        green = findViewById(R.id.kcal_g);
        blue = findViewById(R.id.kcal_b);
        red.setPadding(16,16,16,16);
        green.setPadding(16,16,16,16);
        blue.setPadding(16,16,16,16);

        sat = findViewById(R.id.kcal_sat);
        sat.setPadding(16,16,16,16);
        val = findViewById(R.id.kcal_val);
        val.setPadding(16,16,16,16);
        con = findViewById(R.id.kcal_con);
        con.setPadding(16,16,16,16);
        hue = findViewById(R.id.kcal_hue);
        hue.setPadding(16,16,16,16);

    }
}
