package io.chaldeaprjkt.yumetsuki

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class YumetsukiApp : Application(), Configuration.Provider, ImageLoaderFactory {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration get() =
        Configuration.Builder().setWorkerFactory(workerFactory).build()

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .respectCacheHeaders(false)
            .apply { if (BuildConfig.DEBUG) logger(DebugLogger()) }
            .build()
    }

}
