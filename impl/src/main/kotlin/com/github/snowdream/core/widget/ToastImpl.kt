package com.github.snowdream.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.text.TextUtils
import android.view.View
import com.github.snowdream.core.widget.ToastBean.Companion.MANY_IN_ORDER
import com.github.snowdream.core.widget.ToastBean.Companion.ONE_FIRST
import com.github.snowdream.core.widget.ToastBean.Companion.ONE_LAST
import com.github.snowdream.toybricks.annotation.Implementation

/**
 * Created by snowdream on 17/5/24.
 */
@Implementation(IToast::class)
class ToastImpl : IToast {
    private var mToast: android.widget.Toast? = null
    private var mToastBean: ToastBean? = null
    private var mToastBeanPortrait: ToastBean? = null
    private var mToastBeanLandscape: ToastBean? = null

    /**
     * Set which mPolicy to show the toast

     * @see .ONE_FIRST

     * @see .ONE_LAST

     * @see .MANY_IN_ORDER
     */
    var mPolicy = ONE_LAST


    override fun onConfigurationChanged(newConfig: Configuration) {
        if (!isShown()) return
        if (mToastBeanPortrait != null && mToastBeanLandscape != null) {
            cancel()
            show(mToastBeanPortrait!!, mToastBeanLandscape!!)
        } else {
            // show(mToastBean);
        }
    }

    override fun release() {
        cancel()
        mPolicy = ONE_LAST
    }

    override fun cancel() {
        mToast?.cancel()
    }

    override fun getPolicy(): Long {
        return mPolicy
    }

    override fun setPolicy(policy: Long) {
        this.mPolicy = policy
    }

    override fun isShown(): Boolean {
        val isHidden:Boolean = mToastBean == null || mToast?.view?.windowVisibility != View.VISIBLE

        return !isHidden
    }

    override fun show(context: Context, text: CharSequence, duration: Long) {
        val bean = ToastBean(context.applicationContext, text, duration)
        show(bean)
    }

    override fun show(context: Context, resId: Int, duration: Long) {
        val bean = ToastBean(context.applicationContext, resId, duration)
        show(bean)
    }

    override fun show(context: Context, view: View, duration: Long) {
        val bean = ToastBean(context.applicationContext, view, duration)
        show(bean)
    }

    override fun show(portrait: ToastBean, landscape: ToastBean) {
        mToastBeanLandscape = landscape
        mToastBeanPortrait = portrait

        val context = portrait.mContext
        val bean: ToastBean
        val orientation = context.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            bean = mToastBeanPortrait as ToastBean
        } else {
            bean = mToastBeanLandscape as ToastBean
        }

        show(bean, true)
    }

    override fun show(bean: ToastBean) {
        show(bean, false)
    }

    /**
     * Make a toast with toastBean

     * @param bean The ToastBean to show.
     * *
     * @param hasPortraitAndLandscape   Portrait and Landscape has diffent toast.
     * *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    private fun show(bean: ToastBean, hasPortraitAndLandscape: Boolean) {
        if (!hasPortraitAndLandscape) {
            mToastBeanLandscape = null
            mToastBeanPortrait = null
        }

        if (!isShown()) {
            mToastBean = bean
            mToast = createOrUpdateToastFromToastBean(null, mToastBean!!)
            mToast!!.show()
        } else {
            when (mPolicy) {
                ONE_FIRST -> {
                }
                MANY_IN_ORDER -> {
                    mToastBean = bean
                    mToast = createOrUpdateToastFromToastBean(null, mToastBean!!)
                    mToast!!.show()
                }
                ONE_LAST -> {
                    if (mToastBean!!.isStandard && bean.isStandard) {
                        if (mToastBean == bean) {
                            if (!TextUtils.isEmpty(bean.text)) {
                                mToast!!.setText(bean.text)
                            } else if (bean.resId != -1) {
                                mToast!!.setText(bean.resId)
                            }
                        } else {
                            createOrUpdateToastFromToastBean(mToast, bean)
                        }
                    } else {
                        if (mToastBean != bean) {
                            cancel() // to avoid RuntimeException("This Toast was not created with Toast.makeText()");
                            mToast = createOrUpdateToastFromToastBean(null, bean)
                            mToast!!.show()
                        }
                    }

                    mToastBean = bean
                }
                else -> {

                }
            }
        }
    }

    /**
     * create or update toast from toast bean

     * @param toast Toast
     * *
     * @param bean  Toast Bean
     */
    @SuppressLint("ShowToast")
    private fun createOrUpdateToastFromToastBean(toast: android.widget.Toast?, bean: ToastBean): android.widget.Toast {
        val _toast: android.widget.Toast
        val duration: Int

        if (bean.duration.toInt() == android.widget.Toast.LENGTH_SHORT) {
            duration = android.widget.Toast.LENGTH_SHORT
        } else {
            duration = android.widget.Toast.LENGTH_LONG
        }

        if (toast == null) { //create toast
            if (!TextUtils.isEmpty(bean.text)) {
                _toast = android.widget.Toast.makeText(bean.mContext, bean.text, duration)
                bean.view = _toast.view
            } else if (bean.resId != -1) {
                _toast = android.widget.Toast.makeText(bean.mContext, bean.resId, duration)
                bean.view = _toast.view
            } else {
                _toast = android.widget.Toast(bean.mContext)
                _toast.view = bean.view
            }
        } else {   //update toast
            _toast = toast
            if (!TextUtils.isEmpty(bean.text)) {
                mToast!!.setText(bean.text)
            } else if (bean.resId != -1) {
                mToast!!.setText(bean.resId)
            } else if (bean.view != null) {
                mToast!!.view = bean.view
            }
        }

        _toast.setGravity(bean.gravity, bean.xOffset, bean.yOffset)
        _toast.setMargin(bean.horizontalMargin, bean.horizontalMargin)

        return _toast
    }
}