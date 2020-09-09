package com.theradcolor.kernel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.grarak.kerneladiutor.utils.Device;
import com.theradcolor.kernel.R
import kotlinx.android.synthetic.main.activity_updater.*

class UpdaterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updater)
        val kernelString = "<font color=#FFFFFF> <b> Current kernel version: </b> </font>" + Device.getKernelVersion(true);
        linux_version.text = Html.fromHtml(kernelString)
    }
}