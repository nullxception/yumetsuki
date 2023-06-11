package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget

import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetType

fun NoteWidgetType.stringId(): Int {
    return when (this) {
        NoteWidgetType.GenshinResin -> R.string.resin
        NoteWidgetType.GenshinDailyCommission -> R.string.daily_commissions
        NoteWidgetType.GenshinWeeklyBoss -> R.string.enemies_of_note
        NoteWidgetType.GenshinRealmCurrency -> R.string.realm_currency
        NoteWidgetType.GenshinExpedition -> R.string.expedition
        NoteWidgetType.GenshinParaTransformer -> R.string.parametric_transformer
        NoteWidgetType.StarRailPower -> R.string.trailblaze_power
    }
}
