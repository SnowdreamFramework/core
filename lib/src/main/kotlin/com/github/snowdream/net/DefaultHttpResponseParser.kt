package com.github.snowdream.net

/**
 * Created by snowdream on 17/6/28.
 */
class DefaultHttpResponseParser :IHttpResponseParser<String>{
    override fun parse(body: String): String {
        return body
    }
}