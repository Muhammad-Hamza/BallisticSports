package com.example.sportsballistics.data.api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.data.api.network_interceptor.NetworkInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import com.example.sportsballistics.data.PersistentCookieStore
import java.net.CookiePolicy
import okhttp3.HttpUrl

import okhttp3.Cookie

import okhttp3.CookieJar
import java.util.*
import kotlin.collections.ArrayList

object ApiClient {

    const val BASE_URL = "https://appservice.basketballballistics.com/"

    private lateinit var retrofit: Retrofit
    lateinit var okHttpClient: OkHttpClient
    fun client(contxt: Context): Retrofit {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        val cookieHandler: CookieHandler = CookieManager()
        val cookieManager = CookieManager(PersistentCookieStore(contxt), CookiePolicy.ACCEPT_ALL)

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.cookieJar(SessionCookieJar())
//        clientBuilder.addInterceptor(ChuckerInterceptor(AppSystem.getInstance()))
        clientBuilder.addInterceptor(loggingInterceptor)
        clientBuilder.addInterceptor(NetworkInterceptor(contxt))
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun client(): Retrofit {
        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    private class SessionCookieJar : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            if (url.encodedPath().endsWith("MobileLogin")) {
                AppSystem.getInstance().cookies = ArrayList(cookies)
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return if (!url.encodedPath()
                    .endsWith("MobileLogin") && AppSystem.getInstance().cookies != null
            ) {
                AppSystem.getInstance().cookies
            } else Collections.emptyList()
        }
    }
}