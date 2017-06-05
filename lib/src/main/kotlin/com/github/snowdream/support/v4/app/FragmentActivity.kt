package com.github.snowdream.support.v4.app

import android.content.Context
import android.os.Bundle

/**
 * Created by hui.yang on 2015/2/7.
 */
open class FragmentActivity : android.support.v4.app.FragmentActivity(), Page {

    private var mIsActive: Boolean = false
    private var mIsVisible: Boolean = false
    private var mIsDestoryed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIsDestoryed = false
    }

    override fun onStart() {
        super.onStart()
        mIsActive = true
    }
    override fun onResume() {
        super.onResume()
        mIsVisible = true
    }

    override fun onPause() {
        super.onPause()
        mIsVisible = false
    }

    override fun onStop() {
        super.onStop()
        mIsActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsDestoryed = true
    }

    override fun isActive(): Boolean {
        return mIsActive
    }

    override fun isVisible(): Boolean {
        return  mIsVisible
     }

    override fun isDestoryed(): Boolean {
        return  mIsDestoryed
    }

    override fun getContext(): Context {
        return this
    }

    override fun FragmentActivity(): FragmentActivity {
        return this
    }
}
