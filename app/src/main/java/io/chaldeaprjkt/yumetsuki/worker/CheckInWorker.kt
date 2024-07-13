package io.chaldeaprjkt.yumetsuki.worker

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.common.HoYoApiCode
import io.chaldeaprjkt.yumetsuki.data.common.HoYoData
import io.chaldeaprjkt.yumetsuki.data.common.HoYoError
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.domain.usecase.RequestCheckInUseCase
import io.chaldeaprjkt.yumetsuki.ui.MainActivity
import io.chaldeaprjkt.yumetsuki.util.extension.workManager
import io.chaldeaprjkt.yumetsuki.util.notifier.Notifier
import io.chaldeaprjkt.yumetsuki.util.notifier.NotifierChannel
import io.chaldeaprjkt.yumetsuki.util.notifier.NotifierType
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@HiltWorker
class CheckInWorker
@AssistedInject
constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val settingsRepo: SettingsRepo,
    private val gameAccountRepo: GameAccountRepo,
    private val userRepo: UserRepo,
    private val requestCheckInUseCase: RequestCheckInUseCase,
) : CoroutineWorker(context, workerParams) {

    private val game
        get(): HoYoGame {
            val currentTag = tags.first { it.startsWith("${TAG}:") }
            val gameId = currentTag.substringAfter(":").toIntOrNull() ?: 0
            return HoYoGame.Adapter.fromJson(gameId)
        }

    private fun NotifierType.send() {
        if (this !is NotifierType.CheckIn) return

        val msg = when (status) {
            CheckInWorkerStatus.Success -> {
                when (game) {
                    HoYoGame.ZZZ -> applicationContext.getString(R.string.push_zzz_checkin_success)
                    HoYoGame.StarRail -> applicationContext.getString(R.string.push_starrail_checkin_success)
                    HoYoGame.Genshin -> applicationContext.getString(R.string.push_genshin_checkin_success)
                    else -> applicationContext.getString(R.string.push_houkai_checkin_success)
                }
            }
            CheckInWorkerStatus.Done -> {
                when (game) {
                    HoYoGame.ZZZ -> applicationContext.getString(R.string.push_zzz_checkin_done)
                    HoYoGame.StarRail -> applicationContext.getString(R.string.push_starrail_checkin_done)
                    HoYoGame.Genshin -> applicationContext.getString(R.string.push_genshin_checkin_done)
                    else -> applicationContext.getString(R.string.push_houkai_checkin_done)
                }
            }
            CheckInWorkerStatus.Failed -> {
                when (game) {
                    HoYoGame.ZZZ -> applicationContext.getString(R.string.push_zzz_checkin_failed)
                    HoYoGame.StarRail -> applicationContext.getString(R.string.push_starrail_checkin_failed)
                    HoYoGame.Genshin -> applicationContext.getString(R.string.push_genshin_checkin_failed)
                    else -> applicationContext.getString(R.string.push_houkai_checkin_failed)
                }
            }
            CheckInWorkerStatus.AccountNotFound -> {
                when (game) {
                    HoYoGame.ZZZ -> applicationContext.getString(R.string.push_zzz_checkin_noaccount)
                    HoYoGame.StarRail -> applicationContext.getString(R.string.push_starrail_checkin_noaccount)
                    HoYoGame.Genshin -> applicationContext.getString(R.string.push_genshin_checkin_noaccount)
                    else -> applicationContext.getString(R.string.push_houkai_checkin_noaccount)
                }
            }
        }

        val title = when (game) {
            HoYoGame.ZZZ -> R.string.push_zzz_checkin_title
            HoYoGame.StarRail -> R.string.push_starrail_checkin_title
            HoYoGame.Genshin -> R.string.push_genshin_checkin_title
            else -> R.string.push_houkai_checkin_title
        }
        Notifier.send(this, applicationContext, applicationContext.getString(title), msg)
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val mNotificationId = 123

        val intentMainLanding = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intentMainLanding,
                PendingIntent.FLAG_IMMUTABLE
            )
        val iconNotification: Bitmap? =
            BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher)

        val notification =
            NotificationCompat.Builder(applicationContext, NotifierChannel.CheckIn.id)
                .setContentTitle(applicationContext.getString(R.string.checkin_progress))
                .setTicker(applicationContext.getString(R.string.checkin_progress))
                .setSmallIcon(R.drawable.ic_resin)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)

        if (iconNotification != null) {
            notification.setLargeIcon(Bitmap.createScaledBitmap(iconNotification, 128, 128, false))
        }

        return ForegroundInfo(mNotificationId, notification.build())
    }

    private fun isAssisted(settings: Settings) =
        when (game) {
            HoYoGame.Genshin -> settings.checkIn.genshin
            HoYoGame.StarRail -> settings.checkIn.starRail
            HoYoGame.ZZZ -> settings.checkIn.zzz
            else -> settings.checkIn.houkai
        }

    override suspend fun doWork(): Result {
        val settings = settingsRepo.data.firstOrNull() ?: return Result.failure()
        if (!isAssisted(settings)) return Result.success()
        return withContext(Dispatchers.IO) {
            requestCheckInUseCase(game).collect {
                val notifierSettings = settings.notifier
                when (it) {
                    is HoYoData -> {
                        if (notifierSettings.onCheckInSuccess) {
                            NotifierType.CheckIn(game, CheckInWorkerStatus.Success).send()
                            scheduleNextDay()
                        }
                    }
                    is HoYoError.Api -> {
                        when (it.code) {
                            HoYoApiCode.ClaimedDailyReward,
                            HoYoApiCode.CheckedIntoHoyolab -> {
                                NotifierType.CheckIn(game, CheckInWorkerStatus.Done).send()
                                scheduleNextDay()
                            }
                            HoYoApiCode.AccountNotFound -> {
                                NotifierType.CheckIn(game, CheckInWorkerStatus.AccountNotFound)
                                    .send()
                            }
                            else -> {
                                NotifierType.CheckIn(game, CheckInWorkerStatus.Failed).send()
                                retry()
                            }
                        }
                    }
                    is HoYoError.Code,
                    is HoYoError.Empty,
                    is HoYoError.Network -> {
                        if (notifierSettings.onCheckInFailed) {
                            NotifierType.CheckIn(game, CheckInWorkerStatus.Failed).send()
                        }
                        retry()
                    }
                }
            }
            Result.success()
        }
    }

    private suspend fun retry() {
        val active = gameAccountRepo.getActive(game).firstOrNull() ?: return
        userRepo.ofId(active.hoyolabUid).firstOrNull() ?: return
        start(workManager, game, 30L)
    }

    private suspend fun scheduleNextDay() {
        val active = gameAccountRepo.getActive(game).firstOrNull() ?: return
        userRepo.ofId(active.hoyolabUid).firstOrNull() ?: return
        CheckInScheduler.post(workManager, game)
    }

    companion object {

        private const val TAG = "AssistedCheckIn"

        private fun workerTag(game: HoYoGame) = "$TAG:${game.id}"

        fun start(workManager: WorkManager?, game: HoYoGame, delay: Long) {
            workManager?.cancelAllWorkByTag(workerTag(game))
            val workRequest =
                OneTimeWorkRequestBuilder<CheckInWorker>()
                    .setInitialDelay(delay, TimeUnit.MINUTES)
                    .addTag(workerTag(game))
                    .build()

            workManager?.enqueueUniqueWork(workerTag(game), ExistingWorkPolicy.REPLACE, workRequest)
        }

        fun stop(workManager: WorkManager?, game: HoYoGame) {
            workManager?.cancelAllWorkByTag(workerTag(game))
        }
    }
}
