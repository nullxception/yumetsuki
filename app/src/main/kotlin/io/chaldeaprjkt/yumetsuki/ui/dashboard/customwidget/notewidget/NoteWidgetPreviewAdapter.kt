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
import io.chaldeaprjkt.yumetsuki.data.realtimenote.entity.RealtimeNote
import io.chaldeaprjkt.yumetsuki.data.session.entity.Session
import io.chaldeaprjkt.yumetsuki.data.settings.entity.NoteWidgetOption
import io.chaldeaprjkt.yumetsuki.databinding.ItemWidgetNoteBinding
import io.chaldeaprjkt.yumetsuki.ui.widget.NoteListFactory
import io.chaldeaprjkt.yumetsuki.ui.widget.NoteListItem

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
        items.clear()
        items.addAll(NoteListFactory.build(context, option, RealtimeNote.Sample, Session.Empty))
        fontSize = option.fontSize
        showTitle = option.showDescription
        notifyDataSetChanged()
    }
}
