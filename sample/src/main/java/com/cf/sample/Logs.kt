package com.cf.sample

import android.util.Log

object Logs {
    private const val TAG = "holder-sample"
    fun d(obj: Any?) {
        val msg = obj?.toString() ?: return
        Log.d(TAG, msg)
    }
}