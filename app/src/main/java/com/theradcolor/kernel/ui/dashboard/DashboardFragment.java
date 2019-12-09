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

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private Switch aSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        aSwitch = root.findViewById(R.id.srgb);
        if(getSRGB() == "1"){
            aSwitch.setChecked(true);
        }else if(getSRGB() == "0")
        {
            aSwitch.setChecked(false);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    execCommandLine("active=1\n" +
                            "\n" +
                            "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                            "\n" +
                            "if [ $active = \"1\" ]\n" +
                            "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                            "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                            "fi");
                }else
                {
                    execCommandLine("active=0\n" +
                            "\n" +
                            "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                            "\n" +
                            "if [ $active = \"1\" ]\n" +
                            "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                            "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                            "fi");
                }
            }
        });
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
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

    public static String getSRGB() {
        try {
            Process p = Runtime.getRuntime().exec("/sys/class/graphics/fb0/msm_fb_srgb");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            Log.i("Kernel Version", line);
            br.close();
            return line;
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }

}