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
        NoteWidgetType.ZZZBatteryCharge -> R.string.zzz_battery_charge
    }
}

fun NoteWidgetType.drawableId(): Int {
    return when (this) {
        NoteWidgetType.ZZZBatteryCharge -> R.drawable.ic_zzz_battery
        NoteWidgetType.StarRailPower -> R.drawable.ic_trailblaze_power
        NoteWidgetType.GenshinResin -> R.drawable.ic_resin
        NoteWidgetType.GenshinDailyCommission -> R.drawable.ic_daily_commission
        NoteWidgetType.GenshinWeeklyBoss -> R.drawable.ic_domain
        NoteWidgetType.GenshinExpedition -> R.drawable.ic_warp_point
        NoteWidgetType.GenshinRealmCurrency -> R.drawable.ic_serenitea_pot
        NoteWidgetType.GenshinParaTransformer -> R.drawable.ic_paratransformer
    }
}
