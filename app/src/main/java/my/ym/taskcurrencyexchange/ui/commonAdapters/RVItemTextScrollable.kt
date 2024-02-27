package my.ym.taskcurrencyexchange.ui.commonAdapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class RVItemTextScrollable : ListAdapter<String, VHItemTextScrollable>(COMPARATOR) {

	companion object {
		private val COMPARATOR = object : DiffUtil.ItemCallback<String>() {
			override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
				return oldItem/*.id*/ == newItem/*.id*/
			}

			override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
				return oldItem == newItem
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItemTextScrollable {
		return VHItemTextScrollable(parent)
	}

	override fun onBindViewHolder(holder: VHItemTextScrollable, position: Int) {
		holder.bind(getItem(position))
	}
}
