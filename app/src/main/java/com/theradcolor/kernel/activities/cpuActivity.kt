package com.theradcolor.kernel.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.grarak.kerneladiutor.utils.Prefs
import com.theradcolor.kernel.R
import com.theradcolor.kernel.utils.kernel.cpu.CPU
import kotlinx.android.synthetic.main.activity_cpu.*
import kotlinx.coroutines.*
import kotlin.math.roundToInt
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.model.Viewport

@SuppressLint("SetTextI18n")
class cpuActivity : AppCompatActivity() {

    lateinit var cpu: CPU
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Default + job)
    private val bigScope = CoroutineScope(Dispatchers.Default + job)
    private val littleScope = CoroutineScope(Dispatchers.Default + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu)
        cpu = CPU()
        initGraph()
        ll_cpu_lit_gov.setOnClickListener {  govDialog("little") }
        ll_cpu_lit_min.setOnClickListener {  minDialog("little") }
        ll_cpu_lit_max.setOnClickListener { maxDialog("little") }
        ll_cpu_big_gov.setOnClickListener {  govDialog("big") }
        ll_cpu_big_min.setOnClickListener {  minDialog("big") }
        ll_cpu_big_max.setOnClickListener { maxDialog("big") }
        uiScope.launch { refreshUI() }
        bigScope.launch { readBigData() }
        littleScope.launch { readLittleData() }
    }

    private suspend fun refreshUI() {
        while (true){
            tv_cpu_big_gov.text = cpu.getGovernor(4, true)
            tv_cpu_big_min_freq.text = cpu.getMinFreq(4, true).div(1000).toString().plus(resources.getString(R.string.mhz))
            tv_cpu_big_max_freq.text = cpu.getMaxFreq(4,true).div(1000).toString().plus(resources.getString(R.string.mhz))
            tv_cpu_lit_gov.text = cpu.getGovernor(0, true)
            tv_cpu_lit_min_freq.text = cpu.getMinFreq(0, true).div(1000).toString().plus(resources.getString(R.string.mhz))
            tv_cpu_lit_max_freq.text = cpu.getMaxFreq(0, true).div(1000).toString().plus(resources.getString(R.string.mhz))
        }
    }

    private fun refresh() {
        tv_cpu_big_gov.text = cpu.getGovernor(4, true)
        tv_cpu_big_min_freq.text = cpu.getMinFreq(4, true).div(1000).toString().plus(resources.getString(R.string.mhz))
        tv_cpu_big_max_freq.text = cpu.getMaxFreq(4,true).div(1000).toString().plus(resources.getString(R.string.mhz))
        tv_cpu_lit_gov.text = cpu.getGovernor(0, true)
        tv_cpu_lit_min_freq.text = cpu.getMinFreq(0, true).div(1000).toString().plus(resources.getString(R.string.mhz))
        tv_cpu_lit_max_freq.text = cpu.getMaxFreq(0, true).div(1000).toString().plus(resources.getString(R.string.mhz))
    }

    override fun onResume() {
        super.onResume()
        //refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    private lateinit var mCPUUsages:FloatArray
    private lateinit var mCPUStates:BooleanArray
    private lateinit var mCPUFreq:IntArray

    private var bigValues: MutableList<PointValue> = ArrayList()
    private var littleValues: MutableList<PointValue> = ArrayList()
    private var maxNumberOfPoints = 8

    private suspend fun readBigData(){
        for (i in 0..998)
        {
            delay(500)
            try
            {
                mCPUUsages = cpu.cpuUsage
            }
            catch (e:InterruptedException) {
                e.printStackTrace()
            }
            mCPUStates = BooleanArray(cpu.cpuCount)
            for (s in mCPUStates.indices)
            {
                mCPUStates[s] = !cpu.isOffline(s)
            }
            mCPUFreq = IntArray(cpu.cpuCount)
            for (c in mCPUFreq.indices)
            {
                mCPUFreq[c] = cpu.getCurFreq(c)
            }

            big_cpu_usage.text = (refreshUsages(mCPUUsages, cpu.bigCpuRange, mCPUStates).toString().plus(resources.getString(R.string.percent)))

            val bigData = bigCpu.lineChartData
            bigValues.add(PointValue(i.toFloat(), refreshUsages(mCPUUsages, cpu.bigCpuRange, mCPUStates)))
            bigData.lines[0].values = ArrayList(bigValues)
            bigCpu.lineChartData = bigData
            bigViewPort()

        }
    }

    private suspend fun readLittleData(){
        for (i in 0..998)
        {
            delay(500)
            try
            {
                mCPUUsages = cpu.cpuUsage
            }
            catch (e:InterruptedException) {
                e.printStackTrace()
            }
            mCPUStates = BooleanArray(cpu.cpuCount)
            for (s in mCPUStates.indices)
            {
                mCPUStates[s] = !cpu.isOffline(s)
            }
            mCPUFreq = IntArray(cpu.cpuCount)
            for (c in mCPUFreq.indices)
            {
                mCPUFreq[c] = cpu.getCurFreq(c)
            }

            little_cpu_usage.text = (refreshUsages(mCPUUsages, cpu.littleCpuRange, mCPUStates).toString().plus(resources.getString(R.string.percent)))

            val littleData = littleCpu.lineChartData
            littleValues.add(PointValue(i.toFloat(), refreshUsages(mCPUUsages, cpu.littleCpuRange, mCPUStates)))
            littleData.lines[0].values = ArrayList(littleValues)
            littleCpu.lineChartData = littleData
            littleViewPort()

        }
    }

    private fun bigViewPort() {
        val mSize = bigValues.size
        if (mSize > maxNumberOfPoints)
        {
            val viewport1 = Viewport(bigCpu.maximumViewport)
            viewport1.left = (mSize - 8).toFloat()
            bigCpu.currentViewport = viewport1
        }
    }

    private fun littleViewPort() {
        val nSize = littleValues.size
        if (nSize > maxNumberOfPoints)
        {
            val viewport = Viewport(littleCpu.maximumViewport)
            viewport.left = (nSize - 8).toFloat()
            littleCpu.currentViewport = viewport
        }
    }

    private fun initGraph(){
        bigCpu.isInteractive = true
        bigCpu.isZoomEnabled = false
        bigCpu.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL)
        littleCpu.isInteractive = true
        littleCpu.isZoomEnabled = false
        littleCpu.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL)
        val lines: MutableList<Line> = ArrayList()
        val line = Line()
        line.setHasLines(true)
        line.setHasPoints(false)
        line.color = resources.getColor(R.color.colorAccent)
        line.isFilled = true
        lines.add(line)
        val data = LineChartData(lines)
        data.axisXBottom = null
        data.axisYLeft = null
        data.baseValue = Float.NEGATIVE_INFINITY
        bigCpu.lineChartData = data
        littleCpu.lineChartData = data
    }


    private suspend fun refreshUsages(usages:FloatArray, cores:List<Int>, coreStates:BooleanArray):Float {
        var graph = 0f
        var average = 0f
        var size = 0
        for (core in cores)
        {
            if (core + 1 < usages.size)
            {
                if (coreStates[core])
                {
                    average += usages[core + 1]
                }
                size++
            }
        }
        average /= size.toFloat()
        graph = average.roundToInt().toFloat()
        return graph
    }


    private fun govDialog(bigLittle: String) {
        val bigCores = cpu.bigCpuRange
        val LITTLECores = cpu.littleCpuRange
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
        }
        val dialog = builder.create()
        dialog.show()
    }

    lateinit var cpu_min:Array<String>

    private fun minDialog(bigLittle: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@cpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("CPU minimum freq")
        if (bigLittle == "little") {
            cpu_min = cpu.getAdjustedFreq(cpu.littleCpu, this).toTypedArray()
        } else if (bigLittle == "big") {
            cpu_min = cpu.getAdjustedFreq(cpu.bigCpu, this).toTypedArray()
        }
        builder.setItems(cpu_min) { _, which ->
            if (bigLittle == "little") {
                for (i in 0..3){
                    cpu.setMinFreq(cpu_min[which].replace("MHz", "").toInt().times(1000), i, this@cpuActivity)
                }
            } else if (bigLittle == "big") {
                for (i in 4..7){
                    cpu.setMinFreq(cpu_min[which].replace("MHz", "").toInt().times(1000), i, this@cpuActivity)
                }
            }
            refresh()
        }
        val dialog = builder.create()
        dialog.show()
    }

    lateinit var cpu_max:Array<String>

    private fun maxDialog(bigLittle: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this@cpuActivity, R.style.CustomDialogTheme))
        builder.setTitle("CPU maximum freq")
        if (bigLittle == "little") {
            cpu_max = cpu.getAdjustedFreq(cpu.littleCpu, this).toTypedArray()
        } else if (bigLittle == "big") {
            cpu_max = cpu.getAdjustedFreq(cpu.bigCpu, this).toTypedArray()
        }
        builder.setItems(cpu_max) { _, which ->
            if (bigLittle == "little") {
                for (i in 0..3){
                    cpu.setMaxFreq(cpu_max[which].replace("MHz", "").toInt().times(1000), i, this@cpuActivity)
                }
            } else if (bigLittle == "big") {
                for (i in 4..7){
                    cpu.setMaxFreq(cpu_max[which].replace("MHz", "").toInt().times(1000), i, this@cpuActivity)
                }
            }
            refresh()
        }
        val dialog = builder.create()
        dialog.show()
    }
}