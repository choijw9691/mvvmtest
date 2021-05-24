package com.didimstory.mvvmtest.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY="e51553e80a6693c33e9685e87f74b45a"
const val BASE_URL="https://api.themoviedb.org/3/"
const val POSTER_BASE_URL="https://image.tmdb.org/t/p/w342"

const val FIRST_PAGE=1
const val POST_PER_PAGE=20

object TheMovieDBClient {

fun getClient():TheMovieDBInterface{

    //OkHttp의 Interceptor를 이용해서 Retrofit을 이용한 서버 요청과 응답을 중간에서 가로채 원하는 값을 끼워서 요청하거나 응답을 원하는 형태로 조금 변형할 수 있습니다.
val requestInterceptor = Interceptor{
    chain ->

    val url=chain.request().url().newBuilder().addQueryParameter("api_key", API_KEY).build()

    val request= chain.request().newBuilder().url(url).build()

    return@Interceptor chain.proceed(request)
}
val okHttpClient=OkHttpClient.Builder().addInterceptor(requestInterceptor).connectTimeout(60,TimeUnit.SECONDS).build()

    return Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build().create(TheMovieDBInterface::class.java)
}
}