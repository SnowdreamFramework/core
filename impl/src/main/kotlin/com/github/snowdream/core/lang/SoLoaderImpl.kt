package com.github.snowdream.core.lang

import android.content.Context
import android.util.Log
import com.getkeepsafe.relinker.ReLinker
import com.getkeepsafe.relinker.ReLinkerInstance
import com.github.snowdream.toybricks.annotation.Implementation


/**
 * Created by snowdream on 17/2/25.
 * @author snowdream
 * @date 2017/02/25
 */
@Implementation(ISoLoader::class)
class SoLoaderImpl:ISoLoader {

    @JvmField var TAG = "SF:SoLoader"

    override fun loadLibrary(context: Context, library: String) {
        loadLibrary(context,library,null,null)
    }

    override fun loadLibrary(context: Context, library: String, version: String?) {
        loadLibrary(context,library,version,null)
    }

    override fun loadLibrary(context: Context, library: String, listener: ILoadListener?) {
        loadLibrary(context,library,null,listener)
    }

    override fun loadLibrary(context: Context, library: String, version: String?, listener: ILoadListener?) {
        (getDefaultReLinkerInstance()).loadLibrary(context, library, version, object : ReLinker.LoadListener{

            override fun failure(t: Throwable) {
                listener?.failure(t)
            }

            override fun success() {
                listener?.success()
            }
        })
    }

    private fun getDefaultReLinkerInstance(): ReLinkerInstance {
        return ReLinker.recursively().log { s -> Log.i(TAG, s) }
    }
}