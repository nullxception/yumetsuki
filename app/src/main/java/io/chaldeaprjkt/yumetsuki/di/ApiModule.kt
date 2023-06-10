package io.chaldeaprjkt.yumetsuki.di

import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.BuildConfig
import io.chaldeaprjkt.yumetsuki.constant.Source
import io.chaldeaprjkt.yumetsuki.data.checkin.CheckInApi
import io.chaldeaprjkt.yumetsuki.data.dataswitch.DataSwitchApi
import io.chaldeaprjkt.yumetsuki.data.gameaccount.GameAccountApi
import io.chaldeaprjkt.yumetsuki.data.realtimenote.RealtimeNoteApi
import io.chaldeaprjkt.yumetsuki.data.user.UserFullInfoApi
import io.chaldeaprjkt.yumetsuki.util.okhttp.HeadersInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    private const val Timeout = 60L

    @Singleton
    @Provides
    fun providesOkHttp(cache: Cache) =
        OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(Timeout, TimeUnit.SECONDS)
            .readTimeout(Timeout, TimeUnit.SECONDS)
            .writeTimeout(Timeout, TimeUnit.SECONDS)
            .addInterceptor(HeadersInterceptor())
            .apply {
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor()
                        .apply { level = HttpLoggingInterceptor.Level.BODY }
                        .run { addInterceptor(this) }
                }
            }
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Source.BBSAPI)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun providesUserFullInfoApi(retrofit: Retrofit): UserFullInfoApi =
        retrofit.create(UserFullInfoApi::class.java)

    @Singleton
    @Provides
    fun providesGameAccountApi(retrofit: Retrofit): GameAccountApi =
        retrofit.create(GameAccountApi::class.java)

    @Singleton
    @Provides
    fun providesDailyNoteApi(retrofit: Retrofit): RealtimeNoteApi =
        retrofit.create(RealtimeNoteApi::class.java)

    @Singleton
    @Provides
    fun providesCheckInApi(retrofit: Retrofit): CheckInApi = retrofit.create(CheckInApi::class.java)

    @Singleton
    @Provides
    fun providesDataSwitchApi(retrofit: Retrofit): DataSwitchApi =
        retrofit.create(DataSwitchApi::class.java)
}
