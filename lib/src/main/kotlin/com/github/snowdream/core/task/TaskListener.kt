package com.github.snowdream.core.task

/**
 * Created by snowdream on 17/6/4.
 */
class TaskListener<Result, Progress> {
    fun onStart(isOnUiThread: Boolean) {}

    fun onFinish(isOnUiThread: Boolean) {}

    fun onProgress(isOnUiThread: Boolean, progress: Progress) {}

    fun onSuccess(isOnUiThread: Boolean, result: Result) {}

    fun onCancelled(isOnUiThread: Boolean) {}

    fun onError(isOnUiThread: Boolean, thr: Throwable) {}
}