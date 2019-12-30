package com.theradcolor.kernel.ui.Misc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.theradcolor.kernel.KcalActivity;
import com.theradcolor.kernel.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MiscFragment extends Fragment implements View.OnClickListener{

    private MiscViewModel dashboardViewModel;
    private TextView srgbon,srgboff,vib;
    private SeekBar seekBar;
    private LinearLayout srgb,kcal,vibration;
    int progressChangedValue = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(MiscViewModel.class);
        View root = inflater.inflate(R.layout.fragment_misc, container, false);
        //Toast.makeText(root.getContext(),"Vibration" + getVibration(), Toast.LENGTH_LONG).show();
        seekBar = root.findViewById(R.id.vibration);
        seekBar.setPadding(16,16,16,16);
        vib = root.findViewById(R.id.pervib);
        srgb = root.findViewById(R.id.ll_srgb);
        kcal = root.findViewById(R.id.ll_kcal);
        vibration = root.findViewById(R.id.ll_vib);

        srgb.setOnClickListener(this);
        kcal.setOnClickListener(this);
        vibration.setOnClickListener(this);

        final Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                vib.setText(progress + "%");
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
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
                //execCommandLine("");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                v.cancel();
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

    private static String getVibration() {
        try {
            Process p = Runtime.getRuntime().exec("su");
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

    private void srgb()
    {
        new AlertDialog.Builder(getContext())
                .setTitle("sRGB colors")
                .setPositiveButton("On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        execCommandLine("active=1\n" +
                                "\n" +
                                "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                                "\n" +
                                "if [ $active = \"1\" ]\n" +
                                "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "fi");
                    }
                })
                .setNegativeButton("Off", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        execCommandLine("active=0\n" +
                                "\n" +
                                "echo $active > /sys/module/mdss_fb/parameters/srgb_enabled\n" +
                                "\n" +
                                "if [ $active = \"1\" ]\n" +
                                "then echo \"2\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "else echo \"1\" > /sys/class/graphics/fb0/msm_fb_srgb\n" +
                                "fi");
                    }
                })
                .show();

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
            case R.id.ll_vib:

                break;
        }
    }
}