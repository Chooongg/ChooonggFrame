package chooongg.frame.core.loadState.callback

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import chooongg.frame.core.R
import com.google.android.material.progressindicator.ProgressIndicator

class DefaultLoadingCallback : Callback() {

    private lateinit var progressIndicator: ProgressIndicator

    override fun getViewLayout() = R.layout.callback_default_loading
    override fun onViewCreated(context: Context, view: View) = Unit
    override fun onAttach(context: Context, view: View) {
        progressIndicator = view.findViewById(R.id.progress_indicator)
        progressIndicator.show()
    }

    override fun setVerticalPercentage(percentage: Float) {
        progressIndicator.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = percentage
        }
    }

    override fun setHorizontalPercentage(percentage: Float) {
        progressIndicator.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = percentage
        }
    }

    override fun onDetach(context: Context, view: View) {
        progressIndicator.hide()
    }
}