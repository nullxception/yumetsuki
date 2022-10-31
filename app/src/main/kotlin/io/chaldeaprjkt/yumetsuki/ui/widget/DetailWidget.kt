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
import io.chaldeaprjkt.yumetsuki.util.extension.setViewAlpha
import io.chaldeaprjkt.yumetsuki.worker.WorkerEventDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailWidget : BaseWidget(R.layout.widget_detail_fixed) {

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
            Intent(context, DetailWidget::class.java).setAction(IntentAction.UpdateWidget)
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
                context, 0, intentMainActivity, PendingIntent.FLAG_IMMUTABLE
            )
        )
        setOnClickPendingIntent(
            R.id.iv_daily_commission, PendingIntent.getActivity(
                context, 0, intentMainActivity, PendingIntent.FLAG_IMMUTABLE
            )
        )
        setOnClickPendingIntent(
            R.id.iv_domain, PendingIntent.getActivity(
                context, 0, intentMainActivity, PendingIntent.FLAG_IMMUTABLE
            )
        )
        setOnClickPendingIntent(
            R.id.iv_serenitea_pot, PendingIntent.getActivity(
                context, 0, intentMainActivity, PendingIntent.FLAG_IMMUTABLE
            )
        )
        setOnClickPendingIntent(
            R.id.iv_warp, PendingIntent.getActivity(
                context, 0, intentMainActivity, PendingIntent.FLAG_IMMUTABLE
            )
        )
        setOnClickPendingIntent(
            R.id.llDisable, PendingIntent.getActivity(
                context, 0, intentMainActivity, PendingIntent.FLAG_IMMUTABLE
            )
        )

        val note = realtimeNoteRepo.data.firstOrNull() ?: return@with
        val settings = widgetSettingsRepo.data.firstOrNull()?.detail ?: return@with

        listOf(
            R.id.tv_resin,
            R.id.tv_resin_title,
            R.id.tv_resin_time,
            R.id.tv_resin_time_title,
            R.id.tv_daily_commission,
            R.id.tv_daily_commission_title,
            R.id.tv_weekly_boss,
            R.id.tv_weekly_boss_title,
            R.id.tv_expedition_title,
            R.id.tv_expedition_time,
            R.id.tv_transformer_title,
            R.id.tv_transformer,
            R.id.tv_realm_currency,
            R.id.tv_realm_currency_title,
            R.id.tv_realm_currency_time,
            R.id.tv_realm_currency_time_title,
        ).forEach { setTextViewSize(it, settings.fontSize) }

        setViewAlpha(R.id.llBg, settings.backgroundAlpha)

        setViewVisibility(R.id.pbLoading, View.GONE)
        setViewVisibility(R.id.ivRefresh, View.VISIBLE)
        setViewVisibility(R.id.tvSyncTime, View.VISIBLE)
        if (gameAccountRepo.getActive(HoYoGame.Genshin).firstOrNull() == null) {
            setViewVisibility(R.id.llDisable, View.VISIBLE)
        } else {
            setViewVisibility(R.id.llDisable, View.GONE)
            setTextViewText(R.id.tv_resin_title, context.getString(R.string.resin))
            setTextViewText(
                R.id.tv_resin, note.currentResin.toString() + "/" + note.totalResin.toString()
            )

            setTextViewText(
                R.id.tv_daily_commission_title, context.getString(R.string.daily_commissions)
            )
            setTextViewText(
                R.id.tv_daily_commission, if (note.receivedExtraTaskReward) {
                    context.getString(R.string.done)
                } else {
                    (note.totalTask - note.completedTask).toString() + "/" + note.totalTask.toString()
                }
            )

            setTextViewText(
                R.id.tv_weekly_boss_title, context.getString(R.string.enemies_of_note)
            )
            setTextViewText(
                R.id.tv_weekly_boss, if (note.remainingWeeklyBoss == 0) {
                    context.getString(R.string.done)
                } else {
                    note.remainingWeeklyBoss.toString() + "/" + note.totalWeeklyBoss.toString()
                }
            )

            setTextViewText(
                R.id.tv_realm_currency_title, context.getString(R.string.realm_currency)
            )
            setTextViewText(
                R.id.tv_realm_currency,
                if (note.realmCurrencyRecoveryTime > 0) {
                    (note.currentRealmCurrency).toString() + "/" + (note.totalRealmCurrency).toString()
                } else {
                    context.getString(R.string.widget_ui_parameter_max)
                }
            )

            setTextViewText(
                R.id.tv_transformer_title, context.getString(R.string.parametric_transformer)
            )
            setTextViewText(
                R.id.tv_transformer, when {
                    note.paraTransformerStatus == null -> context.getString(R.string.widget_ui_unknown)
                    !note.paraTransformerStatus.obtained -> context.getString(R.string.widget_ui_transformer_not_obtained)
                    !note.paraTransformerStatus.recoveryTime.isReached -> context.getString(R.string.widget_ui_transformer_cooldown)
                    else -> context.getString(R.string.widget_ui_transformer_ready)
                }
            )

            sessionRepo.data.firstOrNull()?.let {
                setTextViewText(
                    R.id.tvSyncTime, it.lastGameDataSync.formatSyncTime()
                )
                setTextViewText(
                    R.id.tv_expedition_time,
                    context.describeTimeSecs(it.expeditionTime, FullTimeType.Done)
                )
            }

            setTextViewText(R.id.tv_resin_time_title, context.getString(R.string.replenished))
            setTextViewText(
                R.id.tv_realm_currency_time_title,
                context.getString(R.string.replenished)
            )
            setTextViewText(
                R.id.tv_expedition_title,
                context.getString(R.string.expedition_settled)
            )

            setTextViewText(
                R.id.tv_resin_time,
                context.describeTimeSecs(note.resinRecoveryTime, FullTimeType.Max)
            )
            setTextViewText(
                R.id.tv_realm_currency_time,
                context.describeTimeSecs(note.realmCurrencyRecoveryTime, FullTimeType.Max)
            )

            if (settings.showTime && note.resinRecoveryTime > 1) {
                setViewVisibility(R.id.rl_resin_time, View.VISIBLE)
            } else {
                setViewVisibility(R.id.rl_resin_time, View.GONE)
            }

            if (settings.showTime && note.realmCurrencyRecoveryTime > 1) {
                setViewVisibility(R.id.rl_realm_currency_time, View.VISIBLE)
            } else {
                setViewVisibility(R.id.rl_realm_currency_time, View.GONE)
            }

            setViewVisibility(
                R.id.rl_resin, if (settings.showResinData) View.VISIBLE else View.GONE
            )

            setViewVisibility(
                R.id.rl_daily_commission,
                if (settings.showDailyCommissinData) View.VISIBLE else View.GONE
            )
            setViewVisibility(
                R.id.rl_weekly_boss, if (settings.showWeeklyBossData) View.VISIBLE else View.GONE
            )

            setViewVisibility(
                R.id.rl_realm_currency,
                if (settings.showRealmCurrencyData) View.VISIBLE else View.GONE
            )

            setViewVisibility(
                R.id.rl_expedition, if (settings.showExpeditionData) View.VISIBLE else View.GONE
            )

            setViewVisibility(
                R.id.rl_transformer,
                if (settings.showParaTransformerData) View.VISIBLE else View.GONE
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
