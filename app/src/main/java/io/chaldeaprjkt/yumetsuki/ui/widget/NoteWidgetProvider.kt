package io.chaldeaprjkt.yumetsuki.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.constant.IntentAction
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.ui.common.BaseWidgetProvider
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteWidgetProvider : BaseWidgetProvider(R.layout.widget_realtime_note) {

    @Inject lateinit var settingsRepo: SettingsRepo

    @Inject lateinit var sessionRepo: SessionRepo

    @Inject lateinit var realtimeNoteRepo: RealtimeNoteRepo

    @Inject lateinit var gameAccountRepo: GameAccountRepo

    @Inject lateinit var moshi: Moshi

    @Inject lateinit var workerEventDispatcher: WorkerEventDispatcher

    @Inject lateinit var widgetEventDispatcher: WidgetEventDispatcher

    override suspend fun onCreateViews(context: Context, view: RemoteViews, id: Int) =
        with(view) {
            val intentUpdate =
                Intent(context, NoteWidgetProvider::class.java).setAction(IntentAction.UpdateWidget)
            setOnClickPendingIntent(
                R.id.llSync,
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intentUpdate,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            val option = settingsRepo.data.firstOrNull()?.noteWidgetOption ?: return@with
            setViewVisibility(R.id.pbLoading, View.GONE)
            setViewVisibility(R.id.ivRefresh, View.VISIBLE)

            setInt(R.id.compatCard, "setAlpha", (255 * option.backgroundAlpha).roundToInt())
            val activeGames =
                HoYoGame.values()
                    .filter { it != HoYoGame.Unknown }
                    .map { gameAccountRepo.getActive(it) }
            if (activeGames.firstOrNull() == null) {
                setViewVisibility(R.id.llDisable, View.VISIBLE)
            } else {
                setViewVisibility(R.id.llDisable, View.GONE)
                val lastGameDataSync = sessionRepo.data.firstOrNull()?.lastGameDataSync ?: 0
                setTextViewText(R.id.tvSyncTime, lastGameDataSync.formatSyncTime())

                setRemoteAdapter(
                    R.id.lvData,
                    Intent(context, NoteWidgetFactoryService::class.java).apply {
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
                        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                    },
                )

                AppWidgetManager.getInstance(context)
                    .notifyAppWidgetViewDataChanged(id, R.id.lvData)
            }
        }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return
        intent?.action ?: return

        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, this::class.java))

        when (intent.action) {
            IntentAction.RefreshWidget -> {
                widgetEventDispatcher.refresh(this::class.java)
            }
            IntentAction.UpdateWidget,
            Intent.ACTION_BOOT_COMPLETED -> {
                setWidgetUpdating(context, manager, ids)
                updateWorker()
            }
        }
    }

    private fun updateWorker() {
        coroutineScope.launch { workerEventDispatcher.updateRefreshWorker() }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        updateWorker()
    }

    private fun setWidgetUpdating(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) =
        appWidgetIds.forEach {
            context.remoteViews.apply {
                setViewVisibility(R.id.pbLoading, View.VISIBLE)
                setViewVisibility(R.id.ivRefresh, View.GONE)
                setTextViewText(R.id.tvSyncTime, context.getString(R.string.loading))
                setViewVisibility(R.id.llDisable, View.GONE)
                appWidgetManager.updateAppWidget(it, this)
            }
        }
}
