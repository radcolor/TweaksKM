package com.theradcolor.kernel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView eb,bb,bal,gm,pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vardev();

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

    private void parseVal(String io, String cpu_gov, String gpu_gov, int max_cpufreq, int min_cpu_cpufreq){

    }

}

