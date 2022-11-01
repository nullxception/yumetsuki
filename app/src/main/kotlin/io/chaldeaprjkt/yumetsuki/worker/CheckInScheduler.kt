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
import io.chaldeaprjkt.yumetsuki.util.CommonFunction
import io.chaldeaprjkt.yumetsuki.util.extension.workManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
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

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val time = CommonFunction.getTimeLeftUntilChinaTime(true, 0, Calendar.getInstance())
            CheckInWorker.start(workManager, game, time)
            Result.success()
        }
    }

    companion object {
        private const val TAG = "CheckInScheduler"

        private fun workerTag(game: HoYoGame) = "$TAG:${game.id}"

        fun post(workManager: WorkManager?, game: HoYoGame) {
            workManager?.cancelAllWorkByTag(workerTag(game))
            val workRequest = OneTimeWorkRequestBuilder<CheckInScheduler>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .addTag(workerTag(game))
                .build()

            workManager?.enqueueUniqueWork(
                workerTag(game),
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}
