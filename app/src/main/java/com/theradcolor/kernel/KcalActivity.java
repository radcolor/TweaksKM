package com.theradcolor.kernel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;

public class KcalActivity extends AppCompatActivity {

    private SeekBar red,green,blue,sat,val,con,hue;

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
