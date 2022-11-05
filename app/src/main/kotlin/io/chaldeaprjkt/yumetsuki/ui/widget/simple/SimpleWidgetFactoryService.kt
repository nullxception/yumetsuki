package io.chaldeaprjkt.yumetsuki.ui.widget.simple

import android.content.Intent
import android.widget.RemoteViewsService
import dagger.hilt.android.AndroidEntryPoint
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.WidgetSettingsRepo
import javax.inject.Inject

@AndroidEntryPoint
class SimpleWidgetFactoryService : RemoteViewsService() {

    @Inject
    lateinit var sessionRepo: SessionRepo

    @Inject
    lateinit var realtimeNoteRepo: RealtimeNoteRepo

    @Inject
    lateinit var widgetSettingsRepo: WidgetSettingsRepo

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return SimpleWidgetListFactory(
            applicationContext,
            sessionRepo,
            realtimeNoteRepo,
            widgetSettingsRepo
        )
    }
}
