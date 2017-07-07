package com.github.snowdream.image

import android.widget.ImageView
import com.github.snowdream.support.v4.app.Page
import com.github.snowdream.toybricks.annotation.Interface

/**
 * Created by snowdream on 17/7/4.
 */
@Interface
interface IImage {
    fun init(option: ImageOption)

    fun load(page: Page,imageView:ImageView, url: String, placeholder:Int, error:Int)
}