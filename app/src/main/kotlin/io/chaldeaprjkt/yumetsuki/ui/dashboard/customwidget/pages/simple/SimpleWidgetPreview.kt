package io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.pages.simple

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.chaldeaprjkt.yumetsuki.data.widgetsetting.entity.SimpleWidgetSettings
import io.chaldeaprjkt.yumetsuki.databinding.WidgetSimpleBinding
import io.chaldeaprjkt.yumetsuki.ui.dashboard.customwidget.components.simplewidget.SimpleWidgetPreviewAdapter
import io.chaldeaprjkt.yumetsuki.util.extension.replaceWith

@Composable
fun SimpleWidgetPreview(
    modifier: Modifier = Modifier,
    settings: SimpleWidgetSettings,
) {
    val context = LocalContext.current
    val dataAdapter = remember { SimpleWidgetPreviewAdapter(context) }

    AndroidViewBinding(factory = { inflater, parent, attachRoot ->
        WidgetSimpleBinding.inflate(inflater, parent, attachRoot).apply {
            val rv = RecyclerView(root.context)
            lvData.replaceWith(rv)
            pbLoading.visibility = View.GONE
            llDisable.visibility = View.GONE
            llBody.updateLayoutParams {
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
            with(rv) {
                itemAnimator = null
                minimumWidth = (120 * Resources.getSystem().displayMetrics.density).toInt()
                layoutManager = LinearLayoutManager(context)
                adapter = dataAdapter
                updateLayoutParams {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
            }
        }
    }, modifier = modifier) {
        dataAdapter.updateSettings(settings)
        compatCard.alpha = settings.backgroundAlpha
    }
}
