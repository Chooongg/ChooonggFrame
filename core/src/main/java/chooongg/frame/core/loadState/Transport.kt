package chooongg.frame.core.loadState

import android.content.Context
import android.view.View

interface Transport {
    fun order(context: Context, view: View)
}