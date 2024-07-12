package com.aigak.gifviewer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aigak.gifviewer.navigation.NavigationController
import com.aigak.gifviewer.ui.screens.MainScreen
import com.aigak.gifviewer.ui.theme.GifViewerTheme
import com.aigak.gifviewer.viewmodel.GifSearchViewModel
import com.aigak.gifviewer.viewmodel.ParentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val gifSearchViewModel: GifSearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeErrors(gifSearchViewModel)
        enableEdgeToEdge()
        setContent {
            GifViewerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationController(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun observeErrors(vararg errorViewModels: ParentViewModel) {
        errorViewModels.forEach { vm ->
            vm.errorLiveData.observe(this) { error ->
                Timber.e(error)
                Toast
                    .makeText(
                        this,
                        ("request error " + error?.message), Toast.LENGTH_LONG
                    ).show()
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GifViewerTheme {
        NavigationController()
    }
}