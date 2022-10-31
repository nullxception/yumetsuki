package io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AssignmentLate
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.checkin.entity.CheckInNote
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.settings.entity.Settings
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.GameAccSyncState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
private fun PreviewContent() {
    Surface {
        GameAccountsContent(
            accounts = emptyList(),
            checkInStatus = emptyList(),
            settings = Settings.Empty,
            gameAccSyncState = GameAccSyncState.Loading,
            onCheckInSettingsChange = { _, _ -> },
            onCheckInNow = {},
            onActivateGameAccount = {},
            activeAccounts = emptyList(),
        )
    }
}

fun CoroutineScope.switchStateFor(
    timeMilis: Long,
    state: MutableState<Boolean>,
    initial: Boolean
) {
    state.value = initial
    launch {
        delay(timeMilis)
        state.value = !initial
    }
}

@Composable
fun GameAccountsContent(
    modifier: Modifier = Modifier,
    accounts: List<GameAccount>,
    checkInStatus: List<CheckInNote>,
    settings: Settings,
    gameAccSyncState: GameAccSyncState,
    onCheckInSettingsChange: (Boolean, HoYoGame) -> Unit,
    onCheckInNow: () -> Unit,
    onActivateGameAccount: (GameAccount) -> Unit,
    activeAccounts: List<GameAccount>,
) {
    val isAccountSelectorOpen = remember { mutableStateOf(false) }
    val noticeSingleAccountGenshin = remember { mutableStateOf(false) }
    val noticeSingleAccountHoukai = remember { mutableStateOf(false) }
    val accountType = remember { mutableStateOf(HoYoGame.Unknown) }
    val coroutineScope = rememberCoroutineScope()

    fun showSingleAccountNotice(game: HoYoGame) {
        if (game == HoYoGame.Houkai) {
            coroutineScope.switchStateFor(
                timeMilis = 1500,
                state = noticeSingleAccountHoukai,
                initial = true
            )
        } else if (game == HoYoGame.Genshin) {
            coroutineScope.switchStateFor(
                timeMilis = 1500,
                state = noticeSingleAccountGenshin,
                initial = true
            )
        }
    }

    fun openAccSelector(game: HoYoGame) {
        if (!accounts.any { it.game == game }) return
        val accs = accounts.filter { it.game == game }
        val accsActive = activeAccounts.filter { it.game == game }

        if (accs.isNotEmpty() && accsActive.isNotEmpty()) {
            showSingleAccountNotice(game)
            return
        }

        accountType.value = game
        isAccountSelectorOpen.value = true
    }

    if (isAccountSelectorOpen.value) {
        GameAccountsSelectorDialog(
            accounts = accounts,
            game = accountType.value,
            onDismissRequest = {
                isAccountSelectorOpen.value = false
            },
            onCardClicked = {
                onActivateGameAccount(it)
                isAccountSelectorOpen.value = false
            },
        )
    }

    AnimatedVisibility(
        visible = gameAccSyncState is GameAccSyncState.Loading,
        enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top),
        exit = slideOutVertically() + shrinkVertically(shrinkTowards = Alignment.Top)
    ) {
        Card(modifier = modifier) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(id = R.string.game_accounts_syncing),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    Column(modifier = modifier) {
        Card {
            Column {
                val activeHoukai =
                    activeAccounts.firstOrNull { it.game == HoYoGame.Houkai } ?: GameAccount.Empty
                GameAccountCard(
                    modifier = Modifier.clickable {
                        openAccSelector(HoYoGame.Houkai)
                    },
                    account = activeHoukai,
                    game = HoYoGame.Houkai,
                    noticeSingleAccount = noticeSingleAccountHoukai.value,
                )
                CheckInStatusDisplay(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    isCheckedIn = checkInStatus.any { it.uid == activeHoukai.uid && it.checkedToday() },
                )
                Row(
                    modifier = Modifier
                        .toggleable(
                            value = settings.checkIn.houkai,
                            onValueChange = {
                                onCheckInSettingsChange(it, HoYoGame.Houkai)
                            },
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(id = R.string.auto_check_in),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Switch(
                        enabled = activeAccounts.any { it.game == HoYoGame.Houkai },
                        checked = settings.checkIn.houkai,
                        onCheckedChange = null,
                        modifier = Modifier.scale(.75f),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card {
            val activeGenshin = activeAccounts.firstOrNull { it.game == HoYoGame.Genshin }
                ?: GameAccount.Empty
            Column {
                GameAccountCard(
                    modifier = Modifier.clickable {
                        openAccSelector(HoYoGame.Genshin)
                    },
                    account = activeGenshin,
                    game = HoYoGame.Genshin,
                    noticeSingleAccount = noticeSingleAccountGenshin.value,
                )
                CheckInStatusDisplay(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    isCheckedIn = checkInStatus.any { it.uid == activeGenshin.uid && it.checkedToday() },
                )
                Row(
                    modifier = Modifier
                        .toggleable(
                            value = settings.checkIn.genshin,
                            onValueChange = {
                                onCheckInSettingsChange(it, HoYoGame.Genshin)
                            },
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(id = R.string.auto_check_in),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Switch(
                        enabled = activeAccounts.any { it.game == HoYoGame.Genshin },
                        checked = settings.checkIn.genshin,
                        onCheckedChange = null,
                        modifier = Modifier.scale(.75f),
                    )
                }
            }
        }

        ElevatedButton(
            onClick = onCheckInNow,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .padding(horizontal = 8.dp)
                .align(Alignment.End),
            enabled = settings.checkIn.genshin || settings.checkIn.houkai
        ) {
            Icon(Icons.Outlined.AssignmentLate, contentDescription = null)
            Spacer(Modifier.width(16.dp))
            Text(text = stringResource(id = R.string.checkin_now))
        }
    }
}

@Composable
fun CheckInStatusDisplay(modifier: Modifier = Modifier, isCheckedIn: Boolean) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.today_check_in_title),
            style = MaterialTheme.typography.bodySmall
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = if (isCheckedIn) R.string.completed else R.string.not_yet),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            Icon(
                if (isCheckedIn) Icons.Outlined.AssignmentTurnedIn else Icons.Outlined.AssignmentLate,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun GameAccountsSelectorDialog(
    accounts: List<GameAccount>,
    game: HoYoGame,
    onCardClicked: (GameAccount) -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier = Modifier.padding(vertical = 24.dp),
            ) {
                Box(
                    Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        painter = painterResource(
                            id = when (game) {
                                HoYoGame.Houkai -> R.drawable.ic_honkai
                                else -> R.drawable.ic_genshin
                            }
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.select_account),
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                accounts.filter { it.game == game }.forEach {
                    Box(
                        modifier = Modifier
                            .clickable { onCardClicked(it) }
                            .padding(horizontal = 16.dp),
                    ) {
                        GameAccountInfo(
                            account = it,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}
