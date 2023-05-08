package io.chaldeaprjkt.yumetsuki.ui.dashboard.home.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoYoGame
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.HoukaiServer
import io.chaldeaprjkt.yumetsuki.data.gameaccount.entity.GameAccount
import io.chaldeaprjkt.yumetsuki.data.gameaccount.server
import io.chaldeaprjkt.yumetsuki.ui.theme.AppTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewGameAccountCard() {
    AppTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Column {
                GameAccountInfo(account = GameAccount.Preview)
                Spacer(Modifier.height(24.dp))
                GameAccountCard(
                    account = GameAccount.Preview,
                    game = HoYoGame.Genshin,
                    noticeSingleAccount = false
                )
                Spacer(Modifier.height(24.dp))
                GameAccountCard(
                    account = GameAccount.Preview.copy(region = HoukaiServer.SEA.regionId),
                    game = HoYoGame.Houkai,
                    noticeSingleAccount = true
                )
            }
        }
    }
}

@Composable
fun GameAccountInfo(modifier: Modifier = Modifier, account: GameAccount) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = account.nickname,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.level_num, account.level),
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = when (account.game) {
                    HoYoGame.Houkai -> {
                        val names = stringArrayResource(R.array.honkai_server)
                        names[account.server.ordinal]
                    }

                    HoYoGame.StarRail -> {
                        val names = stringArrayResource(R.array.starrail_server)
                        names[account.server.ordinal]
                    }

                    HoYoGame.Genshin -> {
                        val names = stringArrayResource(R.array.genshin_server)
                        names[account.server.ordinal]
                    }
                    else -> ""
                },
                style = MaterialTheme.typography.labelSmall,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${account.uid}",
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Composable
fun GameAccountCard(
    modifier: Modifier = Modifier,
    account: GameAccount,
    game: HoYoGame,
    noticeSingleAccount: Boolean,
) {
    AnimatedVisibility(
        visible = noticeSingleAccount,
        enter = slideInVertically() + expandVertically(expandFrom = Alignment.Bottom),
        exit = slideOutVertically() + shrinkVertically(shrinkTowards = Alignment.Bottom)
    ) {
        SingleAccountNotice(modifier = modifier)
    }

    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (account.active) {
                GameAccountInfo(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 22.dp, vertical = 4.dp),
                    account = account,
                )
            } else {
                NoLinkedAccountsNotice(
                    game = game,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 22.dp, vertical = 4.dp),
                )
            }
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = when (game) {
                            HoYoGame.Houkai -> R.drawable.ic_honkai
                            HoYoGame.StarRail -> R.drawable.ic_starrail
                            else -> R.drawable.ic_genshin
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                )
            }
        }
    }
}

@Composable
fun NoLinkedAccountsNotice(modifier: Modifier = Modifier, game: HoYoGame) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(
                id = R.string.no_account_linked,
                when (game) {
                    HoYoGame.Houkai -> stringResource(id = R.string.honkai_impact_3rd)
                    HoYoGame.StarRail -> stringResource(id = R.string.honkai_star_rail)
                    else -> stringResource(id = R.string.genshin_impact)
                }
            ),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun SingleAccountNotice(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.qiqi_desk),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(48.dp),
        )
        Text(
            text = stringResource(id = R.string.only_single_account),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
