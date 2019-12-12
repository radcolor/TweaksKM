package com.theradcolor.kernel.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.theradcolor.kernel.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class DashboardFragment extends Fragment implements View.OnClickListener{

    private DashboardViewModel dashboardViewModel;
    private TextView srgbon,srgboff;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        srgbon = root.findViewById(R.id.srgbon);
        srgboff = root.findViewById(R.id.srgboff);
        srgbon.setOnClickListener(this);
        srgboff.setOnClickListener(this);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

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

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
            case R.id.srgbon:
                execCommandLine("active=1\n" +
                        "\n" +
                        "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                        "\n" +
                        "if [ $active = \"1\" ]\n" +
                        "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                        "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                        "fi");
                break;
            case R.id.srgboff:
                execCommandLine("active=0\n" +
                        "\n" +
                        "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                        "\n" +
                        "if [ $active = \"1\" ]\n" +
                        "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                        "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                        "fi");
                break;
        }
    }
}