package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetSetting
import io.chaldeaprjkt.yumetsuki.ui.components.TextSwitch
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.WidgetOptionSlider

@Composable
fun NoteWidgetOptions(
    option: NoteWidgetSetting?,
    onUpdate: (suspend (NoteWidgetSetting) -> NoteWidgetSetting) -> Unit,
) {
    option ?: return

    Column {
        WidgetOptionSlider(
            title = stringResource(id = R.string.background_alpha),
            textStyle = MaterialTheme.typography.bodyMedium,
            value = option.backgroundAlpha,
            indicatorFormat = "%.2f",
            steps = 40,
            onValueChange = { new -> onUpdate { it.copy(backgroundAlpha = new) } },
            onResetClicked = {
                onUpdate { it.copy(backgroundAlpha = NoteWidgetSetting.DefaultBackgroundAlpha) }
            },
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).padding(top = 16.dp),
        )
        WidgetOptionSlider(
            title = stringResource(id = R.string.font_size),
            textStyle = MaterialTheme.typography.bodyMedium,
            value = option.fontSize,
            valueRange = 8f..18f,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            onValueChange = { new -> onUpdate { it.copy(fontSize = new) } },
            onResetClicked = { onUpdate { it.copy(fontSize = NoteWidgetSetting.DefaultFontSize) } },
        )
        TextSwitch(
            text = stringResource(id = R.string.widget_show_desc),
            textStyle = MaterialTheme.typography.bodyMedium,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            checked = option.showDescription,
            onCheckedChange = { new -> onUpdate { it.copy(showDescription = new) } },
        )
        TextSwitch(
            text = stringResource(id = R.string.widget_show_est_full_time),
            textStyle = MaterialTheme.typography.bodyMedium,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            checked = option.showRemainTime,
            onCheckedChange = { new -> onUpdate { it.copy(showRemainTime = new) } },
        )
        Text(
            text = stringResource(id = R.string.widget_status_to_show),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}
