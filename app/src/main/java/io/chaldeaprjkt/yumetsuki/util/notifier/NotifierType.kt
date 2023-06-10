package io.chaldeaprjkt.yumetsuki.util.notifier

import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.worker.CheckInWorkerStatus

sealed class NotifierType(val id: Int) {
    data class Resin(val value: Int) : NotifierType(1)
    object ExpeditionCompleted : NotifierType(2)
    object RealmCurrencyFull : NotifierType(3)

    data class CheckIn(
        val game: HoYoGame,
        val status: CheckInWorkerStatus,
    ) : NotifierType(10 + game.ordinal)

    companion object {
        val NotifierType.channel
            get() =
                when (this) {
                    is Resin -> NotifierChannel.Resin
                    is CheckIn -> NotifierChannel.CheckIn
                    is ExpeditionCompleted -> NotifierChannel.Expedition
                    is RealmCurrencyFull -> NotifierChannel.RealmCurrency
                }
    }
}
