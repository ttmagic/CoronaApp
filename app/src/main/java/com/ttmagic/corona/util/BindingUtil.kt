package com.ttmagic.corona.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ttmagic.corona.R


@BindingAdapter("app:imgUrl")
fun ImageView.setImgUrl(url: String?) {
    url?.let {
        val into = Glide.with(context)
            .load(it)
            .error(R.drawable.ic_placeholder)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(25)))
            .into(this)
        into
    }
}
