package com.github.snowdream.support.v4.app

import android.content.Context

/**
 * Page,may be an activity, a fragment, or a dialog.
 * Created by hui.yang on 2015/10/21.
 */
interface Page {
    fun isActive(): Boolean
    fun isVisible(): Boolean
    fun isDestoryed(): Boolean
    fun getContext(): Context
    fun getApplicationContext(): Context
    fun FragmentActivity(): FragmentActivity
    fun finish()
}
