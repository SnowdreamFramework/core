package com.github.snowdream.app

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.os.Bundle

import com.autonavi.common.utils.Logs
import com.autonavi.common.utils.PlaySoundUtils
import com.autonavi.minimap.R

import java.util.ArrayList

/**
 * Created by snowdream on 17/3/5.

 * @see http://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background

 * @see http://stackoverflow.com/a/23299321


 * @author snowdream
 * *
 * @date 2017/03/05
 */
class ApplicationLifecycleManager {
    private val mApplicationLifecycleCallbacks = ArrayList<ApplicationLifecycleCallbacks>()

    private lateinit var mActivityLifecycleHandler: ActivityLifecycleHandler

    private constructor() {
        throw AssertionError("No constructor allowed here!")
    }

    constructor(application: Application) {
        mActivityLifecycleHandler = ActivityLifecycleHandler()
        application.registerActivityLifecycleCallbacks(mActivityLifecycleHandler)
    }

    fun registerAppLifecycleCallbacks(callback: ApplicationLifecycleCallbacks) {
        synchronized(mApplicationLifecycleCallbacks) {
            mApplicationLifecycleCallbacks.add(callback)
        }
    }

    fun unregisterAppLifecycleCallbacks(callback: ApplicationLifecycleCallbacks) {
        synchronized(mApplicationLifecycleCallbacks) {
            mApplicationLifecycleCallbacks.remove(callback)
        }
    }

    private fun collectAppLifecycleCallbacks(): Array<Any>? {
        var callbacks: Array<Any>? = null
        synchronized(mApplicationLifecycleCallbacks) {
            if (mApplicationLifecycleCallbacks.size > 0) {
                callbacks = mApplicationLifecycleCallbacks.toTypedArray()
            }
        }
        return callbacks
    }

    fun dispatchAppCreate() {
        val callbacks = collectAppLifecycleCallbacks()
        if (callbacks != null) {
            for (i in callbacks.indices) {
                (callbacks[i] as ApplicationLifecycleCallbacks).onAppCreate()
            }
        }
    }

    fun dispatchAppConfigurationChanged(newConfig: Configuration) {
        val callbacks = collectAppLifecycleCallbacks()
        if (callbacks != null) {
            for (i in callbacks.indices) {
                (callbacks[i] as ApplicationLifecycleCallbacks).onAppConfigurationChanged(newConfig)
            }
        }
    }

    fun dispatchAppLowMemory() {
        val callbacks = collectAppLifecycleCallbacks()
        if (callbacks != null) {
            for (i in callbacks.indices) {
                (callbacks[i] as ApplicationLifecycleCallbacks).onAppLowMemory()
            }
        }
    }

    fun dispatchAppTrimMemory(level: Int) {
        val callbacks = collectAppLifecycleCallbacks()
        if (callbacks != null) {
            for (i in callbacks.indices) {
                (callbacks[i] as ApplicationLifecycleCallbacks).onAppTrimMemory(level)
            }
        }
    }

    fun dispatchAppTerminate() {
        val callbacks = collectAppLifecycleCallbacks()
        if (callbacks != null) {
            for (i in callbacks.indices) {
                (callbacks[i] as ApplicationLifecycleCallbacks).onAppTerminate()
            }
        }
    }

    private fun dispatchAppForeground() {
        val callbacks = collectAppLifecycleCallbacks()
        if (callbacks != null) {
            for (i in callbacks.indices) {
                (callbacks[i] as ApplicationLifecycleCallbacks).onAppForeground()
            }
        }
    }

    private fun dispatchAppBackground() {
        val callbacks = collectAppLifecycleCallbacks()
        if (callbacks != null) {
            for (i in callbacks.indices) {
                (callbacks[i] as ApplicationLifecycleCallbacks).onAppBackground()
            }
        }
    }

    /**
     * According to the following activity lifecycle.

     * onActivityCreated: SplashActivity
     * onActivityStarted: SplashActivity
     * onActivityResumed: SplashActivity
     * onActivityPaused: SplashActivity
     * onActivityCreated: MainActivity
     * onActivityStarted: MainActivity
     * onActivityResumed: MainActivity
     * onActivityStopped: SplashActivity
     * onActivityDestroyed: SplashActivity
     */
    private inner class ActivityLifecycleHandler : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
            sActivityVisible = true

            if (sAppBackground) {
                sAppBackground = false
                dispatchAppForeground()
            }
        }

        override fun onActivityPaused(activity: Activity) {
            sActivityVisible = false
        }

        override fun onActivityStopped(activity: Activity) {
            if (!sActivityVisible) {
                sAppBackground = true
                dispatchAppBackground()
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }

    companion object {
        private var sActivityVisible: Boolean = false
        private var sAppBackground: Boolean = false
    }
}
