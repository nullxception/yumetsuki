package io.chaldeaprjkt.yumetsuki.util.notifier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.util.notifier.NotifierType.Companion.channel

object Notifier {
    private val Context.notificationManager
        get() = ContextCompat.getSystemService(this, NotificationManager::class.java)

    fun createChannels(context: Context) {
        val channels =
            listOf(
                Triple(
                    NotifierChannel.Resin.id,
                    R.string.push_resin_title,
                    R.string.push_resin_description
                ),
                Triple(
                    NotifierChannel.CheckIn.id,
                    R.string.push_checkin_title,
                    R.string.push_checkin_description
                ),
                Triple(
                    NotifierChannel.Expedition.id,
                    R.string.push_expedition_title,
                    R.string.push_expedition_description
                ),
                Triple(
                    NotifierChannel.RealmCurrency.id,
                    R.string.push_realm_currency_title,
                    R.string.push_realm_currency_description
                ),
            )

        val registeredIds = context.notificationManager?.notificationChannels?.map { it.id }
        val unknownIds = registeredIds?.filter { it !in channels.map { t -> t.first } }
        unknownIds?.filterNotNull()?.forEach {
            context.notificationManager?.deleteNotificationChannel(it)
        }

        context.notificationManager?.createNotificationChannels(
            channels.map {
                NotificationChannel(
                        it.first,
                        context.getString(it.second),
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    .apply { description = context.getString(it.third) }
            }
        )
    }

    fun send(type: NotifierType, context: Context, title: String, msg: String) {
        val builder =
            NotificationCompat.Builder(context, type.channel.id)
                .setSmallIcon(R.drawable.ic_resin)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        context.notificationManager?.notify(type.id, builder.build())
    }
}
