package com.ttmagic.corona.repo.network

import com.ttmagic.corona.BuildConfig
import com.ttmagic.corona.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitFactory {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    private val trustAllCerts =
        arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                fun checkServerTrusted(
                    arr: Array<X509Certificate>,
                    authType: String,
                    host: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
    // Install the all-trusting trust manager
    private var sslContext: SSLContext =
        SSLContext.getInstance("SSL").apply {
            init(null, trustAllCerts, SecureRandom())
        }

    // Create an ssl socket factory with our all-trusting manager
    private val sslSocketFactory = sslContext.socketFactory

    private val client by lazy {
        OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .sslSocketFactory(
                sslSocketFactory,
                (trustAllCerts[0] as X509TrustManager)
            )
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