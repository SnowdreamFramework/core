package com.github.snowdream.app

import android.content.res.Configuration

/**
 * Created by snowdream on 17/3/5.

 * @author snowdream
 * *
 * @date 2017/03/05
 */
interface ApplicationLifecycleCallbacks {
    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     * If you override this method, be sure to call super.onCreate().
     */
    fun onAppCreate()

    fun onAppConfigurationChanged(newConfig: Configuration)

    fun onAppLowMemory()

    fun onAppTrimMemory(level: Int)

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    fun onAppTerminate()

    /**
     * From Background to Foreground
     */
    fun onAppForeground()

    /**
     * From Foreground to Background
     */
    fun onAppBackground()
}
