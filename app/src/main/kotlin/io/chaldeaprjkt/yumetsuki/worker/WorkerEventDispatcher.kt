package io.chaldeaprjkt.yumetsuki.worker

interface WorkerEventDispatcher {

    suspend fun updateRefreshWorker()
    suspend fun checkInNow()
    suspend fun updateCheckInWorkers()
}
