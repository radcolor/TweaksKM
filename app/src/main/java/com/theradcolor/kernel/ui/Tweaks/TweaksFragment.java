package com.theradcolor.kernel.ui.Tweaks;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ms_square.debugoverlay.DebugOverlay;
import com.ms_square.debugoverlay.Position;
import com.ms_square.debugoverlay.modules.CpuUsageModule;
import com.ms_square.debugoverlay.modules.FpsModule;
import com.ms_square.debugoverlay.modules.MemInfoModule;
import com.theradcolor.kernel.GamingService;
import com.theradcolor.kernel.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TweaksFragment extends Fragment{

    private TweaksViewModel homeViewModel;
    public TextView textView,devicetxt,cputxt,androidtxt;
    private ProgressDialog mprogress;
    private CardView eb,bb,bal,pm;
    private CheckBox dozesw,killsw,monsw;
    private Switch gmsw;
    final Handler handler = new Handler();

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(TweaksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tweaks, container, false);
        return null;
    }
}