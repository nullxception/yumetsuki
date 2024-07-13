package io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.SyncProblem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.util.extension.describeDateTime

@Composable
fun DataSyncContent(
    modifier: Modifier = Modifier,
    viewModel: DataSyncViewModel,
    genshinUser: User?,
    starRailUser: User?,
    zzzUser: User?,
) {
    if (genshinUser == null && starRailUser == null) {
        return
    }

    val settingsState by viewModel.settings.collectAsState()
    val dataSyncState by viewModel.dataSyncState.collectAsState()
    val privateNoteState by viewModel.privateNoteState.collectAsState()
    val session by viewModel.session.collectAsState()

    if (
        privateNoteState is PrivateNoteState.Private || privateNoteState is PrivateNoteState.Error
    ) {
        NotePrivateDialog(
            onDismissRequest = { viewModel.ignorePrivateNote() },
            onMakePublic = {
                viewModel.enablePublicNote((privateNoteState as PrivateNoteState.Private).user)
            },
            isError = privateNoteState is PrivateNoteState.Error
        )
    }

    Column(modifier = modifier) {
        DataSyncSectionTitle()
        Spacer(Modifier.height(8.dp))
        DataSync(
            onRequestSync = {
                if (genshinUser != null) viewModel.syncGenshin(genshinUser)
                if (starRailUser != null) viewModel.syncStarRail(starRailUser)
                if (zzzUser != null) viewModel.syncZZZ(zzzUser)
            },
            state = dataSyncState,
            lastSyncTime = session.lastGameDataSync,
        )
        Spacer(Modifier.height(8.dp))
        settingsState?.let { settings ->
            DataSyncPeriodOption(
                period = settings.syncPeriod,
                onSelected = { viewModel.updatePeriodicTime(it) },
            )
        }
    }
}

@Preview
@Composable
private fun Prev_DataSyncContent() {
    Surface {
        Column {
            DataSyncSectionTitle()
            Spacer(Modifier.height(8.dp))
            DataSync(
                onRequestSync = {},
                state = DataSyncState.Error(R.string.fail_get_ingame_data),
                lastSyncTime = System.currentTimeMillis() + 30_000,
            )
            Spacer(Modifier.height(8.dp))
            DataSyncPeriodOption(period = 15L, onSelected = {})
        }
    }
}

@Composable
fun DataSyncSectionTitle() {
    Text(
        text = stringResource(id = R.string.data_sync_title),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp),
    )
}

@Composable
fun DataSync(state: DataSyncState, lastSyncTime: Long, onRequestSync: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                (slideInHorizontally() + fadeIn() togetherWith
                        slideOutHorizontally { w -> w / 2 + w } + fadeOut())
                    .using(SizeTransform(clip = false))
            },
            label = "DataSyncAnim",
        ) { targetState ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                when (targetState) {
                    is DataSyncState.Error ->
                        Text(
                            text = stringResource(id = targetState.messageId),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                    DataSyncState.Loading ->
                        Text(
                            text = stringResource(id = R.string.note_synchronizing),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                    DataSyncState.Success ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(id = R.string.last_sync),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = lastSyncTime.describeDateTime(),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                }
                Spacer(modifier = Modifier.width(16.dp))
                if (targetState is DataSyncState.Loading) {
                    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Button(onClick = onRequestSync) {
                        Icon(Icons.Outlined.CloudSync, contentDescription = null)
                        Spacer(Modifier.width(16.dp))
                        Text(text = stringResource(id = R.string.sync))
                    }
                }
            }
        }
    }
}

@Composable
fun DataSyncPeriodOption(period: Long = 15L, onSelected: (Long) -> Unit = {}) {
    val values = listOf(15L, 30L, 60L, 120L)
    val desc =
        values.map {
            if (it < 60) {
                stringResource(id = R.string.minute_short, it.toInt())
            } else {
                stringResource(id = R.string.hour_short, (it / 60).toInt())
            }
        }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.datasync_timeoption_title),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        Text(
            text = stringResource(id = R.string.datasync_timeoption_desc),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        FlowRow(
            modifier = Modifier
                .selectableGroup()
                .padding(vertical = 8.dp),
        ) {
            values.forEachIndexed { index, time ->
                Row(
                    modifier =
                    Modifier.selectable(
                        selected = time == period,
                        onClick = { onSelected(time) },
                        role = Role.RadioButton,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = time == period, onClick = null)
                    Text(
                        text = desc[index],
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NotePrivateDialog(
    onDismissRequest: () -> Unit = {},
    onMakePublic: () -> Unit = {},
    isError: Boolean,
) {
    val titleId =
        if (isError) {
            R.string.realtimenote_error_data_private
        } else {
            R.string.realtimenote_makepublic_ask
        }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = { Icon(Icons.Outlined.SyncProblem, contentDescription = null) },
        title = {
            Text(
                text = stringResource(id = R.string.realtimenote_error_private),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = { Text(text = stringResource(id = titleId)) },
        dismissButton = {
            if (!isError) {
                TextButton(
                    onClick = {
                        onDismissRequest()
                        onMakePublic()
                    },
                ) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismissRequest()
                    if (!isError) {
                        onMakePublic()
                    }
                },
            ) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
    )
}
