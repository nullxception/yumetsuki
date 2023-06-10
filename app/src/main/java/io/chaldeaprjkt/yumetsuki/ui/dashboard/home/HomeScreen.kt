package io.chaldeaprjkt.yumetsuki.ui.dashboard.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components.DataSyncContent
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components.DataSyncViewModel
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components.GameAccountsContent
import io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components.ProfileCard
import io.chaldeaprjkt.yumetsuki.util.notifier.Notifier

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    dataSyncViewModel: DataSyncViewModel,
    onAddAccount: () -> Unit,
    onNavigateLogin: () -> Unit
) {
    val users by viewModel.users.collectAsState()
    val genshinUser by viewModel.genshinUser.collectAsState()
    val starRailUser by viewModel.starRailUser.collectAsState()
    val settingsState by viewModel.settings.collectAsState()
    val accounts by viewModel.gameAccounts.collectAsState()
    val gameAccountsSyncState by viewModel.gameAccountsSyncState.collectAsState()
    val checkInStatus by viewModel.checkInStatus.collectAsState()
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                scrollBehavior = scrollBehavior,
                actions = {
                    val menuExpanded = remember { mutableStateOf(false) }
                    IconButton(onClick = { menuExpanded.value = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "more")
                    }
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false },
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded.value = false
                                onAddAccount()
                            },
                            text = { Text(text = stringResource(id = R.string.add_more_user)) },
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) {
        Column(
            modifier =
                Modifier.verticalScroll(rememberScrollState()).padding(it).consumeWindowInsets(it)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            users.forEach { user ->
                ProfileCard(
                    user = user,
                    onLogout = {
                        val count = users.count()
                        viewModel.logout(user)
                        if (count == 1) {
                            onNavigateLogin()
                        }
                    },
                    onCopyCookie = { viewModel.copyCookieString(context, user) },
                    onSyncRequest = { viewModel.syncUser(user) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            settingsState?.let { settings ->
                GameAccountsContent(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    accounts = accounts,
                    checkInStatus = checkInStatus,
                    settings = settings,
                    gameAccSyncState = gameAccountsSyncState,
                    onCheckInSettingsChange = viewModel::updateCheckInSettings,
                    onCheckInNow = viewModel::checkInNow,
                    onActivateGameAccount = viewModel::activateGameAccount
                )
            }
            DataSyncContent(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                viewModel = dataSyncViewModel,
                genshinUser = genshinUser,
                starRailUser = starRailUser,
            )
        }
    }

    val requiredPermissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    val permissions = rememberMultiplePermissionsState(permissions = requiredPermissions)
    val showPermissionDialog = remember { mutableStateOf(!permissions.allPermissionsGranted) }

    if (showPermissionDialog.value) {
        val isFirstLaunch = settingsState?.isFirstLaunch ?: return

        PermissionDialogs(
            isFirstLaunch = isFirstLaunch,
            onDismissRequest = { showPermissionDialog.value = false },
            onPermissionRequest = {
                showPermissionDialog.value = false
                viewModel.markLaunched()
                Notifier.createChannels(context.applicationContext)
                permissions.launchMultiplePermissionRequest()
            },
        )
    }
}

@Composable
fun PermissionDialogs(
    isFirstLaunch: Boolean,
    onDismissRequest: () -> Unit = {},
    onPermissionRequest: () -> Unit = {},
) {
    if (!isFirstLaunch) return
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(id = R.string.initial_permission_title)) },
        text = { Text(text = stringResource(id = R.string.initial_permission_msg)) },
        confirmButton = {
            Button(
                onClick = { onPermissionRequest() },
            ) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
    )
}
