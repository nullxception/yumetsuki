package io.chaldeaprjkt.yumetsuki.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.chaldeaprjkt.yumetsuki.ui.widget.WidgetEventDispatcher
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var widgetEventDispatcher: WidgetEventDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Yumetsuki()
        }
    }

    override fun onPause() {
        if (::widgetEventDispatcher.isInitialized) {
            widgetEventDispatcher.refreshAll()
        }
        super.onPause()
    }
}
