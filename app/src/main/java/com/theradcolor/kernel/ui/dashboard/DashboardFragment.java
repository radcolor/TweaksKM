package com.theradcolor.kernel.ui.dashboard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
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
    private TextView srgbon,srgboff,vib;
    private SeekBar seekBar;
    int progressChangedValue = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        srgbon = root.findViewById(R.id.srgbon);
        srgboff = root.findViewById(R.id.srgboff);
        seekBar = root.findViewById(R.id.vibration);
        vib = root.findViewById(R.id.pervib);
        final Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                vib.setText(progress + "%");Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                 // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    long[] pattern = {1, 1000, 0};
                    v.vibrate(VibrationEffect.createWaveform(pattern,1));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getContext(), "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                v.cancel();
            }
        });
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