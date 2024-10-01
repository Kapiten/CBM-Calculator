package com.cbm.android.cbmcalculator.ui.calculcation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cbm.android.cbmcalculator.R

class CalculationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CalculationFragment.newInstance())
                .commitNow()
        }
    }
}