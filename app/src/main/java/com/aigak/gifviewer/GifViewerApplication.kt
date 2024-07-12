package com.aigak.gifviewer

import android.app.Application
import com.aigak.gifviewer.networking.NetworkFactory
import com.aigak.gifviewer.repository.GifSearchRepository
import com.aigak.gifviewer.viewmodel.GifSearchViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber

class GifViewerApplication : Application() {

    private val appModule = module {
        single(named("IODispatcher")) {
            Dispatchers.IO
        }
        single { NetworkFactory(get()).giphyApi() }
        single { GifSearchRepository(get(named("IODispatcher")), get()) }
        viewModel { GifSearchViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@GifViewerApplication)
            modules(appModule)
        }
    }
}