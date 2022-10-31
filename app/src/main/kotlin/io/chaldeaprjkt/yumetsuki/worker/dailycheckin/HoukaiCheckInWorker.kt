package io.chaldeaprjkt.yumetsuki.worker.dailycheckin

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
import io.chaldeaprjkt.yumetsuki.domain.repository.CheckInRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.UserRepo
import io.chaldeaprjkt.yumetsuki.ui.MainActivity
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import io.chaldeaprjkt.yumetsuki.util.extension.workManager
import io.chaldeaprjkt.yumetsuki.util.notifier.Notifier
import io.chaldeaprjkt.yumetsuki.util.notifier.NotifierChannel
import io.chaldeaprjkt.yumetsuki.util.notifier.NotifierType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class HoukaiCheckInWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val settingsRepo: SettingsRepo,
    private val gameAccountRepo: GameAccountRepo,
    private val checkInRepo: CheckInRepo,
    private val userRepo: UserRepo,
) : CoroutineWorker(context, workerParams) {

    private fun NotifierType.send() {
        if (this !is NotifierType.CheckIn) return

        val msg = when (status) {
            CheckInStatus.Success -> applicationContext.getString(R.string.push_honkai_checkin_success)

            CheckInStatus.Done -> applicationContext.getString(R.string.push_honkai_checkin_already)

            CheckInStatus.Failed -> applicationContext.getString(R.string.push_honkai_checkin_failed)

            CheckInStatus.AccountNotFound -> applicationContext.getString(R.string.push_honkai_checkin_account_not_found)
        }

        Notifier.send(
            this,
            applicationContext,
            applicationContext.getString(R.string.push_honkai_checkin_title),
            msg
        )
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val mNotificationId = 123

        val intentMainLanding = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intentMainLanding, PendingIntent.FLAG_IMMUTABLE
        )
        val iconNotification: Bitmap? =
            BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher)

        val notification =
            NotificationCompat.Builder(applicationContext, NotifierChannel.CheckIn.id)
                .setContentTitle(applicationContext.getString(R.string.checkin_progress_houkai))
                .setTicker(applicationContext.getString(R.string.checkin_progress_houkai))
                .setSmallIcon(R.drawable.ic_resin).setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0).setOnlyAlertOnce(true).setContentIntent(pendingIntent).setOngoing(true)

        if (iconNotification != null) {
            notification.setLargeIcon(Bitmap.createScaledBitmap(iconNotification, 128, 128, false))
        }

        return ForegroundInfo(mNotificationId, notification.build())
    }

    override suspend fun doWork(): Result {
        val settings = settingsRepo.data.firstOrNull() ?: return Result.failure()
        if (!settings.checkIn.houkai) return Result.success()
        val active = gameAccountRepo.activeHoukai.firstOrNull() ?: return Result.failure()
        val user = userRepo.ownerOfGameAccount(active).firstOrNull() ?: return Result.failure()

        return withContext(Dispatchers.IO) {
            checkInRepo.checkIn(user, active).collect {
                val notifierSettings = settings.notifier
                when (it) {
                    is HoYoData -> {
                        if (notifierSettings.onCheckInSuccess) {
                            NotifierType.CheckIn(HoYoGame.Houkai, CheckInStatus.Success).send()
                            startMidnightChina()
                        }
                    }
                    is HoYoError.Api -> {
                        when (it.code) {
                            HoYoApiCode.ClaimedDailyReward,
                            HoYoApiCode.CheckedIntoHoyolab -> {
                                NotifierType.CheckIn(
                                    HoYoGame.Houkai, CheckInStatus.Done
                                ).send()
                                startMidnightChina()
                            }
                            HoYoApiCode.AccountNotFound -> {
                                NotifierType.CheckIn(
                                    HoYoGame.Houkai, CheckInStatus.AccountNotFound
                                ).send()
                            }
                            else -> {
                                NotifierType.CheckIn(HoYoGame.Houkai, CheckInStatus.Failed)
                                    .send()
                                retry()
                            }
                        }
                    }
                    is HoYoError.Code,
                    is HoYoError.Empty, is HoYoError.Network -> {
                        if (notifierSettings.onCheckInFailed) {
                            NotifierType.CheckIn(HoYoGame.Houkai, CheckInStatus.Failed).send()
                        }
                        retry()
                    }
                }
            }
            Result.success()
        }
    }

    private suspend fun retry() {
        val activeHoukai = gameAccountRepo.activeHoukai.firstOrNull() ?: return
        userRepo.ownerOfGameAccount(activeHoukai).firstOrNull() ?: return
        start(workManager, 30L)
    }

    private suspend fun startMidnightChina() {
        val activeHoukai = gameAccountRepo.activeHoukai.firstOrNull() ?: return
        userRepo.ownerOfGameAccount(activeHoukai).firstOrNull() ?: return
        val time = CommonFunction.getTimeLeftUntilChinaTime(true, 0, Calendar.getInstance())
        start(workManager, time)
    }

    companion object {

        private const val TAG = "HoukaiCheckInWorker"

        fun start(workManager: WorkManager?, delay: Long) {
            workManager?.cancelAllWorkByTag(TAG)
            val workRequest = OneTimeWorkRequestBuilder<HoukaiCheckInWorker>()
                .setInitialDelay(delay, TimeUnit.MINUTES)
                .addTag(TAG)
                .build()

            workManager?.enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, workRequest)
        }

        fun stop(workManager: WorkManager?) {
            workManager?.cancelAllWorkByTag(TAG)
        }
    }
}
