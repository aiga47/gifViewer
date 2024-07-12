package com.aigak.gifviewer.networking.api

import com.aigak.gifviewer.networking.api.request.GifSearchRequest
import com.aigak.gifviewer.networking.api.response.GifSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface GiphyApiService {
    @GET("gifs/search")
    suspend fun searchGifs(@QueryMap request: Map<String, String>): Response<GifSearchResponse>
}