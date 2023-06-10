package io.chaldeaprjkt.yumetsuki.util.notifier

enum class NotifierChannel {
    Resin,
    CheckIn,
    Expedition,
    RealmCurrency;

    val id
        get() = "pushNotificationOf${name}"
}
