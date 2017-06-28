package com.github.snowdream.net

import com.github.snowdream.core.task.Cancellable
import com.github.snowdream.support.v4.app.Page
import com.github.snowdream.toybricks.annotation.Interface

/**
 *
 * Created by snowdream on 17/6/27.
 */
@Interface
interface IHttp {

    fun init(option: HttpOption)

    fun <Result> start(request: IHttpRequest<Result>, response: HttpResponseCallback<Result>): Cancellable

    fun <Result> start(page: Page, request: IHttpRequest<Result>, response: HttpResponseCallback<Result>): Cancellable
}