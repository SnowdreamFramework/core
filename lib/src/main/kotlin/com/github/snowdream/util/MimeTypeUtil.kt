package com.github.snowdream.util

import android.text.TextUtils
import android.webkit.MimeTypeMap


/**
 *
 * see: https://stackoverflow.com/a/8591230
 *
 * Created by snowdream on 17/6/27.
 */
class MimeTypeUtil private constructor() {

    init {
        throw AssertionError("No constructor allowed here!")
    }

    companion object {

        /**
         * Detect file MimeType
         *
         * @param url
         */
        @JvmStatic fun getMimeType(url: String): String {
            val default: String = "application/octet-stream"

            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }

            if (TextUtils.isEmpty(type)) {
                return default
            } else {
                return type!!
            }
        }
    }
}
