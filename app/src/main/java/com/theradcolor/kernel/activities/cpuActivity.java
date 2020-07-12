package com.theradcolor.kernel.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grarak.kerneladiutor.utils.Prefs;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.utils.kernel.GPU;
import com.theradcolor.kernel.utils.kernel.cpu.CPU;

import java.util.List;

@SuppressLint("SetTextI18n")
public class cpuActivity extends AppCompatActivity implements View.OnClickListener {

    CPU cpu;
    LinearLayout cpu_lit_gov, cpu_lit_min, cpu_lit_max, cpu_big_gov, cpu_big_min, cpu_big_max;
    TextView bigGov, bigMinFreq, bigMaxFreq, litGov, litMinFreq, litMaxFreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        cpu = new CPU();
        bigGov = findViewById(R.id.tv_cpu_big_gov);
        bigMinFreq = findViewById(R.id.tv_cpu_big_min_freq);
        bigMaxFreq = findViewById(R.id.tv_cpu_big_max_freq);
        litGov = findViewById(R.id.tv_cpu_lit_gov);
        litMinFreq = findViewById(R.id.tv_cpu_lit_min_freq);
        litMaxFreq = findViewById(R.id.tv_cpu_lit_max_freq);

        cpu_lit_gov = findViewById(R.id.ll_cpu_lit_gov);
        cpu_lit_min = findViewById(R.id.ll_cpu_lit_min);
        cpu_lit_max = findViewById(R.id.ll_cpu_lit_max);
        cpu_big_gov = findViewById(R.id.ll_cpu_big_gov);
        cpu_big_min = findViewById(R.id.ll_cpu_big_min);
        cpu_big_max = findViewById(R.id.ll_cpu_big_max);

        cpu_lit_gov.setOnClickListener(this);
        cpu_lit_min.setOnClickListener(this);
        cpu_lit_max.setOnClickListener(this);
        cpu_big_gov.setOnClickListener(this);
        cpu_big_min.setOnClickListener(this);
        cpu_big_max.setOnClickListener(this);

        refreshUI();

    }

    public void refreshUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bigGov.setText(cpu.getGovernor(cpu.getBigCpu(), true));
                bigMinFreq.setText(cpu.getMinFreq(true) / 1000 + getResources().getString(R.string.mhz));
                bigMaxFreq.setText(cpu.getMaxFreq(true) / 1000 + getResources().getString(R.string.mhz));

                litGov.setText(cpu.getGovernor(cpu.getLITTLECpu(), true));
                litMinFreq.setText(cpu.getMinFreq(cpu.getLITTLECpu(), true) / 1000 + getResources().getString(R.string.mhz));
                litMaxFreq.setText(cpu.getMaxFreq(cpu.getLITTLECpu(), true) / 1000 + getResources().getString(R.string.mhz));
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*private void bigGraph() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 999; ++i) {
                    try {
                        mCPUUsages = cpu.getCpuUsage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCPUStates = new boolean[cpu.getCpuCount()];
                    for (int s = 0; s < mCPUStates.length; s++) {
                        mCPUStates[s] = !cpu.isOffline(s);
                    }
                    mCPUFreqs = new int[cpu.getCpuCount()];
                    for (int c = 0; c < mCPUFreqs.length; c++) {
                        mCPUFreqs[c] = cpu.getCurFreq(c);
                    }

                    LineChartData bigData = mChart.getLineChartData();
                    bigValues.add(new PointValue(i, refreshUsages(mCPUUsages, cpu.getBigCpuRange(), mCPUStates)));

                    Log.d("CPU B", ""+refreshUsages(mCPUUsages, cpu.getBigCpuRange(), mCPUStates));
                    bigUsage.setText((int)refreshUsages(mCPUUsages, cpu.getBigCpuRange(), mCPUStates)
                            +getResources().getString(R.string.percent));

                    bigData.getLines().get(0).setValues(new ArrayList<>(bigValues));

                    mChart.setLineChartData(bigData);
                    bigViewPort();
                }
            }
        }).start();
    }

    private void littleGraph() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 999; ++i) {
                    try {
                        mCPUUsages = cpu.getCpuUsage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCPUStates = new boolean[cpu.getCpuCount()];
                    for (int s = 0; s < mCPUStates.length; s++) {
                        mCPUStates[s] = !cpu.isOffline(s);
                    }
                    mCPUFreqs = new int[cpu.getCpuCount()];
                    for (int c = 0; c < mCPUFreqs.length; c++) {
                        mCPUFreqs[c] = cpu.getCurFreq(c);
                    }
                    LineChartData littleData = nChart.getLineChartData();
                    littleValues.add(new PointValue(i, refreshUsages(mCPUUsages, cpu.getLITTLECpuRange(), mCPUStates)));

                    Log.d("CPU L", ""+refreshUsages(mCPUUsages, cpu.getLITTLECpuRange(), mCPUStates));
                    littleUsage.setText((int)refreshUsages(mCPUUsages, cpu.getLITTLECpuRange(), mCPUStates)
                            +getResources().getString(R.string.percent));

                    littleData.getLines().get(0).setValues(new ArrayList<>(littleValues));

                    nChart.setLineChartData(littleData);
                    littleViewPort();
                }
            }
        }).start();
    }

    private void bigViewPort() {
        int mSize = bigValues.size();
        if (mSize > maxNumberOfPoints) {
            final Viewport viewport1 = new Viewport(mChart.getMaximumViewport());
            viewport1.left = mSize - 8;
            mChart.setCurrentViewport(viewport1);
        }
    }

    private void littleViewPort() {
        int nSize = littleValues.size();
        if (nSize > maxNumberOfPoints) {
            final Viewport viewport = new Viewport(nChart.getMaximumViewport());
            viewport.left = nSize - 8;
            nChart.setCurrentViewport(viewport);
        }
    }

    private float refreshUsages(float[] usages, List<Integer> cores, boolean[] coreStates) {
        float graph = 0;
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
        graph = Math.round(average);
        return graph;
    }

}*/

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ll_cpu_lit_gov:
                govDialog("little");
                break;
            case R.id.ll_cpu_lit_min:
                minDialog("little");
                break;

        }

    }

    private void govDialog(String bigLittle){

        final List<Integer> bigCores = cpu.getBigCpuRange();
        final List<Integer> LITTLECores = cpu.getLITTLECpuRange();

        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(cpuActivity.this, R.style.CustomDialogTheme));
        builder.setTitle("CPU governor");
        String[] GOVs = CPU.getInstance().getGovernors().toArray(new String[0]);

        builder.setItems(GOVs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(bigLittle.equals("little")){
                    CPU.getInstance().setGovernor(GOVs[which], LITTLECores.get(0), LITTLECores.get(LITTLECores.size() - 1)
                            , cpuActivity.this);
                    Prefs.saveString("cpu_lit_governor", GOVs[which], cpuActivity.this);
                }else if(bigLittle.equals("big")){
                    CPU.getInstance().setGovernor(GOVs[which], bigCores.get(0), bigCores.get(bigCores.size() - 1)
                            , cpuActivity.this);
                    Prefs.saveString("cpu_big_governor", GOVs[which], cpuActivity.this);
                }
                refreshUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    String[] cpu_min;

    private void minDialog(String bigLittle){

        final List<Integer> bigCores = cpu.getBigCpuRange();
        final List<Integer> LITTLECores = cpu.getLITTLECpuRange();

        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(cpuActivity.this, R.style.CustomDialogTheme));
        builder.setTitle("CPU minimum freq");

        if (bigLittle.equals("little")){
            cpu_min = cpu.getAdjustedFreq(cpu.getLITTLECpu(), this).toArray(new String[0]);
        }else if (bigLittle.equals("big")){
            cpu_min = cpu.getAdjustedFreq(cpu.getBigCpu(), this).toArray(new String[0]);
        }

        builder.setItems(cpu_min, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void maxDialog(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(cpuActivity.this, R.style.CustomDialogTheme));
        builder.setTitle("CPU maximum freq");
        String[] gpu_max = GPU.getAdjustedFreqs(this).toArray(new String[0]);
        builder.setItems(gpu_max, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}