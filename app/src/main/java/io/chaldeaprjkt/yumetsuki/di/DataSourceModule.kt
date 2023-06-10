package io.chaldeaprjkt.yumetsuki.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNetworkSource
import io.chaldeaprjkt.yumetsuki.data.checkin.source.CheckInNetworkSourceImpl
import io.chaldeaprjkt.yumetsuki.data.dataswitch.source.DataSwitchNetworkSource
import io.chaldeaprjkt.yumetsuki.data.dataswitch.source.DataSwitchNetworkSourceImpl
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountNetworkSource
import io.chaldeaprjkt.yumetsuki.data.gameaccount.source.GameAccountNetworkSourceImpl
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteDataStore
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteDataStoreImpl
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteNetworkSource
import io.chaldeaprjkt.yumetsuki.data.realtimenote.source.RealtimeNoteNetworkSourceImpl
import io.chaldeaprjkt.yumetsuki.data.session.source.SessionDataStore
import io.chaldeaprjkt.yumetsuki.data.session.source.SessionDataStoreImpl
import io.chaldeaprjkt.yumetsuki.data.settings.source.SettingDataStore
import io.chaldeaprjkt.yumetsuki.data.settings.source.SettingDataStoreImpl
import io.chaldeaprjkt.yumetsuki.data.user.source.UserNetworkSource
import io.chaldeaprjkt.yumetsuki.data.user.source.UserNetworkSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds abstract fun bindCheckInNetwork(impl: CheckInNetworkSourceImpl): CheckInNetworkSource

    @Binds
    abstract fun bindDataSwitchNetwork(impl: DataSwitchNetworkSourceImpl): DataSwitchNetworkSource

    @Binds
    abstract fun bindGameAccountNetwork(
        impl: GameAccountNetworkSourceImpl
    ): GameAccountNetworkSource

    @Binds
    abstract fun bindRealtimeNoteLocal(impl: RealtimeNoteDataStoreImpl): RealtimeNoteDataStore

    @Binds
    abstract fun bindRealtimeNoteNetwork(
        impl: RealtimeNoteNetworkSourceImpl
    ): RealtimeNoteNetworkSource

    @Binds abstract fun bindUserNetwork(impl: UserNetworkSourceImpl): UserNetworkSource

    @Binds abstract fun bindSettingLocal(impl: SettingDataStoreImpl): SettingDataStore

    @Binds abstract fun bindSessionLocal(impl: SessionDataStoreImpl): SessionDataStore
}
