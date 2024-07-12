package com.aigak.gifviewer.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.aigak.gifviewer.networking.api.GiphyApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkFactory(private val context: Context, private val applyInterceptor: Boolean = true) {
    fun giphyApi(): GiphyApiService {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            if(applyInterceptor){
                this.addInterceptor(interceptor)
                    .cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
                    // Add an Interceptor to the OkHttpClient.
                    .addInterceptor { chain ->
                        var request = chain.request()
                        request = if (hasNetwork() == true) {
                            request.newBuilder().header("Cache-Control", "public, max-age=" + 15)
                                .build()
                        } else {
                            request.newBuilder().header(
                                "Cache-Control",
                                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                            ).build()
                        }

                        chain.proceed(request)
                    }
                    // time out setting
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(25, TimeUnit.SECONDS)
            }

        }
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.giphy.com/v1/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: GiphyApiService = retrofit.create(GiphyApiService::class.java)
        return service
    }

    private fun hasNetwork(): Boolean? {
        var isConnected: Boolean? = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}