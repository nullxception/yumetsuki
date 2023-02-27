package io.chaldeaprjkt.yumetsuki.ui.widget

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetOption
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import io.chaldeaprjkt.yumetsuki.util.extension.FullTimeType
import io.chaldeaprjkt.yumetsuki.util.extension.describeTime
import io.chaldeaprjkt.yumetsuki.util.extension.describeTimeSecs
import io.chaldeaprjkt.yumetsuki.util.extension.setTextViewSize
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.util.Collections
import javax.inject.Inject

class NoteListFactory @Inject constructor(
    private val context: Context,
    private val sessionRepo: SessionRepo,
    private val realtimeNoteRepo: RealtimeNoteRepo,
    private val settingsRepo: SettingsRepo,
) : RemoteViewsService.RemoteViewsFactory {
    private val layout = RemoteViews(context.packageName, R.layout.item_widget_note)
    private val items = mutableListOf<NoteListItem>()
    private var option = NoteWidgetOption.Empty

    override fun onCreate() {}

    override fun onDataSetChanged() {
        runBlocking {
            settingsRepo.data.firstOrNull()?.noteWidgetOption?.let { option = it }
            val note = realtimeNoteRepo.data.firstOrNull() ?: return@runBlocking
            val session = sessionRepo.data.firstOrNull() ?: return@runBlocking
            items.clear()
            items.addAll(build(context, option, note, session))
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = items.count()

    override fun getViewAt(position: Int) = layout.apply {
        if (position >= count) {
            return@apply
        }

        val item = items[position]
        setImageViewResource(R.id.icon, item.icon)
        setTextViewText(R.id.status, item.status)
        setTextViewSize(R.id.status, option.fontSize)
        if (option.showDescription) {
            setViewVisibility(R.id.desc, View.VISIBLE)
            setTextViewText(R.id.desc, context.getString(item.desc))
            setTextViewSize(R.id.desc, option.fontSize)
        } else {
            setViewVisibility(R.id.desc, View.INVISIBLE)
        }

        setViewVisibility(R.id.sub, if (item.subdesc != null) View.VISIBLE else View.GONE)
        if (item.subdesc != null) {
            if (option.showDescription) {
                setViewVisibility(R.id.subdesc, View.VISIBLE)
                setTextViewText(R.id.subdesc, context.getString(item.subdesc))
                setTextViewSize(R.id.subdesc, option.fontSize)
            } else {
                setViewVisibility(R.id.subdesc, View.INVISIBLE)
            }
            setTextViewText(R.id.substatus, item.substatus)
            setTextViewSize(R.id.substatus, option.fontSize)
        }
    }

    override fun getLoadingView() = layout

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    companion object {
        fun build(
            context: Context,
            option: NoteWidgetOption,
            note: RealtimeNote,
            session: Session
        ): List<NoteListItem> {
            val items = arrayListOf<NoteListItem>()
            if (option.showResinData) {
                items += NoteListItem(
                    R.string.resin,
                    R.drawable.ic_resin,
                    "${note.currentResin}/${note.totalResin}",
                    if (option.showRemainTime) R.string.widget_full_at else null,
                    context.describeTimeSecs(note.resinRecoveryTime, FullTimeType.Max)
                )
            }
            if (option.showDailyCommissionData) {
                items += NoteListItem(
                    R.string.daily_commissions,
                    R.drawable.ic_daily_commission, if (note.receivedExtraTaskReward) {
                        context.getString(R.string.done)
                    } else {
                        "${(note.totalTask - note.completedTask)}/${note.totalTask}"
                    }
                )
            }
            if (option.showWeeklyBossData) {
                items += NoteListItem(
                    R.string.enemies_of_note,
                    R.drawable.ic_domain,
                    if (note.remainingWeeklyBoss == 0) {
                        context.getString(R.string.done)
                    } else {
                        "${note.remainingWeeklyBoss}/${note.totalWeeklyBoss}"
                    }
                )
            }
            if (option.showExpeditionData) {
                items += NoteListItem(
                    R.string.expedition,
                    R.drawable.ic_warp_point,
                    context.describeTimeSecs(session.expeditionTime, FullTimeType.Done)
                )
            }
            if (option.showRealmCurrencyData) {
                items += NoteListItem(
                    R.string.realm_currency,
                    R.drawable.ic_serenitea_pot,
                    if (note.realmCurrencyRecoveryTime < 1) {
                        context.getString(R.string.widget_ui_parameter_max)
                    } else {
                        "${(note.currentRealmCurrency)}/${(note.totalRealmCurrency)}"
                    },
                    if (option.showRemainTime) R.string.widget_full_at else null,
                    context.describeTimeSecs(note.realmCurrencyRecoveryTime, FullTimeType.Max)
                )
            }
            if (option.showParaTransformerData) {
                items += NoteListItem(
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
            return Collections.unmodifiableList(items)
        }
    }
}
