package com.wsy.ffms

import android.app.Application
import android.content.Context

/**
 *  author : wsy
 *  date   : 2023/2/24
 *  desc   :
 */
class MyApplication: Application() {
    companion object{
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this.applicationContext
    }
}