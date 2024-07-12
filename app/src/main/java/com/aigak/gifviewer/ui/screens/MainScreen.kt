package com.aigak.gifviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aigak.gifviewer.R
import com.aigak.gifviewer.navigation.Details
import com.aigak.gifviewer.networking.api.response.Data
import com.aigak.gifviewer.viewmodel.GifSearchViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: GifSearchViewModel = koinViewModel(),
) {
    val searchLiveData by viewModel.gifSearchLiveData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()
    val searchKeyword by viewModel.searchKeyword.observeAsState()
    var text by remember {
        mutableStateOf(searchKeyword)
    }
    val mappedDataList: List<Data>? = searchLiveData?.flatMap { it?.data ?: emptyList() }

    LazyVerticalStaggeredGrid(
        contentPadding = PaddingValues(6.dp),
        modifier = modifier
            .background(color = Color.Black)
            .fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(minSize = 120.dp),
        verticalItemSpacing = 6.dp,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Box(modifier = modifier) {
                OutlinedTextField(
                    value = text ?: "",
                    onValueChange = {
                        text = it
                        viewModel.searchGifs(it)
                    },
                    maxLines = 1,
                    label = { Text(text = stringResource(R.string.search)) },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()

                        .background(Color.White, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 4.dp)

                )
            }
        }
        items(items = mappedDataList ?: emptyList(),
            key = { imageData ->
                imageData.id
            }
        ) { imageData ->
            Box(modifier = Modifier.fillMaxSize()) {
                GlideImage(
                    modifier = Modifier
                        .clickable(onClick = {
                            navController
                                .navigate(
                                    Details.createRoute(
                                        imageData.images.original.url,
                                        imageData.title
                                    )
                                )
                        })
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    model = imageData.images.small.url,
                    loading = placeholder(R.drawable.chili),
                    failure = placeholder(R.drawable.chili),
                    contentDescription = imageData.title,
                )
                Text(
                    imageData.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier
                        .padding(all = 6.dp)
                        .background(color = Color(0x66FFFFFF))
                        .align(Alignment.BottomCenter)
                )
            }
        }
        if (isLoading == true) {
            items(3) {
                CircularProgressIndicator()
            }
        }
        item {
            LaunchedEffect(true) {
                // load more
                viewModel.searchGifs(delayTime = 0)
            }
        }

    }
}