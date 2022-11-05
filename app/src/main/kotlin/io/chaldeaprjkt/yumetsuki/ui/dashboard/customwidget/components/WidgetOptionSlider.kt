package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.ui.theme.AppTheme

@Preview
@Composable
fun PreviewWidgetOptionSlider() {
    AppTheme {
        Surface(modifier = Modifier.padding(32.dp)) {
            WidgetOptionSlider(title = "Background alpha")
        }
    }
}

@Composable
fun WidgetOptionSlider(
    modifier: Modifier = Modifier,
    title: String = "",
    textStyle: TextStyle = LocalTextStyle.current,
    indicatorFormat: String = "%.1f",
    value: Float = 0f,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChange: (Float) -> Unit = {},
    onResetClicked: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = title, style = textStyle)
            Text(
                text = indicatorFormat.format(value),
                style = textStyle,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Slider(
                value = value,
                steps = steps,
                valueRange = valueRange,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onResetClicked) {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = stringResource(id = R.string.refresh),
                )
            }
        }
    }
}
