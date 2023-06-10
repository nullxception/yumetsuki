package io.chaldeaprjkt.yumetsuki.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.isEmpty
import io.chaldeaprjkt.yumetsuki.data.gameaccount.server
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NotifierSettings
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcher
import io.chaldeaprjkt.yumetsuki.util.elog
import io.chaldeaprjkt.yumetsuki.util.notifier.Notifier
import io.chaldeaprjkt.yumetsuki.util.notifier.NotifierType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class RefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val realtimeNoteRepo: RealtimeNoteRepo,
    private val gameAccountRepo: GameAccountRepo,
    private val sessionRepo: SessionRepo,
    private val widgetEventDispatcher: WidgetEventDispatcher,
    private val settingsRepo: SettingsRepo,
    private val userRepo: UserRepo,
) : CoroutineWorker(context, workerParams) {

    private suspend fun refreshGenshinNote() {
        val active = gameAccountRepo.getActive(HoYoGame.Genshin).firstOrNull() ?: return
        val cookie = userRepo.ofId(active.hoyolabUid).firstOrNull()?.cookie
        if (!active.isEmpty() && cookie != null) {
            realtimeNoteRepo.syncGenshin(
                active.uid,
                active.server,
                cookie,
            ).collect {
                if (it is HoYoData) {
                    updateData(it.data)
                }
                widgetEventDispatcher.refreshAll()
            }
        }
    }

    private suspend fun refreshStarRailNote() {
        val active = gameAccountRepo.getActive(HoYoGame.StarRail).firstOrNull() ?: return
        val cookie = userRepo.ofId(active.hoyolabUid).firstOrNull()?.cookie
        if (!active.isEmpty() && cookie != null) {
            realtimeNoteRepo.syncStarRail(
                active.uid,
                active.server,
                cookie,
            ).collect {
                widgetEventDispatcher.refreshAll()
            }
        }
    }

    private suspend fun updateData(note: GenshinRealtimeNote) {
        val expeditionTime = sessionRepo.data.firstOrNull()?.expeditionTime ?: 0
        val notifierSettings = settingsRepo.data.firstOrNull()?.notifier ?: NotifierSettings.Empty
        val savedNote = realtimeNoteRepo.dataGenshin.firstOrNull() ?: GenshinRealtimeNote.Empty
        val savedResin = savedNote.currentResin
        val currentResin = note.currentResin

        val num = notifierSettings.onResin.value
        if (num >= 40) {
            for (i in 40..160 step 40) {
                if (i % num == 0 && i in savedResin + 1..currentResin) {
                    notify(NotifierType.Resin(i))
                }
            }
        }

        val nowExpeditionTime = note.expeditionSettledTime
        if (notifierSettings.onExpeditionCompleted &&
            1 in (nowExpeditionTime)..expeditionTime &&
            note.expeditions.isNotEmpty() && nowExpeditionTime == 0
        ) {
            notify(NotifierType.ExpeditionCompleted)
        }

        val nowHomeCoinRecoveryTime = note.realmCurrencyRecoveryTime
        if (notifierSettings.onRealmCurrencyFull &&
            1 in nowHomeCoinRecoveryTime..savedNote.realmCurrencyRecoveryTime &&
            note.totalRealmCurrency != 0 && nowHomeCoinRecoveryTime == 0
        ) {
            notify(NotifierType.RealmCurrencyFull)
        }
        sessionRepo.update { session ->
            session.copy(
                lastGameDataSync = System.currentTimeMillis(),
                expeditionTime = note.expeditionSettledTime
            )
        }
        widgetEventDispatcher.refreshAll()
    }

    private fun notify(type: NotifierType) {
        if (type is NotifierType.CheckIn) return
        val title = when (type) {
            is NotifierType.Resin -> applicationContext.getString(R.string.push_resin_title)
            is NotifierType.ExpeditionCompleted -> applicationContext.getString(R.string.push_expedition_title)
            is NotifierType.RealmCurrencyFull -> applicationContext.getString(R.string.push_realm_currency_title)
            else -> return
        }

        val msg = when (type) {
            is NotifierType.Resin,
            -> applicationContext.resources.run {
                if (type.value == 160) {
                    getString(R.string.push_msg_resin_full)
                } else {
                    getString(R.string.push_msg_resin_num, type.value)
                }
            }

            NotifierType.ExpeditionCompleted -> applicationContext.getString(R.string.push_msg_expedition_done)
            NotifierType.RealmCurrencyFull -> applicationContext.getString(R.string.push_msg_realm_currency_full)
            else -> return
        }

        Notifier.send(type, applicationContext, title, msg)
    }

    override suspend fun doWork() = withContext(Dispatchers.IO) {
        try {
            refreshGenshinNote()
            refreshStarRailNote()
            Result.success()
        } catch (e: Exception) {
            elog(e)
            widgetEventDispatcher.refreshAll()
            Result.failure()
        }
    }

    companion object {

        private const val TAG = "AutoRefreshWork"

        fun start(workManager: WorkManager?, period: Long) {
            if (period == -1L || workManager == null) return

            workManager.cancelAllWorkByTag(TAG)
            val request =
                PeriodicWorkRequestBuilder<RefreshWorker>(period, TimeUnit.MINUTES).addTag(TAG)
                    .build()

            workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.UPDATE, request)
        }

        fun stop(workManager: WorkManager?) {
            workManager?.cancelAllWorkByTag(TAG)
        }
    }
}
