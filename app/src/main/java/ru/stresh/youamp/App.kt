package ru.stresh.youamp

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDi(this)
    }
}