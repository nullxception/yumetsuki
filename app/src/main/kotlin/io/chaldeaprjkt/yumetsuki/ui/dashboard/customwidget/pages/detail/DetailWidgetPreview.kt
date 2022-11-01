package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.pages.detail

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isVisible
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.databinding.WidgetDetailFixedBinding
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.ParaTransformerStatus
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.TransformerTime
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.DetailWidgetSettings
import io.chaldeaprjkt.yumetsuki.util.extension.FullTimeType
import io.chaldeaprjkt.yumetsuki.util.extension.describeTime
import io.chaldeaprjkt.yumetsuki.util.extension.describeTimeSecs

@Composable
fun DetailWidgetPreview(
    modifier: Modifier = Modifier,
    settings: DetailWidgetSettings,
) {
    AndroidViewBinding(WidgetDetailFixedBinding::inflate, modifier = modifier) {
        pbLoading.visibility = View.GONE
        llDisable.visibility = View.GONE
        card.alpha = settings.backgroundAlpha

        settings.fontSize.let {
            tvResin.textSize = it
            tvResinTitle.textSize = it
            tvResinTime.textSize = it
            tvResinTimeTitle.textSize = it
            tvDailyCommission.textSize = it
            tvDailyCommissionTitle.textSize = it
            tvWeeklyBoss.textSize = it
            tvWeeklyBossTitle.textSize = it
            tvRealmCurrency.textSize = it
            tvRealmCurrencyTitle.textSize = it
            tvRealmCurrencyTime.textSize = it
            tvRealmCurrencyTimeTitle.textSize = it
            tvExpeditionTitle.textSize = it
            tvExpeditionTitle.textSize = it
            tvExpeditionTime.textSize = it
            tvExpeditionTime.textSize = it
            tvTransformer.textSize = it
            tvTransformerTitle.textSize = it
        }

        if (settings.showTime) {
            rlResinTime.isVisible = settings.showResinData
            rlRealmCurrencyTime.isVisible = settings.showRealmCurrencyData
            tvResinTimeTitle.text = root.context.getString(R.string.replenished)
            tvRealmCurrencyTimeTitle.text =
                root.context.getString(R.string.replenished)
            tvExpeditionTitle.text = root.context.getString(R.string.expedition_settled)
        } else {
            rlResinTime.visibility = View.GONE
            rlRealmCurrencyTime.visibility = View.GONE
        }

        tvResinTime.text = root.context.describeTimeSecs(37913, FullTimeType.Max)
        tvRealmCurrencyTime.text = root.context.describeTimeSecs(96123, FullTimeType.Max)
        tvExpeditionTime.text = root.context.describeTimeSecs(74285, FullTimeType.Done)
        tvTransformer.text = ParaTransformerStatus(
            true,
            TransformerTime(3, 0, 0, 0, true)
        ).describeTime(root.context)


        rlResin.isVisible = settings.showResinData
        rlResinTime.isVisible = settings.showResinData && settings.showTime
        rlDailyCommission.isVisible = settings.showDailyCommissinData
        rlWeeklyBoss.isVisible = settings.showWeeklyBossData
        rlRealmCurrency.isVisible = settings.showRealmCurrencyData
        rlRealmCurrencyTime.isVisible = settings.showRealmCurrencyData && settings.showTime
        rlExpedition.isVisible = settings.showExpeditionData
        rlTransformer.isVisible = settings.showParaTransformerData
    }
}
