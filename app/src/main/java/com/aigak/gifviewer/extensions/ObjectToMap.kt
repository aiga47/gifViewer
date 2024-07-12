package com.aigak.gifviewer.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gsonMapper = Gson()

fun <T> T.serializeToMapValue(): Map<String, String> {
    val json = gsonMapper.toJson(this)
    return gsonMapper.fromJson(json, object : TypeToken<Map<String, String>>() {}.type)
}
