package com.theradcolor.kernel.ui.Misc;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.theradcolor.kernel.R;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MiscFragment extends Fragment {

    private MiscViewModel dashboardViewModel;
    private TextView srgbon,srgboff,vib;
    private SeekBar seekBar;
    int progressChangedValue = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(MiscViewModel.class);
        View root = inflater.inflate(R.layout.fragment_misc, container, false);
        return root;
    }
}