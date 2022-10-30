package io.chaldeaprjkt.yumetsuki.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalEventModule {

    @Binds
    abstract fun bindContainer(impl: LocalEventContainerImpl): LocalEventContainer
}
