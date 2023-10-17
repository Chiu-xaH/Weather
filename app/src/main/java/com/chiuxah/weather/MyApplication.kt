package com.chiuxah.weather

import android.app.Application
import android.content.Context

open class MyApplication : Application() {
    companion object {
        const val mytoken = "trDGvpGzgEqdkU0a"

        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}