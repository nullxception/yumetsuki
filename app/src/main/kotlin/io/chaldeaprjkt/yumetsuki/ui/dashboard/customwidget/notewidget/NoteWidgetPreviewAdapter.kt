package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.notewidget

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import io.chaldeaprjkt.yumetsuki.R
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetOption
import io.chaldeaprjkt.yumetsuki.databinding.ItemWidgetNoteBinding
import io.chaldeaprjkt.yumetsuki.ui.widget.NoteListItem
import io.chaldeaprjkt.yumetsuki.util.extension.FullTimeType
import io.chaldeaprjkt.yumetsuki.util.extension.describeTimeSecs

class NoteWidgetPreviewAdapter(
    private val context: Context
) : RecyclerView.Adapter<NoteWidgetPreviewAdapter.ViewHolder>() {
    private var fontSize = NoteWidgetOption.DefaultFontSize
    private val items = mutableListOf<NoteListItem>()
    private var showTitle = false

    class ViewHolder(private val binding: ItemWidgetNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NoteListItem, fontSize: Float, showDesc: Boolean) {
            binding.icon.setImageResource(item.icon)
            binding.status.apply {
                text = item.status
                textSize = fontSize
                updateLayoutParams<LinearLayout.LayoutParams> {
                    weight = if (showDesc) 0f else 1f
                }
            }
            binding.desc.apply {
                text = context.getString(item.desc)
                textSize = fontSize
                visibility = if (showDesc) View.VISIBLE else View.GONE
            }

            binding.sub.isVisible = item.subdesc != null
            if (item.subdesc != null) {
                binding.subdesc.apply {
                    text = context.getString(item.subdesc)
                    textSize = fontSize
                    visibility = if (showDesc) View.VISIBLE else View.GONE
                }
                binding.substatus.apply {
                    text = item.substatus
                    textSize = fontSize
                    updateLayoutParams<LinearLayout.LayoutParams> {
                        weight = if (showDesc) 0f else 1f
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        return ViewHolder(ItemWidgetNoteBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], fontSize, showTitle)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(option: NoteWidgetOption) {
        val newItems = mutableListOf<NoteListItem>()
        if (option.showResinData) {
            newItems.add(
                NoteListItem(
                    R.string.resin,
                    R.drawable.ic_resin,
                    "159/160",
                    if (option.showRemainTime) R.string.widget_full_at else null,
                    context.describeTimeSecs(37913, FullTimeType.Max)
                )
            )
        }
        if (option.showDailyCommissionData) {
            newItems.add(
                NoteListItem(
                    R.string.daily_commissions, R.drawable.ic_daily_commission, "3/4"
                )
            )
        }
        if (option.showWeeklyBossData) {
            newItems.add(
                NoteListItem(
                    R.string.enemies_of_note, R.drawable.ic_domain, "2/3"
                )
            )
        }
        if (option.showRealmCurrencyData) {
            newItems.add(
                NoteListItem(
                    R.string.realm_currency,
                    R.drawable.ic_serenitea_pot,
                    "1234/5678",
                    if (option.showRemainTime) R.string.widget_full_at else null,
                    context.describeTimeSecs(96123, FullTimeType.Max)
                )
            )
        }
        if (option.showExpeditionData) {
            newItems.add(
                NoteListItem(
                    R.string.expedition,
                    R.drawable.ic_warp_point,
                    context.getString(R.string.widget_ui_parameter_done)
                )
            )
        }
        if (option.showParaTransformerData) {
            newItems.add(
                NoteListItem(
                    R.string.parametric_transformer,
                    R.drawable.ic_paratransformer,
                    context.getString(R.string.widget_ui_transformer_ready)
                )
            )
        }

        items.clear()
        items.addAll(newItems)
        fontSize = option.fontSize
        showTitle = option.showDescription
        notifyDataSetChanged()
    }
}
