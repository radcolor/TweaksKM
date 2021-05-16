package com.theradcolor.kernel.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.grarak.kerneladiutor.utils.Device
import com.theradcolor.kernel.R
import kotlinx.android.synthetic.main.activity_spectrum.*

class SpectrumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spectrum)

        spec_supp_txt.visibility = View.VISIBLE
        spec_scroll_vw.visibility = View.INVISIBLE
    }
}