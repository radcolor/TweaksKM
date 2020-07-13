package com.theradcolor.kernel.activities

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.grarak.kerneladiutor.utils.Prefs
import com.theradcolor.kernel.R
import com.theradcolor.kernel.utils.kernel.GPU
import kotlinx.android.synthetic.main.activity_gpu.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.Viewport

class gpuActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpu)
        val lines:MutableList<Line> = ArrayList()
        val line = Line()
        line.setHasLines(true)
        line.setHasPoints(false)
        line.color = resources.getColor(R.color.colorAccent)
        line.isFilled = true
        lines.add(line)
        val data = LineChartData(lines)
        data.axisXBottom = null
        data.axisYLeft = null
        data.baseValue = java.lang.Float.NEGATIVE_INFINITY
        gpu_chart.lineChartData = data
        refreshUI()
        ll_gpu_gov.setOnClickListener { govDialog() }
        ll_gpu_min.setOnClickListener { minDialog() }
        ll_gpu_max.setOnClickListener { maxDialog() }
        ll_gpu_pwr_lvl.setOnClickListener { pwrDialog() }
        CoroutineScope(Dispatchers.Default).launch {
            readData()
        }
    }

    private var pointValues: MutableList<PointValue> = ArrayList()
    private var maxNumberOfPoints = 16

    private suspend fun readData(){
        for (i in 0..998)
        {
            delay(500)
            gpu_usage.text = GPU.getBusyPer()
            curr_freq.text = GPU.getCurFreq().div(1000000).toString().plus(resources.getString(R.string.mhz))
            val data = gpu_chart.lineChartData
            pointValues.add(PointValue(i.toFloat(), GPU.getBusy().toFloat()))
            data.lines[0].values = ArrayList(pointValues)
            gpu_chart.lineChartData = data
            setViewport()
        }
    }

    private suspend fun setViewport() {
        val size = pointValues.size
        if (size > maxNumberOfPoints) {
            val viewport = Viewport(gpu_chart.maximumViewport)
            viewport.left = (size - 16).toFloat()
            gpu_chart.currentViewport = viewport
        }
    }

    private fun refreshUI() {
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
