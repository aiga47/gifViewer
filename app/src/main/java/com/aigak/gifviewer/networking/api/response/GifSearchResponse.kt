package com.aigak.gifviewer.networking.api.response

import com.google.gson.annotations.SerializedName

data class GifSearchResponse(
    val data: List<Data>,
    val pagination: Pagination
)

data class Data(
    val id: String,
    val images: Images,
    val title: String,

)

data class Pagination(
    val count: Int,
    val offset: Int,
    @SerializedName("total_count")
    val totalCount: Int
)


data class Images(
    @SerializedName("fixed_width")
    val small: Size,
    val original: Size
)


data class Size(
    @SerializedName("webp")
    val url: String,
)