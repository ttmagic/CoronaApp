package com.ttmagic.corona.util

object Const {
    const val BASE_URL = "https://corona-api.kompa.ai/"

    const val QUERY_NEWS =
        "query topTrueNews {\n topTrueNews {title\n content\n url\n siteName\n publishedDate\n picture\n }\n}\n"

    const val QUERY_PROVINCES = "query provinces {\n provinces {\n Province_Name\n Confirmed\n Deaths\n Recovered\n}\n}\n"
}