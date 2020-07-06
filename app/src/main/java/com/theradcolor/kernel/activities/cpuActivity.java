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

public class cpuActivity extends AppCompatActivity {

    LineChartView mChart, nChart;
    List<PointValue> pointValues = new ArrayList<>();
    int maxNumberOfPoints = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
        mChart = (LineChartView) findViewById(R.id.cpu0_chart);
        nChart = (LineChartView) findViewById(R.id.cpu1_chart);

        mChart.setInteractive(true);
        mChart.setZoomEnabled(false);
        mChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);

        nChart.setInteractive(true);
        nChart.setZoomEnabled(false);
        nChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);

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
        nChart.setLineChartData(data);

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
                    nChart.setLineChartData(data);
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
            viewport.left = size - 8;
            mChart.setCurrentViewport(viewport);
            nChart.setCurrentViewport(viewport);
        }
    }

}