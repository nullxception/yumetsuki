package io.chaldeaprjkt.yumetsuki.ui.dashboard.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.ResinOption
import io.chaldeaprjkt.yumetsuki.ui.components.TextSwitch

@Composable
fun NotificationScreen(viewModel: NotificationViewModel) {
    val settingsState by viewModel.settings.collectAsState()
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.notification))
                },
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->
        settingsState?.let { settings ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(paddingValues)
                    .consumedWindowInsets(paddingValues),
            ) {
                Text(
                    text = stringResource(id = R.string.auto_check_in),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                )
                TextSwitch(
                    text = stringResource(id = R.string.on_check_in_success),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    checked = settings.notifier.onCheckInSuccess,
                    onCheckedChange = {
                        viewModel.notifyOnCheckInSuccess(it)
                    },
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                )
                TextSwitch(
                    text = stringResource(id = R.string.on_check_in_failed),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    checked = settings.notifier.onCheckInFailed,
                    onCheckedChange = {
                        viewModel.notifyOnCheckInFailed(it)
                    },
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                )
                Text(
                    text = stringResource(id = R.string.ingame_events),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                )
                ResinOptionSelector(
                    resin = settings.notifier.onResin,
                    onSelected = {
                        viewModel.notifyOnResin(it)
                    },
                )
                TextSwitch(
                    text = stringResource(id = R.string.expedition_done),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    checked = settings.notifier.onExpeditionCompleted,
                    onCheckedChange = {
                        viewModel.notifyOnExpeditionCompleted(it)
                    },
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                )
                TextSwitch(
                    text = stringResource(id = R.string.realmcurrency_full),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    checked = settings.notifier.onRealmCurrencyFull,
                    onCheckedChange = {
                        viewModel.notifyOnRealmCurrencyFull(it)
                    },
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                )
            }
        }
    }
}

@Composable
fun ResinOptionSelector(resin: ResinOption, onSelected: (ResinOption) -> Unit) {
    val isDialogShowing = remember { mutableStateOf(false) }
    val valueDesc = stringArrayResource(id = R.array.resin_values)
    Row(
        modifier = Modifier
            .clickable {
                isDialogShowing.value = true
            }
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.current_resin_status),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = valueDesc[resin.ordinal],
            style = MaterialTheme.typography.bodySmall,
        )
    }

    if (!isDialogShowing.value) return
    Dialog(
        onDismissRequest = { isDialogShowing.value = false },
    ) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(vertical = 24.dp),
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.current_resin_status),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                valueDesc.forEachIndexed { i, desc ->
                    val option = ResinOption.values()[i]
                    Row(
                        modifier = Modifier
                            .clickable {
                                onSelected(option)
                                isDialogShowing.value = false
                            }
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = resin == option,
                            onClick = null
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(text = desc)
                    }
                }
            }
        }
    }
}
