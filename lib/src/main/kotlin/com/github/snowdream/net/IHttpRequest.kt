package com.github.snowdream.net

import com.github.snowdream.toybricks.annotation.Interface

/**
 *
 * Created by snowdream on 17/6/26.
 */
@Interface
interface IHttpRequest<Result> {
    fun addUrl(url: String): IHttpRequest<Result>
    fun addHeader(key: String, value: String): IHttpRequest<Result>
    fun addQueryParameter(key: String, value: String): IHttpRequest<Result>
    fun addPathSegment(pathSegment: String): IHttpRequest<Result>

    fun username(username: String): IHttpRequest<Result>
    fun encodedUsername(encodedUsername: String): IHttpRequest<Result>
    fun password(password: String): IHttpRequest<Result>
    fun encodedPassword(encodedPassword: String): IHttpRequest<Result>

    fun addBodyParam(key: String, value: String): IHttpRequest<Result>
    fun addEncodedBodyParam(key: String, value: String): IHttpRequest<Result>

    fun addBodyFormDataPart(key: String, value: String): IHttpRequest<Result>
    fun addBodyFormDataPart(key: String, filename: String, filepath: String): IHttpRequest<Result>
    fun addBodyFormDataPart(key: String, filename: String, mimetype: String, content: ByteArray): IHttpRequest<Result>
    fun addBodyFormDataPart(key: String, filename: String, mimetype: String, content: ByteArray, offset: Int, byteCount: Int): IHttpRequest<Result>

    fun addHttpResponseParser(parser:IHttpResponseParser<Result>): IHttpRequest<Result>

    fun get()
    fun post()
    fun head()
    fun delete()
    fun put()
    fun patch()
    fun method(method:String)

}