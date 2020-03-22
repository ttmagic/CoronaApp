package com.ttmagic.corona.model

import com.base.mvvm.Entity


data class News(
    val content: String,
    val picture: String,
    val publishedDate: String,
    val siteName: String,
    val title: String,
    val url: String
) : Entity() {
    override val uniqueId: Any
        get() = url
}