package io.chaldeaprjkt.yumetsuki.util.extension

import androidx.work.CoroutineWorker
import androidx.work.WorkManager

val CoroutineWorker.workManager get() = WorkManager.getInstance(applicationContext)
