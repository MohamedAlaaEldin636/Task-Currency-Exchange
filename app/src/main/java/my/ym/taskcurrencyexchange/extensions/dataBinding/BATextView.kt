package my.ym.taskcurrencyexchange.extensions.dataBinding

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("textView_setSelectedBA")
fun TextView.setSelectedBA(value: Boolean?) {
	isSelected = value ?: return
}
