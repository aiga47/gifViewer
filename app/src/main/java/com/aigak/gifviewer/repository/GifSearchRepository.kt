package com.aigak.gifviewer.repository

import com.aigak.gifviewer.extensions.serializeToMapValue
import com.aigak.gifviewer.networking.api.GiphyApiService
import com.aigak.gifviewer.networking.api.request.GifSearchRequest
import com.aigak.gifviewer.networking.api.response.GifSearchResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GifSearchRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val service: GiphyApiService
) {
    suspend fun searchGifs(
        searchKeyword: String,
        offset: Int
    ): Pair<GifSearchResponse?, Throwable?> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    service.searchGifs(
                        GifSearchRequest(
                            searchKeyword = searchKeyword,
                            offset = offset
                        ).serializeToMapValue()
                    )
                if (response.isSuccessful) {
                    return@withContext Pair(response.body(), null)
                } else {
                    return@withContext Pair(null, Throwable(response.errorBody().toString()))
                }
            } catch (e: Exception) {
                return@withContext Pair(null, e)
            }
        }
    }
}