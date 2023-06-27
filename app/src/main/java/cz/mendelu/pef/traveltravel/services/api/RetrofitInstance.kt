package cz.mendelu.pef.traveltravel.services.api

import cz.mendelu.pef.traveltravel.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val YELP_API_BASE_URL: String = "https://api.yelp.com/v3/"
const val YELP_TOKEN = BuildConfig.API_KEY

const val BAD_REQUEST = 400
const val TOO_MANY_REQUESTS = 429
const val INTERNAL_SERVER_ERROR = 500
const val SERVICE_UNAVAILABLE = 503

val client = OkHttpClient.Builder()
    .addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $YELP_TOKEN")
            .build()
        chain.proceed(newRequest)
    })
    .build()

object RetrofitInstance {
    val api: YelpAPIRepository by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(YELP_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YelpAPIRepository::class.java)
    }
}