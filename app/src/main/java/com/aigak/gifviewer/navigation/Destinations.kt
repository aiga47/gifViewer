package com.aigak.gifviewer.navigation


interface Destinations {
    val route: String
}


object Main: Destinations {
    override val route = "Main"
}

object Details: Destinations {
    override val route = "Details"

    var urlKey ="url"
    var titleKey = "title"

    fun createRoute(url:String = urlKey,title: String =titleKey): String {
        return "$route?$urlKey={$url}&$titleKey={$title}";
    }
}

