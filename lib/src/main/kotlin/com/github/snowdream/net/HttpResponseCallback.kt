package com.github.snowdream.net

import com.github.snowdream.core.task.TaskListener

/**
 * Created by snowdream on 17/6/27.
 */
open class HttpResponseCallback<Result>: TaskListener<Result, Void>(){
    override fun onStart(isOnUiThread: Boolean) {}

    override fun onFinish(isOnUiThread: Boolean) {}

    override fun onProgress(isOnUiThread: Boolean, progress: Void) {}

    override fun onSuccess(isOnUiThread: Boolean, result: Result) {}

    override fun onCancelled(isOnUiThread: Boolean) {}

    override fun onError(isOnUiThread: Boolean, thr: Throwable) {}
}
