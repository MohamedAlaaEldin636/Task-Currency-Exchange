package my.ym.taskcurrencyexchange.helperTypes

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes

class NonFilterArrayAdapter<T>(
	context: Context,
	@LayoutRes resource: Int
) : ArrayAdapter<T>(context, resource) {

	override fun getFilter(): Filter = NonFilter()

	class NonFilter : Filter() {
		override fun performFiltering(constraint: CharSequence?) = FilterResults()

		override fun publishResults(constraint: CharSequence?, results: FilterResults?) = Unit
	}
}
