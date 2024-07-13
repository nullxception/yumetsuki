package io.chaldeaprjkt.yumetsuki.worker

import android.content.Context
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.settings.entity.CheckInSettings
import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerEventDispatcherImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val gameAccountRepo: GameAccountRepo,
    private val settingsRepo: SettingsRepo,
) : WorkerEventDispatcher {
    private val workManager
        get() = WorkManager.getInstance(context)

    private fun purge() {
        workManager.cancelAllWork()
        context.noBackupFilesDir
            .listFiles { _, file -> file?.startsWith("androidx.work") ?: false }
            ?.forEach {
                try {
                    it.delete()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
    }

    private suspend fun updateRefreshWorkerFor(game: HoYoGame) {
        val active = gameAccountRepo.getActive(game).firstOrNull()
        val period = settingsRepo.data.firstOrNull()?.syncPeriod ?: Settings.DefaultSyncPeriod
        if (period > 0 && active != null) {
            RefreshWorker.start(workManager, period, game)
        } else {
            RefreshWorker.stop(workManager, game)
        }
    }

    override suspend fun updateRefreshWorker() {
        updateRefreshWorkerFor(HoYoGame.Genshin)
        updateRefreshWorkerFor(HoYoGame.StarRail)
        updateRefreshWorkerFor(HoYoGame.ZZZ)
    }

    override suspend fun checkInNow() {
        val settings = settingsRepo.data.firstOrNull()?.checkIn ?: return
        if (settings.genshin) {
            CheckInWorker.start(workManager, HoYoGame.Genshin, 0L)
        }
        if (settings.houkai) {
            CheckInWorker.start(workManager, HoYoGame.Houkai, 0L)
        }
        if (settings.starRail) {
            CheckInWorker.start(workManager, HoYoGame.StarRail, 0L)
        }
        if (settings.zzz) {
            CheckInWorker.start(workManager, HoYoGame.ZZZ, 0L)
        }
    }

    override suspend fun updateCheckInWorkers() {
        val settings = settingsRepo.data.firstOrNull()?.checkIn ?: CheckInSettings.Empty
        if (settings.genshin) {
            CheckInScheduler.post(workManager, HoYoGame.Genshin)
        } else {
            CheckInWorker.stop(workManager, HoYoGame.Genshin)
        }
        if (settings.houkai) {
            CheckInScheduler.post(workManager, HoYoGame.Houkai)
        } else {
            CheckInWorker.stop(workManager, HoYoGame.Houkai)
        }
        if (settings.starRail) {
            CheckInScheduler.post(workManager, HoYoGame.StarRail)
        } else {
            CheckInWorker.stop(workManager, HoYoGame.StarRail)
        }
        if (settings.zzz) {
            CheckInScheduler.post(workManager, HoYoGame.ZZZ)
        } else {
            CheckInWorker.stop(workManager, HoYoGame.ZZZ)
        }
    }

    override suspend fun reschedule() {
        purge()
        updateRefreshWorker()
        updateCheckInWorkers()
    }
}
