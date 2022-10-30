package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.pages.resin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.ResinWidgetSettings
import io.chaldeaprjkt.yumetsuki.ui.components.TextSwitch
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.WidgetOptionSlider

@Composable
fun ResinWidgetOptions(
    settings: ResinWidgetSettings,
    onUpdate: (suspend (ResinWidgetSettings) -> ResinWidgetSettings) -> Unit,
) {
    Column {
        WidgetOptionSlider(
            title = stringResource(id = R.string.background_alpha),
            value = settings.backgroundAlpha,
            indicatorFormat = "%.2f",
            steps = 40,
            onValueChange = { new ->
                onUpdate {
                    it.copy(backgroundAlpha = new)
                }
            },
            onResetClicked = {
                onUpdate { it.copy(backgroundAlpha = ResinWidgetSettings.DefaultBackgroundAlpha) }
            },
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(top = 16.dp),
        )
        WidgetOptionSlider(
            title = stringResource(id = R.string.font_size),
            value = settings.fontSize,
            valueRange = 16f..42f,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            onValueChange = { new ->
                onUpdate { it.copy(fontSize = new) }
            },
            onResetClicked = {
                onUpdate { it.copy(fontSize = ResinWidgetSettings.DefaultFontSize) }
            },
        )
        TextSwitch(
            text = stringResource(id = R.string.resin_image),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            checked = settings.showResinImage,
            onCheckedChange = { new ->
                onUpdate { it.copy(showResinImage = new) }
            },
        )
        TextSwitch(
            text = stringResource(id = R.string.show_remaining_time),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            checked = settings.showTime,
            onCheckedChange = { new ->
                onUpdate { it.copy(showTime = new) }
            },
        )
        Spacer(Modifier.height(24.dp))
    }
}
