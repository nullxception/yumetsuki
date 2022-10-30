package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.BaseWidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.DetailWidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.ResinWidgetSettings
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import io.chaldeaprjkt.yumetsuki.domain.repository.WidgetSettingsRepo
import io.chaldeaprjkt.yumetsuki.ui.common.BaseViewModel
import io.chaldeaprjkt.yumetsuki.ui.events.LocalEventContainer
import io.chaldeaprjkt.yumetsuki.ui.widget.DetailWidget
import io.chaldeaprjkt.yumetsuki.ui.widget.ResinWidget
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcher
import io.chaldeaprjkt.yumetsuki.ui.widget.simple.SimpleWidget
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomWidgetViewModel @Inject constructor(
    localEventContainer: LocalEventContainer,
    val widgetSettingsRepo: WidgetSettingsRepo,
    val widgetEventDispatcher: WidgetEventDispatcher,
) : BaseViewModel(localEventContainer) {
    val settings = widgetSettingsRepo.data
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    inline fun <reified T : BaseWidgetSettings> update(
        crossinline transform: suspend (T) -> T
    ) {
        viewModelScope.launch {
            when (T::class) {
                ResinWidgetSettings::class -> {
                    widgetSettingsRepo
                        .updateResin { transform(it as T) as ResinWidgetSettings }
                    widgetEventDispatcher.refresh(ResinWidget::class.java)
                }
                DetailWidgetSettings::class -> {
                    widgetSettingsRepo
                        .updateDetail { transform(it as T) as DetailWidgetSettings }
                    widgetEventDispatcher.refresh(DetailWidget::class.java)
                }
                SimpleWidgetSettings::class -> {
                    widgetSettingsRepo
                        .updateSimple { transform(it as T) as SimpleWidgetSettings }
                    widgetEventDispatcher.refresh(SimpleWidget::class.java)
                }
            }
        }
    }
}
