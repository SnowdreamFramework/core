package com.github.snowdream.core.task

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.annotation.CallSuper
import android.text.TextUtils
import com.github.snowdream.support.v4.app.Page
import com.github.snowdream.util.ThreadUtil
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


/**
 * Created by snowdream on 17/6/4.
 */
open abstract class Task<in Params, Progress, Result> : Runnable, Cancelable {
    private var mTaskListener: TaskListener<Result, Progress>? = TaskListener()
    private var mPage: Page? = null
    private val mCancelled = AtomicBoolean()

    private val mCount = AtomicInteger(1)

    private val mInternalHandler = InternalHandler(Looper.getMainLooper())

    private var mSuccess = 0x00

    private val FINISH_UI_CALLBACK = 0x01
    private val FINISH_WORKER_CALLBACK = 0x10
    private val FINISH_ALL_CALLBACK = 0x11

    inner class InternalHandler(looper: android.os.Looper ) : Handler(looper) {

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            if (msg == null) {
                return
            }

            when(msg.what){
                FINISH_UI_CALLBACK -> {
                    mSuccess = mSuccess or 0x01
                }
                FINISH_WORKER_CALLBACK -> {
                    mSuccess = mSuccess or 0x10
                }
            }

            when(mSuccess){
                FINISH_UI_CALLBACK -> {
                    if (mPage == null) {
                        release()
                    }
                }
                FINISH_WORKER_CALLBACK -> {
                    if (mPage == null) {
                        release()
                    }
                }
                FINISH_ALL_CALLBACK -> {
                    release()
                }
            }
        }
    }

    /**
     * Task name
     */
    private var mName: String = "AsyncTask # ${mCount.getAndIncrement()}"

    /**
     * Task Status
     */
    @Volatile private var mStatus = Status.PENDING

    /**
     * Task Priority
     */
    @Volatile private var mPriority = THREAD_PRIORITY_BACKGROUND

    companion object {
        /**
         * Standard priority of application threads.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_DEFAULT = 0

        /*
         * ***************************************
         * ** Keep in sync with utils/threads.h **
         * ***************************************
         */

        /**
         * Lowest available thread priority.  Only for those who really, really
         * don't want to run if anything else is happening.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_LOWEST = 19

        /**
         * Standard priority background threads.  This gives your thread a slightly
         * lower than normal priority, so that it will have less chance of impacting
         * the responsiveness of the user interface.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_BACKGROUND = 10

        /**
         * Standard priority of threads that are currently running a user interface
         * that the user is interacting with.  Applications can not normally
         * change to this priority; the system will automatically adjust your
         * application threads as the user moves through the UI.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_FOREGROUND = -2

        /**
         * Standard priority of system display threads, involved in updating
         * the user interface.  Applications can not
         * normally change to this priority.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_DISPLAY = -4

        /**
         * Standard priority of the most important display threads, for compositing
         * the screen and retrieving input events.  Applications can not normally
         * change to this priority.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_URGENT_DISPLAY = -8

        /**
         * Standard priority of audio threads.  Applications can not normally
         * change to this priority.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_AUDIO = -16

        /**
         * Standard priority of the most important audio threads.
         * Applications can not normally change to this priority.
         * Use with [.setThreadPriority] and
         * [.setThreadPriority], **not** with the normal
         * [java.lang.Thread] class.
         */
        @JvmField val THREAD_PRIORITY_URGENT_AUDIO = -19

        /**
         * Minimum increment to make a priority more favorable.
         */
        @JvmField val THREAD_PRIORITY_MORE_FAVORABLE = -1

        /**
         * Minimum increment to make a priority less favorable.
         */
        @JvmField val THREAD_PRIORITY_LESS_FAVORABLE = +1
    }

    /**
     * Indicates the current status of the task. Each status will be set only once
     * during the lifetime of a task.
     */
    enum class Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING,
        /**
         * Indicates that the task is running.
         */
        RUNNING,
        /**
         * Indicates that [Task.runOnUiThread] [Task.runOnWorkerThread]  or has finished.
         */
        FINISHED
    }

    constructor() : this(null, THREAD_PRIORITY_BACKGROUND, null)

    constructor(name: String?) : this(name, THREAD_PRIORITY_BACKGROUND, null)

    constructor(priority: Int = THREAD_PRIORITY_BACKGROUND) : this(null, priority, null)

    constructor(listener: TaskListener<Result, Progress>?) : this(null, THREAD_PRIORITY_BACKGROUND, listener)

    constructor(name: String?, listener: TaskListener<Result, Progress>?) : this(name, THREAD_PRIORITY_BACKGROUND, listener)

    constructor(name: String?, priority: Int = THREAD_PRIORITY_BACKGROUND) : this(name, priority, null)

    constructor(name: String? = null, priority: Int = THREAD_PRIORITY_BACKGROUND, listener: TaskListener<Result, Progress>? = null) {
        if (name != null && !TextUtils.isEmpty(name)) {
            mName = name
        }

        mPriority = priority

        if (listener != null) {
            mTaskListener = listener
        }
    }

    @CallSuper
    override fun run() {
        if (ThreadUtil.isOnNonUIThread()) {
            if (TextUtils.isEmpty(mName)) {
                Thread.currentThread().name = mName
            }

            android.os.Process.setThreadPriority(mPriority)
        }
    }

    @CallSuper
    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        mCancelled.set(true)
        try {
            if (mayInterruptIfRunning && ThreadUtil.isOnNonUIThread()) {
                val t = Thread.currentThread()
                t?.interrupt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        performOnCancel()
        return true
    }

    override fun isCancelled(): Boolean {
        return mCancelled.get()
    }

    /**
     * Returns the current status of this task.

     * @return The current status.
     */
    fun getStatus(): Status {
        return mStatus
    }


    /**
     * get TaskListener<Result></Result>,Progress>

     * @return TaskListener<Result></Result>,Progress>
     */
    fun getTaskListener(): TaskListener<Result, Progress>? {
        return mTaskListener
    }

    /**
     * get input
     */
   abstract fun onGetParams(params: Params)

    /**
     * get Task Name
     *
     * @return mName,Task Name
     */
    fun getTaskName(): String {
        return mName
    }

    fun execute( params: Params) {
        onGetParams(params)
        runOnThread()
    }

    fun execute(page: Page,  params: Params) {
        mPage = page

        onGetParams(params)

        runOnThread()
    }

    private fun runOnThread() {
        if (mStatus != Status.PENDING) {
            when (mStatus) {
                Task.Status.RUNNING -> throw IllegalStateException("Cannot execute task:" + " the task is already running.")
                Task.Status.FINISHED -> throw IllegalStateException("Cannot execute task:"
                        + " the task has already been executed "
                        + "(a task can be executed only once)")
                else -> {
                }
            }
        }

        mStatus = Status.RUNNING

        performOnStart()

        TaskManager.runOnWorkerThread(this)
    }


    private fun performOnStart() {
        TaskManager.runOnWorkerThread(Runnable { mTaskListener?.onStart(false) })

        if (mPage == null || !mPage!!.isActive()) {
            return
        }

        TaskManager.runOnUiThread(mPage!!, Runnable { mTaskListener?.onStart(true) })
    }

    private fun performOnFinish() {
        mStatus = Status.FINISHED

        TaskManager.runOnWorkerThread(Runnable {
            mTaskListener?.onFinish(false)
        })

        if (mPage == null || !mPage!!.isActive()) {
            return
        }

        TaskManager.runOnUiThread(mPage!!, Runnable {
            mTaskListener?.onFinish(true)
        })
    }

    protected fun performOnProgress(progress: Progress) {
        if (isCancelled()) return

        TaskManager.runOnWorkerThread(Runnable { mTaskListener?.onProgress(false, progress) })

        if (mPage == null || !mPage!!.isActive()) {
            return
        }

        TaskManager.runOnUiThread(mPage!!, Runnable { mTaskListener?.onProgress(true, progress) })
    }

    private fun performOnCancel() {
        performOnFinish()

        if (isCancelled()) return

        TaskManager.runOnWorkerThread(Runnable {
            mTaskListener?.onCancelled(false)
            mInternalHandler.sendMessage(Message.obtain(mInternalHandler, FINISH_WORKER_CALLBACK))
        })

        if (mPage == null || !mPage!!.isActive()) {
            return
        }

        TaskManager.runOnUiThread(mPage!!, Runnable {
            mTaskListener?.onCancelled(true)
            mInternalHandler.sendMessage(Message.obtain(mInternalHandler,FINISH_UI_CALLBACK))
        })
    }

    protected fun performOnSuccess(result: Result) {
        performOnFinish()

        if (isCancelled()) return

        TaskManager.runOnWorkerThread(Runnable {
            mTaskListener?.onSuccess(false, result)
            mInternalHandler.sendMessage(Message.obtain(mInternalHandler, FINISH_WORKER_CALLBACK))
        })

        if (mPage == null || !mPage!!.isActive()) {
            return
        }

        TaskManager.runOnUiThread(mPage!!, Runnable {
            mTaskListener?.onSuccess(true, result)
            mInternalHandler.sendMessage(Message.obtain(mInternalHandler,FINISH_UI_CALLBACK))
        })
    }


    protected fun performOnError(thr: Throwable) {
        performOnFinish()

        if (isCancelled()) return

        TaskManager.runOnWorkerThread(Runnable {
            mTaskListener?.onError(false, thr)
            mInternalHandler.sendMessage(Message.obtain(mInternalHandler, FINISH_WORKER_CALLBACK))
        })

        if (mPage == null || !mPage!!.isActive()) {
            return
        }

        TaskManager.runOnUiThread(mPage!!, Runnable {
            mTaskListener?.onError(true, thr)
            mInternalHandler.sendMessage(Message.obtain(mInternalHandler,FINISH_UI_CALLBACK))
        })
    }

    private fun release(){
        mPage = null
        mTaskListener = null
    }
}

