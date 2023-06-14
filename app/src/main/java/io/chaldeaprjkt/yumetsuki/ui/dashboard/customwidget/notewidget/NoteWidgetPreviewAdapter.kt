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
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.GenshinRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.StarRailRealtimeNote
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetSetting
import io.chaldeaprjkt.yumetsuki.databinding.ItemWidgetNoteBinding
import io.chaldeaprjkt.yumetsuki.ui.widget.NoteListFactory
import io.chaldeaprjkt.yumetsuki.ui.widget.NoteListItem

class NoteWidgetPreviewAdapter(private val context: Context) :
    RecyclerView.Adapter<NoteWidgetPreviewAdapter.ViewHolder>() {
    private val items = mutableListOf<NoteListItem>()
    private var settings = NoteWidgetSetting.Empty

    class ViewHolder(private val binding: ItemWidgetNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NoteListItem, settings: NoteWidgetSetting) {
            binding.icon.setImageResource(item.icon)
            binding.icon.isVisible = settings.showIcons
            binding.status.apply {
                text = item.status
                textSize = settings.fontSize
                updateLayoutParams<LinearLayout.LayoutParams> {
                    weight = if (settings.showDescription) 0f else 1f
                }
            }
            binding.desc.apply {
                text = context.getString(item.desc)
                textSize = settings.fontSize
                visibility = if (settings.showDescription) View.VISIBLE else View.GONE
            }

            binding.sub.isVisible = item.subdesc != null
            if (item.subdesc != null) {
                binding.subdesc.apply {
                    text = context.getString(item.subdesc)
                    textSize = settings.fontSize
                    visibility = if (settings.showDescription) View.VISIBLE else View.GONE
                }
                binding.substatus.apply {
                    text = item.substatus
                    textSize = settings.fontSize
                    updateLayoutParams<LinearLayout.LayoutParams> {
                        weight = if (settings.showDescription) 0f else 1f
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
        holder.bind(items[position], settings)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(settings: NoteWidgetSetting) {
        items.clear()
        items.addAll(
            NoteListFactory.build(
                context,
                settings,
                GenshinRealtimeNote.Sample,
                StarRailRealtimeNote.Sample,
                Session.Empty
            )
        )
        this.settings = settings
        notifyDataSetChanged()
    }
}
