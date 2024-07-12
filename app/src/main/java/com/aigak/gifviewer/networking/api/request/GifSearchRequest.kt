package com.aigak.gifviewer.networking.api.request

import com.aigak.gifviewer.BuildConfig
import com.google.gson.annotations.SerializedName

data class GifSearchRequest(
    @SerializedName("api_key")
    val apiKey: String = BuildConfig.GIPHY_API_KEY,
    val limit:Int = 50,
    var offset: Int = 0,
    @SerializedName("q")
    var searchKeyword:String?
)
