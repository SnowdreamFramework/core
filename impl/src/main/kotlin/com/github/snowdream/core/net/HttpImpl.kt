package com.github.snowdream.core.net

import com.github.snowdream.core.task.Cancellable
import com.github.snowdream.core.task.Task
import com.github.snowdream.net.*
import com.github.snowdream.support.v4.app.Page
import com.github.snowdream.toybricks.annotation.Implementation
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by snowdream on 17/6/27.
 */
@Implementation(value = IHttp::class, singleton = true)
class HttpImpl : IHttp {
    private lateinit var mHttpOption: HttpOption
    private var mOkHttpClient: OkHttpClient = OkHttpClient()

    override fun init(option: HttpOption) {
        mHttpOption = option

        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.connectTimeout(mHttpOption.connectTimeout, TimeUnit.SECONDS)
        builder.readTimeout(mHttpOption.readTimeout, TimeUnit.SECONDS)
        builder.writeTimeout(mHttpOption.writeTimeout, TimeUnit.SECONDS)
        mOkHttpClient = builder.build()
    }

    override fun <Result> start(page: Page, request: IHttpRequest<Result>, response: HttpResponseCallback<Result>): Cancellable {
        val task: AsyncHttpTask<Result> = AsyncHttpTask(request)
        val cancellable: Cancellable = task.execute(page, null, response)
        return cancellable
    }

    override fun <Result> start(request: IHttpRequest<Result>, response: HttpResponseCallback<Result>): Cancellable {
        val task: AsyncHttpTask<Result> = AsyncHttpTask(request)
        val cancellable: Cancellable = task.execute(null, response)
        return cancellable
    }

    inner class AsyncHttpTask<Result>(request: IHttpRequest<Result>) : Task<Void, Void, Result>("AsyncHttpTask") {
        var mRequest: IHttpRequest<Result> = request
        lateinit var mCall:Call

        override fun onGetParams(params: Void?) {
        }

        override fun run() {
            super.run()

            if (mRequest !is HttpRequestImpl) {
                performOnError(Throwable("Wrong Implemention for IHttpRequest"))
                return
            }

            val request: HttpRequestImpl<Result> = mRequest as HttpRequestImpl

            mCall = mOkHttpClient.newCall(request.mRequest)
            mCall.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    performOnError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        performOnError(IOException("Unexpected code " + response))
                        return
                    }

                    val responseHeaders = response.headers()
                    var i = 0
                    val size = responseHeaders.size()
                    while (i < size) {
                        println(responseHeaders.name(i) + ": " + responseHeaders.value(i))
                        i++
                    }

                    val result: Result =  request.mResponseParser.parse(response.body()!!.string())

                    performOnSuccess(result)
                }
            })
        }

        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            mCall.cancel()
            return super.cancel(mayInterruptIfRunning)
        }

        override fun isCancelled(): Boolean {
            return super.isCancelled() && mCall.isCanceled
        }
    }
}