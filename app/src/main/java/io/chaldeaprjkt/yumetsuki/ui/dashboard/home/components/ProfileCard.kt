package io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.user.entity.User
import io.chaldeaprjkt.yumetsuki.ui.theme.AppTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProfileCard() {
    AppTheme {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(24.dp)
            ) {
                ProfileCard(user = User.Preview)
                Spacer(Modifier.height(16.dp))
                ProfileCard(user = User.Preview.copy(nickname = "Kaedehara Kazuha"))
                Spacer(Modifier.height(16.dp))
                ProfileCard(user = User.Preview.copy(nickname = "Rita Rossweisse", status = ""))
            }
        }
    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    user: User,
    onLogout: () -> Unit = {},
    onCopyCookie: () -> Unit = {},
    onSyncRequest: () -> Unit = {},
) {
    val menuExpanded = remember { mutableStateOf(false) }

    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    menuExpanded.value = true
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AvatarBox(modifier = Modifier.padding(horizontal = 8.dp), user = user)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(
                    text = user.nickname,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                AnimatedVisibility(visible = user.status.trim().isNotBlank()) {
                    Text(
                        text = user.status,
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
            Column {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "more",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                DropdownMenu(
                    expanded = menuExpanded.value,
                    onDismissRequest = { menuExpanded.value = false },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            menuExpanded.value = false
                            onSyncRequest()
                        },
                        text = {
                            Text(text = stringResource(id = R.string.sync_account))
                        },
                    )
                    DropdownMenuItem(
                        onClick = {
                            menuExpanded.value = false
                            onCopyCookie()
                        },
                        text = {
                            Text(text = stringResource(id = R.string.copy_cookie))
                        },
                    )
                    DropdownMenuItem(
                        onClick = {
                            menuExpanded.value = false
                            onLogout()
                        },
                        text = {
                            Text(text = stringResource(id = R.string.logout))
                        },
                    )
                }

            }
        }
    }
}

@Composable
fun AvatarBox(modifier: Modifier = Modifier, user: User) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(8.dp)
            .size(82.dp),
    ) {
        AsyncImage(
            model = user.avatarUrl,
            placeholder = painterResource(R.drawable.avatar_default),
            error = painterResource(R.drawable.avatar_default),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            contentDescription = null
        )
        AnimatedVisibility(visible = user.frameUrl.contains("http")) {
            AsyncImage(
                model = user.frameUrl,
                placeholder = painterResource(R.drawable.avatar_frame_default),
                modifier = Modifier.fillMaxSize(),
                contentDescription = null
            )
        }
    }
}
