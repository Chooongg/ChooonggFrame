package chooongg.frame.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chooongg.frame.adapter.animation.BaseAnimator
import chooongg.frame.adapter.viewHolder.BaseViewHolder

abstract class BaseAdapter<DATA, VH : BaseViewHolder> @JvmOverloads constructor(
    @LayoutRes private val layoutResId: Int,
    data: MutableList<DATA>? = null
) : RecyclerView.Adapter<VH>() {

    companion object {
        const val HEADER_VIEW = 0x10000111
        const val LOAD_MORE_VIEW = 0x10000222
        const val FOOTER_VIEW = 0x10000333
        const val EMPTY_VIEW = 0x10000555
    }

    /**
     * 数据, 只允许 get。
     */
    var data: MutableList<DATA> = data ?: arrayListOf()
        internal set

    /**
     * 当显示空布局时，是否显示 Header
     */
    var headerWithEmptyEnable = false

    /** 当显示空布局时，是否显示 Foot */
    var footerWithEmptyEnable = false

    /** 是否使用空布局 */
    var isUseEmpty = true

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    var headerViewAsFlow: Boolean = false
    var footerViewAsFlow: Boolean = false

    /**
     * 是否打开动画
     */
    var animationEnable: Boolean = false

    /**
     * 动画是否仅第一次执行
     */
    var isAnimationFirstOnly = true

    /**
     * 设置自定义动画
     */
    var adapterAnimator: BaseAnimator? = null
        set(value) {
            animationEnable = true
            field = value
        }

    /**
     * 加载更多模块
     */
    val loadMoreModule: BaseLoadMoreModule
        get() {
            checkNotNull(mLoadMoreModule) { "Please first implements LoadMoreModule" }
            return mLoadMoreModule!!
        }

    /**
     * 向上加载模块
     */
    val upFetchModule: BaseUpFetchModule
        get() {
            checkNotNull(mUpFetchModule) { "Please first implements UpFetchModule" }
            return mUpFetchModule!!
        }

    /**
     * 拖拽模块
     */
    val draggableModule: BaseDraggableModule
        get() {
            checkNotNull(mDraggableModule) { "Please first implements DraggableModule" }
            return mDraggableModule!!
        }
}