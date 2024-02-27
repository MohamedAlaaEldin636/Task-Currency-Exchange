package my.ym.taskcurrencyexchange.helperTypes

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import my.ym.taskcurrencyexchange.extensions.layoutInflater

abstract class BaseRecyclerViewViewHolder<VDB : ViewDataBinding>(
	parent: ViewGroup,
	@LayoutRes layoutRes: Int,
	protected val binding: VDB = DataBindingUtil.inflate(
		parent.context.layoutInflater,
		layoutRes,
		parent,
		false
	)
) : RecyclerView.ViewHolder(binding.root)
