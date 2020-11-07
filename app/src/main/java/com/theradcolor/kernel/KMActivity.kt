package com.theradcolor.kernel

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.grarak.kerneladiutor.utils.Device
import com.grarak.kerneladiutor.utils.root.RootUtils
import com.theradcolor.kernel.fragments.AboutFragment
import com.theradcolor.kernel.fragments.KernelFragment
import com.theradcolor.kernel.fragments.MonitorFragment
import com.theradcolor.kernel.utils.checkRoot
import com.topjohnwu.superuser.Shell
import java.io.IOException

class KMActivity : AppCompatActivity() {

    private var editor: Editor? = null
    private var preferences: SharedPreferences? = null
    private val fragment1: Fragment = MonitorFragment()
    private val fragment2: Fragment = KernelFragment()
    private val fragment3: Fragment = AboutFragment()
    private val fm = supportFragmentManager
    private var active = fragment1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_km)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.actionbar)

        preferences = getPreferences(Context.MODE_PRIVATE)
        val navigation = findViewById<BottomNavigationView>(R.id.nav_view)
        fm.beginTransaction().add(R.id.main_container, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.main_container, fragment1, "1").commit()
        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    fm.beginTransaction().hide(active).show(fragment1).commit()
                    active = fragment1
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    fm.beginTransaction().hide(active).show(fragment2).commit()
                    active = fragment2
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    fm.beginTransaction().hide(active).show(fragment3).commit()
                    active = fragment3
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        if (checkRoot.isDeviceRooted()) {
            Log.d("MainActivity", "Root Check Passed!")
            RootUtils.getSU()
        } else {
            Log.d("MainActivity", "Root access and kernel verification failed")
            finish()
        }
    }

    private var state = true
    private fun dialog(): Boolean {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.CustomDialogTheme))
                .setTitle("Unsupported kernel/device!")
                .setCancelable(false) //.setSingleChoiceItems(list, 1, null)
                .setPositiveButton("exit") { dialog, whichButton ->
                    val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    editor = preferences!!.edit()
                    state = if (selectedPosition == 0) {
                        editor!!.putBoolean("Show dialog", false)
                        false
                    } else {
                        editor!!.putBoolean("Show dialog", true)
                        true
                    }
                    editor!!.apply()
                    finish()
                }
                .show()
        return state
    }

    override fun onDestroy() {
        RootUtils.closeSU()
        try {
            Shell.getShell().close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.onDestroy()
    }

    override fun finish() {
        RootUtils.closeSU()
        super.finish()
    }
}