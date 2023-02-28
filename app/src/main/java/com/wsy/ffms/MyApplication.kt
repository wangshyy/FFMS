package com.wsy.ffms

import android.app.Application
import android.content.Context
import com.wsy.ffms.core.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 *  author : wsy
 *  date   : 2023/2/24
 *  desc   : MyApplication，初始化操作
 */
class MyApplication : Application() {
    companion object {
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this.applicationContext

        //启动koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(viewModelModule) //记载module
        }
    }
}