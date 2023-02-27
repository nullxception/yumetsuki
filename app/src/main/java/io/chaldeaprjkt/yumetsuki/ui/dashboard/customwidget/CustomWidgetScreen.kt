package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumedWindowInsets
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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.rememberWallpaperBitmap
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetOptions
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget.NoteWidgetPreview
import kotlinx.coroutines.launch

@Composable
fun CustomWidgetScreen(viewModel: CustomWidgetViewModel) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(appBarState)
    val pagerState = rememberPagerState()
    val pageNames = listOf(R.string.tab_note)
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
            bottomBar = {
                if (pageNames.size > 1) {
                    WidgetOptionsTabs(
                        state = pagerState,
                        names = pageNames,
                    )
                }
            },
            contentWindowInsets = WindowInsets.statusBars,
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(paddingValues)
                    .consumedWindowInsets(paddingValues)
            ) {
                widgetSettingsState?.let { widgetSettings ->
                    WidgetPreviews(
                        state = pagerState,
                        settings = widgetSettings
                    )

                    WidgetOptionsPager(
                        state = pagerState,
                        names = pageNames,
                        viewModel = viewModel,
                        settings = widgetSettings
                    )
                }
            }
        }
    }
}

@Composable
fun WidgetOptionsTabs(modifier: Modifier = Modifier, state: PagerState, names: List<Int>) {
    val coroutineScope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = state.currentPage,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .pagerTabIndicatorOffsetMD3(state, tabPositions)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50))
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
            )
        },
        divider = {},
        modifier = modifier,
        edgePadding = 0.dp,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        names.forEachIndexed { index, nameId ->
            Tab(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .zIndex(2f),
                text = {
                    Text(
                        text = stringResource(id = nameId),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                selected = state.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        state.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@Composable
fun WidgetPreviews(
    state: PagerState,
    settings: Settings,
) {
    AnimatedContent(targetState = state.currentPage, modifier = Modifier.height(300.dp)) {
        val previewModifier = Modifier.padding(24.dp)
        when (it) {
            0 -> NoteWidgetPreview(option = settings.noteWidgetOption, modifier = previewModifier)
        }
    }
}

@Composable
fun WidgetOptionsPager(
    names: List<Int>,
    state: PagerState,
    settings: Settings,
    viewModel: CustomWidgetViewModel,
) {
    Surface(shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)) {
        HorizontalPager(
            count = names.size,
            state = state,
            verticalAlignment = Alignment.Top,
        ) { pageNum ->
            when (names[pageNum]) {
                R.string.tab_note -> NoteWidgetOptions(
                    option = settings.noteWidgetOption,
                    onUpdate = viewModel::updateNoteWidget,
                )
            }
        }
    }
}

// TODO: Replace it with actual MD3 implementation by Accompanist
fun Modifier.pagerTabIndicatorOffsetMD3(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        // If there are no pages, nothing to show
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = pagerState.currentPageOffset
        val indicatorWidth = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.width, nextTab.width, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
        } else {
            currentTab.width.roundToPx()
        }
        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
        } else {
            currentTab.left.roundToPx()
        }
        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )
        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.placeRelative(
                indicatorOffset, maxOf(constraints.minHeight - placeable.height, 0)
            )
        }
    }
}
