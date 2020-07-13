package com.theradcolor.kernel.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_cpu.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.grarak.kerneladiutor.utils.Prefs
import com.theradcolor.kernel.R
import com.theradcolor.kernel.utils.kernel.GPU
import com.theradcolor.kernel.utils.kernel.cpu.CPU

@SuppressLint("SetTextI18n")
class cpuActivity : AppCompatActivity() {

    var cpu: CPU? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu)
        cpu = CPU()
        refreshUI()
        ll_cpu_lit_gov.setOnClickListener {  govDialog("little") }
        ll_cpu_lit_min.setOnClickListener {  minDialog("little") }
    }

    private fun refreshUI() {
        tv_cpu_big_gov.text = cpu!!.getGovernor(cpu!!.bigCpu, true)
        tv_cpu_big_min_freq.text = cpu!!.getMinFreq(4, true).div(1000).toString().plus(resources.getString(R.string.mhz))
        tv_cpu_big_max_freq.text = cpu!!.getMaxFreq(4,true).div(1000).toString().plus(resources.getString(R.string.mhz))
        tv_cpu_lit_gov.text = cpu!!.getGovernor(0, true)
        tv_cpu_lit_min_freq.text = cpu!!.getMinFreq(0, true).div(1000).toString().plus(resources.getString(R.string.mhz))
        tv_cpu_lit_max_freq.text = cpu!!.getMaxFreq(0, true).div(1000).toString().plus(resources.getString(R.string.mhz))
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
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

    private fun govDialog(bigLittle: String) {
        val bigCores = cpu!!.bigCpuRange
        val LITTLECores = cpu!!.littleCpuRange
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@cpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("CPU governor")
        val GOVs = CPU.getInstance().governors.toTypedArray()
        builder.setItems(GOVs) { dialog, which ->
            if (bigLittle == "little") {
                CPU.getInstance().setGovernor(GOVs[which], LITTLECores[0], LITTLECores[LITTLECores.size - 1]
                        , this@cpuActivity)
                Prefs.saveString("cpu_lit_governor", GOVs[which], this@cpuActivity)
            } else if (bigLittle == "big") {
                CPU.getInstance().setGovernor(GOVs[which], bigCores[0], bigCores[bigCores.size - 1]
                        , this@cpuActivity)
                Prefs.saveString("cpu_big_governor", GOVs[which], this@cpuActivity)
            }
            refreshUI()
        }
        val dialog = builder.create()
        dialog.show()
    }

    lateinit var cpu_min:Array<String>

    private fun minDialog(bigLittle: String) {
        val bigCores = cpu!!.bigCpuRange
        val LITTLECores = cpu!!.littleCpuRange
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@cpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("CPU minimum freq")
        if (bigLittle == "little") {
            cpu_min = cpu!!.getAdjustedFreq(cpu!!.littleCpu, this).toTypedArray()
        } else if (bigLittle == "big") {
            cpu_min = cpu!!.getAdjustedFreq(cpu!!.bigCpu, this).toTypedArray()
        }
        builder.setItems(cpu_min) {dialog, which ->  }
        val dialog = builder.create()
        dialog.show()
    }

    private fun maxDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@cpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("CPU maximum freq")
        val gpu_max = GPU.getAdjustedFreqs(this).toTypedArray()
        builder.setItems(gpu_max) { dialog, which -> refreshUI() }
        val dialog = builder.create()
        dialog.show()
    }
}