package com.theradcolor.kernel.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
import com.theradcolor.kernel.utils.kernel.Entropy;
import com.theradcolor.kernel.utils.kernel.GPU;
import com.theradcolor.kernel.utils.kernel.WireGuard;

import java.util.ArrayList;
import java.util.List;

public class MonitorFragment extends Fragment{

    private static final String TAG = "MonitorFragment";
    View root;
    CPU cpu;
    GPU gpu;
    Entropy entropy;
    WireGuard wireGuard;
    PieChart memchart, battchart;
    TextView kernel_name, kernel_name_full;
    TextView cpu0,cpu1,cpu2,cpu3,cpu4,cpu5,cpu6,cpu7, little_max, big_max, board, cpu_gov, oem_name;
    TextView gpu_usage_per, gpu_crr_freq, gpu_max_freq, gpu_model;
    TextView ent_lvl, wireguard_ver;
    SharedPreferences preferences;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_monitor, container, false);

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        InitView();
        InitUI();

        monitorTask myTask = new monitorTask();
        myTask.execute();
        Log.d(TAG," "+Thread.currentThread().getName());

        return root;
    }

    private void InitView(){

        cpu = new CPU();
        gpu = new GPU();
        entropy = new Entropy();
        wireGuard = new WireGuard();

        kernel_name = root.findViewById(R.id.kernel_name);
        kernel_name_full = root.findViewById(R.id.kernel_name_full);

        oem_name = root.findViewById(R.id.oem_name);

        little_max = root.findViewById(R.id.little_max);
        big_max = root.findViewById(R.id.big_max);
        cpu_gov = root.findViewById(R.id.cpu_gov);
        board = root.findViewById(R.id.cpu_arch);

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
        gpu_usage_per = root.findViewById(R.id.gpu_usage);
        gpu_model = root.findViewById(R.id.gpu_model);

        ent_lvl = root.findViewById(R.id.ent_lvl);
        wireguard_ver = root.findViewById(R.id.wg_ver);

        memchart = root.findViewById(R.id.memchart);
        battchart = root.findViewById(R.id.battchart);

    }

    @SuppressLint("SetTextI18n")
    private void InitUI(){
        String kernelString = "<font color=#FFFFFF> <b> Kernel: </b> </font>" + Device.getKernelVersion(true);
        kernel_name.setText(RootUtils.runCommand("uname -a"));
        kernel_name_full.setText(Html.fromHtml(kernelString));
        oem_name.setText(Device.getVendor() + " " + Device.getModel());
        little_max.setText(getString(R.string.title_little_max)+ " "+cpu.getMaxFreq(0)/1000 + "Mhz");
        big_max.setText(getString(R.string.title_big_max)+ " "+cpu.getMaxFreq(4)/1000 + "MHz");
        cpu_gov.setText(getString(R.string.cpu_gov)+ " "+cpu.getGovernor(0));
        board.setText(getString(R.string.dev_board) + " " + Device.getHardware() + " " + Device.getBoard());
        wireguard_ver.setText("v"+wireGuard.getWireguard());
        //Sample pie chart data
        memchart.setCenterCircleColor(R.color.colorAccent);
        battchart.setCenterCircleColor(R.color.colorAccent);
        List<ChartData> data = new ArrayList<>();
        data.add(new ChartData("", 66));
        data.add(new ChartData("", 34));
        memchart.setChartData(data);
        battchart.setChartData(data);
    }

    @SuppressLint("StaticFieldLeak")
    class monitorTask extends AsyncTask<String,Integer,String>{

        int freq_little = 0, freq_big = 0;
        CPU cpu;
        GPU gpu;
        Entropy entropy;
        int ent_avl, ent_tot;
        String gpu_usage_str;
        int gpu_usage, gpu_clk, gpu_max_clk;

        @Override
        protected void onPreExecute() {
            cpu = new CPU();
            gpu = new GPU();
            entropy = new Entropy();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "AsyncTask start");
            while (!Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                freq_big = cpu.getCurFreq(4)/1000;
                freq_little = cpu.getCurFreq(0)/1000;
                gpu_clk = gpu.getCurFreq()/1000000;
                gpu_max_clk = gpu.getMaxFreq()/1000000;
                gpu_usage_str = gpu.getGpuBusy();
                gpu_usage_str = gpu_usage_str.replaceAll("[^0-9]","");
                gpu_usage = Integer.parseInt(gpu_usage_str);
                ent_avl = entropy.getAvailable();
                ent_tot = entropy.getPoolSize();

                publishProgress(freq_little,freq_big,gpu_usage,gpu_clk,gpu_max_clk,ent_avl,ent_tot);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
           updateUI(values);
        }

        @SuppressLint("SetTextI18n")
        private void updateUI(Integer... i) {
            cpu0.setText(i[0]+getString(R.string.mhz));
            cpu1.setText(i[0]+getString(R.string.mhz));
            cpu2.setText(i[0]+getString(R.string.mhz));
            cpu3.setText(i[0]+getString(R.string.mhz));
            cpu4.setText(i[1]+getString(R.string.mhz));
            cpu5.setText(i[1]+getString(R.string.mhz));
            cpu6.setText(i[1]+getString(R.string.mhz));
            cpu7.setText(i[1]+getString(R.string.mhz));
            gpu_usage_per.setText(i[2]+getString(R.string.percent));
            gpu_crr_freq.setText(i[3]+getString(R.string.mhz));
            gpu_max_freq.setText(i[4]+getString(R.string.mhz));
            String entropyString = "<font color=#FFFFFF> <b> Entropy: </b> </font>" + String.valueOf(i[5]) + "/" + String.valueOf(i[6]);
            ent_lvl.setText(Html.fromHtml(entropyString));
        }
    }

}