package io.chaldeaprjkt.yumetsuki.ui.widget.simple

import android.content.Intent
import android.widget.RemoteViewsService
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.util.extension.FullTimeType
import io.chaldeaprjkt.yumetsuki.util.extension.describeTime
import io.chaldeaprjkt.yumetsuki.util.extension.describeTimeSecs
import io.chaldeaprjkt.yumetsuki.util.extension.getBundledParcel

class SimpleWidgetFactoryService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val data = intent.getBundledParcel(SimpleWidget.Extra.WidgetData, SimpleWidgetData.Empty)
        val children = createChildren(data)
        return SimpleWidgetFactory(applicationContext, children, data.settings).also {
            intent.removeExtra(SimpleWidget.Extra.WidgetData)
        }
    }

    private fun createChildren(data: SimpleWidgetData): List<SimpleWidgetItem> {
        val settings = data.settings
        val note = data.note
        val session = data.session
        val items = mutableListOf<SimpleWidgetItem>()

        if (settings.showResinData) items.add(
            SimpleWidgetItem(
                R.string.resin,
                R.drawable.ic_resin,
                "${note.currentResin}/${note.totalResin}",
            )
        )
        if (settings.showDailyCommissionData) items.add(
            SimpleWidgetItem(
                R.string.daily_commissions,
                R.drawable.ic_daily_commission, if (note.receivedExtraTaskReward) {
                    getString(R.string.done)
                } else {
                    "${(note.totalTask - note.completedTask)}/${note.totalTask}"
                }
            )
        )
        if (settings.showWeeklyBossData) items.add(
            SimpleWidgetItem(
                R.string.enemies_of_note,
                R.drawable.ic_domain,
                if (note.remainingWeeklyBoss == 0) {
                    getString(R.string.done)
                } else {
                    "${note.remainingWeeklyBoss}/${note.totalWeeklyBoss}"
                }
            )
        )
        if (settings.showExpeditionData) items.add(
            SimpleWidgetItem(
                R.string.expedition_settled,
                R.drawable.ic_warp_point,
                describeTimeSecs(session.expeditionTime, FullTimeType.Done)
            )
        )
        if (settings.showRealmCurrencyData) items.add(
            SimpleWidgetItem(
                R.string.realm_currency,
                R.drawable.ic_serenitea_pot,
                if (note.realmCurrencyRecoveryTime < 1) {
                    getString(R.string.widget_ui_parameter_max)
                } else {
                    "${(note.currentRealmCurrency)}/${(note.totalRealmCurrency)}"
                }
            )
        )
        if (settings.showParaTransformerData) items.add(
            SimpleWidgetItem(
                R.string.parametric_transformer,
                R.drawable.ic_paratransformer,
                when {
                    note.paraTransformerStatus == null -> getString(R.string.widget_ui_unknown)
                    !note.paraTransformerStatus.obtained -> getString(R.string.widget_ui_transformer_not_obtained)
                    note.paraTransformerStatus.recoveryTime.isReached -> getString(R.string.widget_ui_transformer_ready)
                    else -> note.paraTransformerStatus.describeTime(this)
                }
            )
        )

        return items
    }
}
