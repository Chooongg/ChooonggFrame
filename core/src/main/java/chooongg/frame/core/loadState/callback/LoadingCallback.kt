package chooongg.frame.core.loadState.callback

import android.content.Context
import android.view.View
import chooongg.frame.core.R

class LoadingCallback : Callback() {
    override fun getViewLayout() = R.layout.callback_loading
    override fun onViewCreated(context: Context, view: View) = Unit
    override fun onAttach(context: Context, view: View) = Unit
    override fun onDetach(context: Context, view: View) = Unit
}