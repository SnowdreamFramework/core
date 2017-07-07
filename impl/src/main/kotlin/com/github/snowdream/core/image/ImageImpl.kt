package com.github.snowdream.core.image

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.placeholderOf
import com.github.snowdream.image.IImage
import com.github.snowdream.image.ImageOption
import com.github.snowdream.support.v4.app.Page
import com.github.snowdream.toybricks.annotation.Implementation

/**
 * Created by snowdream on 17/7/4.
 */
@Implementation(value = IImage::class, singleton = true)
class ImageImpl : IImage {
    private lateinit var mImageOption: ImageOption

    override fun init(option: ImageOption) {
        mImageOption = option
    }

    override fun load(page: Page, imageView: ImageView, url: String, placeholder: Int, error: Int) {
        Glide.with(page.getContext())
                .load(url)
                .apply(placeholderOf(placeholder)
                        .error(error))
                .into(imageView)
    }
}