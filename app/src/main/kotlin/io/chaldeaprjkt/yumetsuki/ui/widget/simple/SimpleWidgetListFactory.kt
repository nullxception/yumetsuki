package io.chaldeaprjkt.yumetsuki.ui.widget.simple

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.WidgetSettingsRepo
import io.chaldeaprjkt.yumetsuki.util.extension.FullTimeType
import io.chaldeaprjkt.yumetsuki.util.extension.describeTime
import io.chaldeaprjkt.yumetsuki.util.extension.describeTimeSecs
import io.chaldeaprjkt.yumetsuki.util.extension.setTextViewSize
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SimpleWidgetListFactory @Inject constructor(
    private val context: Context,
    private val sessionRepo: SessionRepo,
    private val realtimeNoteRepo: RealtimeNoteRepo,
    private val widgetSettingsRepo: WidgetSettingsRepo,
) : RemoteViewsService.RemoteViewsFactory {
    private val layout = RemoteViews(context.packageName, R.layout.item_simple_widget)
    private val items = mutableListOf<SimpleWidgetItem>()
    private var settings = SimpleWidgetSettings.Empty

    private fun createItems(note: RealtimeNote, session: Session) {
        items.clear()
        if (settings.showResinData) {
            items += SimpleWidgetItem(
                R.string.resin,
                R.drawable.ic_resin,
                "${note.currentResin}/${note.totalResin}",
            )
            if (settings.showRemainTime) {
                items += SimpleWidgetItem(
                    R.string.widget_full_at,
                    0,
                    context.describeTimeSecs(note.resinRecoveryTime, FullTimeType.Max)
                )
            }
        }
        if (settings.showDailyCommissionData) {
            items += SimpleWidgetItem(
                R.string.daily_commissions,
                R.drawable.ic_daily_commission, if (note.receivedExtraTaskReward) {
                    context.getString(R.string.done)
                } else {
                    "${(note.totalTask - note.completedTask)}/${note.totalTask}"
                }
            )
        }
        if (settings.showWeeklyBossData) {
            items += SimpleWidgetItem(
                R.string.enemies_of_note,
                R.drawable.ic_domain,
                if (note.remainingWeeklyBoss == 0) {
                    context.getString(R.string.done)
                } else {
                    "${note.remainingWeeklyBoss}/${note.totalWeeklyBoss}"
                }
            )
        }
        if (settings.showExpeditionData) {
            items += SimpleWidgetItem(
                R.string.expedition,
                R.drawable.ic_warp_point,
                context.describeTimeSecs(session.expeditionTime, FullTimeType.Done)
            )
        }
        if (settings.showRealmCurrencyData) {
            items += SimpleWidgetItem(
                R.string.realm_currency,
                R.drawable.ic_serenitea_pot,
                if (note.realmCurrencyRecoveryTime < 1) {
                    context.getString(R.string.widget_ui_parameter_max)
                } else {
                    "${(note.currentRealmCurrency)}/${(note.totalRealmCurrency)}"
                }
            )
            if (settings.showRemainTime) {
                items += SimpleWidgetItem(
                    R.string.widget_full_at,
                    0,
                    context.describeTimeSecs(note.realmCurrencyRecoveryTime, FullTimeType.Max)
                )
            }
        }
        if (settings.showParaTransformerData) {
            items += SimpleWidgetItem(
                R.string.parametric_transformer,
                R.drawable.ic_paratransformer,
                when {
                    note.paraTransformerStatus == null -> context.getString(R.string.widget_ui_unknown)
                    !note.paraTransformerStatus.obtained -> context.getString(R.string.widget_ui_transformer_not_obtained)
                    note.paraTransformerStatus.recoveryTime.isReached -> context.getString(R.string.widget_ui_transformer_ready)
                    else -> note.paraTransformerStatus.describeTime(context)
                }
            )
        }
    }

    override fun onCreate() {}

    override fun onDataSetChanged() {
        runBlocking {
            widgetSettingsRepo.data.firstOrNull()?.simple?.let {
                settings = it
            }
            val note = realtimeNoteRepo.data.firstOrNull() ?: return@runBlocking
            val session = sessionRepo.data.firstOrNull() ?: return@runBlocking
            createItems(note, session)
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = items.count()

    override fun getViewAt(position: Int) = layout.apply {
        if (position >= count) {
            return@apply
        }

        val item = items[position]
        if (item.icon > 0) {
            setImageViewResource(R.id.icon, item.icon)
        } else {
            setImageViewBitmap(R.id.icon, null)
        }

        setTextViewText(R.id.status, item.status)
        setTextViewSize(R.id.status, settings.fontSize)
        if (settings.showDescription) {
            setViewVisibility(R.id.desc, View.VISIBLE)
            setTextViewText(R.id.desc, context.getString(item.desc))
            setTextViewSize(R.id.desc, settings.fontSize)
        } else {
            setViewVisibility(R.id.desc, View.INVISIBLE)
        }
    }

    override fun getLoadingView() = layout

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}
