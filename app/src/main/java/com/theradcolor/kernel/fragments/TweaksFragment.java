package com.theradcolor.kernel.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.grarak.kerneladiutor.utils.Device;
import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.R;
import com.theradcolor.utils.kernel.CPU;

import java.util.Timer;
import java.util.TimerTask;

public class TweaksFragment extends Fragment implements View.OnClickListener{

    CPU cpu;
    public TextView textView;
    private TextView cpu0,cpu1,cpu2,cpu3,cpu4,cpu5,cpu6,cpu7, little_max, big_max, board, cpu_gov, oem_name;
    SharedPreferences preferences;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tweaks, container, false);
        textView = root.findViewById(R.id.kernel_name);
        textView.setText("Kernel: " + RootUtils.runCommand("uname -a"));

        cpu = new CPU();

        oem_name = root.findViewById(R.id.oem_name);
        oem_name.setText(Device.getVendor() + " " + Device.getModel());

        little_max = root.findViewById(R.id.little_max);
        little_max.setText("Little max: "+cpu.getMaxFreq(0)/1000 + "Mhz");
        big_max = root.findViewById(R.id.big_max);
        big_max.setText("Big max: "+cpu.getMaxFreq(4)/1000 + "MHz");

        cpu_gov = root.findViewById(R.id.cpu_gov);
        cpu_gov.setText("CPU governer: "+cpu.getGovernor(0));
        board = root.findViewById(R.id.cpu_arch);
        board.setText("Board: " + Device.getHardware() + " " + Device.getBoard());

        cpu0 = root.findViewById(R.id.cpu0);
        cpu1 = root.findViewById(R.id.cpu1);
        cpu2 = root.findViewById(R.id.cpu2);
        cpu3 = root.findViewById(R.id.cpu3);
        cpu4 = root.findViewById(R.id.cpu4);
        cpu5 = root.findViewById(R.id.cpu5);
        cpu6 = root.findViewById(R.id.cpu6);
        cpu7 = root.findViewById(R.id.cpu7);

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        refreshfreq();
        return root;
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
        }
    }

    public void refreshfreq(){
        //Declare the timer
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String littleFreq = cpu.getCurFreq(0)/1000 + "MHz";
                String bigFreq = cpu.getCurFreq(4)/1000 + "MHz";
                cpu0.setText(littleFreq);
                cpu1.setText(littleFreq);
                cpu2.setText(littleFreq);
                cpu3.setText(littleFreq);
                cpu4.setText(bigFreq);
                cpu5.setText(bigFreq);
                cpu6.setText(bigFreq);
                cpu7.setText(bigFreq);
            }
            }, 0, 500);
    }

}