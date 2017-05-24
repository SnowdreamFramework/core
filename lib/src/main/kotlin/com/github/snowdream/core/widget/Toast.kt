package com.github.snowdream.core.widget

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.support.annotation.StringRes
import android.view.View
import com.github.snowdream.toybricks.ToyBricks

/**
 * Created by snowdream on 17/5/24.
 */
class Toast {
    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        @JvmField val sToast = ToyBricks.getImplementation(IToast::class.java)

        @JvmStatic fun onConfigurationChanged(newConfig: Configuration) {
            sToast.onConfigurationChanged(newConfig)
        }

        @JvmStatic fun release() {
            sToast.release()
        }

        @JvmStatic fun cancel() {
            sToast.cancel()
        }

        @JvmStatic fun isShown(): Boolean {
            return sToast.isShown()
        }

        /**
         * Return the policy.

         * @see .setPolicy
         */
        @ToastBean.Companion.Policy
        @JvmStatic fun getPolicy(): Long {
            return sToast.getPolicy()
        }

        /**
         * Set which policy to show the toast

         * @see .ONE_FIRST

         * @see .ONE_LAST

         * @see .MANY_IN_ORDER
         */
        @JvmStatic fun setPolicy(@ToastBean.Companion.Policy policy: Long) {
            sToast.setPolicy(policy)
        }

        /**
         * Make a standard toast that just contains a text view.

         * @param context  The context to use.  Usually your [android.app.Application] or [android.app.Activity] object.
         * *
         * @param text     The text to show.  Can be formatted text.
         * *
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or [.LENGTH_LONG]
         */
        @JvmStatic fun show(context: Context, text: CharSequence, @ToastBean.Companion.Duration duration: Long) {
            sToast.show(context, text, duration)
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
        @JvmStatic fun show(context: Context, @StringRes resId: Int, @ToastBean.Companion.Duration duration: Long) {
            sToast.show(context, resId, duration)
        }

        /**
         * Make a toast with custom view

         * @param context  The context to use.  Usually your [android.app.Application] or [android.app.Activity] object.
         * *
         * @param view     The view to show.
         * *
         * @param duration How long to display the message.  Either [.LENGTH_SHORT] or [.LENGTH_LONG]
         */
        @JvmStatic fun show(context: Context, view: View, @ToastBean.Companion.Duration duration: Long) {
            sToast.show(context, view, duration)
        }

        /**
         * Make a toast with toastBean

         * @param portrait The ToastBean to show for portrait.
         * *
         * @param landscape The ToastBean to show for landscape.
         * *
         * @throws Resources.NotFoundException if the resource can't be found.
         */
        @JvmStatic fun show(portrait: ToastBean, landscape: ToastBean) {
            sToast.show(portrait, landscape)
        }

        /**
         * Make a toast with toastBean

         * @param bean The ToastBean to show.
         * *
         * @throws Resources.NotFoundException if the resource can't be found.
         */
        @JvmStatic fun show(bean: ToastBean) {
            sToast.show(bean)
        }
    }
}