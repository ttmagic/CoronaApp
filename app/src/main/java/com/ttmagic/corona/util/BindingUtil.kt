package com.ttmagic.corona.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ttmagic.corona.R
import java.text.SimpleDateFormat
import java.util.*


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

@BindingAdapter("app:lastUpdate")
fun TextView.setLastUpdate(millis: Long) {
    val date = Date(millis)
    val format = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault())
    text = "Cập nhật lần cuối: ${format.format(date)}"
}


/**
 * Set text with number thousand separator.
 * Ex: 12345 => 12.345
 */
@BindingAdapter("app:textNumber")
fun TextView.setTextNumber(str: String?) {
    if (str.isNullOrBlank()) return
    this.text = str
    val number = str.toIntOrNull()
    number?.let {
        this.text = String.format("%,d", it)
    }
}