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
import com.intrusoft.scatter.ChartData;
import com.intrusoft.scatter.PieChart;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.utils.kernel.CPU;
import com.theradcolor.kernel.utils.kernel.GPU;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorFragment extends Fragment{

    CPU cpu;
    GPU gpu;
    int clk, max_clk;
    String littleFreq, bigFreq;
    TextView kernel_name, kernel_name_full;
    TextView cpu0,cpu1,cpu2,cpu3,cpu4,cpu5,cpu6,cpu7, little_max, big_max, board, cpu_gov, oem_name;
    TextView gpu_usage, gpu_crr_freq, gpu_max_freq;
    SharedPreferences preferences;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_monitor, container, false);
        kernel_name = root.findViewById(R.id.kernel_name);
        kernel_name_full = root.findViewById(R.id.kernel_name_full);
        kernel_name.setText(RootUtils.runCommand("uname -a"));
        kernel_name_full.setText(Device.getKernelVersion(true));

        cpu = new CPU();
        gpu = new GPU();

        oem_name = root.findViewById(R.id.oem_name);
        oem_name.setText(Device.getVendor() + " " + Device.getModel());

        little_max = root.findViewById(R.id.little_max);
        little_max.setText(getString(R.string.title_little_max)+ " "+cpu.getMaxFreq(0)/1000 + "Mhz");
        big_max = root.findViewById(R.id.big_max);
        big_max.setText(getString(R.string.title_big_max)+ " "+cpu.getMaxFreq(4)/1000 + "MHz");

        cpu_gov = root.findViewById(R.id.cpu_gov);
        cpu_gov.setText(getString(R.string.cpu_gov)+ " "+cpu.getGovernor(0));
        board = root.findViewById(R.id.cpu_arch);
        board.setText(getString(R.string.dev_board) + " " + Device.getHardware() + " " + Device.getBoard());

        cpu0 = root.findViewById(R.id.cpu0);
        cpu1 = root.findViewById(R.id.cpu1);
        cpu2 = root.findViewById(R.id.cpu2);
        cpu3 = root.findViewById(R.id.cpu3);
        cpu4 = root.findViewById(R.id.cpu4);
        cpu5 = root.findViewById(R.id.cpu5);
        cpu6 = root.findViewById(R.id.cpu6);
        cpu7 = root.findViewById(R.id.cpu7);

        gpu_crr_freq = root.findViewById(R.id.gpu_curr_freq);
        gpu_max_freq = root.findViewById(R.id.gpu_max_freq);

        //Sample pie chart data
        PieChart memchart = root.findViewById(R.id.memchart);
        PieChart battchart = root.findViewById(R.id.battchart);
        memchart.setCenterCircleColor(R.color.colorAccent);
        battchart.setCenterCircleColor(R.color.colorAccent);
        List<ChartData> data = new ArrayList<>();
        data.add(new ChartData("", 66));     //ARGS-> (display text, percentage)
        data.add(new ChartData("", 34));
        memchart.setChartData(data);
        battchart.setChartData(data);

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        thread.start();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshUI();
            }
        });
        return root;
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                synchronized (this) {
                    wait(100);
                    //Declare the timer
                    Timer t = new Timer();
                    t.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            littleFreq = cpu.getCurFreq(0)/1000 + "MHz";
                            bigFreq = cpu.getCurFreq(4)/1000 + "MHz";
                            clk = gpu.getCurFreq()/1000;
                            max_clk = gpu.getMaxFreq()/1000;
                        }
                    }, 0, 500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    };

    public void refreshUI(){
        //Declare the timer
        Timer ui = new Timer();
        ui.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
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