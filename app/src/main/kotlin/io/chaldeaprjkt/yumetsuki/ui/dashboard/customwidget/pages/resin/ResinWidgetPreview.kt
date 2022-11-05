package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.pages.resin

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isVisible
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.ResinWidgetSettings
import io.chaldeaprjkt.yumetsuki.databinding.WidgetResinFixedBinding

@Composable
fun ResinWidgetPreview(
    modifier: Modifier = Modifier,
    settings: ResinWidgetSettings,
) {
    AndroidViewBinding(WidgetResinFixedBinding::inflate, modifier = modifier) {
        pbLoading.visibility = View.GONE
        llDisable.visibility = View.GONE
        ivResin.isVisible = settings.showResinImage
        tvRemainTime.isVisible = settings.showTime
        tvResin.textSize = settings.fontSize
        compatCard.alpha = settings.backgroundAlpha
        tvRemainTime.text = root.context.getString(R.string.widget_ui_remain_time, 0, 0)
    }
}
