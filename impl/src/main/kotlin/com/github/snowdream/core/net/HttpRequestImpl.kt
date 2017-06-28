package com.github.snowdream.core.net

import com.github.snowdream.net.HTTP.Companion.DEFAULT_USER_AGENT
import com.github.snowdream.net.HTTP.Companion.USER_AGENT
import com.github.snowdream.net.IHttpRequest
import com.github.snowdream.net.IHttpResponseParser
import com.github.snowdream.toybricks.annotation.Implementation
import com.github.snowdream.util.MimeTypeUtil
import okhttp3.*
import java.io.File

/**
 * Constants and static helpers related to the HTTP protocol.
 *
 * Created by snowdream on 17/6/26.
 */
@Implementation(IHttpRequest::class)
class HttpRequestImpl<Result> : IHttpRequest<Result> {
    lateinit var mResponseParser: IHttpResponseParser<Result>
    lateinit var mRequest: Request
    private val mRequestBuilder: Request.Builder = Request.Builder()
    private var mRequestUrlBuilder: HttpUrl.Builder = HttpUrl.Builder()
    private val mFormBodyBuilder: FormBody.Builder = FormBody.Builder()
    private val mMultipartBodyBuilder: MultipartBody.Builder = MultipartBody.Builder()
    private var mIsMultipart: Boolean = false
    private var mUserAgent:String = DEFAULT_USER_AGENT

    override fun addUrl(url: String): IHttpRequest<Result> {
        mRequestUrlBuilder = HttpUrl.parse(url)!!.newBuilder()

        return this
    }

    override fun addHeader(key: String, value: String): IHttpRequest<Result> {
        mRequestBuilder.addHeader(key, value)

        if (key == USER_AGENT){
            mUserAgent = value
        }

        return this
    }

    override fun addQueryParameter(key: String, value: String): IHttpRequest<Result> {
        mRequestUrlBuilder.addQueryParameter(key, value)

        return this
    }

    override fun addPathSegment(pathSegment: String): IHttpRequest<Result> {
        mRequestUrlBuilder.addPathSegment(pathSegment)

        return this
    }

    override fun username(username: String): IHttpRequest<Result> {
        mRequestUrlBuilder.username(username)

        return this
    }

    override fun encodedUsername(encodedUsername: String): IHttpRequest<Result> {
        mRequestUrlBuilder.encodedUsername(encodedUsername)

        return this
    }

    override fun password(password: String): IHttpRequest<Result> {
        mRequestUrlBuilder.password(password)

        return this
    }

    override fun encodedPassword(encodedPassword: String): IHttpRequest<Result> {
        mRequestUrlBuilder.encodedPassword(encodedPassword)

        return this
    }

    override fun addBodyParam(key: String, value: String): IHttpRequest<Result> {
        mFormBodyBuilder.add(key, value)

        return this
    }

    override fun addEncodedBodyParam(key: String, value: String): IHttpRequest<Result> {
        mFormBodyBuilder.addEncoded(key, value)

        return this
    }

    override fun addBodyFormDataPart(key: String, value: String): IHttpRequest<Result> {
        mMultipartBodyBuilder.addFormDataPart(key, value)

        mIsMultipart = true

        return this
    }

    override fun addBodyFormDataPart(key: String, filename: String, filepath: String): IHttpRequest<Result> {
        val mediatype: MediaType? = MediaType.parse(MimeTypeUtil.getMimeType(filepath))
        val requestBody: RequestBody = RequestBody.create(mediatype, File(filepath))

        mMultipartBodyBuilder.addFormDataPart(key, filename, requestBody)

        mIsMultipart = true

        return this
    }

    override fun addBodyFormDataPart(key: String, filename: String, mimetype: String, content: ByteArray): IHttpRequest<Result> {
        val mediatype: MediaType? = MediaType.parse(mimetype)
        val requestBody: RequestBody = RequestBody.create(mediatype, content)

        mMultipartBodyBuilder.addFormDataPart(key, filename, requestBody)

        mIsMultipart = true

        return this
    }

    override fun addBodyFormDataPart(key: String, filename: String, mimetype: String, content: ByteArray, offset: Int, byteCount: Int): IHttpRequest<Result> {
        val mediatype: MediaType? = MediaType.parse(mimetype)
        val requestBody: RequestBody = RequestBody.create(mediatype, content, offset, byteCount)

        mMultipartBodyBuilder.addFormDataPart(key, filename, requestBody)

        mIsMultipart = true
        return this
    }

    override fun get() {
        mRequest = getRequestBuilder()
                .get()
                .build()
    }

    override fun post() {
        mRequest = getRequestBuilder()
                .post(getRequestBody())
                .build()
    }

    override fun head() {
        mRequest =getRequestBuilder()
                .head()
                .build()
    }

    override fun delete() {
        mRequest = getRequestBuilder()
                .delete(getRequestBody())
                .build()
    }

    override fun put() {
        mRequest =getRequestBuilder()
                .put(getRequestBody())
                .build()
    }

    override fun patch() {
        mRequest = getRequestBuilder()
                .patch(getRequestBody())
                .build()
    }

    override fun method(method: String) {
        mRequest = getRequestBuilder()
                .method(method, getRequestBody())
                .build()
    }

    private fun getRequestBuilder():Request.Builder {
       mRequestBuilder.url(mRequestUrlBuilder.build())

        if (mUserAgent == DEFAULT_USER_AGENT) {
            mRequestBuilder.addHeader(USER_AGENT, mUserAgent)
        }

        return mRequestBuilder
    }

    private fun getRequestBody(): RequestBody {
        val body: RequestBody

        if (mIsMultipart) {
            body = mMultipartBodyBuilder.build()
        } else {
            body = mFormBodyBuilder.build()
        }

        return body
    }

    override fun addHttpResponseParser(parser: IHttpResponseParser<Result>): IHttpRequest<Result> {
        mResponseParser = parser

        return this
    }
}