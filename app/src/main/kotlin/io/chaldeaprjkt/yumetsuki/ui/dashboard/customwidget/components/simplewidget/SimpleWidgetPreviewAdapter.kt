package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.simplewidget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import io.chaldeaprjkt.yumetsuki.ui.widget.simple.SimpleWidgetItem

class SimpleWidgetPreviewAdapter(
    val context: Context,
) : RecyclerView.Adapter<SimpleWidgetPreviewAdapter.ViewHolder>() {
    private var fontSize = SimpleWidgetSettings.DefaultFontSize
    private val items = mutableListOf<SimpleWidgetItem>()

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: SimpleWidgetItem, fontSize: Float) {
            view.findViewById<ImageView>(R.id.icon)?.setImageResource(item.icon)
            view.findViewById<TextView>(R.id.status)?.apply {
                text = item.status
                textSize = fontSize
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_simple_widget, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], fontSize)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFontSize(size: Float) {
        fontSize = size
        notifyDataSetChanged()
    }

    fun updateSettings(settings: SimpleWidgetSettings) {
        val newItems = mutableListOf<SimpleWidgetItem>()
        if (settings.showResinData) newItems.add(SimpleWidgetItem(R.drawable.ic_resin, "159/160"))
        if (settings.showDailyCommissionData) newItems.add(
            SimpleWidgetItem(
                R.drawable.ic_daily_commission, "3/4"
            )
        )
        if (settings.showWeeklyBossData) newItems.add(SimpleWidgetItem(R.drawable.ic_domain, "2/3"))
        if (settings.showRealmCurrencyData) newItems.add(
            SimpleWidgetItem(
                R.drawable.ic_serenitea_pot, "1234/5678"
            )
        )
        if (settings.showExpeditionData) newItems.add(
            SimpleWidgetItem(
                R.drawable.ic_warp_point, context.getString(R.string.widget_ui_parameter_done)
            )
        )
        if (settings.showParaTransformerData) newItems.add(
            SimpleWidgetItem(
                R.drawable.ic_paratransformer,
                context.getString(R.string.widget_ui_transformer_ready)
            )
        )

        val diff = DiffUtil.calculateDiff(SimpleWidgetDiffCallback(items, newItems))
        items.clear()
        items.addAll(newItems)
        diff.dispatchUpdatesTo(this)
    }
}
