package io.chaldeaprjkt.yumetsuki.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.chaldeaprjkt.yumetsuki.R

@Preview
@Composable
fun PreviewSNSHelpBottomSheet() {
    SNSHelpBottomSheet()
}

@Composable
fun SNSHelpBottomSheet() {
    Surface {
        Column(
            Modifier
                .padding(16.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(id = R.string.grab_cookie_from_pc),
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(id = R.string.cookie_help_step_one),
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
            ScreenshotImage(
                id = R.drawable.cookie_help_1,
                contentDescription = "Inspect page menu",
            )
            Text(
                text = stringResource(id = R.string.cookie_help_step_two),
                modifier = Modifier.padding(vertical = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
            ScreenshotImage(
                id = R.drawable.cookie_help_2,
                contentDescription = "Inspect Cookie",
            )
        }
    }
}

@Composable
fun ColumnScope.ScreenshotImage(
    modifier: Modifier = Modifier,
    id: Int,
    contentDescription: String,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val painter = painterResource(id = id)
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .weight(1f, fill = false)
            .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
            .fillMaxWidth(),
        contentScale = contentScale
    )
}
