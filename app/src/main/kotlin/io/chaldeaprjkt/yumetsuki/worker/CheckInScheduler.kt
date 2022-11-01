package io.chaldeaprjkt.yumetsuki.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.util.extension.workManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@HiltWorker
class CheckInScheduler @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val game
        get(): HoYoGame {
            val currentTag = tags.first { it.startsWith("${TAG}:") }
            val gameId = currentTag.substringAfter(":").toIntOrNull() ?: 0
            return HoYoGame.Adapter.fromJson(gameId)
        }

    private fun nextCheckIn(): Duration {
        val now = Instant.now()
        val serverZone = ZoneId.of("UTC+8")
        val next = now.plus(1, ChronoUnit.DAYS).atZone(serverZone).withHour(0)
            .withMinute((10..30).random()).toInstant()

        return Duration.between(now, next)
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            CheckInWorker.start(workManager, game, nextCheckIn().toMinutes())
            Result.success()
        }
    }

    companion object {
        private const val TAG = "CheckInScheduler"

        private fun workerTag(game: HoYoGame) = "$TAG:${game.id}"

        fun post(workManager: WorkManager?, game: HoYoGame) {
            workManager?.cancelAllWorkByTag(workerTag(game))
            val workRequest =
                OneTimeWorkRequestBuilder<CheckInScheduler>().setInitialDelay(1, TimeUnit.MINUTES)
                    .addTag(workerTag(game)).build()

            workManager?.enqueueUniqueWork(
                workerTag(game), ExistingWorkPolicy.REPLACE, workRequest
            )
        }
    }
}
