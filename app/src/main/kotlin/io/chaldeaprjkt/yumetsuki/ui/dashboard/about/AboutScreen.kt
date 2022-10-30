package io.chaldeaprjkt.yumetsuki.ui.dashboard.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feed
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Github
import io.chaldeaprjkt.yumetsuki.BuildConfig
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.constant.Source
import io.chaldeaprjkt.yumetsuki.ui.theme.AppTheme

@Composable
fun AboutScreen(onOpenLicense: () -> Unit) {
    AboutContent(onOpenLicense = onOpenLicense)
}

@Preview
@Composable
private fun PreviewScreen() {
    AppTheme {
        AboutContent(onOpenLicense = {})
    }
}

@Composable
fun AboutContent(onOpenLicense: () -> Unit) {
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.about_title))
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(paddingValues)
                .consumedWindowInsets(paddingValues),
        ) {
            val uriHandler = LocalUriHandler.current
            VersionInfo()
            Divider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .alpha(0.5f)
                    .fillMaxWidth()
            )
            ListItem(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        uriHandler.openUri(Source.App.GitHub)
                    },
                leadingContent = {
                    Icon(FeatherIcons.Github, contentDescription = null)
                },
                headlineText = {
                    Text("GitHub")
                },
            )
            ListItem(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onOpenLicense()
                    },
                leadingContent = {
                    Icon(Icons.Outlined.Feed, contentDescription = null)
                },
                headlineText = {
                    Text(stringResource(id = R.string.license_title))
                },
            )
            ListItem(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        uriHandler.openUri(Source.App.PrivacyPolicy)
                    },
                leadingContent = {
                    Icon(Icons.Outlined.Policy, contentDescription = null)
                },
                headlineText = {
                    Text(stringResource(id = R.string.privacy_policy))
                },
            )
        }
    }
}

@Composable
fun VersionInfo() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painterResource(R.drawable.ic_yumetsuki),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clip(CircleShape)
                .size(84.dp),
            contentDescription = stringResource(R.string.app_name)
        )
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            stringResource(R.string.version_name, BuildConfig.VERSION_NAME),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }
}
