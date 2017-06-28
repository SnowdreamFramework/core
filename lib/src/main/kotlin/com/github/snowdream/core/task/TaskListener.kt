package com.github.snowdream.core.task

/**
 * Created by snowdream on 17/6/4.
 */
open class TaskListener<Result, Progress> {
    open fun onStart(isOnUiThread: Boolean) {}

    open fun onFinish(isOnUiThread: Boolean) {}

    open fun onProgress(isOnUiThread: Boolean, progress: Progress) {}

    open fun onSuccess(isOnUiThread: Boolean, result: Result) {}

    open fun onCancelled(isOnUiThread: Boolean) {}

    open fun onError(isOnUiThread: Boolean, thr: Throwable) {}
}