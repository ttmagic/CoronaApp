package com.ttmagic.corona.util

import com.ttmagic.corona.model.ReqBody

object Const {
    const val BASE_URL = "https://corona-api.kompa.ai/"

    val provinceQuery = ReqBody(
        "provinces",
        "query provinces {\n provinces {\n Province_Name\n Confirmed\n Deaths\n Recovered\n}\n}\n"
    )

    val newsQuery = ReqBody(
        "topTrueNews",
        "query topTrueNews {\n topTrueNews {title\n content\n url\n siteName\n publishedDate\n picture\n }\n}\n"
    )

    object Bus {
        const val REFRESH = "refresh"
    }
}