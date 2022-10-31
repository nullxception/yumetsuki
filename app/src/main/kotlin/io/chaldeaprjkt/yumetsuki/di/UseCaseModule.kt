package io.chaldeaprjkt.yumetsuki.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.domain.usecase.SyncCheckInStatusUseCase
import io.chaldeaprjkt.yumetsuki.domain.usecase.SyncCheckInStatusUseCaseImpl
import io.chaldeaprjkt.yumetsuki.domain.usecase.SyncGameAccUseCase
import io.chaldeaprjkt.yumetsuki.domain.usecase.SyncGameAccUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindSyncGameAcc(impl: SyncGameAccUseCaseImpl): SyncGameAccUseCase

    @Binds
    abstract fun bindSyncCheckInStatus(impl: SyncCheckInStatusUseCaseImpl): SyncCheckInStatusUseCase
}
