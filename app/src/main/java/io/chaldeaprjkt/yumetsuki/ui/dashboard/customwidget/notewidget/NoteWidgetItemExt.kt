package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget

import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetItem

fun NoteWidgetItem.stringId(): Int {
    return when (this) {
        NoteWidgetItem.GenshinResin -> R.string.resin
        NoteWidgetItem.GenshinDailyCommission -> R.string.daily_commissions
        NoteWidgetItem.GenshinWeeklyBoss -> R.string.enemies_of_note
        NoteWidgetItem.GenshinRealmCurrency -> R.string.realm_currency
        NoteWidgetItem.GenshinExpedition -> R.string.expedition
        NoteWidgetItem.GenshinParaTransformer -> R.string.parametric_transformer
        NoteWidgetItem.StarRailPower -> R.string.trailblaze_power
    }
}
