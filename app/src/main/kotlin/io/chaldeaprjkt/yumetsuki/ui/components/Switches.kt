package io.chaldeaprjkt.yumetsuki.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.ui.theme.AppTheme

@Preview
@Composable
fun PreviewTextSwitch() {
    AppTheme {
        Surface {
            TextSwitch(text = "Enable things", contentPadding = PaddingValues(32.dp))
        }
    }
}

@Composable
fun TextSwitch(
    modifier: Modifier = Modifier,
    text: String = "",
    contentPadding: PaddingValues = PaddingValues(0.dp),
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    textStyle: TextStyle = LocalTextStyle.current,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .toggleable(
                value = checked,
                onValueChange = { onCheckedChange(it) },
            )
            .padding(contentPadding)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = text, style = textStyle)
        Switch(
            enabled = enabled,
            checked = checked,
            onCheckedChange = null,
        )
    }
}
