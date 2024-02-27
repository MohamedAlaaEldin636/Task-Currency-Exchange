package my.ym.taskcurrencyexchange.ui.commonAdapters

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import my.ym.taskcurrencyexchange.R
import my.ym.taskcurrencyexchange.databinding.ItemTextScrollableBinding
import my.ym.taskcurrencyexchange.extensions.findFragmentOrNull
import my.ym.taskcurrencyexchange.helperTypes.BaseRecyclerViewViewHolder

class VHItemTextScrollable(
	parent: ViewGroup,
) : BaseRecyclerViewViewHolder<ItemTextScrollableBinding>(
	parent,
	R.layout.item_text_scrollable
) {

	val text = MutableLiveData("")

	init {
		binding.holder = this
		binding.lifecycleOwner = parent.findFragmentOrNull<Fragment>()?.viewLifecycleOwner
	}

	fun bind(data: String) {
		text.value = data
	}

}
