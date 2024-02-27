package my.ym.taskcurrencyexchange.extensions

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @param onGridLayoutSpanSizeLookup Returns the number of span occupied by the item at position,
 * Same as [GridLayoutManager.SpanSizeLookup.getSpanSize] isa.
 *
 * @param onLayoutManager Corresponds to overriding [GridLayoutManager.checkLayoutParams] ->
 * Ex. of Usage layoutParams.width = width / 3 to ensure item is 1 third of parent in width isa.
 */
fun RecyclerView.setupWithAdapter(
	adapter: RecyclerView.Adapter<*>,
	isVerticalNotHorizontal: Boolean,
	spanCount: Int,
	context: Context? = getContext(),
	reverseLayout: Boolean = false,
	onGridLayoutSpanSizeLookup: (GridLayoutManager.(Int) -> Int)? = null,
	onLayoutManager: RecyclerView.LayoutManager.(RecyclerView.LayoutParams) -> Unit = {},
) {
	val toBeUsedContext = context ?: return

	layoutManager = object : GridLayoutManager(
		toBeUsedContext,
		spanCount,
		if (isVerticalNotHorizontal) VERTICAL else HORIZONTAL,
		reverseLayout
	) {
		override fun checkLayoutParams(layoutParams: RecyclerView.LayoutParams?): Boolean {
			if (layoutParams != null) {
				onLayoutManager(layoutParams)
			}

			return super.checkLayoutParams(layoutParams)
		}
	}.also { gridLayoutManager ->
		if (onGridLayoutSpanSizeLookup != null) {
			gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
				override fun getSpanSize(position: Int): Int {
					return gridLayoutManager.onGridLayoutSpanSizeLookup(position)
				}
			}
		}
	}

	this.adapter = adapter
}
