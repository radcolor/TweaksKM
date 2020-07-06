package com.theradcolor.kernel.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.theradcolor.kernel.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class gpuActivity extends AppCompatActivity {

    LineChartView mChart;
    List<PointValue> pointValues = new ArrayList<>();
    int maxNumberOfPoints = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);
        mChart = (LineChartView) findViewById(R.id.gpu_chart);

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
    }

    private void drawGraph() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 999; ++i) {
                    float yValue = (float) (Math.random() * 100);
                    LineChartData data = mChart.getLineChartData();
                    pointValues.add(new PointValue(i, yValue));
                    data.getLines().get(0).setValues(new ArrayList<>(pointValues));
                    mChart.setLineChartData(data);
                    setViewport();
                    try {
                        Thread.sleep(1000);
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
}