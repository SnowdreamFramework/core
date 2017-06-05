package com.github.snowdream.support.v4.app

import android.content.Context
import android.os.Bundle

/**
 * Created by hui.yang on 2015/2/7.
 */
open class Fragment : android.support.v4.app.Fragment(), Page {

    private var mIsActive: Boolean = false
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
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        mIsActive = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsDestoryed = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsDestoryed = true
    }

    override fun isActive(): Boolean {
        return mIsActive
    }

    override fun isDestoryed(): Boolean {
        return  mIsDestoryed
    }

    override fun getContext(): Context {
        val _activity = activity ?: throw IllegalStateException("Fragment " + this
                + " not attached to Activity")

        return _activity
    }

    override fun FragmentActivity(): FragmentActivity {
        val _activity = activity ?: throw IllegalStateException("Fragment " + this
                + " not attached to Activity")

        return _activity as FragmentActivity
    }

    override fun finish() {
        val _activity = activity ?: throw IllegalStateException("Fragment " + this
                + " not attached to Activity")

        _activity.onBackPressed()
    }

    override fun getApplicationContext(): Context {
        return context.applicationContext
    }
}