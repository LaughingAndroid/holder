package com.cf.holder

import android.app.Application
import android.content.Context

/**
 *
 * @ClassName: utils
 * @Description:
 * @Author: Laughing
 * @CreateDate: 2019/10/10 19:25
 * @Version: 1.7.0
 */
object HolderManager {
    lateinit var application: Application
    fun init(app: Context) {
        application = app.applicationContext as Application
    }
}