package com.theradcolor.kernel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView eb,bb,bal,gm,pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vardev();
        if(checkRoot.isDeviceRooted() && System.getProperty("os.version").contains("rad")){
                Log.d("MainActivity", "Kernel and Root Check Passed");
                execCommandLine("su");
                startActivity(new Intent(MainActivity.this, RadActivity.class));
                finish();
        }else
        {
           Log.d("MainActivity", "Kernel and Root Check failed");
            finish();
            System.exit(0);
        }
    }

    void vardev()
    {
        eb = findViewById(R.id.cv_eb);
        bb = findViewById(R.id.cv_bb);
        bal = findViewById(R.id.cv_bal);
        gm = findViewById(R.id.cv_gm);
        pm = findViewById(R.id.cv_pm);

        eb.setOnClickListener(this);
        bb.setOnClickListener(this);
        bal.setOnClickListener(this);
        gm.setOnClickListener(this);
        pm.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {

            case R.id.cv_eb:
                execCommandLine("echo 40 > /proc/sys/vm/swappiness");
                execCommandLine("echo " + "\"noop\"" + " > /sys/block/mmcblk0/queue/scheduler");
                execCommandLine("echo 0 > /sys/class/kgsl/kgsl-3d0/devfreq/adrenoboost");
                execCommandLine("echo 2048 > /sys/block/mmcblk0/queue/read_ahead_kb");
                execCommandLine("echo 2048 > /sys/block/mmcblk0/queue/read_ahead_kb");
                break;
            case R.id.cv_bb:

                break;
            case R.id.cv_bal:

                break;
            case R.id.cv_gm:

                break;
            case R.id.cv_pm:

                break;

        }
    }

}

