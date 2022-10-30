package io.chaldeaprjkt.yumetsuki.di

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.settings.entity.ResinOption
import okhttp3.Cache
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YumetsukiModule {

    @Singleton
    @Provides
    fun providesCache(@ApplicationContext context: Context) =
        Cache(context.cacheDir, 10L * 1024 * 1024)

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(ResinOption.Adapter)
            .add(HoYoApiCode.Adapter)
            .add(HoYoGame.Adapter)
            .build()
}
