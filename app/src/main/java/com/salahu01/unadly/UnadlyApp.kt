package com.salahu01.unadly

import android.app.Application

class UnadlyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AdBlocker.init(this)
    }
}
