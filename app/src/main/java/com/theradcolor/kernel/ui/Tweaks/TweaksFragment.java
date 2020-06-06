package com.theradcolor.kernel.ui.Tweaks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.R;

public class TweaksFragment extends Fragment implements View.OnClickListener{

    private TweaksViewModel homeViewModel;
    public TextView textView;
    SharedPreferences preferences;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(TweaksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tweaks, container, false);
        textView = root.findViewById(R.id.kernel_name);
        textView.setText("Kernel: " + RootUtils.runCommand("uname -a"));

        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        return root;
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
        }
    }
}