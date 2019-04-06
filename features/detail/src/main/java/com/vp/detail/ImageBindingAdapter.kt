package com.vp.detail

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

abstract class ImageBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun setImageUrl(view: ImageView, url: String?) {
            Glide.with(view).load(url).into(view)
        }
    }
}
