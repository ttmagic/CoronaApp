package com.ttmagic.corona.repo.network

import com.ttmagic.corona.BuildConfig
import com.ttmagic.corona.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val client by lazy {
        OkHttpClient().newBuilder()
/*            .addInterceptor {
                val token = "auth_token_here"
                if (!token.isNullOrBlank()) {
                    it.proceed(
                        it.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .addHeader("Content-Type", "application/json")
                            .build()
                    )
                } else {
                    it.proceed(it.request())
                }
            }*/
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val api: Api = Retrofit.Builder()
        .client(client)
        .baseUrl(Const.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(Api::class.java)
}

suspend fun network(): Api = withContext(Dispatchers.IO) {
    RetrofitFactory.api
}