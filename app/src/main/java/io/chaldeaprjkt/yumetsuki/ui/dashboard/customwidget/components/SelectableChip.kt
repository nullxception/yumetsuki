package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun SelectableChip(
    modifier: Modifier = Modifier,
    label: String = "",
    labelStyle: TextStyle = LocalTextStyle.current,
    selected: Boolean,
    onSelectionChange: (Boolean) -> Unit = {},
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = { onSelectionChange(!selected) },
        label = { Text(label, style = labelStyle) },
        leadingIcon = {
            Icon(
                imageVector = if (selected) {
                    Icons.Filled.Check
                } else {
                    Icons.Outlined.Add
                },
                contentDescription = null,
                modifier = Modifier.size(FilterChipDefaults.IconSize),
            )
        },
    )
}
