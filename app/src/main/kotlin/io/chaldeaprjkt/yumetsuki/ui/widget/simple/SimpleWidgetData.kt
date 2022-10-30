package io.chaldeaprjkt.yumetsuki.ui.widget.simple

import android.os.Parcelable
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleWidgetData(
    val session: Session = Session.Empty,
    val settings: SimpleWidgetSettings = SimpleWidgetSettings.Empty,
    val note: RealtimeNote = RealtimeNote.Empty
) : Parcelable {
    companion object {
        val Empty = SimpleWidgetData()
    }
}
