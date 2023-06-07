package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.rememberWallpaperBitmap
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetOptions
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetPreview

@Composable
fun CustomWidgetScreen(viewModel: CustomWidgetViewModel) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(appBarState)
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
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
            ) {
                widgetSettingsState?.let { widgetSettings ->
                    NoteWidgetPreview(
                        option = widgetSettings.noteWidgetOption,
                        modifier = Modifier.padding(24.dp)
                                .height(300.dp)
                    )

                    Surface(shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)) {
                        NoteWidgetOptions(
                            option = widgetSettings.noteWidgetOption,
                            onUpdate = viewModel::updateNoteWidget,
                        )
                    }
                }
            }
        }
    }
}