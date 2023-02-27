package io.chaldeaprjkt.yumetsuki.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chaldeaprjkt.yumetsuki.data.checkin.CheckInRepoImpl
import io.chaldeaprjkt.yumetsuki.data.dataswitch.DataSwitchRepoImpl
import io.chaldeaprjkt.yumetsuki.data.gameaccount.GameAccountRepoImpl
import io.chaldeaprjkt.yumetsuki.data.realtimenote.RealtimeNoteRepoImpl
import io.chaldeaprjkt.yumetsuki.data.session.SessionRepoImpl
import io.chaldeaprjkt.yumetsuki.data.settings.SettingsRepoImpl
import io.chaldeaprjkt.yumetsuki.data.user.UserRepoImpl
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.DataSwitchRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindDailyNote(impl: RealtimeNoteRepoImpl): RealtimeNoteRepo

    @Binds
    abstract fun bindCheckIn(impl: CheckInRepoImpl): CheckInRepo

    @Binds
    abstract fun bindGameRecordCard(impl: GameAccountRepoImpl): GameAccountRepo

    @Binds
    abstract fun bindDataSwitch(impl: DataSwitchRepoImpl): DataSwitchRepo

    @Binds
    abstract fun bindUserInfo(impl: UserRepoImpl): UserRepo

    @Binds
    abstract fun bindSession(impl: SessionRepoImpl): SessionRepo

    @Binds
    abstract fun bindSettingsStore(impl: SettingsRepoImpl): SettingsRepo
}
