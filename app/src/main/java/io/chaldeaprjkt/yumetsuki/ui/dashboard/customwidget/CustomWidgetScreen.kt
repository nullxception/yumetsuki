package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.rememberWallpaperBitmap
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteItemsVisibility
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetOptions
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetPreview

@Composable
fun CustomWidgetScreen(viewModel: CustomWidgetViewModel) {
    val widgetSettingsState by viewModel.settings.collectAsState()
    Box {
        Image(
            bitmap = rememberWallpaperBitmap(),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .padding(bottom = 128.dp),
            contentScale = ContentScale.Crop,
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                val paddingValues = WindowInsets.safeDrawing.asPaddingValues()
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
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
            LazyColumn(
                modifier = Modifier
                    .consumeWindowInsets(paddingValues)
            ) {
                    item {
                        NoteWidgetPreview(
                            option = widgetSettingsState?.noteWidgetOption,
                            modifier = Modifier
                                .padding(24.dp)
                                .height(300.dp)
                        )
                    }
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
                        ) {
                            NoteWidgetOptions(
                                option = widgetSettingsState?.noteWidgetOption,
                                onUpdate = viewModel::updateNoteWidget,
                            )
                        }
                    }
                    item {
                        Surface(modifier = Modifier.fillMaxWidth()) {
                            NoteItemsVisibility(
                                option = widgetSettingsState?.noteWidgetOption,
                                onUpdate = viewModel::updateNoteWidget,
                            )
                        }
                    }
            }
        }
    }
}