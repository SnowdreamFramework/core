package com.github.snowdream.core.widget

import android.content.Context
import android.support.annotation.IntDef
import android.support.annotation.StringRes
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.github.snowdream.util.DensityUtil

/**
 *
 * Created by snowdream on 17/5/24.
 */
class ToastBean {
    companion object {
        /**
         * If one toast is showing, then the next toast will be skipped.
         */
        const val ONE_FIRST: Long = 0L

        /**
         * If one toast is showing, then it will be calcelled, and the next toast will be shown.
         */
        const val ONE_LAST: Long = 1L

        /**
         * All the toasts will be shown in order. It is the default mPolicy for android,but not prefect.
         */
        const val MANY_IN_ORDER: Long = 2L

        /**
         * Show the view or text notification for a short period of time.  This time could be
         * user-definable.  This is the default.

         * @see ToastBean.setDuration
         */
        const val LENGTH_SHORT: Long = 0L

        /**
         * Show the view or text notification for a long period of time.  This time could be
         * user-definable.

         * @see ToastBean.setDuration
         */
        const val LENGTH_LONG: Long = 1L

        /**
         * @hide
         */
        @IntDef(ONE_FIRST, ONE_LAST, MANY_IN_ORDER)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Policy

        /**
         * @hide
         */
        @IntDef(LENGTH_SHORT, LENGTH_LONG)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Duration
    }


    lateinit var mContext: Context

    /**
     * Return the mPolicy.

     * @see .setMPolicy
     */


    /**
     * Return the duration.

     * @see .setDuration
     */
    /**
     * Set how long to show the view for.

     * @see .LENGTH_SHORT

     * @see .LENGTH_LONG
     */
    var duration = LENGTH_SHORT

    /**
     * the view to show.

     * @see
     */
    var view: View? = null

    var text: CharSequence? = null
    var resId = -1

    /**
     * Get the location at which the notification should appear on the screen.

     * @see Gravity

     * @see .getGravity
     */
    var gravity: Int = 0
        set

    /**
     * Return the X offset in pixels to apply to the gravity's location.
     */
    var xOffset: Int = 0
        set
    /**
     * Return the Y offset in pixels to apply to the gravity's location.
     */
    var yOffset: Int = 0
        set
    /**
     * Return the horizontal margin.
     */
    var horizontalMargin: Float = 0.toFloat()
        set
    /**
     * Return the vertical margin.
     */
    var verticalMargin: Float = 0.toFloat()
        set

    private constructor() {
        throw AssertionError("No constructor allowed here!")
    }

    constructor(context: Context, text: CharSequence, @Duration duration: Long) {
        this.mContext = context
        this.text = text
        this.duration = duration
        init()
    }

    constructor(context: Context, @StringRes resId: Int, @Duration duration: Long) {
        this.mContext = context
        this.resId = resId
        this.text = context.resources.getText(resId)
        this.duration = duration
        init()
    }

    constructor(context: Context, view: View, @Duration duration: Long) {
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