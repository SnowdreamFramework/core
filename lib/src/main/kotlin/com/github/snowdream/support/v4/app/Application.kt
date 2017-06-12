package com.github.snowdream.support.v4.app

import android.content.res.Configuration
import android.support.multidex.MultiDexApplication
import com.github.snowdream.app.ApplicationLifecycleCallbacks
import com.github.snowdream.app.ApplicationLifecycleManager
import com.github.snowdream.core.widget.Toast

/**
 * Created by hui.yang on 2015/2/26.
 */
open class Application : MultiDexApplication() {
    private lateinit var applicationlifecyclemanager:ApplicationLifecycleManager

    override fun onCreate() {
        super.onCreate()


        applicationlifecyclemanager = ApplicationLifecycleManager(this)
        applicationlifecyclemanager.dispatchAppCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.onConfigurationChanged(newConfig)
        applicationlifecyclemanager.dispatchAppConfigurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationlifecyclemanager.dispatchAppLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationlifecyclemanager.dispatchAppTerminate()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        applicationlifecyclemanager.dispatchAppTrimMemory(level)
    }

    fun registerAppLifecycleCallbacks(callback: ApplicationLifecycleCallbacks) {
        applicationlifecyclemanager.registerAppLifecycleCallbacks(callback)
    }

    fun unregisterAppLifecycleCallbacks(callback: ApplicationLifecycleCallbacks) {
        applicationlifecyclemanager.unregisterAppLifecycleCallbacks(callback)
    }
}
