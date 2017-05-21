package com.github.snowdream.core.widget

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.support.annotation.IntDef
import android.support.annotation.StringRes
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.github.snowdream.util.DensityUtil


/**
 * Toast

 * Created by yanghui.yangh on 2016/6/15.
 */
object Toast {
    private var mToast: android.widget.Toast? = null
    private var mToastBean: ToastBean? = null
    private var mToastBeanPortrait: ToastBean? = null
    private var mToastBeanLandscape: ToastBean? = null

    /**
     * @hide
     */
    @IntDef(ONE_FIRST.toLong(), ONE_LAST.toLong(), MANY_IN_ORDER.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Policy

    /**
     * @hide
     */
    @IntDef(LENGTH_SHORT.toLong(), LENGTH_LONG.toLong())
    @Retention(AnnotationRetention.SOURCE)
    annotation class Duration


    /**
     * If one toast is showing, then the next toast will be skipped.
     */
    const val ONE_FIRST = 0

    /**
     * If one toast is showing, then it will be calcelled, and the next toast will be shown.
     */
    const val ONE_LAST = 1

    /**
     * All the toasts will be shown in order. It is the default policy for android,but not prefect.
     */
    const val MANY_IN_ORDER = 2

    /**
     * Return the policy.

     * @see .setPolicy
     */
    /**
     * Set which policy to show the toast

     * @see .ONE_FIRST

     * @see .ONE_LAST

     * @see .MANY_IN_ORDER
     */
    var policy = ONE_LAST

    /**
     * Show the view or text notification for a short period of time.  This time could be
     * user-definable.  This is the default.

     * @see ToastBean.setDuration
     */
    const val LENGTH_SHORT = 0

    /**
     * Show the view or text notification for a long period of time.  This time could be
     * user-definable.

     * @see ToastBean.setDuration
     */
    const val LENGTH_LONG = 1


    //when activity or fragment change Configuration
    fun onConfigurationChanged(newConfig: Configuration) {
        if (!isShown) return
        if (mToastBeanPortrait != null && mToastBeanLandscape != null) {
            cancel()
            show(mToastBeanPortrait!!, mToastBeanLandscape!!)
        } else {
            //            show(mToastBean);
        }
    }

    //release
    fun release() {
        cancel()
        policy = ONE_LAST
    }

    //cancel
    fun cancel() {
        if (mToast != null) {
            mToast!!.cancel()
        }
        mToast = null
        mToastBean = null
    }

    /**
     * Make a standard toast that just contains a text view.

     * @param context  The context to use.  Usually your [android.app.Application] or [android.app.Activity] object.
     * *
     * @param text     The text to show.  Can be formatted text.
     * *
     * @param duration How long to display the message.  Either [.LENGTH_SHORT] or [.LENGTH_LONG]
     */
    fun show(context: Context, text: CharSequence, @Duration duration: Int) {
        val bean = ToastBean(context.applicationContext, text, duration)
        show(bean)
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.

     * @param context  The context to use.  Usually your [android.app.Application] or [android.app.Activity] object.
     * *
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * *
     * @param duration How long to display the message.  Either [.LENGTH_SHORT] or[.LENGTH_LONG]
     * *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    fun show(context: Context, @StringRes resId: Int, @Duration duration: Int) {
        val bean = ToastBean(context.applicationContext, resId, duration)
        show(bean)
    }

    /**
     * Make a toast with custom view

     * @param context  The context to use.  Usually your [android.app.Application] or [android.app.Activity] object.
     * *
     * @param view     The view to show.
     * *
     * @param duration How long to display the message.  Either [.LENGTH_SHORT] or [.LENGTH_LONG]
     */
    fun show(context: Context, view: View, @Duration duration: Int) {
        val bean = ToastBean(context.applicationContext, view, duration)
        show(bean)
    }

    /**
     * Make a toast with toastBean

     * @param portrait The ToastBean to show for portrait.
     * *
     * @param landscape The ToastBean to show for landscape.
     * *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    fun show(portrait: ToastBean, landscape: ToastBean) {
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

        if (!isShown) {
            mToastBean = bean
            mToast = createOrUpdateToastFromToastBean(null, mToastBean!!)
            mToast!!.show()
        } else {
            when (policy) {
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
                else ->{

                }
            }
        }
    }


    /**
     * Make a toast with toastBean

     * @param bean The ToastBean to show.
     * *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    fun show(bean: ToastBean) {
        show(bean, false)
    }

    /**
     * create or update toast from toast bean

     * @param toast Toast
     * *
     * @param bean  Toast Bean
     */
    private fun createOrUpdateToastFromToastBean(toast: android.widget.Toast?, bean: ToastBean): android.widget.Toast {
        var _toast = toast
        val duration: Int
        if (bean.duration == android.widget.Toast.LENGTH_SHORT) {
            duration = android.widget.Toast.LENGTH_SHORT
        } else {
            duration = android.widget.Toast.LENGTH_LONG
        }

        if (_toast == null) { //create toast
            if (!TextUtils.isEmpty(bean.text)) {
                _toast = android.widget.Toast.makeText(bean.mContext, bean.text, duration)
                bean.view = _toast!!.view
            } else if (bean.resId != -1) {
                _toast = android.widget.Toast.makeText(bean.mContext, bean.resId, duration)
                bean.view = _toast!!.view
            } else {
                _toast = android.widget.Toast(bean.mContext)
                _toast.view = bean.view
            }
        } else {   //update toast
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


    /**
     * Check whether the toast is showing now.
     */
    private val isShown: Boolean
        get() {
            val isHidden = mToastBean == null || mToast == null || mToast!!.view == null || mToast!!.view.windowVisibility != View.VISIBLE

            return !isHidden
        }

    class ToastBean {
        lateinit var mContext: Context

        /**
         * Return the duration.

         * @see .setDuration
         */
        /**
         * Set how long to show the view for.

         * @see .LENGTH_SHORT

         * @see .LENGTH_LONG
         */
        @get:Duration
        var duration = Toast.LENGTH_SHORT
        /**
         * Return the view.

         * @see .setView
         */
        /**
         * Set the view to show.

         * @see .getView
         */
        var view: View? = null
        internal var text: CharSequence? = null
        internal var resId = -1
        /**
         * Get the location at which the notification should appear on the screen.

         * @see Gravity

         * @see .getGravity
         */
        var gravity: Int = 0
            internal set
        /**
         * Return the X offset in pixels to apply to the gravity's location.
         */
        var xOffset: Int = 0
            internal set
        /**
         * Return the Y offset in pixels to apply to the gravity's location.
         */
        var yOffset: Int = 0
            internal set
        /**
         * Return the horizontal margin.
         */
        var horizontalMargin: Float = 0.toFloat()
            internal set
        /**
         * Return the vertical margin.
         */
        var verticalMargin: Float = 0.toFloat()
            internal set

        private constructor() {
            throw AssertionError("No constructor allowed here!")
        }

        constructor(context: Context, @StringRes text: CharSequence, @Duration duration: Int) {
            this.mContext = context
            this.text = text
            this.duration = duration
            init()
        }

        constructor(context: Context, @StringRes resId: Int, @Duration duration: Int) {
            this.mContext = context
            this.resId = resId
            this.text = context.resources.getText(resId)
            this.duration = duration
            init()
        }

        constructor(context: Context, view: View, @Duration duration: Int) {
            this.mContext = context
            this.view = view
            this.duration = duration
            init()
        }

        /**
         * set the default toast position
         */
        private fun init() {
            yOffset = DensityUtil.applyDimensionOffset(mContext, TypedValue.COMPLEX_UNIT_DIP, 64.0f)
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        }

        override fun equals(obj: Any?): Boolean {
            if (obj == null) return false

            if (obj !is ToastBean) return false

            val bean = obj

            if (this.text !== bean.text) {
                if (this.text == null || bean.text == null) return false

                if (this.text != bean.text) {
                    return false
                }
            }

            if (this.text != null || bean.text == null) {
                return false
            } else if (this.text == null || bean.text != null) {
                return false
            } else if (this.text == null || bean.text == null) {
                if (this.view !== bean.view) return false
            } else {
                //Nothing to do.
            }

            if (this.gravity != bean.gravity ||
                    this.xOffset != bean.xOffset || this.yOffset != bean.yOffset ||
                    this.horizontalMargin != bean.horizontalMargin ||
                    this.verticalMargin != bean.verticalMargin) {
                return false
            }

            return true
        }

        /**
         * Check whether the toast is standard which just contains a text view.
         */
        val isStandard: Boolean
            get() = !TextUtils.isEmpty(text) || resId != -1

        /**
         * Set the margins of the view.

         * @param horizontalMargin The horizontal margin, in percentage of the container width,
         * *                         between the container's edges and the notification
         * *
         * @param verticalMargin   The vertical margin, in percentage of the container height,
         * *                         between the container's edges and the notification
         */
        fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
            this.horizontalMargin = horizontalMargin
            this.verticalMargin = verticalMargin
        }

        /**
         * Set the location at which the notification should appear on the screen.

         * @see Gravity

         * @see .getGravity
         */
        fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
            this.gravity = gravity
            this.xOffset = xOffset
            this.yOffset = yOffset
        }
    }
}
