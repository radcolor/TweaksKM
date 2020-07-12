package com.theradcolor.kernel.activities

import android.os.Bundle
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_gpu.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.grarak.kerneladiutor.utils.Prefs
import com.theradcolor.kernel.R
import com.theradcolor.kernel.utils.kernel.GPU

class gpuActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpu)
        refreshUI()
        ll_gpu_gov.setOnClickListener { govDialog() }
        ll_gpu_min.setOnClickListener { minDialog() }
        ll_gpu_max.setOnClickListener { maxDialog() }
        ll_gpu_pwr_lvl.setOnClickListener { pwrDialog() }
    }

    /*private void drawGraph() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 999; ++i) {
                    String gpuUsage = GPU.getGpuBusy().replaceAll("[-+.^:,%]","");
                    usage.setText(""+GPU.getGpuBusy());
                    curr_freq.setText(GPU.getCurFreq()/1000000+getResources().getString(R.string.mhz));
                    LineChartData data = mChart.getLineChartData();
                    pointValues.add(new PointValue(i, Utils.strToFloat(gpuUsage)));
                    data.getLines().get(0).setValues(new ArrayList<>(pointValues));
                    mChart.setLineChartData(data);
                    setViewport();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void setViewport() {
        int size = pointValues.size();
        if (size > maxNumberOfPoints) {
            final Viewport viewport = new Viewport(mChart.getMaximumViewport());
            viewport.left = size - 16;
            mChart.setCurrentViewport(viewport);
        }
    }*/

    fun refreshUI() {
        Thread(Runnable {
            tv_gpu_gov!!.text = GPU.getGovernor()
            val minFreq = GPU.getMinFreq().div(GPU.getMinFreqOffset())
            val maxFreq  = GPU.getMaxFreq().div(GPU.getMaxFreqOffset())
            tv_gpu_min_freq.text = (minFreq.toString().plus(resources.getString(R.string.mhz)))
            tv_gpu_max_freq.text = (maxFreq.toString().plus(resources.getString(R.string.mhz)))
            tv_gpu_pwr_lvl!!.text = GPU.getPwrLevel()
        }).start()
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun govDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@gpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("GPU governor")
        val GOVs = GPU.getAvailableGovernors().toTypedArray()
        builder.setItems(GOVs) { dialog, which ->
            GPU.setGovernor(GOVs[which], this@gpuActivity)
            Prefs.saveString("gpu_governor", GOVs[which], this@gpuActivity)
            refreshUI()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun minDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@gpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("GPU minimum freq")
        val gpu_min = GPU.getAdjustedFreqs(this).toTypedArray()
        builder.setItems(gpu_min) { dialog, which ->
            GPU.setMinFreq(GPU.getAvailableFreqs()[which] / 1000000, this@gpuActivity)
            Prefs.saveInt("gpu_min_freq", GPU.getAvailableFreqs()[which] / 1000000, this@gpuActivity)
            refreshUI()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun maxDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@gpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("GPU maximum freq")
        val gpu_max = GPU.getAdjustedFreqs(this).toTypedArray()
        builder.setItems(gpu_max) { dialog, which ->
            GPU.setMaxFreq(GPU.getAvailableFreqs()[which], this@gpuActivity)
            Prefs.saveInt("gpu_max_freq", GPU.getAvailableFreqs()[which], this@gpuActivity)
            refreshUI()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun pwrDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@gpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("GPU power level")
        val mpLayout = layoutInflater.inflate(R.layout.gpu_pwr_dialog, null)
        val editText = mpLayout.findViewById<EditText>(R.id.pwr_lvl_et)
        editText.setText(GPU.getPwrLevel())
        builder.setPositiveButton("Apply") { dialog, which ->
            GPU.setPwrLevel(editText.text.toString(), this@gpuActivity)
            Prefs.saveString("gpu_pwr_lvl", editText.text.toString(), this@gpuActivity)
            refreshUI()
        }
        builder.setView(mpLayout)
        val dialog = builder.create()
        dialog.show()
    }
}
