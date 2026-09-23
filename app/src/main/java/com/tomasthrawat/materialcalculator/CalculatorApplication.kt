package com.tomasthrawat.materialcalculator

import android.app.Application
import com.google.android.material.color.DynamicColors

class CalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
