package com.theradcolor.kernel.fragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.grarak.kerneladiutor.utils.Device;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.activities.cpuActivity;
import com.theradcolor.kernel.activities.gpuActivity;
import com.theradcolor.kernel.utils.kernel.Battery;
import com.theradcolor.kernel.utils.kernel.Entropy;
import com.theradcolor.kernel.utils.kernel.GPU;
import com.theradcolor.kernel.utils.kernel.WireGuard;
import com.theradcolor.kernel.utils.kernel.cpu.CPU;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class MonitorFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "MonitorFragment";
    View root;
    LinearLayout ll_gpu, ll_cpu;
    int mBatteryTemp, mBatteryLvl;
    CPU cpu; GPU gpu; Entropy entropy; WireGuard wireGuard;
    TextView kernel_name, kernel_name_full;
    TextView cpu0,cpu1,cpu2,cpu3,cpu4,cpu5,cpu6,cpu7, little_max, big_max, board, cpu_gov, oem_name;
    TextView gpu_usage_per, gpu_crr_freq, gpu_max_freq, gpu_model;
    TextView used_mem, avl_mem, tot_mem, bat_status, bat_temp, bat_lvl;
    TextView ent_lvl, wireguard_ver, uptime, deepsleeptime;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_monitor, container, false);
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

        ll_gpu = root.findViewById(R.id.ll_gpu);
        ll_gpu.setOnClickListener(this);
        ll_cpu = root.findViewById(R.id.ll_cpu);
        ll_cpu.setOnClickListener(this);

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

        bat_status = root.findViewById(R.id.batt_stats);
        bat_temp = root.findViewById(R.id.batt_temp);
        bat_lvl = root.findViewById(R.id.batt_lvl);

        ent_lvl = root.findViewById(R.id.ent_lvl);
        wireguard_ver = root.findViewById(R.id.wg_ver);
        uptime = root.findViewById(R.id.up_time);
        deepsleeptime = root.findViewById(R.id.deep_slp);
    }

    @SuppressLint("SetTextI18n")
    private void InitUI(){
        String kernelString = "<font color=#FFFFFF> <b> Kernel: </b> </font>" + Device.getKernelVersion(true);
        kernel_name.setText(RootUtils.runCommand("uname -a"));
        kernel_name_full.setText(Html.fromHtml(kernelString));
        oem_name.setText(Device.getVendor() + " " + Device.getModel());
        little_max.setText(getString(R.string.title_little_max)+ " "+cpu.getMaxFreq(0, true)/1000 + "Mhz");
        big_max.setText(getString(R.string.title_big_max)+ " "+cpu.getMaxFreq(4, true)/1000 + "MHz");
        cpu_gov.setText(getString(R.string.cpu_gov)+ " "+cpu.getGovernor(0, true));
        board.setText(getString(R.string.dev_board) + " " + Device.getHardware() + " " + Device.getBoard());
        String gpuString = "<font color=#FFFFFF> <b> GPU model: </b> </font>" + RootUtils.runAndGetError("dumpsys SurfaceFlinger | grep GLES");
        gpu_model.setText(Html.fromHtml(gpuString));
        wireguard_ver.setText("v"+wireGuard.getWireguard());
        long systemTime = SystemClock.elapsedRealtime();
        long awakeTime = SystemClock.uptimeMillis();
        long deepSleepTime = systemTime-awakeTime;
        uptime.setText(Utils.getDurationBreakdown(systemTime));
        deepsleeptime.setText(Utils.getDurationBreakdown(deepSleepTime));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ll_gpu:
                startActivity(new Intent(getContext(), gpuActivity.class));
                break;
            case R.id.ll_cpu:
                startActivity(new Intent(getContext(), cpuActivity.class));
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class monitorTask extends AsyncTask<String,Integer,String>{

        int freq_little = 0, freq_big = 0;
        ActivityManager activityManager;
        ActivityManager.MemoryInfo mi;
        private PieChartView memChart, batChart;
        private PieChartData data;
        List<SliceValue> values;
        SliceValue sliceValue;
        int total_memory, used_memory, free_memory, percentAvail;
        int battery_status, battery_charge;
        CPU cpu;
        GPU gpu;
        Entropy entropy;
        int ent_avl, ent_tot;
        String gpu_usage_str;
        int gpu_usage, gpu_clk, gpu_max_clk;

        private float[] mCPUUsages;
        private boolean[] mCPUStates;

        @Override
        protected void onPreExecute() {
            cpu = new CPU();
            gpu = new GPU();
            mi = new ActivityManager.MemoryInfo();
            activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
            memChart = root.findViewById(R.id.memchart);
            batChart = root.findViewById(R.id.batChart);
            batChart.setValueSelectionEnabled(true);
            memChart.setValueSelectionEnabled(true);
            entropy = new Entropy();
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "AsyncTask start");
            while (!Thread.currentThread().isInterrupted()){
                try {
                    Thread.sleep(500);
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
                if(Battery.ChargingStatus().equals("Charging")){
                    battery_status=1;
                }else{
                    battery_status=0;
                }
                battery_charge = Integer.parseInt(Battery.getChargingStatus());
                ent_avl = entropy.getAvailable();
                ent_tot = entropy.getPoolSize();

                publishProgress(freq_little,freq_big,
                        gpu_usage,gpu_clk,gpu_max_clk,
                        total_memory,used_memory,free_memory,percentAvail,
                        battery_status,battery_charge,
                        ent_avl,ent_tot);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
           updateUI(values);
        }

        @SuppressLint("SetTextI18n")
        private void updateUI(Integer... i) {
            cpu0.setText(i[0]+getResources().getString(R.string.mhz));
            cpu1.setText(i[0]+getResources().getString(R.string.mhz));
            cpu2.setText(i[0]+getResources().getString(R.string.mhz));
            cpu3.setText(i[0]+getResources().getString(R.string.mhz));
            cpu4.setText(i[1]+getResources().getString(R.string.mhz));
            cpu5.setText(i[1]+getResources().getString(R.string.mhz));
            cpu6.setText(i[1]+getResources().getString(R.string.mhz));
            cpu7.setText(i[1]+getResources().getString(R.string.mhz));
            gpu_usage_per.setText(i[2]+getString(R.string.percent));
            gpu_crr_freq.setText(i[3]+getResources().getString(R.string.mhz));
            gpu_max_freq.setText(i[4]+getResources().getString(R.string.mhz));
            tot_mem.setText(i[5] + " MB");
            used_mem.setText(i[6] + " MB");
            avl_mem.setText(i[7] + " MB " +i[8]+ "% approx");
            if(i[9] == 1){
                bat_status.setText("Charging " + i[10] + " mA");
            }else{
                bat_status.setText("Discharging " + i[10] + " mA");
            }
            String entropyString = "<font color=#FFFFFF> <b> Entropy: </b> </font>" + String.valueOf(i[11]) + "/" + String.valueOf(i[12]);
            ent_lvl.setText(Html.fromHtml(entropyString));
            bat_temp.setText(mBatteryTemp +"°C");
            bat_lvl.setText(+mBatteryLvl+"%");
            generateMemData(memChart, data, i[7], i[6]);
            generateBatData(batChart, data, mBatteryLvl, 100-mBatteryLvl);
            /*try {
                refreshUsages(mCPUUsages, mCPUUsageBig, mCPUFreq.getBigCpuRange(), mCPUStates);
                float[] f = CPU.getCpuUsage();
                Log.d(TAG, ""+f[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    private void refreshUsages(float[] usages, List<Integer> cores, boolean[] coreStates) {
        String graph = "null";
        if (graph != null) {
            float average = 0;
            int size = 0;
            for (int core : cores) {
                if (core + 1 < usages.length) {
                    if (coreStates[core]) {
                        average += usages[core + 1];
                    }
                    size++;
                }
            }
            average /= size;
            graph = Math.round(average) + "%";
        }
    }

    private void generateBatData(PieChartView chart, PieChartData mData, int lvl, int used_lvl) {
        List<SliceValue> values = new ArrayList<SliceValue>();
        SliceValue free = new SliceValue(used_lvl, getResources().getColor(R.color.green));
        values.add(free);
        SliceValue used = new SliceValue(lvl, getResources().getColor(R.color.green_light));
        values.add(used);
        mData = new PieChartData(values);
        chart.setPieChartData(mData);
        chart.setValueSelectionEnabled(true);
        chart.setValueTouchEnabled(true);
    }

    private void generateMemData(PieChartView chart, PieChartData mData, int free_mem, int used_mem) {
        List<SliceValue> values = new ArrayList<SliceValue>();
        SliceValue free = new SliceValue(free_mem, getResources().getColor(R.color.green));
        values.add(free);
        SliceValue used = new SliceValue(used_mem, getResources().getColor(R.color.green_light));
        values.add(used);
        mData = new PieChartData(values);
        chart.setPieChartData(mData);
        chart.setValueSelectionEnabled(true);
        chart.setValueTouchEnabled(true);
        chart.setOnValueTouchListener(new ValueTouchListener());
    }

    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            if(arcIndex==0){
                Toast.makeText(getActivity(), "Free Memory", Toast.LENGTH_SHORT).show();
            }else if(arcIndex==1){
                Toast.makeText(getActivity(), "Used Memory", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onValueDeselected() {}

    }

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mBatteryLvl = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            mBatteryTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        InitView();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            requireActivity().unregisterReceiver(mBatteryReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }
}