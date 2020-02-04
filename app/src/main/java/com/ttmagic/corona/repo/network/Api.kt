package com.ttmagic.corona.repo.network

import com.ttmagic.corona.model.NewsResponse
import com.ttmagic.corona.model.ProvinceResponse
import com.ttmagic.corona.model.ReqBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("graphql")
    suspend fun getNews(@Body body: ReqBody): Response<NewsResponse>

    @POST("graphql")
    suspend fun getProvinces(@Body body: ReqBody): Response<ProvinceResponse>
}