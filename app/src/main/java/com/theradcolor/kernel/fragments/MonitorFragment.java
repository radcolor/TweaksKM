package com.theradcolor.kernel.fragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.grarak.kerneladiutor.utils.Device;
import com.grarak.kerneladiutor.utils.Utils;
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
    TextView used_mem, avl_mem, tot_mem;
    TextView ent_lvl, wireguard_ver, uptime, deepsleeptime;
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

        used_mem = root.findViewById(R.id.mem_used);
        avl_mem = root.findViewById(R.id.mem_free);
        tot_mem = root.findViewById(R.id.mem_tot);

        ent_lvl = root.findViewById(R.id.ent_lvl);
        wireguard_ver = root.findViewById(R.id.wg_ver);
        uptime = root.findViewById(R.id.up_time);
        deepsleeptime = root.findViewById(R.id.deep_slp);

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
        String gpuString = "<font color=#FFFFFF> <b> GPU model: </b> </font>" + RootUtils.runAndGetError("dumpsys SurfaceFlinger | grep GLES");
        gpu_model.setText(Html.fromHtml(gpuString));
        //Sample pie chart data
        memchart.setCenterCircleColor(R.color.colorAccent);
        battchart.setCenterCircleColor(R.color.colorAccent);
        List<ChartData> data = new ArrayList<>();
        data.add(new ChartData("", 66));
        data.add(new ChartData("", 34));
        memchart.setChartData(data);
        battchart.setChartData(data);
        wireguard_ver.setText("v"+wireGuard.getWireguard());
        long systemTime = SystemClock.elapsedRealtime();
        long awakeTime = SystemClock.uptimeMillis();
        long deepSleepTime = systemTime-awakeTime;
        uptime.setText(Utils.getDurationBreakdown(systemTime));
        deepsleeptime.setText(Utils.getDurationBreakdown(deepSleepTime));
    }

    @SuppressLint("StaticFieldLeak")
    class monitorTask extends AsyncTask<String,Integer,String>{

        int freq_little = 0, freq_big = 0;
        ActivityManager activityManager;
        ActivityManager.MemoryInfo mi;
        int total_memory, used_memory, free_memory, percentAvail;
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
            mi = new ActivityManager.MemoryInfo();
            activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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
                activityManager.getMemoryInfo(mi);
                total_memory = (int) (mi.totalMem / 0x100000L);
                free_memory  = (int) (mi.availMem / 0x100000L);
                used_memory = total_memory-free_memory;
                percentAvail = (int) (mi.availMem / (double) mi.totalMem * 100.0);
                ent_avl = entropy.getAvailable();
                ent_tot = entropy.getPoolSize();

                publishProgress(freq_little,freq_big,gpu_usage,gpu_clk,gpu_max_clk,total_memory,used_memory,free_memory,percentAvail,ent_avl,ent_tot);
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
            tot_mem.setText(i[5] + " MB");
            used_mem.setText(i[6] + " MB");
            avl_mem.setText(i[7] + " MB " +i[8]+ "% approx");
            String entropyString = "<font color=#FFFFFF> <b> Entropy: </b> </font>" + String.valueOf(i[9]) + "/" + String.valueOf(i[10]);
            ent_lvl.setText(Html.fromHtml(entropyString));
        }
    }

    @Override
    public void onResume() {
        InitView();
        super.onResume();
    }

}