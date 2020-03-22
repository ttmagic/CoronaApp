package com.ttmagic.corona.repo.network

import com.ttmagic.corona.model.MapResponse
import com.ttmagic.corona.model.StatsVnResponse
import com.ttmagic.corona.model.StatsWorldResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("CovidPatient")
    suspend fun getPatientMap(): Response<MapResponse>

    @GET("CovidVietNam")
    suspend fun getStatsVn(): Response<StatsVnResponse>

    @GET("CovidWorld")
    suspend fun getStatsWorld(): Response<StatsWorldResponse>
}