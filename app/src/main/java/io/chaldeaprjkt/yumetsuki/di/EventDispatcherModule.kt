package io.chaldeaprjkt.yumetsuki.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcher
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcherImpl
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcherImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class EventDispatcherModule {

    @Binds
    abstract fun bindWidget(impl: WidgetEventDispatcherImpl): WidgetEventDispatcher

    @Binds
    abstract fun bindWorker(impl: WorkerEventDispatcherImpl): WorkerEventDispatcher
}
