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


//Input: "yyyy-MM-dd'T'HH:mm:ss"
fun String?.formatDate(): String {
    if (this == null) return ""
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        this
    }
}

@BindingAdapter("app:lastUpdate")
fun TextView.setLastUpdate(millis: Long?) {
    if (millis == null){
        text = ""
        return
    }
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