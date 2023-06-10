package io.chaldeaprjkt.yumetsuki.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.data.store.YumeDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesYumeDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, YumeDatabase::class.java, YumeDatabase.Name).build()

    @Singleton @Provides fun providesGameAccountDao(db: YumeDatabase) = db.gameAccountDao()

    @Singleton @Provides fun providesUserDao(db: YumeDatabase) = db.userDao()

    @Singleton @Provides fun providesCheckInNoteDao(db: YumeDatabase) = db.checkInNoteDao()
}
