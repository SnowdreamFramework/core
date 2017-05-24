package com.github.snowdream.support.v4.app

import android.content.res.Configuration
import android.support.multidex.MultiDexApplication
import com.github.snowdream.core.widget.Toast

/**
 * Created by hui.yang on 2015/2/26.
 */
class Application : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Toast.onConfigurationChanged(newConfig)
    }
}
