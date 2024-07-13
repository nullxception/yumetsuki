package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetItem
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetSetting
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.rememberWallpaperBitmap
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetOptions
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetPreview
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.drawableId
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.stringId
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun CustomWidgetScreen(viewModel: CustomWidgetViewModel) {
    val widgetSettingsState by viewModel.settings.collectAsState()
    Box {
        Image(
            bitmap = rememberWallpaperBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
        // avoid "transparent gap" when overscrolled
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .align(Alignment.BottomCenter)
        ) {}
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                val paddingValues = WindowInsets.safeDrawing.asPaddingValues()
                Box(
                    modifier =
                    Modifier
                        .background(
                            Brush.verticalGradient(
                                colors =
                                listOf(
                                    MaterialTheme.colorScheme.background.copy(alpha = .5f),
                                    Color.Transparent
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                        .fillMaxWidth()
                        .height(paddingValues.calculateTopPadding() * 2),
                    content = {}
                )
            },
            contentWindowInsets = WindowInsets.statusBars,
        ) { paddingValues ->
            widgetSettingsState?.noteWidgetOption?.let {
                CustomWidgetContent(
                    paddingValues = paddingValues,
                    settings = it,
                    onUpdate = viewModel::updateNoteWidget
                )
            }
        }
    }
}

private fun getNewItems(items: List<NoteWidgetItem>) =
    NoteWidgetSetting.DefaultItems.filter { d -> !items.any { it.type == d.type } }
        .map { NoteWidgetItem(it.type, false) }

@Composable
fun CustomWidgetContent(
    paddingValues: PaddingValues,
    settings: NoteWidgetSetting,
    onUpdate: (suspend (NoteWidgetSetting) -> NoteWidgetSetting) -> Unit,
) {
    val data = remember { mutableStateOf(settings.items + getNewItems(settings.items)) }
    val state =
        rememberReorderableLazyListState(
            onMove = { from, to ->
                val offset = 3
                data.value =
                    data.value.toMutableList().apply {
                        add(to.index - offset, removeAt(from.index - offset))
                    }
                onUpdate { it.copy(items = data.value) }
            }
        )
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .consumeWindowInsets(paddingValues)
            .reorderable(state),
    ) {
        item {
            NoteWidgetPreview(
                option = settings, modifier = Modifier
                    .padding(24.dp)
                    .height(300.dp)
            )
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
            ) {
                NoteWidgetOptions(option = settings, onUpdate = onUpdate)
            }
        }
        item {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.widget_status_to_show),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }
        }
        items(data.value, { it }, { x -> x }) { item ->
            ReorderableItem(
                state,
                key = item,
            ) { isDragging ->
                val backgroundColor =
                    animateColorAsState(
                        if (isDragging) MaterialTheme.colorScheme.background.copy(alpha = .85f)
                        else MaterialTheme.colorScheme.background,
                        label = "background while dragging"
                    )
                val padding =
                    animateDpAsState(
                        if (isDragging) 16.dp else 0.dp,
                        label = "padding while dragging"
                    )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding.value)
                        .background(
                            backgroundColor.value,
                            shape = RoundedCornerShape(padding.value)
                        )
                        .detectReorderAfterLongPress(state)
                        .padding(horizontal = 24.dp)
                ) {
                    Image(
                        painter = painterResource(id = item.type.drawableId()),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium,
                        text = stringResource(id = item.type.stringId())
                    )
                    IconToggleButton(
                        checked = item.show,
                        onCheckedChange = { checked ->
                            onUpdate {
                                val newItems =
                                    data.value.toMutableList().apply {
                                        set(indexOf(item), item.copy(show = checked))
                                    }
                                data.value = newItems
                                it.copy(items = newItems)
                            }
                        }
                    ) {
                        Icon(
                            imageVector =
                            if (item.show) Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff,
                            contentDescription = "toggle visibility"
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.DragIndicator,
                        contentDescription = "drag handle",
                        modifier = Modifier.detectReorderAfterLongPress(state)
                    )
                }
            }
        }
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {}
        }
    }
}
