package com.github.snowdream.core.lang

import android.content.Context
import com.github.snowdream.toybricks.annotation.Interface

/**
 * Created by snowdream on 17/2/25.
 * @author snowdream
 * @date 2017/02/25
 */
@Interface
interface ISoLoader {

    /**
     * Load libraries synchronously. Like System.loadLibrary();
     */
    fun loadLibrary(context: Context, library: String)

    /**
     * Load libraries synchronously. Like System.loadLibrary();
     */
    fun loadLibrary(context: Context, library: String,version: String?)

    /**
     * To load libraries asynchronously.
     * Simply pass a LoadListener instance to the loadLibrary call.
     */
    fun loadLibrary(context: Context, library: String,listener: ILoadListener?)

    /**
     * To load libraries asynchronously.
     * Simply pass a LoadListener instance to the loadLibrary call.
     */
    fun loadLibrary(context: Context, library: String,version: String? ,listener: ILoadListener?)
}