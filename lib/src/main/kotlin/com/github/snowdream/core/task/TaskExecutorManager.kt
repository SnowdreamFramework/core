package com.github.snowdream.core.task

import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger


/**
 * Created by snowdream on 17/6/3.
 */
class TaskExecutorManager {
    companion object {
        /**
         * An [Executor] that can be used to execute tasks in parallel.
         */
        @JvmField val PARALLEL_EXECUTOR: Executor = ParallelExecutor()

        private class ParallelExecutor : Executor {
            private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
            // We want at least 2 threads and at most 4 threads in the core pool,
            // preferring to have 1 less than the CPU count to avoid saturating
            // the CPU with background work
            private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
            private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
            private val KEEP_ALIVE_SECONDS = 30L

            private val sThreadFactory = object : ThreadFactory {
                private val mCount = AtomicInteger(1)

                override fun newThread(r: Runnable): Thread {
                    return Thread(r, "AsyncTask #" + mCount.getAndIncrement())
                }
            }

            private val sPoolWorkQueue = LinkedBlockingQueue<Runnable>(128)

            private val threadPoolExecutor = ThreadPoolExecutor(
                    CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                    sPoolWorkQueue, sThreadFactory)

            init {
                threadPoolExecutor.allowCoreThreadTimeOut(true);
            }

            override fun execute(r: Runnable) {
                threadPoolExecutor.execute(r)
            }
        }

        /**
         * An [Executor] that executes tasks one at a time in serial
         * order.  This serialization is global to a particular process.
         */
        @JvmField val SERIAL_EXECUTOR: Executor = SerialExecutor()

        private class SerialExecutor : Executor {
            internal val mTasks = ArrayDeque<Runnable>()
            internal var mActive: Runnable? = null

            @Synchronized override fun execute(r: Runnable) {
                mTasks.offer(Runnable {
                    try {
                        r.run()
                    } finally {
                        scheduleNext()
                    }
                })
                if (mActive == null) {
                    scheduleNext()
                }
            }

            @Synchronized protected fun scheduleNext() {
                mActive = mTasks.poll()
                mActive?.let {
                    PARALLEL_EXECUTOR.execute(mActive!!)
                }
            }
        }

        private var mExecutor = PARALLEL_EXECUTOR

        @JvmStatic fun setExecutor(executor: Executor) {
            mExecutor = executor
        }

        @JvmStatic fun getExecutor(): Executor {
            return mExecutor
        }
    }
}

