package io.chaldeaprjkt.yumetsuki.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService
import dagger.hilt.android.AndroidEntryPoint
import io.chaldeaprjkt.yumetsuki.domain.repository.RealtimeNoteRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SessionRepo
import io.chaldeaprjkt.yumetsuki.domain.repository.SettingsRepo
import javax.inject.Inject

@AndroidEntryPoint
class NoteWidgetFactoryService : RemoteViewsService() {

    @Inject lateinit var sessionRepo: SessionRepo

    @Inject lateinit var realtimeNoteRepo: RealtimeNoteRepo

    @Inject lateinit var settingsRepo: SettingsRepo

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return NoteListFactory(applicationContext, sessionRepo, realtimeNoteRepo, settingsRepo)
    }
}
