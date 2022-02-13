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
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.utils.AppConstant
import com.example.sportsballistics.utils.HttpsTrustManager
import java.net.CookiePolicy
import okhttp3.HttpUrl

import okhttp3.Cookie

import okhttp3.CookieJar
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

object ApiClient
{

    private const val BASE_URL_BB = "https://appservice.basketballballistics.com/"
    private const val BASE_URL_TODD = "https://appservice.toddlerballistics.com/"
    private const val BASE_URL_QB = "https://appservice.qbballistics.com/"
    private const val BASE_URL_VB = "https://appservice.volleyballballistics.com/"

    private lateinit var retrofit: Retrofit
    lateinit var okHttpClient: OkHttpClient
    fun client(contxt: Context): Retrofit
    {
        HttpsTrustManager.allowAllSSL();
        val clientBuilder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        val loggingInterceptor = HttpLoggingInterceptor()
        val cookieHandler: CookieHandler = CookieManager()
        val cookieManager = CookieManager(PersistentCookieStore(contxt), CookiePolicy.ACCEPT_ALL)

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.cookieJar(SessionCookieJar())
        clientBuilder.addInterceptor(ChuckerInterceptor(AppSystem.context))
        clientBuilder.addInterceptor(loggingInterceptor)
        clientBuilder.addInterceptor(NetworkInterceptor(contxt))
        retrofit = Retrofit.Builder().baseUrl(getURL()).client(clientBuilder.build()).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit
    }

    fun client(): Retrofit
    {
        HttpsTrustManager.allowAllSSL();

        val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        retrofit = Retrofit.Builder().baseUrl(getURL()).client(clientBuilder.build()).addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit
    }

    private fun getURL(): String
    {
        return when (SharedPrefUtil.getInstance().sportsType)
        {
            AppConstant.TODDLER -> BASE_URL_TODD
            AppConstant.BASEBALL -> BASE_URL_BB
            AppConstant.QB -> BASE_URL_QB
            AppConstant.VOLLEYBALL -> BASE_URL_VB
            else->{
                BASE_URL_TODD
            }
        }
    }

    private class SessionCookieJar : CookieJar
    {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>)
        {
            if (url.encodedPath().endsWith("MobileLogin"))
            {
                SharedPrefUtil.getInstance().saveCookies(cookies)
                AppSystem.getInstance().cookies = ArrayList(cookies)
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie>
        {
            val cookies = SharedPrefUtil.getInstance().cookies
            return if (!url.encodedPath().endsWith("MobileLogin") && cookies != null)
            {
                cookies
            }
            else Collections.emptyList()
        }
    }

    fun getBaseURL()
    {
        val sportsType = SharedPrefUtil.getInstance().sportsType
        when (sportsType)
        {
            AppConstant.BASEBALL ->
            {
            }
            AppConstant.VOLLEYBALL ->
            {
            }
            AppConstant.QB ->
            {
            }
        }
    }
}