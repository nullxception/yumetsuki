package io.chaldeaprjkt.yumetsuki.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcher
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PackageUpdateReceiver : BroadcastReceiver() {
    @Inject
    lateinit var workerEventDispatcher: WorkerEventDispatcher

    @Inject
    lateinit var widgetEventDispatcher: WidgetEventDispatcher

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            GlobalScope.launch { refresh() }
        }
    }

    private suspend fun refresh() {
        if (::workerEventDispatcher.isInitialized) {
            workerEventDispatcher.updateRefreshWorker()
            workerEventDispatcher.updateCheckInWorkers()
        }
        if (::widgetEventDispatcher.isInitialized) {
            widgetEventDispatcher.refreshAll()
        }
    }
}
