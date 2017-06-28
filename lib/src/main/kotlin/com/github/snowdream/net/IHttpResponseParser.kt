package com.github.snowdream.net

import java.io.InputStream

/**
 * Created by snowdream on 17/6/28.
 */
interface IHttpResponseParser<out Result> {

    fun parse(body:String):Result

//    fun parse(body:InputStream):Result
}