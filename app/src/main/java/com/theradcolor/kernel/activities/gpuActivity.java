package com.theradcolor.kernel.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grarak.kerneladiutor.utils.Prefs;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.utils.kernel.GPU;

public class gpuActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout governor, gpu_min, gpu_max, gpu_pwr;
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
        gpu_min = findViewById(R.id.ll_gpu_min);
        gpu_min.setOnClickListener(this);
        gpu_max = findViewById(R.id.ll_gpu_max);
        gpu_max.setOnClickListener(this);
        gpu_pwr = findViewById(R.id.ll_gpu_pwr_lvl);
        gpu_pwr.setOnClickListener(this);

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
                gov.setText(GPU.getGovernor());
                min_freq.setText(GPU.getMinFreq() / GPU.getMinFreqOffset()+getResources().getString(R.string.mhz));
                max_freq.setText(GPU.getMaxFreq() / GPU.getMaxFreqOffset()+getResources().getString(R.string.mhz));
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
                govDialog();
                break;
            case R.id.ll_gpu_min:
                minDialog();
                break;
            case R.id.ll_gpu_max:
                maxDialog();
                break;
            case R.id.ll_gpu_pwr_lvl:
                pwrDialog();
                break;
        }
    }

    private void govDialog(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(gpuActivity.this, R.style.CustomDialogTheme));
        builder.setTitle("GPU governor");
        String[] GOVs = GPU.getAvailableGovernors().toArray(new String[0]);
        builder.setItems(GOVs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GPU.setGovernor(GOVs[which], gpuActivity.this);
                Prefs.saveString("gpu_governor", GOVs[which], gpuActivity.this);
                refreshUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void minDialog(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(gpuActivity.this, R.style.CustomDialogTheme));
        builder.setTitle("GPU minimum freq");
        String[] gpu_min = GPU.getAdjustedFreqs(this).toArray(new String[0]);
        builder.setItems(gpu_min, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GPU.setMinFreq(GPU.getAvailableFreqs().get(which)/1000000, gpuActivity.this);
                Prefs.saveInt("gpu_min_freq", GPU.getAvailableFreqs().get(which)/1000000, gpuActivity.this);
                refreshUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void maxDialog(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(gpuActivity.this, R.style.CustomDialogTheme));
        builder.setTitle("GPU maximum freq");
        String[] gpu_max = GPU.getAdjustedFreqs(this).toArray(new String[0]);
        builder.setItems(gpu_max, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GPU.setMaxFreq(GPU.getAvailableFreqs().get(which), gpuActivity.this);
                Prefs.saveInt("gpu_max_freq", GPU.getAvailableFreqs().get(which), gpuActivity.this);
                refreshUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void pwrDialog() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(new ContextThemeWrapper(gpuActivity.this, R.style.CustomDialogTheme));
        builder.setTitle("GPU power level");
        final View mpLayout = getLayoutInflater().inflate(R.layout.gpu_pwr_dialog, null);
        EditText editText = mpLayout.findViewById(R.id.pwr_lvl_et);
        editText.setText(GPU.getPwrLevel());
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GPU.setPwrLevel(editText.getText().toString(), gpuActivity.this);
                Prefs.saveString("gpu_pwr_lvl", editText.getText().toString(), gpuActivity.this);
                refreshUI();
            }
        });

        builder.setView(mpLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}