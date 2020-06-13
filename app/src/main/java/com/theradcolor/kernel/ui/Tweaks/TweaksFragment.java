package com.theradcolor.kernel.ui.Tweaks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.R;
import com.theradcolor.utils.kernel.CPU;

import java.util.Timer;
import java.util.TimerTask;

public class TweaksFragment extends Fragment implements View.OnClickListener{

    private TweaksViewModel homeViewModel;
    CPU cpu;
    public TextView textView;
    private TextView cpu0,cpu1,cpu2,cpu3,cpu4,cpu5,cpu6,cpu7;
    SharedPreferences preferences;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(TweaksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tweaks, container, false);
        textView = root.findViewById(R.id.kernel_name);
        textView.setText("Kernel: " + RootUtils.runCommand("uname -a"));

        cpu = new CPU();
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
                cpu0.setText(cpu.getCurFreq(0)/1000+"MHz");
                cpu1.setText(cpu.getCurFreq(1)/1000+"MHz");
                cpu2.setText(cpu.getCurFreq(2)/1000+"MHz");
                cpu3.setText(cpu.getCurFreq(3)/1000+"MHz");
                cpu4.setText(cpu.getCurFreq(4)/1000+"MHz");
                cpu5.setText(cpu.getCurFreq(5)/1000+"MHz");
                cpu6.setText(cpu.getCurFreq(6)/1000+"MHz");
                cpu7.setText(cpu.getCurFreq(7)/1000+"MHz");
            }
            }, 0, 1000);
    }

}