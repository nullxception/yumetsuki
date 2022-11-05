package io.chaldeaprjkt.yumetsuki.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import dagger.hilt.android.AndroidEntryPoint
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.constant.IntentAction
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.domain.repository.GameAccountRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.WidgetSettingsRepo
import io.chaldeaprjkt.yumetsuki.ui.MainActivity
import io.chaldeaprjkt.yumetsuki.util.extension.FullTimeType
import io.chaldeaprjkt.yumetsuki.util.extension.describeTimeSecs
import io.chaldeaprjkt.yumetsuki.util.extension.setTextViewSize
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class ResinWidget : BaseWidget(R.layout.widget_resin_fixed) {

    @Inject
    lateinit var widgetSettingsRepo: WidgetSettingsRepo

    @Inject
    lateinit var realtimeNoteRepo: RealtimeNoteRepo

    @Inject
    lateinit var gameAccountRepo: GameAccountRepo

    @Inject
    lateinit var workerEventDispatcher: WorkerEventDispatcher

    @Inject
    lateinit var widgetEventDispatcher: WidgetEventDispatcher

    @Inject
    lateinit var settingsRepo: SettingsRepo

    @Inject
    lateinit var sessionRepo: SessionRepo

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return
        intent?.action ?: return

        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(ComponentName(context, this::class.java))

        when (intent.action) {
            IntentAction.RefreshWidget -> widgetEventDispatcher.refresh(this::class.java)
            IntentAction.UpdateWidget,
            Intent.ACTION_BOOT_COMPLETED -> {
                setWidgetUpdating(context, manager, ids)
                updateWorker()
            }
        }
    }

    private fun updateWorker() {
        coroutineScope.launch {
            workerEventDispatcher.updateRefreshWorker()
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        updateWorker()
    }

    override suspend fun onCreateViews(context: Context, view: RemoteViews, id: Int) = with(view) {
        val intentUpdate =
            Intent(context, ResinWidget::class.java).setAction(IntentAction.UpdateWidget)
        setOnClickPendingIntent(
            R.id.llSync, PendingIntent.getBroadcast(
                context,
                0,
                intentUpdate,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        val intentMainActivity = Intent(context, MainActivity::class.java)
        setOnClickPendingIntent(
            R.id.iv_resin, PendingIntent.getActivity(
                context,
                0,
                intentMainActivity,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
        setOnClickPendingIntent(
            R.id.llDisable, PendingIntent.getActivity(
                context,
                0,
                intentMainActivity,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        val note = realtimeNoteRepo.data.firstOrNull() ?: return@with
        val settings = widgetSettingsRepo.data.firstOrNull()?.resin ?: return@with

        setTextViewSize(R.id.tv_resin, settings.fontSize)

        setInt(R.id.compatCard, "setAlpha", (255 * settings.backgroundAlpha).roundToInt())

        setViewVisibility(R.id.pbLoading, View.GONE)
        setViewVisibility(R.id.ivRefresh, View.VISIBLE)
        setViewVisibility(R.id.tvSyncTime, View.VISIBLE)

        if (gameAccountRepo.getActive(HoYoGame.Genshin).firstOrNull() == null) {
            setViewVisibility(R.id.llDisable, View.VISIBLE)
        } else {
            sessionRepo.data.firstOrNull()?.let {
                setTextViewText(
                    R.id.tvSyncTime,
                    it.lastGameDataSync.formatSyncTime()
                )
            }
            setViewVisibility(R.id.llDisable, View.GONE)
            setTextViewText(R.id.tv_resin, "${note.currentResin}")
            setTextViewText(R.id.tv_resin_max, "/${note.totalResin}")
            setTextViewText(
                R.id.tv_remain_time,
                context.describeTimeSecs(note.resinRecoveryTime, FullTimeType.Max)
            )
            setViewVisibility(
                R.id.iv_resin, if (settings.showTime) View.VISIBLE else View.GONE
            )
            setViewVisibility(
                R.id.iv_resin, if (settings.showResinImage) View.VISIBLE else View.GONE
            )
        }
    }

    private fun setWidgetUpdating(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) = appWidgetIds.forEach {
        context.remoteViews.apply {
            setViewVisibility(R.id.pbLoading, View.VISIBLE)
            setViewVisibility(R.id.ivRefresh, View.GONE)
            setViewVisibility(R.id.tvSyncTime, View.GONE)
            setViewVisibility(R.id.llDisable, View.GONE)
            appWidgetManager.updateAppWidget(it, this)
        }
    }
}
