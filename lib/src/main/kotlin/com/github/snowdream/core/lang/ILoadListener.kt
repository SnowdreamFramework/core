package com.github.snowdream.core.lang

/**
 * Created by snowdream on 17/2/25.
 * @author snowdream
 * @date 2017/02/25
 */
interface ILoadListener{
     fun success()

     fun failure(t: Throwable)
}