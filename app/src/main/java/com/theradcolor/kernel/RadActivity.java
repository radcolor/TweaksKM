package com.theradcolor.kernel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grarak.kerneladiutor.utils.root.RootUtils;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class RadActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rad);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        preferences = this.getPreferences(Context.MODE_PRIVATE);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if (checkRoot.isDeviceRooted() && System.getProperty("os.version").contains("rad")) {
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
        new AlertDialog.Builder(this)
                .setTitle("Unsupported kernel/device!")
                .setCancelable(false)
                //.setSingleChoiceItems(list, 1, null)
                .setPositiveButton("exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        if (selectedPosition == 0) {
                            editor = preferences.edit();
                            editor.putBoolean("Show dialog", false);
                            state = false;
                        } else {
                            editor = preferences.edit();
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
}
