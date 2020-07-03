package com.theradcolor.kernel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grarak.kerneladiutor.utils.Device;
import com.grarak.kerneladiutor.utils.root.RootUtils;
import com.theradcolor.kernel.fragments.AboutFragment;
import com.theradcolor.kernel.fragments.KernelFragment;
import com.theradcolor.kernel.fragments.MonitorFragment;
import com.theradcolor.kernel.utils.checkRoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class KMActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    final Fragment fragment1 = new MonitorFragment();
    final Fragment fragment2 = new KernelFragment();
    final Fragment fragment3 = new AboutFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        preferences = this.getPreferences(Context.MODE_PRIVATE);

        BottomNavigationView navigation = findViewById(R.id.nav_view);

        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(fragment1).commit();
                        active = fragment1;
                        return true;

                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(fragment2).commit();
                        active = fragment2;
                        return true;

                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(fragment3).commit();
                        active = fragment3;
                        return true;
                }
                return false;
            }
        });

        if (checkRoot.isDeviceRooted()
                && Device.getKernelVersion(false).contains("rad")
                && Device.getKernelVersion(false).contains("fakerad"))
        {
            Log.d("MainActivity", "Kernel and Root Check Passed");
            RootUtils.getSU();
        } else if (checkRoot.isDeviceRooted()) {
            Log.d("MainActivity", "Rooted and unsupported kernel");
            RootUtils.getSU();
            if (preferences.getBoolean("Show dialog", true)) {
                dialog();
            }
        } else {
            Log.d("MainActivity", "Root access and kernel verification failed");
            finish();
        }
    }

    Boolean state = true;

    private boolean dialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert))
                .setTitle("Unsupported kernel/device!")
                .setCancelable(false)
                //.setSingleChoiceItems(list, 1, null)
                .setPositiveButton("exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        editor = preferences.edit();
                        if (selectedPosition == 0) {
                            editor.putBoolean("Show dialog", false);
                            state = false;
                        } else {
                            editor.putBoolean("Show dialog", true);
                            state = true;
                        }
                        editor.apply();
                        finish();
                    }
                })
                .show();
        return state;
    }

    @Override
    protected void onDestroy() {
        RootUtils.closeSU();
        super.onDestroy();
    }

    @Override
    public void finish() {
        RootUtils.closeSU();
        super.finish();
    }
}
