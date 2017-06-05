package com.github.snowdream.core.task

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.github.snowdream.support.v4.app.Page
import com.github.snowdream.util.ThreadUtil

/**
 * Created by snowdream on 17/6/3.
 */
class TaskManager {
    companion object {
        private val mHandler = Handler(Looper.getMainLooper())

        @JvmStatic fun runOnUiThread(page: Page, action: Runnable) {
            if (!page.isActive()) {
                Log.w("","Page is null or not active.")
                return
            }

            if (ThreadUtil.isOnNonUIThread()) {
                mHandler.post(action)
            } else {
                action.run()
            }
        }

        @JvmStatic fun runOnWorkerThread(action: Runnable) {
            if (ThreadUtil.isOnUIThread()) {
                TaskExecutorManager.getExecutor().execute(action)
            } else {
                action.run()
            }
        }
    }
}