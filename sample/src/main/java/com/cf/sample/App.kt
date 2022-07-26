package com.cf.sample

import android.app.Application
import com.cf.holder.HolderManager
import leakcanary.LeakCanary

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        HolderManager.init(this)
    }
}