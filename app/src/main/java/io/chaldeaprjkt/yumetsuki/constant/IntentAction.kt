package io.chaldeaprjkt.yumetsuki.constant

import io.chaldeaprjkt.yumetsuki.BuildConfig

object IntentAction {
    const val RefreshWidget = "${BuildConfig.APPLICATION_ID}.action.APPWIDGET_RELOAD"
    const val UpdateWidget = "${BuildConfig.APPLICATION_ID}.action.APPWIDGET_UPDATE"
}
