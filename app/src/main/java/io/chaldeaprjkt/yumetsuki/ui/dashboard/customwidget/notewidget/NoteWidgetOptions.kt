package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetOption
import io.chaldeaprjkt.yumetsuki.ui.components.TextSwitch
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.SelectableChip
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.WidgetOptionSlider

@Composable
fun NoteWidgetOptions(
    option: NoteWidgetOption,
    onUpdate: (suspend (NoteWidgetOption) -> NoteWidgetOption) -> Unit,
) {
    Column {
        WidgetOptionSlider(
            title = stringResource(id = R.string.background_alpha),
            textStyle = MaterialTheme.typography.bodyMedium,
            value = option.backgroundAlpha,
            indicatorFormat = "%.2f",
            steps = 40,
            onValueChange = { new ->
                onUpdate { it.copy(backgroundAlpha = new) }
            },
            onResetClicked = {
                onUpdate { it.copy(backgroundAlpha = NoteWidgetOption.DefaultBackgroundAlpha) }
            },
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(top = 16.dp),
        )
        WidgetOptionSlider(
            title = stringResource(id = R.string.font_size),
            textStyle = MaterialTheme.typography.bodyMedium,
            value = option.fontSize,
            valueRange = 8f..18f,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            onValueChange = { new ->
                onUpdate { it.copy(fontSize = new) }
            },
            onResetClicked = {
                onUpdate { it.copy(fontSize = NoteWidgetOption.DefaultFontSize) }
            },
        )
        TextSwitch(
            text = stringResource(id = R.string.widget_show_desc),
            textStyle = MaterialTheme.typography.bodyMedium,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            checked = option.showDescription,
            onCheckedChange = { new ->
                onUpdate { it.copy(showDescription = new) }
            },
        )
        TextSwitch(
            text = stringResource(id = R.string.widget_show_est_full_time),
            textStyle = MaterialTheme.typography.bodyMedium,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            checked = option.showRemainTime,
            onCheckedChange = { new ->
                onUpdate { it.copy(showRemainTime = new) }
            },
        )
        Text(
            text = stringResource(id = R.string.widget_status_to_show),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
        ItemsVisibility(option = option, onUpdate = onUpdate)
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ItemsVisibility(
    option: NoteWidgetOption,
    onUpdate: (suspend (NoteWidgetOption) -> NoteWidgetOption) -> Unit,
) {

    FlowRow(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(top = 8.dp),
        mainAxisSpacing = 16.dp
    ) {
        SelectableChip(
            label = stringResource(id = R.string.resin),
            labelStyle = MaterialTheme.typography.bodySmall,
            selected = option.showResinData,
            onSelectionChange = { new ->
                onUpdate { it.copy(showResinData = new) }
            },
        )
        SelectableChip(
            label = stringResource(id = R.string.daily_commissions),
            labelStyle = MaterialTheme.typography.bodySmall,
            selected = option.showDailyCommissionData,
            onSelectionChange = { new ->
                onUpdate { it.copy(showDailyCommissionData = new) }
            },
        )
        SelectableChip(
            label = stringResource(id = R.string.enemies_of_note),
            labelStyle = MaterialTheme.typography.bodySmall,
            selected = option.showWeeklyBossData,
            onSelectionChange = { new ->
                onUpdate { it.copy(showWeeklyBossData = new) }
            },
        )
        SelectableChip(
            label = stringResource(id = R.string.realm_currency),
            labelStyle = MaterialTheme.typography.bodySmall,
            selected = option.showRealmCurrencyData,
            onSelectionChange = { new ->
                onUpdate { it.copy(showRealmCurrencyData = new) }
            },
        )
        SelectableChip(
            label = stringResource(id = R.string.expedition),
            labelStyle = MaterialTheme.typography.bodySmall,
            selected = option.showExpeditionData,
            onSelectionChange = { new ->
                onUpdate { it.copy(showExpeditionData = new) }
            },
        )
        SelectableChip(
            label = stringResource(id = R.string.parametric_transformer),
            labelStyle = MaterialTheme.typography.bodySmall,
            selected = option.showParaTransformerData,
            onSelectionChange = { new ->
                onUpdate { it.copy(showParaTransformerData = new) }
            },
        )
    }
}