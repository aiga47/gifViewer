package com.aigak.gifviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aigak.gifviewer.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import timber.log.Timber

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    url: String?,
    title: String?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        GlideImage(
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            model = url,
            failure = placeholder(R.drawable.chili),
            contentDescription = title,
        )
        Text(
            title ?: "",
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
                .padding(all = 6.dp)
                .background(color = Color(0x66FFFFFF))
                .align(Alignment.BottomCenter)
        )
    }
}