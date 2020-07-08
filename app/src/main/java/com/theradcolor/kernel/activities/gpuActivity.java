package com.theradcolor.kernel.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.utils.kernel.GPU;

public class gpuActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout governor;
    TextView gov, min_freq, max_freq, pwr_lvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);
        gov = findViewById(R.id.tv_gpu_gov);
        min_freq = findViewById(R.id.tv_gpu_min_freq);
        max_freq = findViewById(R.id.tv_gpu_max_freq);
        pwr_lvl = findViewById(R.id.tv_gpu_pwr_lvl);

        governor = findViewById(R.id.ll_gpu_gov);
        governor.setOnClickListener(this);

        refreshUI();
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

    public void refreshUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gov.setText(GPU.getGoverner());
                min_freq.setText(GPU.getMinFreq()/1000000+getResources().getString(R.string.mhz));
                max_freq.setText(GPU.getMaxFreq()/1000000+getResources().getString(R.string.mhz));
                pwr_lvl.setText(GPU.getPwrLevel());
            }
        }).start();
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