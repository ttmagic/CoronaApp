package com.ttmagic.corona.model

data class NewsResponse(
    val `data`: Data
)

data class News(
    val content: String,
    val picture: String,
    val publishedDate: String,
    val siteName: String,
    val title: String,
    val url: String
)