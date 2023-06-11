package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetSetting
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.rememberWallpaperBitmap
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetOptions
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetPreview
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
            modifier = Modifier.matchParentSize().padding(bottom = 128.dp),
            contentScale = ContentScale.Crop,
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                val paddingValues = WindowInsets.safeDrawing.asPaddingValues()
                Box(
                    modifier =
                        Modifier.background(
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

@Composable
fun CustomWidgetContent(
    paddingValues: PaddingValues,
    settings: NoteWidgetSetting,
    onUpdate: (suspend (NoteWidgetSetting) -> NoteWidgetSetting) -> Unit,
) {

    val data = remember { mutableStateOf(settings.items) }
    val state =
        rememberReorderableLazyListState(
            onMove = { from, to ->
                data.value =
                    data.value.toMutableList().apply { add(to.index - 2, removeAt(from.index - 2)) }
                onUpdate { it.copy(items = data.value) }
            }
        )
    LazyColumn(
        state = state.listState,
        modifier = Modifier.consumeWindowInsets(paddingValues).reorderable(state),
    ) {
        item {
            NoteWidgetPreview(option = settings, modifier = Modifier.padding(24.dp).height(300.dp))
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
            ) {
                NoteWidgetOptions(option = settings, onUpdate = onUpdate)
            }
        }
        items(data.value, { it }, { x -> x }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val bg =
                    animateColorAsState(
                        if (isDragging) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.background,
                        label = ""
                    )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier =
                        Modifier.fillMaxWidth()
                            .background(bg.value)
                            .detectReorderAfterLongPress(state)
                ) {
                    Checkbox(
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
                    )
                    Text(text = stringResource(id = item.type.stringId()))
                    Icon(
                        imageVector = Icons.Filled.DragHandle,
                        contentDescription = "drag",
                        modifier = Modifier.detectReorderAfterLongPress(state)
                    )
                }
            }
        }
    }
}
