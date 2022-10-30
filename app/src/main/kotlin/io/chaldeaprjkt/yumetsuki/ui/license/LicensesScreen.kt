package io.chaldeaprjkt.yumetsuki.ui.license

import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Feed
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.HtmlCompat
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.util.withJson
import io.chaldeaprjkt.yumetsuki.R

@Preview
@Composable
private fun PreviewLicenseScreen() {
    LicenseScreen(onPopBack = {})
}

@Composable
fun LicenseScreen(onPopBack: () -> Unit) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onPopBack) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(id = android.R.string.cancel)
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.license_title))
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { contentPadding ->
        LibsLicenseContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumedWindowInsets(contentPadding),
        )
    }
}

@Composable
fun LibsLicenseContent(modifier: Modifier = Modifier) {
    var dialogLibData by remember { mutableStateOf<Library?>(null) }
    val repository = remember { mutableStateOf<Libs?>(null) }
    val libs = remember(repository.value) {
        repository.value?.libraries?.map {
            it.copy(name = it.name.replace(" library", "", true).trim())
        }?.distinctBy { it.name.trim() }
    }
    val context = LocalContext.current

    dialogLibData?.let {
        LicenseContentDialog(library = it, onDismiss = { dialogLibData = null })
    }

    LaunchedEffect(Unit) {
        if (libs == null) {
            repository.value = Libs.Builder().withJson(context, R.raw.aboutlibraries).build()
        }
    }

    libs ?: return
    LazyColumn(modifier) {
        items(libs) {
            LibLicenseItem(library = it, onClick = { dialogLibData = it })
        }
    }
}

@Composable
fun LibLicenseItem(library: Library, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = library.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val version = library.artifactVersion
            if (version != null) {
                Text(
                    version,
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        if (library.author.isNotBlank()) {
            Text(
                text = library.author,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (library.licenses.isNotEmpty()) {
            Row(modifier = Modifier.padding(top = 8.dp)) {
                library.licenses.forEach {
                    Badge(
                        modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                        contentColor = MaterialTheme.colorScheme.secondaryContainer,
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp), text = it.name
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LicenseContentDialog(
    library: Library,
    onDismiss: () -> Unit,
) {
    val scrollVState = rememberScrollState()
    val content = library.licenses.firstOrNull()?.licenseContent.orEmpty()
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            modifier = Modifier
                .padding(24.dp)
                .wrapContentSize()
                .animateContentSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Outlined.Feed, contentDescription = null,
                    modifier = Modifier.padding(top = 24.dp),
                )
                Text(
                    library.name,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                )
                Text(
                    library.author,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                )
                Divider(modifier = Modifier
                    .padding(top = 16.dp)
                    .alpha(0.5f))
                AndroidView(
                    modifier = Modifier
                        .verticalScroll(scrollVState)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .weight(1f),
                    factory = { TextView(it) },
                    update = {
                        it.text = HtmlCompat.fromHtml(
                            content.replace("\n", "<br />"),
                            HtmlCompat.FROM_HTML_MODE_COMPACT
                        )
                    },
                )
                Divider(modifier = Modifier.alpha(0.5f))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.close),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

private val Library.author
    get() = developers.takeIf { it.isNotEmpty() }?.map { it.name }?.joinToString(", ")
        ?: organization?.name ?: ""
