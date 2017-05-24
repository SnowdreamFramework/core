package com.github.snowdream.core.lang

import android.content.Context
import com.github.snowdream.toybricks.ToyBricks

/**
 * Created by snowdream on 17/2/25.
 * @author snowdream
 * @date 2017/02/25
 */
class System private constructor() {

    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {
        @JvmField val sSoLoader = ToyBricks.getImplementation(ISoLoader::class.java)

        /**
         * Load libraries synchronously. Like System.loadLibrary();
         */
        @JvmStatic fun loadLibrary(context: Context, library: String){
            sSoLoader.loadLibrary(context, library)
        }

        /**
         * Load libraries synchronously. Like System.loadLibrary();
         */
        @JvmStatic fun loadLibrary(context: Context, library: String, version: String?){
            sSoLoader.loadLibrary(context,library,version)
        }

        /**
         * To load libraries asynchronously.
         * Simply pass a LoadListener instance to the loadLibrary call.
         */
        @JvmStatic fun loadLibrary(context: Context, library: String, listener: ILoadListener?){
            sSoLoader.loadLibrary(context, library, listener)
        }

        /**
         * To load libraries asynchronously.
         * Simply pass a LoadListener instance to the loadLibrary call.
         */
        @JvmStatic fun loadLibrary(context: Context, library: String, version: String?, listener: ILoadListener?){
            sSoLoader.loadLibrary(context, library, version, listener)
        }
    }

}