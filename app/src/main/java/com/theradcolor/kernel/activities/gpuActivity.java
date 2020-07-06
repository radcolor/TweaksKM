package com.theradcolor.kernel.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grarak.kerneladiutor.utils.Utils;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.utils.kernel.GPU;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class gpuActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout governor;
    TextView usage, curr_freq, gov, min_freq, max_freq, pwr_lvl;
    LineChartView mChart;
    List<PointValue> pointValues = new ArrayList<>();
    int maxNumberOfPoints = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);
        mChart = (LineChartView) findViewById(R.id.gpu_chart);
        usage = findViewById(R.id.gpu_usage);
        curr_freq = findViewById(R.id.curr_freq);
        gov = findViewById(R.id.tv_gpu_gov);
        min_freq = findViewById(R.id.tv_gpu_min_freq);
        max_freq = findViewById(R.id.tv_gpu_max_freq);
        pwr_lvl = findViewById(R.id.tv_gpu_pwr_lvl);

        governor = findViewById(R.id.ll_gpu_gov);
        governor.setOnClickListener(this);

        mChart.setInteractive(true);
        mChart.setZoomEnabled(false);
        mChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);

        List<Line> lines = new ArrayList<>();
        Line line = new Line();
        line.setHasLines(true);
        line.setHasPoints(false);
        line.setColor(getResources().getColor(R.color.colorAccent));
        line.setFilled(true);

        lines.add(line);
        LineChartData data = new LineChartData(lines);
        data.setAxisXBottom(null);
        data.setAxisYLeft(null);
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        mChart.setLineChartData(data);

        drawGraph();
        refreshUI();
    }

    private void drawGraph() {
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
    }

    private void refreshUI(){
        gov.setText(GPU.getGoverner());
        min_freq.setText(GPU.getMinFreq()/1000000+getResources().getString(R.string.mhz));
        max_freq.setText(GPU.getMaxFreq()/1000000+getResources().getString(R.string.mhz));
        pwr_lvl.setText(GPU.getPwrLevel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ll_gpu_gov:
                break;
            case R.id.ll_gpu_min:
                break;
            case R.id.ll_gpu_max:
                break;
            case R.id.ll_gpu_pwr_lvl:
                break;
        }
    }
    
}