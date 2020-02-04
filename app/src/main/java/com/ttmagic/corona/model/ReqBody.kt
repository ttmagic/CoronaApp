package com.ttmagic.corona.model

data class ReqBody(val operationName: String, val query: String)


data class Data(val topTrueNews: List<News>?, val provinces: List<Province>?)