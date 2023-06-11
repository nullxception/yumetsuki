package io.chaldeaprjkt.yumetsuki.ui.widget

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetSetting
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetType
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

class NoteListFactory
@Inject
constructor(
    private val context: Context,
    private val sessionRepo: SessionRepo,
    private val realtimeNoteRepo: RealtimeNoteRepo,
    private val settingsRepo: SettingsRepo,
) : RemoteViewsService.RemoteViewsFactory {
    private val layout = RemoteViews(context.packageName, R.layout.item_widget_note)
    private val items = mutableListOf<NoteListItem>()
    private var option = NoteWidgetSetting.Empty

    override fun onCreate() {}

    override fun onDataSetChanged() {
        runBlocking {
            settingsRepo.data.firstOrNull()?.noteWidgetOption?.let { option = it }
            val genshinNote = realtimeNoteRepo.dataGenshin.firstOrNull() ?: return@runBlocking
            val starRailNote = realtimeNoteRepo.dataStarRail.firstOrNull() ?: return@runBlocking
            val session = sessionRepo.data.firstOrNull() ?: return@runBlocking
            items.clear()
            items.addAll(build(context, option, genshinNote, starRailNote, session))
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = items.count()

    override fun getViewAt(position: Int) =
        layout.apply {
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
            option: NoteWidgetSetting,
            genshinNote: GenshinRealtimeNote,
            starRailNote: StarRailRealtimeNote,
            session: Session
        ): List<NoteListItem> {
            val items =
                option.items.map {
                    if (!it.show) {
                        return@map null
                    }

                    when (it.type) {
                        NoteWidgetType.StarRailPower ->
                            NoteListItem(
                                R.string.trailblaze_power,
                                R.drawable.ic_trailblaze_power,
                                "${starRailNote.currentStamina}/${starRailNote.totalStamina}"
                            )
                        NoteWidgetType.GenshinResin ->
                            NoteListItem(
                                R.string.resin,
                                R.drawable.ic_resin,
                                "${genshinNote.currentResin}/${genshinNote.totalResin}",
                                if (option.showRemainTime) R.string.widget_full_at else null,
                                context.describeTimeSecs(
                                    genshinNote.resinRecoveryTime,
                                    FullTimeType.Max
                                )
                            )
                        NoteWidgetType.GenshinDailyCommission ->
                            NoteListItem(
                                R.string.daily_commissions,
                                R.drawable.ic_daily_commission,
                                if (genshinNote.receivedExtraTaskReward) {
                                    context.getString(R.string.done)
                                } else {
                                    "${(genshinNote.totalTask - genshinNote.completedTask)}/${genshinNote.totalTask}"
                                }
                            )
                        NoteWidgetType.GenshinWeeklyBoss ->
                            NoteListItem(
                                R.string.enemies_of_note,
                                R.drawable.ic_domain,
                                if (genshinNote.remainingWeeklyBoss == 0) {
                                    context.getString(R.string.done)
                                } else {
                                    "${genshinNote.remainingWeeklyBoss}/${genshinNote.totalWeeklyBoss}"
                                }
                            )
                        NoteWidgetType.GenshinExpedition ->
                            NoteListItem(
                                R.string.expedition,
                                R.drawable.ic_warp_point,
                                context.describeTimeSecs(session.expeditionTime, FullTimeType.Done)
                            )
                        NoteWidgetType.GenshinRealmCurrency ->
                            NoteListItem(
                                R.string.realm_currency,
                                R.drawable.ic_serenitea_pot,
                                if (genshinNote.realmCurrencyRecoveryTime < 1) {
                                    context.getString(R.string.widget_ui_parameter_max)
                                } else {
                                    "${(genshinNote.currentRealmCurrency)}/${(genshinNote.totalRealmCurrency)}"
                                },
                                if (option.showRemainTime) R.string.widget_full_at else null,
                                context.describeTimeSecs(
                                    genshinNote.realmCurrencyRecoveryTime,
                                    FullTimeType.Max
                                )
                            )
                        NoteWidgetType.GenshinParaTransformer ->
                            NoteListItem(
                                R.string.parametric_transformer,
                                R.drawable.ic_paratransformer,
                                when {
                                    genshinNote.paraTransformerStatus == null ->
                                        context.getString(R.string.widget_ui_unknown)
                                    !genshinNote.paraTransformerStatus.obtained ->
                                        context.getString(
                                            R.string.widget_ui_transformer_not_obtained
                                        )
                                    genshinNote.paraTransformerStatus.recoveryTime.isReached ->
                                        context.getString(R.string.widget_ui_transformer_ready)
                                    else -> genshinNote.paraTransformerStatus.describeTime(context)
                                }
                            )
                    }
                }

            return Collections.unmodifiableList(items.filterNotNull())
        }
    }
}
